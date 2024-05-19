package com.zzb.whichhospital.presentation.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.zzb.whichhospital.base.Status
import com.zzb.whichhospital.data.remote.Model.HospitalResp
import com.zzb.whichhospital.data.remote.dto.HospitalReq
import com.zzb.whichhospital.domain.usecase.HospitalUseCase
import com.zzb.whichhospital.presentation.model.Hospital
import com.zzb.whichhospital.presentation.model.HospitalInfo
import com.zzb.whichhospital.presentation.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HospitalListViewModel @Inject constructor(
    private val hospitalUseCase: HospitalUseCase,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) :
    ViewModel() {

    private val _hospitalStateFlow = MutableStateFlow(
        UiState(
            status = Status.NONE,
            data = Hospital(),
        )
    )
    val hospitalStateFlow = _hospitalStateFlow.asStateFlow()

    // 위치 권한 부여 여부
    private val _grantedLocationStateFlow = MutableStateFlow(false)
    var grantedLocationStateFlow = _grantedLocationStateFlow.asStateFlow()

    fun setLocationPermission(isGranted: Boolean) {
        _grantedLocationStateFlow.value.apply {
            viewModelScope.launch {
                _grantedLocationStateFlow.value = true
            }
        }
    }

    fun getHospitalList(hospitalReq: HospitalReq) {
        _hospitalStateFlow.value.apply {
            viewModelScope.launch {
                hospitalUseCase.getHospitalList(hospitalReq)
                    .onStart {
                        _hospitalStateFlow.value = UiState(
                            status = Status.LOADING,
                            message = "병원 정보를 불러오는 중입니다.."
                        )
                    }
                    .stateIn(viewModelScope)
                    .collect { response ->
                        response.enqueue(object : Callback<HospitalResp> {
                            override fun onResponse(
                                call: Call<HospitalResp>,
                                response: Response<HospitalResp>
                            ) {
                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        val hospInfos =
                                            it.response.body.hospitalList.hospitalList.map { info ->
                                                HospitalInfo(
                                                    hospName = info.hospName,
                                                    hospTel = info.hospTel,
                                                    hospX = info.yPos,
                                                    hospY = info.xPos,
                                                    hospAddr = info.addr,
                                                )
                                            }
                                        val body = it.response.body

                                        _hospitalStateFlow.value = UiState(
                                            status = Status.SUCCESS,
                                            data = Hospital(
                                                hospInfos = hospInfos,
                                                pageNo = body.pageNo,
                                                numOfRows = body.numOfRows,
                                                totalCount = body.totalCount
                                            )
                                        )
                                    }

                                } else {
                                    _hospitalStateFlow.value = UiState(
                                        status = Status.FAIL,
                                        message = "병원 정보를 불러오는 데에 실패했습니다."
                                    )
                                }
                            }

                            override fun onFailure(call: Call<HospitalResp>, t: Throwable) {
                                _hospitalStateFlow.value = UiState(
                                    status = Status.ERROR,
                                    message = "일시적인 오류로 병원 리스트를 불러올 수 없습니다.",
                                    throwable = t
                                )
                                t.printStackTrace()
                            }

                        })
                    }
            }
        }
    }

}