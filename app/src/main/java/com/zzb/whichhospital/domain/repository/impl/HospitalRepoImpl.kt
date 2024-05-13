package com.zzb.whichhospital.domain.repository.impl

import com.zzb.whichhospital.data.remote.HospitalRemoteDataSource
import com.zzb.whichhospital.data.remote.Model.HospitalResp
import com.zzb.whichhospital.data.remote.dto.HospitalReq
import com.zzb.whichhospital.domain.repository.HospitalRepo
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import javax.inject.Inject

class HospitalRepoImpl @Inject constructor(private val hospitalRemoteDataSource: HospitalRemoteDataSource) :
    HospitalRepo {
    override suspend fun getHospitalList(hospitalReq: HospitalReq): Flow<Call<HospitalResp>> {
        return hospitalRemoteDataSource.getHospitalList(hospitalReq)
    }
}