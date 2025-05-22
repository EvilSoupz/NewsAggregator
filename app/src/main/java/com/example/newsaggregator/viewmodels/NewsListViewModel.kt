package com.example.newsaggregator.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsaggregator.data.CurrentAppRepository
import com.example.newsaggregator.domain.GetAllNewsUseCase
import com.example.newsaggregator.ui.NewsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val repository: CurrentAppRepository,
    val getAllNewsUseCase: GetAllNewsUseCase

) : ViewModel() {

    var screenState: NewsListScreenState by mutableStateOf(NewsListScreenState.Loading)
        private set

    private var oldFirst = false

    private var domainList: List<String> = listOf()


    init {
        getAllNews()
    }

    private fun getAllNews() {
        viewModelScope.launch {
            screenState = try {

                val result = repository.getAllNewsList()
                if (result.isEmpty()) {
                    NewsListScreenState.Error("EmptyNewsList")
                } else {
                    domainList = repository.getDomainList()
                    if (oldFirst) {
                        NewsListScreenState.Success(result.sortedBy { it.dcDate }, domainList)
                    } else {
                        NewsListScreenState.Success(
                            result.sortedByDescending { it.dcDate },
                            domainList
                        )
                    }

                }
            } catch (e: Throwable) {
                NewsListScreenState.Error("${e.message}")

            }
        }
    }

    fun sortNewFirst() {
        if (oldFirst) {
            oldFirst = false
            getAllNews()
        }
    }

    fun sortOldFirst() {
        if (!oldFirst) {
            oldFirst = true
            getAllNews()
        }
    }

    fun updateAndGetAllNews() {
        screenState = NewsListScreenState.Loading
        viewModelScope.launch {
            repository.updateNewsList()
            getAllNews()
        }
    }

    fun getNewsByCategoryDomain(domain: String) {
        screenState = NewsListScreenState.Loading
        viewModelScope.launch {

            screenState = try {
                val result = repository.getNewsListByCategoryDomain(domain)
                if (result.isEmpty()) {
                    NewsListScreenState.Error("EmptyNewsList")
                } else {
                    if (oldFirst) {
                        NewsListScreenState.Success(result.sortedBy { it.dcDate }, domainList)
                    } else {
                        NewsListScreenState.Success(
                            result.sortedByDescending { it.dcDate },
                            domainList
                        )
                    }
                }
            } catch (e: IOException) {
                NewsListScreenState.Error("IOException")
            }

        }
    }


}


sealed interface NewsListScreenState {
    data class Success(val newsList: List<NewsItem>, val domainsList: List<String>) :
        NewsListScreenState

    data object Loading : NewsListScreenState
    data class Error(val msg: String) : NewsListScreenState
}