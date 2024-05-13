package com.zzb.whichhospital.data.remote.api

import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import com.zzb.whichhospital.BuildConfig
import com.zzb.whichhospital.data.remote.Model.HospitalResp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HospitalApi {

    @GET("getHospBasisList")
    fun getHospBasisList(
        @Query("ServiceKey") serviceKey: String = BuildConfig.SERVICE_KEY,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int = 10,
        @Query("clCd") hospType: String,
        @Query("dgsbjtCd") operateCode: String,
        @Query("_type") type: String = "json",
    ): Call<HospitalResp>
}