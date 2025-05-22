package com.example.newsaggregator.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.newsaggregator.R

@Composable
fun NewsItemCard(
    item: NewsItem,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
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
                    Text(text = stringResource(R.string.creator) + item.dcCreator, fontSize = 12.sp)
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