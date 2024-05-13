package com.zzb.whichhospital.di

import com.zzb.whichhospital.data.local.AppDatabase
import com.zzb.whichhospital.data.local.data_source.impl.DiseaseLocalDataSourceImpl
import com.zzb.whichhospital.data.remote.api.HospitalApi
import com.zzb.whichhospital.data.remote.impl.HospitalRemoteDataSourceImpl
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

    @Singleton
    @Provides
    fun providesHospitalRemoteDataSource(hospitalApi: HospitalApi) =
        HospitalRemoteDataSourceImpl(hospitalApi)
}