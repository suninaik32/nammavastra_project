package com.nammavastra.app.data.model

data class Trend(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val category: String = "",
    val badge: String = "", // e.g., "Trending", "New", "Classic"
    var isSaved: Boolean = false
)
