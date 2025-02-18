package com.ar.musicplayer.components.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ar.musicplayer.data.models.SongResponse

@Composable
fun NextPlaylist(
    songResponse: SongResponse,
    isPlaying: Boolean,
    showAnim: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 5.dp, top = 10.dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = songResponse.image,
            contentDescription = "image",
            modifier = Modifier
                .size(50.dp)
                .padding(4.dp)
                .clip(RoundedCornerShape(3.dp)),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        Column(
            modifier = Modifier
                .padding(15.dp, top = 5.dp, bottom = 5.dp, end = 10.dp)
                .weight(1f)
        ) {
            Text(
                text = songResponse.title ?: "null",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                modifier = Modifier.padding(bottom = 2.dp),
                maxLines = 1,
                softWrap = true,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = songResponse.subtitle ?: "unknown",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                maxLines = 1,
                softWrap = true,
                overflow = TextOverflow.Ellipsis
            )
        }

        if(showAnim) {
            MusicPlayingAnimation(isPlaying = isPlaying, modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .width(80.dp)
                .height(30.dp))
        }


    }
}


//        if(currentSong != songResponse){
//
//            DownloadButton(
//                isDownloaded = isDownloaded.value,
//                isDownloading = isDownloading.value,
//                downloadProgress = downloadProgress?.toFloat(),
//                onDownloadClick = remember(songResponse) {
//                    {
//                        if (!isDownloaded.value) {
//                            songResponse.let {
//                                downloaderViewModel.onEvent(
//                                    DownloadEvent.downloadSong(it)
//                                )
//                            }
//                        }
//                    }
//                }
//            )
//            FavToggleButton(
//                isFavorite = isFavourite.value,
//                onFavClick = remember{
//                    {
//                        favViewModel.onEvent(
//                            FavoriteSongEvent.toggleFavSong(
//                                songResponse = songResponse
//                            )
//                        )
//                        favButtonClick.value = !favButtonClick.value
//                    }
//                }
//            )
//
//            IconButton(onClick = { /* Handle menu button click */ }) {
//                Icon(
//                    Icons.Default.MoreVert,
//                    contentDescription = "Menu",
//                    tint = Color.White
//                )
//            }
//        }
//        else{
//            MusicPlayingAnimation(isPlaying = isPlaying, modifier = Modifier
//                .padding(start = 10.dp, end = 10.dp)
//                .width(80.dp)
//                .height(30.dp))
//        }