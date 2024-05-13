package com.zzb.whichhospital.di

import com.zzb.whichhospital.domain.repository.impl.DiseaseRepoImpl
import com.zzb.whichhospital.domain.repository.impl.HospitalRepoImpl
import com.zzb.whichhospital.domain.usecase.DiseaseUseCase
import com.zzb.whichhospital.domain.usecase.HospitalUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun providesDiseaseUseCase(diseaseRepoImpl: DiseaseRepoImpl): DiseaseUseCase =
        DiseaseUseCase(diseaseRepoImpl)

    @Singleton
    @Provides
    fun providesHospitalUseCase(hospitalRepoImpl: HospitalRepoImpl): HospitalUseCase =
        HospitalUseCase(hospitalRepoImpl)
}