package com.ar.musicplayer.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.activity.viewModels
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModel
import com.ar.musicplayer.models.Playlist
import com.ar.musicplayer.models.SongResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class PlayerViewModel @Inject constructor() : ViewModel()  {

    val currentSong = MutableStateFlow<SongResponse?>(null)
    val playlist = MutableStateFlow<List<SongResponse>>(emptyList())
    val isPlaying = MutableStateFlow(false)
    val playlistId = MutableStateFlow<String>("")

    val bitmapImg = MutableStateFlow<Bitmap?>( null)
    val isBottomNavVisible = MutableStateFlow<Boolean?>( true)


    // Function to update the current song
    fun updateCurrentSong(song: SongResponse) {
        currentSong.value = song

    }
    fun bitmapload (bitmap: Bitmap){
        bitmapImg.value = bitmap
    }
    fun playPlaylist(list: List<SongResponse>,id: String){
        playlist.value = list
        playlistId.value = id
    }

    fun setBottomNavVisibility(boolean: Boolean) {
        isBottomNavVisible.value = boolean
    }

    fun play() {
        isPlaying.value = true
    }

    fun pause() {
        isPlaying.value = false
    }

}