package com.example.newsaggregator.data.room


interface RoomRepository {

    suspend fun getAllNews(): List<NewsWithPictureAndCategories>

//    suspend fun getNewsByCategory(categories: List<CategoryEntity>): List<NewsWithPictureAndCategories>

    suspend fun getDomainList() : List<String>

    suspend fun deleteAll()

//    suspend fun insertAll(news: List<NewsWithPictureAndCategories>)

    suspend fun insertNews(news: List<NewsItemEntity>)


    suspend fun insertPictures(pictures: List<PicturesEntity>)

    suspend fun insertCategories(categories: List<CategoryEntity>)

    suspend fun insertNewsCategories(newsCategoriesList: List<NewsCategoryCrossRef>)


}