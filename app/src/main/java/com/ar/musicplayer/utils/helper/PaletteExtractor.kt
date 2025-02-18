package com.ar.musicplayer.utils.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.lifecycle.MutableLiveData
import androidx.palette.graphics.Palette
import com.ar.musicplayer.ui.theme.onBackgroundDark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class PaletteExtractor() {


    fun getColorFromImg(imageUrl: String): MutableLiveData<Color> {
        val color = MutableLiveData<Color>()
        if (imageUrl.isEmpty()) {
            color.postValue(onBackgroundDark)
        } else {
            GlobalScope.launch {
                val bitmap = getBitmapFromURL(imageUrl)
                withContext(Dispatchers.Main) {
                    if (bitmap != null && !bitmap.isRecycled) {
                        val palette: Palette = Palette.from(bitmap).generate()


                        val dominant = palette.dominantSwatch?.rgb?.let { color ->
                            arrayListOf(color.red, color.green, color.blue)

                        }
                        val composeColor =
                            dominant?.get(0)?.let { it1 ->
                                Color(
                                    red = it1,
                                    green = dominant[1],
                                    blue = dominant[2]
                                )
                            }

                        if(composeColor != null){
                            if(composeColor.isColorLight()){
                                color.postValue(composeColor.darkenColor(0.7f))
                            } else{
                                color.postValue(composeColor!!)
                            }
                        }

                    }

                }
            }
        }

        return color
    }


    suspend fun getColorsFromImg(imageUrl: String): List<Color> {
        return if (imageUrl.isEmpty()) {
            listOf(Color.Black, Color.Black, Color.Black, Color.Black)
        } else {

            withContext(Dispatchers.IO) {
                val bitmap = getBitmapFromURL(imageUrl)
                if (bitmap != null && !bitmap.isRecycled) {
                    val palette: Palette = Palette.from(bitmap).generate()

                    val dominant = palette.dominantSwatch?.rgb?.toColor() ?: Color.Black
                    val vibrant = palette.vibrantSwatch?.rgb?.toColor() ?: Color.Black
                    val muted = palette.darkMutedSwatch?.rgb?.toColor() ?: Color.Black
                    val lightVibrant = palette.lightVibrantSwatch?.rgb?.toColor() ?: Color.Black

                    listOf(vibrant, dominant, lightVibrant, muted)
                } else {
                    listOf(Color.Black, Color.Black, Color.Black, Color.Black)
                }
            }
        }
    }



    fun Int.toColor(): Color {
        return Color(android.graphics.Color.red(this) / 255f,
            android.graphics.Color.green(this) / 255f,
            android.graphics.Color.blue(this) / 255f, 1f)
    }




    private fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null

        }
    }



    companion object {
        private const val TAG = "PaletteExtractor"
    }
}


fun Color.isColorLight(): Boolean {
    val darkness = 1 - (0.299 * this.red + 0.587 * this.green + 0.114 * this.blue)
    return darkness < 0.5
}


fun Color.darkenColor(factor: Float = 0.7f): Color {
    val r = (this.red * factor).coerceAtLeast(0f)
    val g = (this.green * factor).coerceAtLeast(0f)
    val b = (this.blue * factor).coerceAtLeast(0f)
    return Color(r, g, b, this.alpha)
}