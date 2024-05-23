package com.zzb.whichhospital.presentation.model

data class Hospital(
    val hospInfos: List<HospitalInfo> = listOf(),
    val numOfRows: Int = 0,
    val pageNo: Int = 0,
    val totalCount: Int = 0,
)

data class HospitalInfo(
    val hospName: String = "",
    val hospTel: String = "",
    val hospX: Double = 0.0,
    val hospY: Double = 0.0,
    val hospAddr: String = "",
    val hospDistance: Long = 0L,
)
