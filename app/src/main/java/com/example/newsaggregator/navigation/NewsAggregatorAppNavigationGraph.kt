package com.example.newsaggregator.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.newsaggregator.viewmodels.NewsListViewModel
import kotlinx.serialization.Serializable

@Composable
fun NewsAggregatorAppNavigationGraph(
    navController: NavHostController,
    newsListScreenContent: @Composable (viewmodel: NewsListViewModel) -> Unit,
    newsOverviewScreenContent: @Composable (url: String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NewsList,
        modifier = modifier
    ) {
        composable<Screen.NewsList> {
            val viewModel = hiltViewModel<NewsListViewModel>()
            newsListScreenContent(viewModel)
        }
        composable<Screen.NewsOverview> {
            val url = it.toRoute<Screen.NewsOverview>().guid
            newsOverviewScreenContent(url)
        }
    }
}

@Serializable
sealed interface Screen {
    @Serializable
    data object NewsList

    @Serializable
    data class NewsOverview(val guid: String)
}