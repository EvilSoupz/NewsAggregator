package com.example.newsaggregator.ui

import androidx.compose.runtime.Immutable

@Immutable
data class NewsItem(
    val guid: String,
    val title: String,
    val description: String,
    val dcCreator: String,
    val dcDate: String,
    val categories: List<String>,
    val pictures: List<String>
)