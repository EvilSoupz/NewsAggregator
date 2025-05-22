package com.example.newsaggregator.data.room.entities

import androidx.room.Entity

@Entity(primaryKeys = ["guid", "value"], tableName = "news_categories_cross_ref")
data class NewsCategoryCrossRef(
    val guid: String,
    val value: String
) {
}