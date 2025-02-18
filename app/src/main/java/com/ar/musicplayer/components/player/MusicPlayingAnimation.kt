package com.ar.musicplayer.components.player

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun MusicPlayingAnimation(isPlaying: Boolean, modifier: Modifier = Modifier) {
    val animatables = remember { List(5) { Animatable(0f) } }
    LaunchedEffect(Unit) {
        if(!isPlaying) {
            animatables.forEachIndexed { index, item ->
                launch {
                    item.snapTo(0.2f * index)
                }

            }
        }
    }
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            animatables.forEachIndexed { index, animatable ->
                launch {
                    animatable.animateTo(
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(500, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse,
                            initialStartOffset = StartOffset(index * 100)
                        )
                    )
                }
            }
        } else {
            animatables.forEach { it.stop() }
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        animatables.forEach { item ->
            MusicBar(heightFraction = { item.value })
        }
    }
}

@Composable
fun MusicBar(heightFraction: () -> Float) {

    Box(modifier = Modifier
        .width(5.dp)
        .fillMaxHeight()
        .drawBehind {
            val barHeight = heightFraction() * 100
            drawRect(
                color = Color.Blue,
                topLeft = Offset(0f, size.height - barHeight),
                size = Size(size.width, barHeight)
            )
        }
    )
}

