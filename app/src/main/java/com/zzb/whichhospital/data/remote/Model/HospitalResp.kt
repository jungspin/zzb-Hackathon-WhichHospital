package com.zzb.whichhospital.data.remote.Model

import com.google.gson.annotations.SerializedName


data class HospitalResp(
    val response: Response
)

data class Response(
    val header: Header,
    val body: Body
)

data class Body(
    @SerializedName("items")
    val hospitalList: HospitalList,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)

data class HospitalList(
    @SerializedName("item")
    val hospitalList: List<HospitalInfo>
)

data class HospitalInfo(
    val addr: String,
    @SerializedName("clCd")
    val hospTypeCode: Int,
    @SerializedName("clCdNm")
    val hospTypeName: String,
    val hospUrl: String,
    @SerializedName("telno")
    val hospTel: String,
    @SerializedName("XPos")
    val xPos: Double,
    @SerializedName("YPos")
    val yPos: Double,
    @SerializedName("yadmNm")
    val hospName: String,
)

data class Header(
    val resultCode: String,
    val resultMsg: String
)


