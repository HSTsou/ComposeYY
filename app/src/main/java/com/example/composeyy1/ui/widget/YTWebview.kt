package com.example.composeyy1.ui.widget

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper.getMainLooper
import android.util.Log
import android.webkit.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

var webView: WebView? = null
var vId: String? = ""

//const val host = "https://www.youtube.com/watch?v="
const val host = "https://hstsou.github.io/ytPage/?video_id="
//const val host =  "https://www.youtube.com/embed/"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YTWebView(
    modifier: Modifier = Modifier,
    id: String,
    width: Int,
    onGetWebViewRef: (webView: WebView?) -> Unit,
    isPlayingCB: (isPlaying: Boolean) -> Unit,
) {
    Log.d("TAG", "CustomWebView start $id")
    vId = id
    val realUrl = "$host${vId}&w=${width}&h=${width / 1.77}"

    val webViewChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
//            onProgressChange(newProgress)
            super.onProgressChanged(view, newProgress)
            Log.d("TAG", "onProgressChanged")
        }
    }
    val webViewClient = object : WebViewClient() {
        override fun onPageStarted(
            view: WebView?, url: String?,
            favicon: Bitmap?
        ) {
            super.onPageStarted(view, url, favicon)
//            onProgressChange(-1)
            Log.d("TAG", "!onPageStarted")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
//            onProgressChange(100)
            Log.d("TAG", "!onPageFinished = " + url)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (null == request?.url) return false
            val showOverrideUrl = request.url.toString()
            try {
                if (!showOverrideUrl.startsWith("http://")
                    && !showOverrideUrl.startsWith("https://")
                ) {

                    Log.d("TAG", "!showOverrideUrl")
                    return true
                }
            } catch (e: Exception) {
                Log.d("TAG", "shouldOverrideUrlLoading Exception")
                return true
            }
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            Log.d("TAG", "onReceivedError")
//            onReceivedError(error)
        }
    }


    AndroidView(modifier = modifier, factory = { ctx ->
        WebView(ctx).apply {
            webView = this
//            webView!!.layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            ) 會版面過大
            this.webViewClient = webViewClient
            this.webChromeClient = webViewChromeClient
//            settings.pluginState = WebSettings.PluginState.ON_DEMAND 可不用
            settings.mediaPlaybackRequiresUserGesture = false // 需要加否則會一直 unstarted
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.useWideViewPort = true
            isVerticalFadingEdgeEnabled = false
            isHorizontalScrollBarEnabled = false
//            settings.loadWithOverviewMode = true

            val webViewInterface = WebViewInterface();
            webViewInterface.onVideoListener = object : OnVideoListener {
                override fun isPlaying(playing: Boolean) {
                    Log.d("TAG", "isPlaying $playing")
                    isPlayingCB(playing)
                }

                override fun onReady() {
                    Log.d("TAG", "onReady $vId")
                    if (vId!!.isNotEmpty()) {
                        postMessage(webView, "playVideo")
                        isPlayingCB(true)
                    }
                }
            }

            addJavascriptInterface(webViewInterface, "ReactNativeWebView")
            Log.d("TAG", "CustomWebView $realUrl")
            loadUrl(realUrl)
            onGetWebViewRef(this)
        }
    }, update = {
        Log.d("TAG", "CustomWebView $realUrl")
        it.loadUrl(realUrl)
    })
}

interface OnVideoListener {
    fun isPlaying(playing: Boolean)

    fun onReady()
}

private class WebViewInterface {

    var onVideoListener: OnVideoListener? = null

    @JavascriptInterface
    fun postMessage(msg: String) {
        Log.d("TAG", msg)
        if (msg.contains("onPlayerReady")) {
            Handler(getMainLooper()).postDelayed({
                onVideoListener?.onReady()
            }, 1000)
        } else if (msg.contains("onPlayerError")) {
            onVideoListener?.isPlaying(false)
        }
    }
}

fun postMessage(webView: WebView?, method: String) {
    val script =
        "handleReactNativeMessage({\"data\":\"{\\\"method\\\":\\\"${method}\\\",\\\"payload\\\":\\\"\\\"}\"})"
    webView?.evaluateJavascript(script) { call ->
        Log.d("TAG", "evaluateJavascript done")
    }
}


// =========== 一年前寫法 ===========

//fun AndroidWebView(url: String) {
//    return AndroidView(R.layout.webview) { view ->
//        val webView = view as WebView
//        WebViewRef.webViewInstance = webView
//        webView.settings.javaScriptEnabled = true
//        webView.settings.allowFileAccess = true
//        webView.settings.allowContentAccess = true
//        webView.addJavascriptInterface(WebViewInterface(), "ReactNativeWebView")
//        webView.loadUrl("https://movie2019.appspot.com/?video_id=" + url)
//    }
//}

//private fun postMessage(method: String) {
//    val script = "handleReactNativeMessage({\"data\":\"{\\\"method\\\":\\\"${method}\\\",\\\"payload\\\":\\\"\\\"}\"})"
//    WebViewRef.webViewInstance?.evaluateJavascript(script, { call ->
//        Log.d("TAG", "fine")
//    })
//}
//