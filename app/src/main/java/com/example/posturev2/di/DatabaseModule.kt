package com.example.posturev2.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.posturev2.database.FeedbackDao
import com.example.posturev2.database.PostureDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun providePostureDatabase(@ApplicationContext appContext: Context): PostureDatabase {
        return Room.databaseBuilder(
            appContext,
            PostureDatabase::class.java,
            "posture_database")
            .build()
    }

    @Provides
    fun provideFeedbackDao(database: PostureDatabase): FeedbackDao {
        return database.feedbackDao()
    }
}