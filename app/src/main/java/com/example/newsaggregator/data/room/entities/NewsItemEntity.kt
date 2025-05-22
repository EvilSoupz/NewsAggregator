package com.example.newsaggregator.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsItemEntity(
    @PrimaryKey
    val guid: String,
    val title: String,
    val description: String,
    val dcCreator: String,
    val dcDate: String,
)