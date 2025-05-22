package com.example.newsaggregator.data

import com.example.newsaggregator.data.mappers.toNewsItem
import com.example.newsaggregator.data.mappers.toNewsWithPictureAndCategories
import com.example.newsaggregator.data.room.CategoryEntity
import com.example.newsaggregator.data.room.NewsCategoryCrossRef
import com.example.newsaggregator.data.room.NewsItemDao
import com.example.newsaggregator.data.room.NewsItemEntity
import com.example.newsaggregator.data.room.NewsWithPictureAndCategories
import com.example.newsaggregator.data.room.PicturesEntity
import com.example.newsaggregator.data.room.RoomRepository
import com.example.newsaggregator.data.rss.OnlineRssRepository
import com.example.newsaggregator.data.rss.RssFeed
import com.example.newsaggregator.data.rss.dto.RssDto
import com.example.newsaggregator.ui.NewsItem
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val rssFeed: RssFeed,
    private val newsItemDao: NewsItemDao
) : OnlineRssRepository, RoomRepository, CurrentAppRepository {
    override suspend fun getRss(): RssDto {
        return rssFeed.getRss()
    }

    override suspend fun getAllNews(): List<NewsWithPictureAndCategories> {
        return newsItemDao.getAllNews()
    }

    override suspend fun getDomainList(): List<String> {
        return newsItemDao.getDomainList()
    }

    override suspend fun deleteAll() {
        newsItemDao.deleteAllNews()
        newsItemDao.deletePictures()
        newsItemDao.deleteCategories()
    }

    override suspend fun insertNews(news: List<NewsItemEntity>) {
        newsItemDao.insertNews(news)
    }

    override suspend fun insertPictures(pictures: List<PicturesEntity>) {
        newsItemDao.insertPictures(pictures)
    }

    override suspend fun insertCategories(categories: List<CategoryEntity>) {
        newsItemDao.insertCategories(categories)
    }

    override suspend fun insertNewsCategories(newsCategoriesList: List<NewsCategoryCrossRef>) {
        newsItemDao.insertNewsCategories(newsCategoriesList)
    }

    private suspend fun insertAll(news: List<NewsWithPictureAndCategories>) {
        news.forEach { item ->
            insertNews(listOf(item.newsItem))
            insertPictures(item.pictures)
            insertCategories(item.categories)
            item.categories.forEach {
                insertNewsCategories(
                    listOf(
                        NewsCategoryCrossRef(
                            guid = item.newsItem.guid,
                            value = it.value
                        )
                    )
                )
            }
        }
    }

    override suspend fun getAllNewsList(): List<NewsItem> {
        val result = getAllNews()
        if (result.isEmpty()) {
            updateNewsList()
            val r = getAllNews()
            return if (r.isEmpty()) {
                emptyList()
            } else {
                r.map { it.toNewsItem() }
            }
        } else {
            return result.map { it.toNewsItem() }
        }
    }

    override suspend fun updateNewsList() {
        newsItemDao.getAllNews()
        val rss = getRss()
        insertAll(rss.channel.items.map { it.toNewsWithPictureAndCategories() })
    }

    override suspend fun getNewsListByCategoryDomain(domain: String): List<NewsItem> {
        val result = newsItemDao.getNewsByCategoryDomain(domain)
        return result.map { it.toNewsItem()}
    }
}

interface CurrentAppRepository {
    suspend fun getAllNewsList(): List<NewsItem>

    suspend fun getDomainList(): List<String>

    suspend fun updateNewsList()

    suspend fun getNewsListByCategoryDomain(domain : String) : List<NewsItem>
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule() {
    @Binds
    abstract fun bindsOnlineRssRepository(appRepository: AppRepository): CurrentAppRepository
}