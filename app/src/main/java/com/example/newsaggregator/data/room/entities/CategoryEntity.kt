package com.example.newsaggregator.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val value: String,
    @ColumnInfo(defaultValue = "")
    val domain: String
) {
}