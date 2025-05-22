package com.example.newsaggregator.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pictures")
data class PicturesEntity(
    @PrimaryKey
    val url: String,
    val newsGuid: String
)