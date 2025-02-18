package com.ar.musicplayer.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import com.ar.musicplayer.R
import com.ar.musicplayer.navigation.DownloadSettingsScreenObj
import com.ar.musicplayer.navigation.LanguageSettingsScreenObj
import com.ar.musicplayer.navigation.PlaybackSettingsScreenObj
import com.ar.musicplayer.navigation.StorageSettingScreenObj
import com.ar.musicplayer.navigation.ThemeSettingObj

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackPressed: () -> Unit,
    onNavigate: (Any) -> Unit
){

    val context = LocalContext.current

    val url = "https://rewatch.online/music-player/download"

    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        color = Color.White,
                        modifier = Modifier,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                },
                colors = TopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Transparent,
                    navigationIconContentColor = Color.Transparent,
                    actionIconContentColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
            )
        },
        containerColor = Color.Transparent,
    ) { innerPadding ->

        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()){
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clickable {
                            onNavigate(LanguageSettingsScreenObj)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_language),
                        contentDescription = "Audio Quality",
                        tint = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = "Language",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .weight(1f)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clickable {
                            onNavigate(PlaybackSettingsScreenObj)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_music_note_24),
                        contentDescription = "Music Playback ",
                        tint = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = "Music Playback",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .weight(1f)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clickable {
                            onNavigate(StorageSettingScreenObj)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_storage),
                        contentDescription = "Storage",
                        tint = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = "Storage",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .weight(1f)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clickable {
                            onNavigate(ThemeSettingObj)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_dark_mode),
                        contentDescription = "Downloads",
                        tint = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = "Theme",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .weight(1f)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clickable {
                            onNavigate(DownloadSettingsScreenObj)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_download),
                        contentDescription = "Download",
                        tint = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = "Download",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .weight(1f)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_help_outline),
                        contentDescription = "Help",
                        tint = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = "Help",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .weight(1f)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val string = getString(context,R.string.githubReadMe)
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(string))
                            context.startActivity(intent)
                        }
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_verified_user),
                        contentDescription = "About",
                        tint = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = "About",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .weight(1f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clickable {
                            context.startActivity(Intent.createChooser(sendIntent,null))
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share App",
                        tint = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = "Share App",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .weight(1f)
                    )
                }

            }
        }

    }

}

@Preview
@Composable
fun PreviewSettingScreen(){
    val blackToGrayGradient =
        Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.primaryContainer,
                Color.Black,
                Color.Black,
                Color.Black
            ),
            start = Offset.Zero
        )
    SettingsScreen(
        {},
        remember {
            { path ->

            }
        },
    )
}