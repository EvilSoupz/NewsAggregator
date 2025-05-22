package com.example.newsaggregator.data.di

import android.app.Application
import androidx.room.Room
import com.example.newsaggregator.data.room.NewsDao
import com.example.newsaggregator.data.room.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun getDatabase(app: Application): NewsDatabase {
        return Room.databaseBuilder(
            context = app,
            NewsDatabase::class.java,
            name = "news_database"
        ).addMigrations().build()
    }

    @Provides
    @Singleton
    fun getNewsDao(db: NewsDatabase): NewsDao {
        return db.getNewsItemDao()
    }
}