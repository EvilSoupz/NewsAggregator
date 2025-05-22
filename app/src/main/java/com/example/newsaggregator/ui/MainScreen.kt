package com.example.newsaggregator.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.newsaggregator.ui.NewsItem
import com.example.newsaggregator.viewmodels.NewsListScreenState
import com.example.newsaggregator.viewmodels.NewsListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onItemClick: (String) -> Unit,
    viewModel: NewsListViewModel,

    ) {
    when (val screenState = viewModel.screenState) {
        is NewsListScreenState.Error -> Text(text = screenState.msg)
        is NewsListScreenState.Loading -> Text("Loading")
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

                        Box() {
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
                                NewsItem(
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

@Composable
fun NewsItem(
    item: NewsItem,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clickable { onClick(item.guid) }) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = item.pictures[0],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.weight(1f)
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(3f)
                    .padding(2.dp), verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = item.title, fontWeight = FontWeight.SemiBold, maxLines = 2)
                    Text(text = "Creator: " + item.dcCreator, fontSize = 12.sp)
                    val shortDesc = item.description.drop(3).substringBefore("</")
                    Text(text = shortDesc, maxLines = 6)

                }
                Column {
                    Text(text = "${item.categories}", fontSize = 12.sp, maxLines = 1)
                    Text(text = item.dcDate, fontSize = 12.sp)
                }


            }

        }
    }
}