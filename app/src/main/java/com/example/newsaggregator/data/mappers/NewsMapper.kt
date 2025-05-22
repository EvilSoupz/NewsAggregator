package com.example.newsaggregator.data.mappers

import com.example.newsaggregator.data.room.entities.CategoryEntity
import com.example.newsaggregator.data.room.entities.NewsItemEntity
import com.example.newsaggregator.data.room.entities.NewsWithPictureAndCategories
import com.example.newsaggregator.data.room.entities.PicturesEntity
import com.example.newsaggregator.data.rss.dto.CategoryDto
import com.example.newsaggregator.data.rss.dto.ContentDto
import com.example.newsaggregator.data.rss.dto.ItemDto
import com.example.newsaggregator.ui.NewsItem

fun ItemDto.toNewsWithPictureAndCategories(): NewsWithPictureAndCategories =
    NewsWithPictureAndCategories(
        newsItem = NewsItemEntity(
            guid = this.guid,
            title = this.title,
            description = this.description,
            dcCreator = this.dcCreator,
            dcDate = this.dcDate,
        ),
        pictures = this.contents.toPicturesEntityList(guid = this.guid),
        categories = this.categories.toCategoryEntityList()
    )


private fun List<ContentDto>.toPicturesEntityList(guid: String): List<PicturesEntity> {
    return this.map { PicturesEntity(url = it.url, newsGuid = guid) }

}

private fun List<CategoryDto>.toCategoryEntityList(): List<CategoryEntity> {
    return this.map {
        CategoryEntity(
            value = it.value,
            domain = it.domain.removePrefix("https://www.theguardian.com/").substringBefore("/")
        )
    }
}

fun NewsWithPictureAndCategories.toNewsItem(): NewsItem = NewsItem(
    guid = this.newsItem.guid,
    title = this.newsItem.title,
    description = this.newsItem.description,
    dcCreator = this.newsItem.dcCreator,
    dcDate = this.newsItem.dcDate,
    categories = this.categories.map { it.value },
    pictures = this.pictures.map { it.url }
)