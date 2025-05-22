package com.example.newsaggregator.data.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "news")
data class NewsItemEntity(
    @PrimaryKey
    val guid: String,
    val title: String,
    val description: String,
    val dcCreator: String,
    val dcDate: String,
)

@Entity
data class PicturesEntity(
    @PrimaryKey
    val url: String,
    val newsGuid: String
)

@Entity
data class CategoryEntity(
    @PrimaryKey
    val value : String,
    @ColumnInfo(defaultValue = "")
    val domain : String
)
@Entity(primaryKeys = ["guid","value"])
data class NewsCategoryCrossRef(
    val guid : String,
    val value : String
)

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
    val categories : List<CategoryEntity>

)