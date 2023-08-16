package com.zzb.whichhospital.presentation.model

import com.zzb.whichhospital.base.Status

/**
 * UI 표현을 위한 클래스
* @author jungspin
* @since 2023/08/15 4:54 PM
*/
data class UiState<T> (
    val status: Status = Status.NONE,
    val data: T? = null,
    val message: String = "",
    val throwable: Throwable? = null,
)