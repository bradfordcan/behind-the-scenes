package com.example.devbyteviewer.repository

import android.service.autofill.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.devbyteviewer.database.Video
import com.example.devbyteviewer.database.VideosDatabase
import com.example.devbyteviewer.database.asDomainModel
import com.example.devbyteviewer.network.Network
import com.example.devbyteviewer.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
/**
 * Repository for fetching devbyte videos from the network and storing them on disk.
 * */
class VideosRepository(private val database:  VideosDatabase) {

    val videos: LiveData<List<Video>> = Transformations.map(database.videoDao.getVideos()) {
        it.asDomainModel()
    }

    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            val playlist = Network.devbytes.getPlaylist().await()
            database.videoDao.insertAll(*playlist.asDatabaseModel())
        }
    }
}