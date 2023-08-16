package com.zzb.whichhospital.di

import com.zzb.whichhospital.data.local.data_source.impl.DiseaseLocalDataSourceImpl
import com.zzb.whichhospital.domain.repository.impl.DiseaseRepoImpl
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
}