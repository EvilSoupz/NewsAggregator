package com.example.newsaggregator.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface NewsDao {
    @Transaction
    @Query("SELECT * FROM news")
    suspend fun getAllNews(): List<NewsWithPictureAndCategories>


    @Query("SELECT DISTINCT domain FROM categoryentity")
    suspend fun getDomainList(): List<String>

    @Transaction
    @Query(
        "SELECT * FROM news WHERE guid IN (" +
                "SELECT guid FROM newscategorycrossref WHERE value IN (" +
                "SELECT value FROM categoryentity WHERE domain LIKE :domain))"
    )
    suspend fun getNewsByCategoryDomain(domain: String): List<NewsWithPictureAndCategories>

    @Transaction
    @Query("DELETE FROM news")
    suspend fun deleteAllNews()

    @Query("DELETE FROM categoryentity")
    suspend fun deleteCategories()

    @Query("DELETE FROM picturesentity")
    suspend fun deletePictures()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: List<NewsItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictures(pictures: List<PicturesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsCategories(newsCategoriesList: List<NewsCategoryCrossRef>)
}