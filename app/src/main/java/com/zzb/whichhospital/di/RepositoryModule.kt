package com.zzb.whichhospital.di

import com.zzb.whichhospital.data.local.data_source.impl.DiseaseLocalDataSourceImpl
import com.zzb.whichhospital.data.remote.HospitalRemoteDataSource
import com.zzb.whichhospital.data.remote.impl.HospitalRemoteDataSourceImpl
import com.zzb.whichhospital.domain.repository.impl.DiseaseRepoImpl
import com.zzb.whichhospital.domain.repository.impl.HospitalRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providesDiseaseRepository(diseaseLocalDataSource: DiseaseLocalDataSourceImpl) =
        DiseaseRepoImpl(diseaseLocalDataSource)

    @Provides
    @Singleton
    fun providesHospitalRepository(hospitalRemoteDataSource: HospitalRemoteDataSourceImpl) =
        HospitalRepoImpl(hospitalRemoteDataSource)
}
