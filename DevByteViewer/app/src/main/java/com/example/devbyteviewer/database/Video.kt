package com.example.devbyteviewer.database

import com.example.devbyteviewer.util.smartTruncate


data class Video constructor(
    val url: String,
    val updated: String,
    val title: String,
    val description: String,
    val thumbnail: String
) {
    /**
     * Short description is used for displaying truncated descriptions in the UI
     */
    val shortDescription: String
        get() = description.smartTruncate(200)
}