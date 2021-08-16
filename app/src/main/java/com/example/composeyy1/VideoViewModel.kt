package com.example.composeyy1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composeyy1.data.VideoInfo


class VideoViewModel : ViewModel() {
    private val _videoInfoList = MutableLiveData<List<VideoInfo>>()
    val videoInfoList: MutableLiveData<List<VideoInfo>> = _videoInfoList

    private val _videoId = MutableLiveData("")
    val videoId: MutableLiveData<String> = _videoId

    fun onVideoIdChange(comingId: String) {
        _videoId.postValue(comingId)
    }

    fun onVideoInfoListLoaded(videoInfos: List<VideoInfo>) {
        _videoInfoList.postValue(videoInfos)
    }
}

