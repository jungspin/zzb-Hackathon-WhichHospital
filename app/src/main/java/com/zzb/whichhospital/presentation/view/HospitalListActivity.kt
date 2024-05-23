package com.zzb.whichhospital.presentation.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.zzb.whichhospital.base.Status
import com.zzb.whichhospital.data.remote.dto.HospitalReq
import com.zzb.whichhospital.presentation.model.Hospital
import com.zzb.whichhospital.presentation.model.HospitalInfo
import com.zzb.whichhospital.presentation.view_model.HospitalListViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 병원 리스트를 확인하는 화면
 * @author jungspin
 * @since 2023/08/13 3:16 PM
 */

@AndroidEntryPoint
class HospitalListActivity : ComponentActivity() {

    private val viewModel: HospitalListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val diseaseName = intent.getStringExtra(SymptomActivity.INTENT_KEY_DISEASE_NAME) ?: ""

        val permissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

        viewModel.getHospitalList(
            HospitalReq(
                pageNo = 1,
                hospType = "11",
                operateCode = "07"
            )
        )
        viewModel.hospitalStateFlow.asLiveData().observe(this@HospitalListActivity) { data ->
            setContent {
                RootSurface {
                    CheckPermissions(
                        context = LocalContext.current,
                        permissions = permissions,
                        onAlreadyGranted = { viewModel.setLocationPermission(true) }) { permissionsMap ->
                        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
                        viewModel.setLocationPermission(areGranted)
                    }

                    ListView(
                        diseaseName = diseaseName,
                        backButtonAction = { finish() },
                        isNeedBottomButton = false
                    ) {
                        var hospital by remember { mutableStateOf(Hospital()) }
                        data.data?.let {
                            hospital = it
                        }

                        when (data.status) {
                            Status.NONE -> Unit
                            Status.LOADING -> LoadingView()
                            Status.SUCCESS -> HospitalList(hospitalList = hospital.hospInfos)
                            Status.FAIL -> {}
                            Status.ERROR -> {
                                Log.d("HospList", "${data.throwable?.localizedMessage}")
                                ErrorView {
                                    viewModel.getHospitalList(
                                        HospitalReq(
                                            pageNo = 1,
                                            hospType = "11",
                                            operateCode = "07"
                                        )
                                    )
                                }
                            }
                        }


                    }


                }
            }
        }
    }


    companion object {
        val sampleData = listOf(
            HospitalInfo(
                hospName = "부산대병원",
                hospTel = "051-000-0000",
                hospX = 35.3282205,
                hospY = 129.0056385,
                hospAddr = "경상남도 양산시 물금읍 금오로 20"
            ),
            HospitalInfo(
                hospName = "부산백병원",
                hospTel = "051-000-0000",
                hospX = 35.1734932,
                hospY = 129.1819789,
                hospAddr = "경상남도 양산시 물금읍 금오로 20"
            ),
            HospitalInfo(
                hospName = "서울대 병원",
                hospTel = "02-0000-0000",
                hospX = 37.5795427,
                hospY = 126.9990602,
                hospAddr = "서울특별시 종로구 대학로 101"
            ),
        )
    }
}

@Composable
fun HospitalList(hospitalList: List<HospitalInfo>) {
    LazyColumn {
        items(hospitalList) { hospital ->
            HospitalItem(hospital = hospital)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalItem(hospital: HospitalInfo) {
    val context = LocalContext.current
    val showDialog = remember {
        mutableStateOf(false)
    }

    if (showDialog.value) {
        HospitalDetailDialog(setShowDialog = { showDialog.value = it }, hospital = hospital)
    }

    Card(
        onClick = { showDialog.value = true },
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(100.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            SymptomText(text = hospital.hospName)
            SymptomText(
                // TODO: 거리 추가하기
                text = "${hospital.hospDistance}km",
                textAlign = TextAlign.End,
            )
        }
        val permissions = arrayOf(
            android.Manifest.permission.CALL_PHONE
        )
        val launcherPermissions =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsMap ->
                val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
                if (areGranted) callToHospital(
                    context = context,
                    tel = "tel:${hospital.hospTel}"
                )
            }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // TODO: 따로 빼기
            Text(
                text = hospital.hospTel,
                color = Color.Blue,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        checkPermission(
                            context = context,
                            permissions = permissions,
                            launcher = launcherPermissions
                        )
                    },
            )
            IconButton(
                onClick = { showDialog.value = true },
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "병원 자세히 보기 버튼",
                )
            }
        }
    }
}

fun callToHospital(context: Context, tel: String) {
    context.startActivity(
        Intent("android.intent.action.CALL", Uri.parse(tel))
    )
}

fun checkPermission(
    context: Context,
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
) {
    if (permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }) {
        //
    } else {
        launcher.launch(permissions)
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun HospitalDetailDialog(setShowDialog: (Boolean) -> Unit, hospital: HospitalInfo) {
    Dialog(
        onDismissRequest = { setShowDialog(false) },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(15.dp),
        ) {
            Column(
                modifier = Modifier
                    .width(500.dp)
                    .height(300.dp)
                    .padding(12.dp),
            ) {
                NaverMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f),
                    cameraPositionState = CameraPositionState(
                        position = CameraPosition(LatLng(hospital.hospX, hospital.hospY), 11.0)
                    )
                ) {
                    Marker(
                        state = MarkerState(position = LatLng(hospital.hospX, hospital.hospY))
                    )
                }
                Column(verticalArrangement = Arrangement.Center) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = hospital.hospName)
                        Text(text = hospital.hospTel, color = Color.Blue)
                    }
                    Text(text = hospital.hospAddr)
                }
            }
        }
    }
}

/**
 * 권한 확인
 *
 * @param context
 * @param permissions
 * @param onAlreadyGranted
 * @param onRequestResultPermission
 */
@Composable
fun CheckPermissions(
    context: Context,
    permissions: Array<String>,
    onAlreadyGranted: () -> Unit,
    onRequestResultPermission: (permissionsMap: Map<String, Boolean>) -> Unit,
) {
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsMap ->
            onRequestResultPermission(permissionsMap)
        }

    if (permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }) {
        // 권한이 있는 경우
        onAlreadyGranted()
    } else {
        // 권한이 없는 경우
        LaunchedEffect(Unit) {
            permissionLauncher.launch(permissions)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HospitalListPreview() {
    RootSurface {
        ListView(diseaseName = "심근경색", backButtonAction = {}, isNeedBottomButton = false) {
            HospitalList(hospitalList = HospitalListActivity.sampleData)
        }
    }
}