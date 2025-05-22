package com.example.newsaggregator.domain.usecases

import com.example.newsaggregator.domain.CurrentAppRepository
import javax.inject.Inject

class UpdateNewListUseCase @Inject constructor(private val repository: CurrentAppRepository) {

    suspend operator fun invoke() {
        repository.updateNewsList()
    }
}
