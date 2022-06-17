package com.example.posturev2.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Feedback.TABLE_NAME)
data class Feedback(
    @PrimaryKey
    val timeStamp: Long, // milli-seconds
    val isStraight: Boolean
) {
    companion object {
        const val TABLE_NAME = "feedback"
    }
}