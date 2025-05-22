package com.example.newsaggregator.domain

import com.example.newsaggregator.data.CurrentAppRepository
import javax.inject.Inject


class GetAllNewsUseCase @Inject constructor( val repository: CurrentAppRepository) {


}