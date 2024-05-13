package com.zzb.whichhospital.domain.usecase

import com.zzb.whichhospital.data.remote.Model.HospitalResp
import com.zzb.whichhospital.data.remote.dto.HospitalReq
import com.zzb.whichhospital.domain.repository.HospitalRepo
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import javax.inject.Inject

class HospitalUseCase @Inject constructor(private val hospitalRepo: HospitalRepo) {

    suspend fun getHospitalList(hospitalReq: HospitalReq) : Flow<Call<HospitalResp>> {
        return hospitalRepo.getHospitalList(hospitalReq)
    }
}