package com.example.newsaggregator.data.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsaggregator.data.room.entities.CategoryEntity
import com.example.newsaggregator.data.room.entities.NewsCategoryCrossRef
import com.example.newsaggregator.data.room.entities.NewsItemEntity
import com.example.newsaggregator.data.room.entities.PicturesEntity

@Database(
    entities = [NewsItemEntity::class, PicturesEntity::class, CategoryEntity::class, NewsCategoryCrossRef::class],
    version = 2, autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun getNewsItemDao(): NewsDao
}

