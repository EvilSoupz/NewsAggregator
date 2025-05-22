package com.example.newsaggregator.data.di

import com.example.newsaggregator.data.AppRepository
import com.example.newsaggregator.domain.CurrentAppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule() {
    @Binds
    abstract fun bindsOnlineRssRepository(appRepository: AppRepository): CurrentAppRepository
}