package com.zzb.whichhospital.data.local.data_source.impl

import com.zzb.whichhospital.data.local.dao.DiseaseDao
import com.zzb.whichhospital.data.local.data_source.DiseaseLocalDataSource
import com.zzb.whichhospital.data.local.mapper.toPresentationDisease
import com.zzb.whichhospital.presentation.model.Disease
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * 질환 관련 로컬 데이터 구현체
 * @author jungspin
 * @since 2023/08/15 4:27 PM
 */
class DiseaseLocalDataSourceImpl @Inject constructor(private val diseaseDao: DiseaseDao) :
    DiseaseLocalDataSource {
    override suspend fun getDisease(): Flow<List<Disease>> {
        return flow { emit(diseaseDao.getDiseases().map { it.toPresentationDisease() }) }
    }

    override suspend fun getDiseaseById(id: Long): Flow<Disease> {
        return flow { emit(diseaseDao.getDiseaseById(id).toPresentationDisease()) }
    }
}