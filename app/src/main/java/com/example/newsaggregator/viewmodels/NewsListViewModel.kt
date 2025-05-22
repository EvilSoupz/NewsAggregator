package com.example.newsaggregator.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsaggregator.domain.usecases.GetAllNewsUseCase
import com.example.newsaggregator.domain.usecases.GetDomainListUseCase
import com.example.newsaggregator.domain.usecases.GetNewsListByDomainUseCase
import com.example.newsaggregator.domain.usecases.UpdateNewListUseCase
import com.example.newsaggregator.ui.NewsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val getAllNewsUseCase: GetAllNewsUseCase,
    private val getDomainListUseCase: GetDomainListUseCase,
    private val updateNewListUseCase: UpdateNewListUseCase,
    private val getNewsListByDomainUseCase: GetNewsListByDomainUseCase,
) : ViewModel() {
    private val _screenStateFlow: MutableStateFlow<NewsListScreenState> =
        MutableStateFlow(NewsListScreenState.Loading)
    val screenStateFlow: StateFlow<NewsListScreenState> = _screenStateFlow.asStateFlow()

    private var oldFirst = false

    init {
        getAllNews()
    }

   fun getAllNews() {
       _screenStateFlow.update { NewsListScreenState.Loading }
        viewModelScope.launch {
            _screenStateFlow.update {
                try {
                    val result = getAllNewsUseCase()
                    if (result.isEmpty()) {
                        NewsListScreenState.Error("EmptyNewsList")
                    } else {
                        val domainList = getDomainListUseCase()
                        if (oldFirst) {
                            NewsListScreenState.Success(
                                newsList = result.sortedBy { it.dcDate },
                                domainsList = domainList
                            )
                        } else {
                            NewsListScreenState.Success(
                                newsList = result.sortedByDescending { it.dcDate },
                                domainsList = domainList
                            )
                        }
                    }
                } catch (e: Throwable) {
                    NewsListScreenState.Error("${e.message}")
                }
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
        _screenStateFlow.value = NewsListScreenState.Loading
        viewModelScope.launch {
            runCatching {
                updateNewListUseCase()
                getAllNews()
            }.onFailure {
                _screenStateFlow.value = NewsListScreenState.Error(it.message ?: "Ошибка загрузки")
            }
        }
    }

    fun getNewsByCategoryDomain(domain: String) {
        require(_screenStateFlow.value is NewsListScreenState.Success)
        val oldState = _screenStateFlow.value as NewsListScreenState.Success
        _screenStateFlow.value = NewsListScreenState.Loading
        viewModelScope.launch {
            val newState = try {
                val result = getNewsListByDomainUseCase(domain)
                if (result.isEmpty()) {
                    NewsListScreenState.Error("EmptyNewsList")
                } else {
                    if (oldFirst) {
                        oldState.copy(newsList = result.sortedBy { it.dcDate })
                    } else {
                        oldState.copy(newsList = result.sortedByDescending { it.dcDate })
                    }
                }
            } catch (e: IOException) {
                NewsListScreenState.Error("IOException")
            }
            _screenStateFlow.value = newState
        }
    }
}


sealed interface NewsListScreenState {
    data class Success(
        val newsList: List<NewsItem>,
        val domainsList: List<String>,
    ) : NewsListScreenState

    data object Loading : NewsListScreenState
    data class Error(val msg: String) : NewsListScreenState
}