package com.example.newsaggregator.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.example.newsaggregator.R
import com.example.newsaggregator.navigation.NewsAggregatorAppNavigationGraph
import com.example.newsaggregator.navigation.Screen

@Composable
fun NewsAggregatorApp() {

    val navController = rememberNavController()

    Scaffold(
        topBar = { TopBar(modifier = Modifier.fillMaxWidth()) }
    ) { innerPadding ->
        NewsAggregatorAppNavigationGraph(
            navController,
            modifier = Modifier.padding(innerPadding),
            newsListScreenContent = { viewModel ->
                MainScreen(
                    viewModel = viewModel,
                    onItemClick = { guid ->
                        navController.navigate(Screen.NewsOverview(guid))
                    }
                )
            },
            newsOverviewScreenContent = { url ->
                NewsOverviewScreen(url)
            }
        )
    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) { Text(text = stringResource(R.string.newsaggregator)) }
}