package com.example.composeyy1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.composeyy1.data.VideoInfo
import com.example.composeyy1.ui.widget.VideoList
import com.example.composeyy1.ui.widget.VideoPlayer

class MainActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoViewModel = VideoViewModel()
        setContent {
            val videoId: String by videoViewModel.videoId.observeAsState("")
            val videoInfoList: List<VideoInfo> by videoViewModel.videoInfoList.observeAsState(
                getFakeData()
            )

            Column {
                VideoPlayer(ytVideoId = videoId)
                Spacer(modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .background(Color.LightGray))
                VideoList(
                    videoInfoList,
                    onClickItemCallBack = { videoInfo: VideoInfo ->
                        videoViewModel.onVideoIdChange(videoInfo.id)
                    },
                    onRemoveItemCallBack = { key: String ->
                        val newList = ArrayList(videoInfoList)
                        newList.removeAt(key.toInt())
                        videoViewModel.onVideoInfoListLoaded(newList)
                    })
            }
        }
    }
}

private fun getFakeData(): List<VideoInfo> {
    return listOf<VideoInfo>(
        VideoInfo(
            "NYdl3-PxEhQ",
            "夏夜晚風 Summer night wind",
            "https://img.youtube.com/vi/NYdl3-PxEhQ/mqdefault.jpg"
        ),
        VideoInfo(
            "7jYDYon4sGQ",
            "Last Dance",
            "https://img.youtube.com/vi/7jYDYon4sGQ/mqdefault.jpg"
        ),
        VideoInfo(
            "Ngyz1gmZzb0",
            "秘密 Secret",
            "https://img.youtube.com/vi/Ngyz1gmZzb0/mqdefault.jpg"
        ),
        VideoInfo(
            "NYdl3-PxEhQ",
            "夏夜晚風 Summer night wind",
            "https://thumbs.gfycat.com/FixedBountifulFurseal-size_restricted.gif"
        ),
        VideoInfo(
            "7jYDYon4sGQ",
            "Last Dance",
            "https://img.youtube.com/vi/GGGGGGGG/mqdefault.jpg" // 壞的
        ),
        VideoInfo(
            "Ngyz1gmZzb0",
            "秘密 Secret",
            "https://img.youtube.com/vi/Ngyz1gmZzb0/mqdefault.jpg"
        ),
        VideoInfo(
            "NYdl3-PxEhQ",
            "夏夜晚風 Summer night wind",
            "https://img.youtube.com/vi/NYdl3-PxEhQ/mqdefault.jpg"
        ),
        VideoInfo(
            "7jYDYon4sGQ",
            "Last Dance",
            "https://img.youtube.com/vi/GGGGGGGG/mqdefault.jpg" // 壞的
        ),
        VideoInfo(
            "Ngyz1gmZzb0",
            "秘密 Secret",
            "https://img.youtube.com/vi/Ngyz1gmZzb0/mqdefault.jpg"
        ),
        VideoInfo(
            "NYdl3-PxEhQ",
            "夏夜晚風 Summer night wind",
            "https://img.youtube.com/vi/NYdl3-PxEhQ/mqdefault.jpg"
        ),
        VideoInfo(
            "7jYDYon4sGQ",
            "Last Dance",
            "https://img.youtube.com/vi/GGGGGGGG/mqdefault.jpg" // 壞的
        ),
        VideoInfo(
            "_6TtTRrno3E",
            "車車",
            "https://img.youtube.com/vi/_6TtTRrno3E/mqdefault.jpg"
        )
    )
}