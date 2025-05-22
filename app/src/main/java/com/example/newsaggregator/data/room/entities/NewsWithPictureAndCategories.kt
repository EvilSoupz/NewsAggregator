package com.example.newsaggregator.data.room.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class NewsWithPictureAndCategories(
    @Embedded val newsItem: NewsItemEntity,
    @Relation(
        parentColumn = "guid",
        entityColumn = "newsGuid"
    )
    val pictures: List<PicturesEntity>,
    @Relation(
        parentColumn = "guid",
        entityColumn = "value",
        associateBy = Junction(NewsCategoryCrossRef::class)
    )
    val categories: List<CategoryEntity>

)