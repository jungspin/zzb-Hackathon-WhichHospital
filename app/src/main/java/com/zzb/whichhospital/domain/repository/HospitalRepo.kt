package com.zzb.whichhospital.domain.repository

import com.zzb.whichhospital.data.remote.Model.HospitalResp
import com.zzb.whichhospital.data.remote.dto.HospitalReq
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

interface HospitalRepo {

    suspend fun getHospitalList(hospitalReq: HospitalReq): Flow<Call<HospitalResp>>
}