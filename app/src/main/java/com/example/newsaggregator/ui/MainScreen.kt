package com.example.newsaggregator.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newsaggregator.viewmodels.NewsListScreenState
import com.example.newsaggregator.viewmodels.NewsListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onItemClick: (String) -> Unit,
    viewModel: NewsListViewModel,
) {
    when (val screenState = viewModel.screenStateFlow.collectAsState().value) {
        is NewsListScreenState.Error -> {
            ErrorScreen(msg = screenState.msg) {
                    viewModel.getAllNews()
            }
        }
        is NewsListScreenState.Loading -> {
           LoadingScreen()
        }
        is NewsListScreenState.Success -> {
            var sortMenuState by remember { mutableStateOf(false) }
            var domainMenuState by remember { mutableStateOf(false) }

            Box(modifier = Modifier.fillMaxSize()) {
                val lazyColumnState = rememberLazyListState()
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )  //search
                        }
                        Box() {
                            IconButton(
                                onClick = {
                                    sortMenuState = !sortMenuState
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Build,
                                    contentDescription = null
                                )  // filter //заменить иконку
                            }
                            DropdownMenu(
                                expanded = sortMenuState,
                                onDismissRequest = {
                                    sortMenuState = false
                                }
                            ) {
                                DropdownMenuItem(text = { Text("New First") }, onClick = {
                                    viewModel.sortNewFirst()
                                    sortMenuState = false
                                })
                                DropdownMenuItem(text = { Text("Old First") }, onClick = {
                                    viewModel.sortOldFirst()
                                    sortMenuState = false
                                })
                            }
                        }
                        Box {
                            IconButton(
                                onClick = {
                                    domainMenuState = !domainMenuState
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBox,
                                    contentDescription = null
                                )  //sort//заменить иконку
                            }
                            DropdownMenu(
                                expanded = domainMenuState,
                                onDismissRequest = { domainMenuState = false }
                            ) {
                                screenState.domainsList.forEach { domain ->
                                    DropdownMenuItem(
                                        text = { Text(domain) },
                                        onClick = {
                                            viewModel.getNewsByCategoryDomain(domain)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    PullToRefreshBox(
                        onRefresh = {
                            viewModel.updateAndGetAllNews()
                        },
                        isRefreshing = false
                    ) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            state = lazyColumnState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                        ) {
                            items(screenState.newsList) { item ->
                                NewsItemCard(
                                    item = item,
                                    onClick = { guid ->
                                        onItemClick(guid)
                                    },
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }
                        if (lazyColumnState.canScrollBackward) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.BottomEnd
                            ) {
                                FloatingActionButton(
                                    onClick = {
                                        lazyColumnState.requestScrollToItem(0)
                                    }
                                ) {
                                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
