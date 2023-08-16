package com.zzb.whichhospital.base

/**
* 데이터 공통 응답 형식
* @author jungspin
* @since 2023/08/15 4:17 PM
*/
data class BaseResponse<T> (val code: Int, val message: String, val data: T)