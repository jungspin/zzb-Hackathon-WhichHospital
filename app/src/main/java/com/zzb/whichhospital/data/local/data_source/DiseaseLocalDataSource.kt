package com.zzb.whichhospital.data.local.data_source

import com.zzb.whichhospital.presentation.model.Disease
import kotlinx.coroutines.flow.Flow

/**
 * 질환 관련 로컬 데이터
* @author jungspin
* @since 2023/08/15 4:28 PM
*/
interface DiseaseLocalDataSource {

    suspend fun getDisease(): Flow<List<Disease>>
    suspend fun getDiseaseById(id: Long): Flow<Disease>
}