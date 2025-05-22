package com.example.newsaggregator.domain.usecases

import com.example.newsaggregator.domain.CurrentAppRepository
import com.example.newsaggregator.ui.NewsItem
import javax.inject.Inject

class GetNewsListByDomainUseCase @Inject constructor(private val repository: CurrentAppRepository) {

    suspend operator fun invoke(domain: String): List<NewsItem> {
        return repository.getNewsListByCategoryDomain(domain)
    }
}