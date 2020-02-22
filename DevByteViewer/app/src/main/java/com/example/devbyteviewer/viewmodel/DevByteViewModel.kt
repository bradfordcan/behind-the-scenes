package com.example.devbyteviewer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.devbyteviewer.database.getDatabase
import com.example.devbyteviewer.repository.VideosRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DevByteViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /*private val _playlist = MutableLiveData<List<Video>>()

    val playlist: LiveData<List<Video>>
        get() = _playlist

    // called immediately when ViewModel is created
    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() = viewModelScope.launch {
        val playlist = Network.devbytes.getPlaylist().await() // await() waits for the network result
        _playlist.postValue(playlist.asDomainModel()) // passes value to live data
    }*/

    private val database = getDatabase(application)
    private val videosRepository = VideosRepository(database)

    init {
        viewModelScope.launch {
            videosRepository.refreshVideos()
        }
    }

    val playlist = videosRepository.videos

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DevByteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DevByteViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }

}