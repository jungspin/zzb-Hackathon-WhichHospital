package com.zzb.whichhospital.data.remote

import com.zzb.whichhospital.data.remote.Model.HospitalResp
import com.zzb.whichhospital.data.remote.dto.HospitalReq
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

interface HospitalRemoteDataSource {

    suspend fun getHospitalList(hospitalReq: HospitalReq) : Flow<Call<HospitalResp>>
}