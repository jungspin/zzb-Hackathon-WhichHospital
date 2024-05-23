package com.zzb.whichhospital.presentation.view_model

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.naver.maps.geometry.LatLng
import com.zzb.whichhospital.base.Status
import com.zzb.whichhospital.data.remote.Model.Body
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
import kotlin.math.log

@HiltViewModel
class HospitalListViewModel @Inject constructor(
    private val hospitalUseCase: HospitalUseCase,
    private val locationManager: LocationManager,
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
//        _grantedLocationStateFlow.value.apply {
//            viewModelScope.launch {
//                _grantedLocationStateFlow.value = isGranted
//            }
//        }
        _grantedLocationStateFlow.value = isGranted
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
                                Log.d("HospList", "onResponse: ${response.body()}")
                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        val body = it.response.body
                                        Log.d("HospList", "onResponse: ${body}")
                                        Log.d("HospList", "_grantedLocationStateFlow.value: ${_grantedLocationStateFlow.value}")
                                        if (_grantedLocationStateFlow.value) {
                                            // 위치 데이터 수집 + 거리 계산
                                            getCurrentLocation(body = body)
                                        } else {
                                            // 데이터 그대로 내보냄
                                            val hospInfos =
                                                body.hospitalList.hospitalList.map { info ->
                                                    HospitalInfo(
                                                        hospName = info.hospName,
                                                        hospTel = info.hospTel,
                                                        hospX = info.yPos,
                                                        hospY = info.xPos,
                                                        hospAddr = info.addr,
                                                    )
                                                }

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

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(body: Body) {
        Log.d("HospList", "getCurrentLocation: ${!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)}")
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

        } else {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location == null) {
                        val locationCallback = object : LocationCallback() {
                            override fun onLocationResult(result: LocationResult) {
                                super.onLocationResult(result)
                                result.locations.forEach { location ->
                                    getHospInfosByCurrentLocation(location, body)
                                }
                            }
                        }
                        fusedLocationProviderClient.requestLocationUpdates(
                            locationRequest, locationCallback, Looper.myLooper()
                        )
                    } else {
                        getHospInfosByCurrentLocation(location, body)
                    }

                }
                .addOnFailureListener {
                    _hospitalStateFlow.value = UiState(
                        status = Status.ERROR,
                        throwable = it
                    )
                }
        }
    }

    // TODO: deprecated
    private val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
        priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
    }

    private fun getHospInfosByCurrentLocation(location: Location, body: Body) {
        Log.d("HospList", "getHospInfosByCurrentLocation: ")
        val currentLocation = LatLng(location.latitude, location.longitude)

        val hospInfos =
            body.hospitalList.hospitalList.map { info ->
                HospitalInfo(
                    hospName = info.hospName,
                    hospTel = info.hospTel,
                    hospX = info.yPos,
                    hospY = info.xPos,
                    hospAddr = info.addr,
                    hospDistance = calculateDistance(info.yPos, info.xPos, currentLocation)
                )
            }

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

    private fun calculateDistance(x: Double, y: Double, currentLocation: LatLng): Long {
        val hospLocation = LatLng(x, y)
        Log.d("HospList", "calculateDistance: $currentLocation")
        return Math.round(currentLocation.distanceTo(hospLocation) / 1000)
    }

}