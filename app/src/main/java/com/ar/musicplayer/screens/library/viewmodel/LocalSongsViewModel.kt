package com.ar.musicplayer.screens.library.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ar.musicplayer.data.models.Artist
import com.ar.musicplayer.data.models.ArtistMap
import com.ar.musicplayer.data.models.MoreInfoResponse
import com.ar.musicplayer.data.models.SongResponse
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.Session
import com.arthenica.ffmpegkit.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class
LocalSongsViewModel @Inject constructor(
    application: Application,
    private val contentResolver: ContentResolver
): AndroidViewModel(application) {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _songResponseList = MutableStateFlow<List<SongResponse>>(emptyList())
    val songResponseList: StateFlow<List<SongResponse>> get() = _songResponseList

    private val _songsByArtist = MutableStateFlow<Map<String, List<SongResponse>>>(emptyMap())
    val songsByArtist: StateFlow<Map<String, List<SongResponse>>> get() = _songsByArtist

    private val _songsByAlbum = MutableStateFlow<Map<String, List<SongResponse>>>(emptyMap())
    val songsByAlbum: StateFlow<Map<String, List<SongResponse>>> get() = _songsByAlbum


    init {
        fetchLocalSongs()
    }

    fun fetchLocalSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val songList = mutableListOf<SongResponse>()

            // Trigger media scan for the music directory
            val musicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
            val musicPaths = musicDirectory.listFiles()?.map { it.absolutePath }?.toTypedArray()
            if (musicPaths != null) {
                scanMedia(getApplication<Application>().applicationContext, musicPaths)
            }

            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION, // Add duration to the projection
                MediaStore.Audio.Media.ALBUM
            )

            // Modify the selection to include duration check and avoid recordings
            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND " +
                    "${MediaStore.Audio.Media.DURATION} >= 60000" // Only include songs with duration >= 1 minute
            val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

            val cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder
            )

            cursor?.use {
                val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val albumColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)

                while (it.moveToNext()) {
                    val title = it.getString(titleColumn)
                    val artist = it.getString(artistColumn)
                    val uri = it.getString(dataColumn)
                    val albumId = it.getString(albumIdColumn)
                    val album = it.getString(albumColumn)
                    val id = extractStringIdFromM4a(uri)
                    val duration = it.getLong(durationColumn)


                    // Optional: Check the file extension or MIME type to avoid recordings
                    if (uri.endsWith(".3gp") || uri.endsWith(".mp4") || uri.contains("recording")) {
                        continue // Skip call recordings or similar
                    }

                    val albumArtUri = Uri.withAppendedPath(
                        Uri.parse("content://media/external/audio/albumart"),
                        albumId.toString()
                    )

                    val song = SongResponse(
                        id = id,
                        title = title,
                        moreInfo = MoreInfoResponse(
                            artistMap = ArtistMap(
                                artists = listOf(Artist(name = artist))
                            ),
                            album = album,
                            duration = duration.toString()
                        ),
                        uri = uri,
                        image = albumArtUri.toString(),
                    )

                    Timber.tag("Local").d(album)
                    songList.add(song)
                }
            }
            _songResponseList.value = songList
            groupSongsByAlbum()
            groupSongsByArtist()
            delay(2500)
            _isLoading.value = false
        }
    }


    private fun groupSongsByArtist() {
        val groupedByArtist = _songResponseList.value
            .filterNotNull()
            .flatMap { song ->
                song.moreInfo?.artistMap?.artists?.filterNotNull()?.mapNotNull { artist ->
                    artist.name?.let { it to song }
                } ?: emptyList()
            }
            .groupBy({ it.first }, { it.second })

        _songsByArtist.value = groupedByArtist
    }

    private fun groupSongsByAlbum() {
        val groupedByAlbum = _songResponseList.value
            .filterNotNull()
            .filter { it.moreInfo?.album != null }
            .groupBy { it.moreInfo?.album!! }
        _songsByAlbum.value = groupedByAlbum
    }

    private fun scanMedia(context: Context, paths: Array<String>) {
        MediaScannerConnection.scanFile(context, paths, null) { path, uri ->
            Timber.tag("MediaScan").d("Scanned  $path")
            Timber.tag("MediaScan").d("$uri")
        }
    }

    private suspend fun extractStringIdFromM4a(filePath: String): String?  {

        return withContext(Dispatchers.IO) {
            try {

                val command = arrayOf(
                    "-i", filePath,
                    "-f", "ffmetadata",
                    "-"
                )
                val session: Session = FFmpegKit.execute(command.joinToString(" "))

                // Check if the session is completed
                if (session.state == SessionState.COMPLETED) {

                    val output = session.output


                    val result = output.substringBefore("=song_id").substringAfterLast(": ")


                    return@withContext result

                } else {
                     return@withContext null
                }
            } catch (e: Exception) {
                 return@withContext null
            }
        }

    }

}