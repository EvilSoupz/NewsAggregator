package com.example.newsaggregator.domain.usecases

import com.example.newsaggregator.domain.CurrentAppRepository
import com.example.newsaggregator.ui.NewsItem
import javax.inject.Inject


class GetAllNewsUseCase @Inject constructor(private val repository: CurrentAppRepository) {

    suspend operator fun invoke(): List<NewsItem> {
        return repository.getAllNewsList()
    }
}