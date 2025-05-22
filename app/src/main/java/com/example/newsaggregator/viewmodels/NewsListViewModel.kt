package com.example.newsaggregator.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsaggregator.R
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

    init {
        getAllNews()
    }

    fun onRetryClicked() {
        getAllNews()
    }

    fun onOptionSelected(sortOption: SortOption) {
        val currentState = _screenStateFlow.value as NewsListScreenState.Success
        if (currentState.selectedSortOption == sortOption) return

        _screenStateFlow.value = currentState.copy(
            selectedSortOption = sortOption
        )
        getAllNews()
    }

    fun onScreenRefreshed() {
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

    fun onCategoryDomainSelected(domain: String) {
        val previousSuccessState = _screenStateFlow.value as NewsListScreenState.Success
        _screenStateFlow.value = NewsListScreenState.Loading

        viewModelScope.launch {
            val newState = try {
                val result = getNewsListByDomainUseCase(domain)
                if (result.isEmpty()) {
                    NewsListScreenState.Error("EmptyNewsList")
                } else {
                    val newsList =
                        if (previousSuccessState.selectedSortOption.type == SortType.OLDEST_FIRST) {
                            result.sortedBy { it.dcDate }
                        } else {
                            result.sortedByDescending { it.dcDate }
                        }
                    previousSuccessState.copy(newsList = newsList)
                }
            } catch (e: IOException) {
                NewsListScreenState.Error("IOException")
            }
            _screenStateFlow.value = newState
        }
    }

    private fun getAllNews() {
        val previousSuccessState = _screenStateFlow.value as? NewsListScreenState.Success
        val selectedSortType = previousSuccessState?.selectedSortOption?.type ?: SortType.NEWEST_FIRST
        _screenStateFlow.update { NewsListScreenState.Loading }

        viewModelScope.launch {
            _screenStateFlow.update {
                try {
                    val result = getAllNewsUseCase()
                    if (result.isEmpty()) {
                        NewsListScreenState.Error("EmptyNewsList")
                    } else {
                        val domainList = getDomainListUseCase()
                        val newsList = if (selectedSortType == SortType.NEWEST_FIRST) {
                            result.sortedByDescending { it.dcDate }
                        } else {
                            result.sortedBy { it.dcDate }
                        }
                        previousSuccessState?.copy(
                            newsList = newsList,
                            domainsList = domainList
                        ) ?: NewsListScreenState.Success(
                            newsList = newsList,
                            domainsList = domainList
                        )
                    }
                } catch (e: Throwable) {
                    NewsListScreenState.Error("${e.message}")
                }
            }
        }
    }
}

sealed interface NewsListScreenState {

    data class Success(
        val newsList: List<NewsItem>,
        val domainsList: List<String>,
        val availableSortOptions: List<SortOption> = listOf(
            SortOption(SortType.NEWEST_FIRST, R.string.sort_by_newest_title),
            SortOption(SortType.OLDEST_FIRST, R.string.sort_by_oldest_title)
        ),
        val selectedSortOption: SortOption = SortOption(
            SortType.NEWEST_FIRST,
            R.string.sort_by_newest_title
        )
    ) : NewsListScreenState

    data object Loading : NewsListScreenState
    data class Error(val msg: String) : NewsListScreenState
}