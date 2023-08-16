package com.zzb.whichhospital.data.remote

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml


/**
 * [data class]
 * 진료과목 num, 병원명, 병원주소(위도, 경도), 병원 전화번호
 * -> 병원명으로 네이버 앱에 검색해서 나오게하기
 */
class RemoteDataSource {
    @Xml(name = "response")
    data class Hospital(
        @Element(name = "body")
        val body : Body,
        @Element(name = "header")
        val header: Header
    )

    @Xml(name ="header")
    data class Header(
        @PropertyElement(name="resultCode")
        val resultCode: Int,
        @PropertyElement(name="resultMsg")
        val resultMsg: String

    )
    @Xml(name = "body")
    data class Body(
        @Element(name ="items")
        val items : Items

    )

    @Xml(name = "items")
    data class Items(
        @Element(name="item")
        val item : List<Item>

    )

    @Xml(name = "item")
    data class Item(
        @PropertyElement(name="dutyName")
        var dutyName : String,
        @PropertyElement(name="dutyAddr")
        var dutyAddr : String,
        @PropertyElement(name="dgidIdName")
        var dgidIdName : String,
        @PropertyElement(name="dutyTel1")
        var dutyTel1 : String

    )

}