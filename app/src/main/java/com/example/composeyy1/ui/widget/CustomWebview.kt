package com.example.composeyy1.ui.widget

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

var webView: WebView? = null

@Composable
fun CustomWebView(
    modifier: Modifier = Modifier,
    url: String,
    width: Int,
    onGetWebViewRef: (webView: WebView?) -> Unit,
) {
    Log.d("TAG", "CustomWebView ${url}")
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
    Log.d("TAG", "CustomWebView")
    AndroidView(modifier = modifier, factory = { ctx ->
        WebView(ctx).apply {
            webView = this
            webView!!.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            this.webViewClient = webViewClient
            this.webChromeClient = webViewChromeClient
            settings.pluginState = WebSettings.PluginState.ON_DEMAND
            settings.mediaPlaybackRequiresUserGesture = false // 需要加否則會一直 unstarted
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.useWideViewPort = true
//            settings.loadWithOverviewMode = true
            addJavascriptInterface(WebViewInterface(), "ReactNativeWebView")
            Log.d("TAG", "CustomWebView ${url}&w=${width}&h=${width/1.77}")
            loadUrl("${url}&w=${width}&h=${width/1.77}")
            onGetWebViewRef(this)
        }
    }, update = {
        Log.d("TAG", "CustomWebView ${url}&w=${width}&h=${width/1.77}")
        it.loadUrl("${url}&w=${width}&h=${width/1.77}")
    })
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
private class WebViewInterface {
    @JavascriptInterface
    fun postMessage(msg: String) {
        Log.d("TAG", msg)
        if (msg.contains("onPlayerReady")) {
            Log.d("TAG onPlayerReady", msg)
            postMessage(webView, "playVideo")
        } else if (msg.contains("onPlayerError")) {

        }
    }
}

fun postMessage(webView: WebView?, method: String) {
    val script =
        "handleReactNativeMessage({\"data\":\"{\\\"method\\\":\\\"${method}\\\",\\\"payload\\\":\\\"\\\"}\"})"
    webView?.evaluateJavascript(script) { call ->
        Log.d("TAG", "fine")
    }
}