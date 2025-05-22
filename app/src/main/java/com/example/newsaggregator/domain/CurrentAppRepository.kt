package com.example.newsaggregator.domain

import com.example.newsaggregator.ui.NewsItem

interface CurrentAppRepository {
    suspend fun getAllNewsList(): List<NewsItem>

    suspend fun getDomainList(): List<String>

    suspend fun updateNewsList()

    suspend fun getNewsListByCategoryDomain(domain: String): List<NewsItem>
}