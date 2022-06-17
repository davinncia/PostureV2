package com.example.posturev2.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Feedback::class], version = 1)
abstract class PostureDatabase: RoomDatabase() {

    abstract fun feedbackDao(): FeedbackDao

}