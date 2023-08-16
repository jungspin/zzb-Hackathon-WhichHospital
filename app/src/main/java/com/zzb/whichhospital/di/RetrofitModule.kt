package com.zzb.whichhospital.di

import android.util.Log
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import com.zzb.whichhospital.data.remote.RemoteDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 가져오기 getBlah()
 *
 * [data class]
 * 진료과목 num, 병원명, 병원주소(위도, 경도), 병원 전화번호
 * -> 병원명으로 네이버 앱에 검색해서 나오게하기
 *
 * [Retrofit]
 * builder
 * data
 * interface
 *
 */
class RetrofitModule {

    /**
     * Create Hospital API
     */
    companion object{

        //https://apis.data.go.kr/B552657/HsptlAsembySearchService/  // : endPoint
        val baseAPIURL = "https://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlBassInfoInqire/"

        var instance : Retrofit? = null
        var xmlParcer = TikXml.Builder().exceptionOnUnreadXml(false).build()

        fun getHospitalInstance() : Retrofit {
            if(instance == null) {
                instance = Retrofit.Builder()
                    .baseUrl(baseAPIURL)
                    .addConverterFactory(TikXmlConverterFactory.create(xmlParcer))
                    .build()
            }
            return instance!!
        }
    }

    /**
     * Get Hospital Data
     */
    interface HospitalService{
        companion object{
            val serviceKey = "fu7gLt1kvMpPj77E5Veta7zhNds0R8JSKhu1LpI29orK4Uc8rqvbVd3Bytv8fL9rcy4xv6Effc20LAnpjKobFQ=="

        }

        @GET("getHospitalData")
        fun getHospitalData(
            @Query("serviceKey")
            serviceKey : String = HospitalService.serviceKey

        ) : Call<RemoteDataSource>


    }

    /**
     * Request Hospital Data
     */
    fun requestHospitalData(){
        val requestHospitalService = RetrofitModule.getHospitalInstance()
            .create(RetrofitModule.HospitalService::class.java)
        val hospitalCall = requestHospitalService.getHospitalData()

        hospitalCall.enqueue(object : Callback<RemoteDataSource> {
            override fun onResponse(
                call: Call<RemoteDataSource>,
                response: Response<RemoteDataSource>
            ) {
                if(response.isSuccessful && response.code() == 200){
                    Log.d("=== Retrofit success ===", "onResponse: ")
                }
            }

            override fun onFailure(call: Call<RemoteDataSource>, t: Throwable) {
                Log.e("=== Retrofit fail ===", t.stackTraceToString())

            }

        })

    }

}