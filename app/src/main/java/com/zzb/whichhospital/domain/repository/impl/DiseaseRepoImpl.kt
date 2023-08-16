package com.zzb.whichhospital.domain.repository.impl

import com.zzb.whichhospital.data.local.data_source.DiseaseLocalDataSource
import com.zzb.whichhospital.domain.repository.DiseaseRepo
import com.zzb.whichhospital.presentation.model.Disease
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 질환 관련 로컬 데이터 repository 구현체
 * @author jungspin
 * @since 2023/08/15 4:46 PM
 */
class DiseaseRepoImpl @Inject constructor(private val diseaseLocalDataSource: DiseaseLocalDataSource) :
    DiseaseRepo {
    override suspend fun getDisease(): Flow<List<Disease>> = diseaseLocalDataSource.getDisease()

    override suspend fun getDiseaseById(id: Long): Flow<Disease> =
        diseaseLocalDataSource.getDiseaseById(id)
}