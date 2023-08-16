package com.zzb.whichhospital.domain.usecase

import com.zzb.whichhospital.domain.repository.DiseaseRepo
import com.zzb.whichhospital.presentation.model.Disease
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
* 질환 관련 usecase
* @author jungspin
* @since 2023/08/15 5:27 PM
*/
class DiseaseUseCase @Inject constructor(private val diseaseRepo: DiseaseRepo) {
    suspend fun getDisease(): Flow<List<Disease>> = diseaseRepo.getDisease()

    suspend fun getDiseaseById(id: Long): Flow<Disease> =
        diseaseRepo.getDiseaseById(id)
}