package com.example.composeyy1.ui.widget

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.example.composeyy1.data.VideoInfo

@ExperimentalFoundationApi
@Composable
fun VideoList(
    videoInfoList: List<VideoInfo>,
    onClickItemCallBack: (videoInfo: VideoInfo) -> Unit,
    onRemoveItemCallBack: (key: String) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    ) {
        items(
            count = videoInfoList.size,
            key = { index -> "$index" }
        ) { index ->
            VideoTile(
                key = "$index",
                videoInfo = videoInfoList[index],
                onClickItemCallBack = { videoInfo: VideoInfo ->
                    onClickItemCallBack(videoInfo)
                },
                onRemoveItemCallBack = { key: String ->
                    onRemoveItemCallBack(key)
                }
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun VideoTile(
    key: String,
    videoInfo: VideoInfo,
    onClickItemCallBack: (videoInfo: VideoInfo) -> Unit,
    onRemoveItemCallBack: (key: String) -> Unit,
) {
    Card(
        modifier = Modifier
            .combinedClickable(onClick = {
                Log.d("TAG", "onClick ${videoInfo.title} and vId = ${videoInfo.id}")
                onClickItemCallBack(videoInfo)
            }, onLongClick = {
                Log.d("TAG", "onRemoveItemCallBack ${videoInfo.title} and key = $key")
                onRemoveItemCallBack(key)
            })
            .padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            VideoPic(videoInfo.imageUrl)
            VideoTitle(modifier = Modifier.weight(1f), title = videoInfo.title)
        }
    }
}

@Composable
fun VideoPic(
    imageUrl: String,
) {
    val painter = rememberImagePainter(
        data = imageUrl,
        builder = {
            crossfade(true)
            transformations(RoundedCornersTransformation())
        }
    )

    Box(
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(96.dp)
        )

        when (painter.state) {
            is ImagePainter.State.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            is ImagePainter.State.Error -> {
                Text(
                    text = "error", fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun VideoTitle(
    modifier: Modifier = Modifier,
    title: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title, fontSize = 20.sp
        )
    }
}