package com.zzb.whichhospital.data.remote.dto

/**
* 병원 정보 요청 데이터 클래스
* @author jungspin
* @since 5/8/24 9:15PM
 *
 * @param hospType 종별코드
 * @param operateCode 진료과목코드
*/
data class HospitalReq(
    val pageNo: Int,
    val numOfRows: Int = 10,
    val hospType: String,
    val operateCode: String,
)
