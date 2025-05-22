package com.example.newsaggregator.data

import com.example.newsaggregator.data.mappers.toNewsItem
import com.example.newsaggregator.data.mappers.toNewsWithPictureAndCategories
import com.example.newsaggregator.data.room.NewsDao
import com.example.newsaggregator.data.room.RoomRepository
import com.example.newsaggregator.data.room.entities.CategoryEntity
import com.example.newsaggregator.data.room.entities.NewsCategoryCrossRef
import com.example.newsaggregator.data.room.entities.NewsItemEntity
import com.example.newsaggregator.data.room.entities.NewsWithPictureAndCategories
import com.example.newsaggregator.data.room.entities.PicturesEntity
import com.example.newsaggregator.data.rss.OnlineRssRepository
import com.example.newsaggregator.data.rss.RssFeed
import com.example.newsaggregator.data.rss.dto.RssDto
import com.example.newsaggregator.domain.CurrentAppRepository
import com.example.newsaggregator.ui.NewsItem
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val rssFeed: RssFeed,
    private val newsDao: NewsDao
) : OnlineRssRepository, RoomRepository, CurrentAppRepository {
    override suspend fun getRss(): RssDto {
        return rssFeed.getRss()
    }

    override suspend fun getAllNews(): List<NewsWithPictureAndCategories> {
        return newsDao.getAllNews()
    }

    override suspend fun getDomainList(): List<String> {
        return newsDao.getDomainList()
    }

    override suspend fun deleteAll() = coroutineScope {
        val deleteAllNews = launch {
            newsDao.deleteAllNews()
        }
        val deletePictures = launch {
            newsDao.deletePictures()
        }
        val deleteCategories = launch {
            newsDao.deleteCategories()
        }

        deleteAllNews.join()
        deletePictures.join()
        deleteCategories.join()
    }

    override suspend fun insertNews(news: List<NewsItemEntity>) {
        newsDao.insertNews(news)
    }

    override suspend fun insertPictures(pictures: List<PicturesEntity>) {
        newsDao.insertPictures(pictures)
    }

    override suspend fun insertCategories(categories: List<CategoryEntity>) {
        newsDao.insertCategories(categories)
    }

    override suspend fun insertNewsCategories(newsCategoriesList: List<NewsCategoryCrossRef>) {
        newsDao.insertNewsCategories(newsCategoriesList)
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
        deleteAll()
        val rss = getRss()
        insertAll(rss.channel.items.map { it.toNewsWithPictureAndCategories() })
    }

    override suspend fun getNewsListByCategoryDomain(domain: String): List<NewsItem> {
        val result = newsDao.getNewsByCategoryDomain(domain)
        return result.map { it.toNewsItem() }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule() {
    @Binds
    abstract fun bindsOnlineRssRepository(appRepository: AppRepository): CurrentAppRepository
}