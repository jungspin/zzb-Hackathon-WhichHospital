package com.zzb.whichhospital.di

import android.content.Context
import androidx.room.Room
import com.zzb.whichhospital.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "disease_database")
            .createFromAsset("database/init-data.db").build()

    @Provides
    fun provideDiseaseDao(appDatabase: AppDatabase) = appDatabase.diseaseDao()
}