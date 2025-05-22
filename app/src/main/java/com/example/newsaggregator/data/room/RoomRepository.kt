package com.example.newsaggregator.data.room

import com.example.newsaggregator.data.room.entities.CategoryEntity
import com.example.newsaggregator.data.room.entities.NewsCategoryCrossRef
import com.example.newsaggregator.data.room.entities.NewsItemEntity
import com.example.newsaggregator.data.room.entities.NewsWithPictureAndCategories
import com.example.newsaggregator.data.room.entities.PicturesEntity


interface RoomRepository {

    suspend fun getAllNews(): List<NewsWithPictureAndCategories>

    suspend fun getDomainList(): List<String>

    suspend fun deleteAll()

    suspend fun insertNews(news: List<NewsItemEntity>)

    suspend fun insertPictures(pictures: List<PicturesEntity>)

    suspend fun insertCategories(categories: List<CategoryEntity>)

    suspend fun insertNewsCategories(newsCategoriesList: List<NewsCategoryCrossRef>)


}