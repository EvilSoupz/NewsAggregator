package com.example.newsaggregator.data.rss

import com.example.newsaggregator.data.rss.dto.RssDto

interface OnlineRssRepository {
    suspend fun getRss(): RssDto
}

