package com.example.composeyy1.ui.widget

import android.util.Log
import android.webkit.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow

var webViewRef: WebView? = null
var playing = MutableStateFlow(false)

@Composable
fun VideoPlayer(
    ytVideoId: String = "",
) {
    Log.d("TAG", "VideoPlayer")
    /***
     * mutableStateOf -> update to trigger recompose
     *
     * remember ->
     * To not have a different state for every recomposition, remember the mutable state using remember.
     *
     *
     * Composable functions can store a single object in memory by using the remember composable.
     * A value computed by remember is stored in the Composition during initial composition,
     * and the stored value is returned during recomposition.
     * remember can be used to store both mutable and immutable objects.
     */
    var screenWidth by remember { mutableStateOf(0) }
//    val (screenWidth, setScreenWidth) = remember { mutableStateOf(0) } // every time reset


    Column {
        YTWebView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.78F) // 16/9
                .onSizeChanged {
                    Log.d("TAG", "VideoPlayer onSizeChanged")
                    screenWidth = it.width
//                    setScreenWidth(it.width)
                },
            id = ytVideoId,
            width = LocalDensity.current.run { screenWidth.toDp() }.value.toInt(),
            onGetWebViewRef = { webView: WebView? ->
                webViewRef = webView
            },
            isPlayingCB = { isPlaying: Boolean ->
                playing.value = isPlaying
            }
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
                .padding(4.dp)
                .border(
                    width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp)
                )
//                .background(color = Color.Black)
        ) {
            Text(
                text = "Play/Pause",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier
                    .padding(4.dp)
                    .clickable {
                        Log.d("TAG", "clickable")
                        if (playing.value) {
                            postMessage(webViewRef, "pauseVideo")
                            playing.value = false
                        } else {
                            postMessage(webViewRef, "playVideo")
                            playing.value = true
                        }
                    }
            )
        }
    }
}
