package com.zzb.whichhospital.data.remote.impl

import com.zzb.whichhospital.data.remote.HospitalRemoteDataSource
import com.zzb.whichhospital.data.remote.Model.HospitalResp
import com.zzb.whichhospital.data.remote.api.HospitalApi
import com.zzb.whichhospital.data.remote.dto.HospitalReq
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import javax.inject.Inject

class HospitalRemoteDataSourceImpl @Inject constructor(private val hospitalApi: HospitalApi) :
    HospitalRemoteDataSource {
    override suspend fun getHospitalList(hospitalReq: HospitalReq): Flow<Call<HospitalResp>> {
        return flow {
            emit(
                hospitalApi.getHospBasisList(
                    pageNo = hospitalReq.pageNo,
                    hospType = hospitalReq.hospType,
                    operateCode = hospitalReq.operateCode
                )
            )
        }
    }
}