package com.example.composeyy1.ui.widget

import android.util.Log
import android.webkit.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow

var webViewRef: WebView? = null
var playing = MutableStateFlow(false)

//const val host = "https://www.youtube.com/watch?v="
const val host = "https://hstsou.github.io/ytPage/?video_id="
//const val host =  "https://www.youtube.com/embed/"

@Composable
fun VideoPlayer(
    ytVideoId: String = "",
) {
    var screenWidth by remember { mutableStateOf(0) }

    Column {
        CustomWebView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.78F)
                .onSizeChanged { screenWidth = it.width },
            url = "${host}${ytVideoId}",
//            width = LocalDensity.current.run { screenWidth.dp.toPx() }.toInt(),
            width = LocalDensity.current.run { screenWidth.toDp() }.value.toInt(),
            onGetWebViewRef = { webView: WebView? ->
                webViewRef = webView
            })


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(4.dp)
                .background(color = Color.Transparent)
        ) {
            Text(
                text = "Play/Pause",
                fontSize = 30.sp,
                modifier = Modifier.clickable {
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
