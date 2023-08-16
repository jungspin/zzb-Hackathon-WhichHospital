package com.zzb.whichhospital.domain.repository

import com.zzb.whichhospital.presentation.model.Disease
import kotlinx.coroutines.flow.Flow

/**
* 질환 관련 로컬 데이터 repository
* @author jungspin
* @since 2023/08/15 4:47 PM
*/
interface DiseaseRepo {

    suspend fun getDisease(): Flow<List<Disease>>
    suspend fun getDiseaseById(id: Long): Flow<Disease>
}