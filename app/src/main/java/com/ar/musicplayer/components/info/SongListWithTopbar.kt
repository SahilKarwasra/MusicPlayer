package com.ar.musicplayer.components.info

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.ar.musicplayer.components.InfoDropdownMenu
import com.ar.musicplayer.data.models.PlaylistResponse
import com.ar.musicplayer.screens.library.mymusic.toDp
import com.ar.musicplayer.screens.library.mymusic.toPx
import com.ar.musicplayer.utils.download.DownloaderViewModel
import com.ar.musicplayer.utils.roomdatabase.favoritedb.FavoriteViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongListWithTopBar(
    mainImage: String,
    scrollState: ScrollState,
    color: Color,
    subtitle: String,
    data: PlaylistResponse?,
    favViewModel: FavoriteViewModel,
    downloaderViewModel: DownloaderViewModel,
    onFollowClicked: () -> Unit,
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onSongClicked: (Int) -> Unit,
    onBackPressed: () -> Unit
) {

    val minImgSize = 0.dp
    val maxImgSize =  300.dp
    val comparePxl = 150.dp.toPx()
    var currentImgSize by remember { mutableStateOf(maxImgSize.toPx()) }


    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if(scrollState.value < comparePxl) {
                    val delta = available.y.toInt()
                    val newImgSize = currentImgSize + delta
                    val previousImgSize = currentImgSize

                    currentImgSize = newImgSize.coerceIn(minImgSize.toPx(), maxImgSize.toPx())

                    val consumed = currentImgSize - previousImgSize
                    return Offset(0f, consumed)
                }
                else{
                    return Offset.Zero
                }
            }
        }
    }


    val topBarAlpha by remember {
        derivedStateOf {
             1 - (currentImgSize /maxImgSize.toPx())
        }
    }
    val imageAlpha by remember {
        derivedStateOf{
            currentImgSize/maxImgSize.toPx()
        }
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(nestedScrollConnection)
    ) {
        Box(Modifier.fillMaxWidth().zIndex(1f)) {
            Box{
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = data?.title ?: "",
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(16.dp)
                                .alpha(topBarAlpha)
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {onBackPressed()}
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack, tint = Color.White,
                                contentDescription = "back",
                                modifier = Modifier
                            )
                        }
                    },
                    modifier = Modifier.zIndex(1f),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = color.copy(alpha = topBarAlpha )
                    )
                )
            }
            AsyncImage(
                model = mainImage,
                contentDescription = "image",
                modifier = Modifier
                    .statusBarsPadding()
                    .size(currentImgSize.toDp().coerceIn(50.dp, 250.dp))
                    .alpha(imageAlpha)
                    .clip(RoundedCornerShape(2))
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )

            Row(
                horizontalArrangement = Arrangement.Absolute.Right,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset {
                        IntOffset(x = 0, currentImgSize.toInt())
                    }
            ) {
                Log.d("alpha", "${scrollState.value},,, size = ${currentImgSize.toDp().toPx()}" )

                AnimatedPlayPauseButton(
                    isPlaying = isPlaying,
                    onPlayPauseToggle = {
                        onPlayPause()
                    },
                    modifier = Modifier
                        .offset {
                            IntOffset(x = 0, y = 60.dp.toPx().toInt())
                        }
                        .zIndex(1f)
                        .padding(end = 20.dp)
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Column (
                modifier = Modifier
                    .offset {
                        IntOffset(x = 0, currentImgSize.toInt())
                    }
                    .verticalScroll(scrollState)
            ) {
                Column(
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Text(
                        text = data?.title.toString(),
                        style = typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        maxLines = 1,
                        softWrap = true,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(1f)
                            .basicMarquee()
                    )
                    Text(
                        text = subtitle,
                        color = Color.LightGray,
                        maxLines = 1,
                        softWrap = true,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(4.dp),
                    )
                    ButtonBox(
                        onFollowClicked = onFollowClicked,
                        isFavourite = false,
                        isDownloading = false,
                        downloadProgress = 0,
                        isDownloaded = false
                    )
                }

                data?.let {
                    repeat(it.list?.size ?: 0) { index->
                        val track  = data.list?.get(index)
                        if (track != null) {
                            SongItemRepresentation(
                                track = track,
                                index = index,
                                favViewModel = favViewModel,
                                downloaderViewModel = downloaderViewModel,
                                onTrackClicked = {
                                    onSongClicked(index)
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(125.dp))
            }
        }

    }



}


@Composable
fun ButtonBox(
    onFollowClicked: () -> Unit,
    isFavourite: Boolean,
    isDownloading: Boolean,
    downloadProgress: Int,
    isDownloaded: Boolean

){
    var isExpandedDropDown by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Row() {
            IconButton(onClick = {
                onFollowClicked()
            }) {
                Icon(
                    imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (isFavourite) Color.Red else Color.White
                )
            }


            IconButton(
                onClick = {

                }
            ) {
                if(isDownloading){
                    CircularProgressIndicator(
                        modifier = Modifier,
                        progress = downloadProgress?.div(100.toFloat()) ?: 0f,
                        color = Color.LightGray
                    )
                    Text(text = "${downloadProgress}%", color = Color.White, fontSize = 14.sp)
                }

                else{
                    Icon(
                        modifier = Modifier.weight(1f),
                        imageVector = if(isDownloaded) Icons.Default.DownloadDone  else Icons.Default.FileDownload,
                        contentDescription = "Download",
                        tint = Color.White
                    )
                }
            }

            IconButton(onClick = { isExpandedDropDown = !isExpandedDropDown }) {
                Icon(
                    Icons.Default.MoreHoriz,
                    contentDescription = "Menu",
                    tint = Color.White
                )
                InfoDropdownMenu(
                    onDismissRequest = {
                        isExpandedDropDown = false
                    },
                    expended = isExpandedDropDown
                )
            }

        }


    }
    Spacer(
        modifier = Modifier
            .height(16.dp)
    )
}





//
//@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
//@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
//@Composable
//fun SongListWithTopBar(
//    mainImage: String,
//    scrollState: LazyListState,
//    color: Color,
//    subtitle: String,
//    data: PlaylistResponse?,
//    favViewModel: FavoriteViewModel,
//    downloaderViewModel: DownloaderViewModel,
//    onFollowClicked: () -> Unit,
//    isPlaying: Boolean,
//    onPlayPause: () -> Unit,
//    onSongClicked: (Int) -> Unit,
//    onBackPressed: () -> Unit
//) {
//    val isFavourite by remember { mutableStateOf(false) }
//    var isExpandedDropDown by remember { mutableStateOf(false) }
//
//
//    val minImgSize = 0.dp
//    val maxImgSize =  300.dp
//    var currentImgSize by remember { mutableStateOf(maxImgSize.toPx()) }
//
//
//    val dynamicAlpha = 1 - ((currentImgSize.toDp() )/ maxImgSize)
//
//    val nestedScrollConnection = remember {
//        object : NestedScrollConnection {
//            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
//                if(scrollState.firstVisibleItemIndex < 1) {
//                    val delta = available.y.toInt()
//                    val newImgSize = currentImgSize + delta
//                    val previousImgSize = currentImgSize
//
//                    currentImgSize = newImgSize.coerceIn(minImgSize.toPx(), maxImgSize.toPx())
//
//                    val consumed = currentImgSize - previousImgSize
//                    return Offset(0f, consumed)
//                }
//                else{
//                    return Offset.Unspecified
//                }
//            }
//        }
//    }
//
//
//
//
//        Box(
//            modifier = Modifier
//                .nestedScroll(nestedScrollConnection)
//                .fillMaxSize()
//        ) {
//
//
//            Box(modifier = Modifier.zIndex(1f)) {
//                Row(
//                    modifier = Modifier
//                        .wrapContentHeight()
//                        .padding(
//                            top = WindowInsets.statusBars
//                                .asPaddingValues()
//                                .calculateTopPadding()
//                        )
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center,
//                ) {
//
//                    SubcomposeAsyncImage(
//                        model = mainImage,
//                        contentDescription = "",
//                        modifier = Modifier
//                            .padding(8.dp)
//                            .size(currentImgSize.toDp().coerceAtMost(250.dp))
//                            .clip(RoundedCornerShape(2))
//                    ) {
//                        val state = painter.state
//                        if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
//                            CircularProgress()
//                        } else {
//                            SubcomposeAsyncImageContent()
//                        }
//                    }
//
//                }
//
//                CenterAlignedTopAppBar(
//                    title = {
//                        Text(
//                            text = data?.title ?: "",
//                            color = Color.White,
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis,
//                            modifier = Modifier
//                                .padding(16.dp)
//                                .alpha(dynamicAlpha)
//                        )
//                    },
//                    navigationIcon = {
//                        IconButton(
//                            onClick = {onBackPressed()}
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.ArrowBack, tint = Color.White,
//                                contentDescription = "back",
//                                modifier = Modifier
//                            )
//                        }
//                    },
//
//                    modifier = Modifier.zIndex(1f),
//                    colors = TopAppBarDefaults.topAppBarColors(
//                        containerColor = color.copy(alpha = dynamicAlpha ),
//                    )
//                )
//
//
//                Row(
//                    horizontalArrangement = Arrangement.Absolute.Right,
//                    verticalAlignment = Alignment.Bottom,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .zIndex(1f)
//                        .offset {
//                            IntOffset(x = 0, currentImgSize.toInt())
//                        }
//                ) {
//                    AnimatedPlayPauseButton(
//                        isPlaying = isPlaying,
//                        onPlayPauseToggle = {
//                            onPlayPause()
//                        },
//                        modifier = Modifier
//                            .offset {
//                                IntOffset(x = 0, y = 60.dp.toPx().toInt())
//                            }
//                            .padding(end = 20.dp)
//                    )
//                }
//
//
//            }
//
//            LazyColumn(
//                state = scrollState,
//                modifier = Modifier
//                    .offset {
//                        IntOffset(x = 0, y =  currentImgSize.toInt())
//                    }
//
//            ) {
//                item{
//                    Column(modifier = Modifier.padding(start = 10.dp)) {
//                        Text(
//                            text = data?.title.toString(),
//                            style = typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
//                            color = Color.White,
//                            maxLines = 1,
//                            softWrap = true,
//                            overflow = TextOverflow.Ellipsis,
//                            modifier = Modifier
//                                .padding(4.dp)
//                                .fillMaxWidth(1f)
//                                .basicMarquee()
//                        )
//                        Text(
//                            text = subtitle,
//                            color = Color.LightGray,
//                            maxLines = 1,
//                            softWrap = true,
//                            overflow = TextOverflow.Ellipsis,
//                            fontSize = 10.sp,
//                            modifier = Modifier.padding(4.dp),
//                        )
//                    }
//                }
//                item {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                    ) {
//
//                            Row() {
//                                IconButton(onClick = {
//                                    onFollowClicked()
//                                }) {
//                                    Icon(
//                                        imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
//                                        contentDescription = "Like",
//                                        tint = if (isFavourite) Color.Red else Color.White
//                                    )
//                                }
//
//
//                                IconButton(
//                                    onClick = {
//                                        //                                    if(!isDownloaded.value){
//                                        //                                        downloaderViewModel.onEvent(DownloadEvent.downloadSong(track))
//                                        //                                        inDownloadQueue = true
//                                        //                                    }
//                                    }
//                                ) {
//                                    //                                if(isDownloading.value){
//                                    //                                    CircularProgressIndicator(
//                                    //                                        modifier = Modifier,
//                                    //                                        progress = downloadProgress?.div(100.toFloat()) ?: 0f,
//                                    //                                        color = Color.LightGray
//                                    //                                    )
//                                    //                                    Text(text = "${downloadProgress}%", color = Color.White, fontSize = 14.sp)
//                                    //                                }
//
//                                    //                                else{
//                                    Icon(
//                                        modifier = Modifier.weight(1f),
//                                        imageVector = Icons.Default.Download,
//                                        //                                        imageVector = if(isDownloaded.value) Icons.Default.DownloadDone else if (inDownloadQueue) Icons.Filled.HourglassTop else Icons.Default.FileDownload,
//                                        contentDescription = "Download",
//                                        tint = Color.White
//                                    )
//                                    //                                }
//                                }
//
//                                IconButton(onClick = { isExpandedDropDown = !isExpandedDropDown }) {
//                                    Icon(
//                                        Icons.Default.MoreHoriz,
//                                        contentDescription = "Menu",
//                                        tint = Color.White
//                                    )
//                                    InfoDropdownMenu(
//                                        onDismissRequest = {
//                                            isExpandedDropDown = false
//                                        },
//                                        expended = isExpandedDropDown
//                                    )
//                                }
//
//                            }
//
//
//                    }
//                    Spacer(
//                        modifier = Modifier
//                            .height(16.dp)
//                    )
//
//                }
//
//
//
//                data?.list?.let { item ->
//                    itemsIndexed(item) { index, track ->
//
//                        if (track != null) {
//                            SongItemRepresentation(
//                                track = track,
//                                index = index,
//                                favViewModel = favViewModel,
//                                downloaderViewModel = downloaderViewModel,
//                                onTrackClicked = {
//                                    onSongClicked(index)
//                                }
//                            )
//                        }
//                    }
//
//                }
//                item {
//                    Spacer(
//                        modifier = Modifier
//                            .height(125.dp)
//                    )
//                }
//
//            }
//
//        }
//
//}
//