package com.nammavastra.app.data.model

data class Saree(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val category: String = "",
    val material: String = "",
    val price: Double = 0.0,
    val uploadedAt: Long = System.currentTimeMillis() 
)
