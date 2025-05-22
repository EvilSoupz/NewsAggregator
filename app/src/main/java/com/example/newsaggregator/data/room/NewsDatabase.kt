package com.example.newsaggregator.data.room

import android.app.Application
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsaggregator.data.room.entities.CategoryEntity
import com.example.newsaggregator.data.room.entities.NewsCategoryCrossRef
import com.example.newsaggregator.data.room.entities.NewsItemEntity
import com.example.newsaggregator.data.room.entities.PicturesEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(
    entities = [NewsItemEntity::class, PicturesEntity::class, CategoryEntity::class, NewsCategoryCrossRef::class],
    version = 2, autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun getNewsItemDao(): NewsDao
}

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