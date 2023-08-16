package com.zzb.whichhospital.di

import com.zzb.whichhospital.data.local.AppDatabase
import com.zzb.whichhospital.data.local.data_source.impl.DiseaseLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Singleton
    @Provides
    fun providesDiseaseLocalDataSource(appDatabase: AppDatabase) =
        DiseaseLocalDataSourceImpl(appDatabase.diseaseDao())
}