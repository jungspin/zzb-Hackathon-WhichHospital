package com.zzb.whichhospital.base

/**
* 상태를 나타내는 enum class
* @author jungspin
* @since 2023/08/15 4:19 PM
*/
enum class Status {
    /*
    * NONE : nothing
    * LOADING : loading data
    * SUCCESS : success to load data
    * FAIL : fail to load data
    * ERROR : something happens while loading data
    */
    NONE, LOADING, SUCCESS, FAIL, ERROR,
}