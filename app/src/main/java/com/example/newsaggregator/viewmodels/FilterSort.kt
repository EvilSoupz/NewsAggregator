package com.example.newsaggregator.viewmodels

import androidx.annotation.StringRes

data class SortOption(
    val type: SortType,
    @StringRes val title: Int,
)