package com.example.newsaggregator.data.rss

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.adaptivity.xmlutil.serialization.XML
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GuardianRetrofitModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Таймаут на подключение
            .readTimeout(30, TimeUnit.SECONDS)     // Таймаут на чтение ответа
            .writeTimeout(30, TimeUnit.SECONDS)    // Таймаут на отправку данных
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    HttpLoggingInterceptor.Level.BODY // Уровень логирования (BODY, HEADERS, BASIC)
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRssFeed(okHttpClient: OkHttpClient): RssFeed {
        return Retrofit.Builder()
            .baseUrl("https://www.theguardian.com")
            .client(okHttpClient)
            .addConverterFactory(
                XML.asConverterFactory(
                    "application/xml; charset=UTF8".toMediaType()
                )
            )
            .build()
            .create(RssFeed::class.java)
    }
}
