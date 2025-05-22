package com.example.newsaggregator.data.room


import com.example.newsaggregator.data.room.entities.NewsWithPictureAndCategories


interface RoomRepository {

    suspend fun getAllNews(): List<NewsWithPictureAndCategories>

    suspend fun getDomainList(): List<String>

    suspend fun deleteAll()

}