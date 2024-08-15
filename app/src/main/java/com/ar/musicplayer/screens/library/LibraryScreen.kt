package com.ar.musicplayer.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ar.musicplayer.navigation.FavoriteScreenObj
import com.ar.musicplayer.navigation.ListeningHisScreenObj
import com.ar.musicplayer.navigation.MyMusicScreenObj
import com.ar.musicplayer.navigation.PlaylistFetchScreenObj
import com.ar.musicplayer.navigation.SettingsScreenObj

@Composable
fun LibraryScreen(navController: NavHostController, brush: Brush){
    Box(modifier = Modifier.fillMaxSize().background(brush)){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding( start = 10.dp, top = 40.dp, end = 10.dp )
        ){
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp), verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "My Library",
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
                IconButton(onClick = { navController.navigate(SettingsScreenObj) }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clickable {
                        navController.navigate(FavoriteScreenObj)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Liked",
                    tint = Color.White,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "Favorite Songs",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 20.dp).weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Album,
                    contentDescription = "Albums",
                    tint = Color.White,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "Albums",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 20.dp).weight(1f)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clickable { navController.navigate(ListeningHisScreenObj) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Last Session",
                    tint = Color.White,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "Listening History",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 20.dp).weight(1f)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clickable {
                        navController.navigate(MyMusicScreenObj)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = "My Music",
                    tint = Color.White,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "My Music",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 20.dp).weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DownloadDone,
                    contentDescription = "Downloads",
                    tint = Color.White,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "Downloads",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 20.dp).weight(1f)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clickable {
                        navController.navigate(PlaylistFetchScreenObj)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlaylistPlay,
                    contentDescription = "Playlists",
                    tint = Color.White,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "Playlists",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 20.dp).weight(1f)
                )
            }

        }
    }
}

@Preview
@Composable
fun LibraryScreenPreview(){
    val blackToGrayGradient =
        Brush.verticalGradient(
            colors = listOf(Color(0xFF000000),Color(0xFF161616)),
            startY = Float.POSITIVE_INFINITY,
            endY = 0f
        )
    LibraryScreen(rememberNavController(),blackToGrayGradient)
}