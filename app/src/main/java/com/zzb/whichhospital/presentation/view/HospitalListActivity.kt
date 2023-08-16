package com.zzb.whichhospital.presentation.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import dagger.hilt.android.AndroidEntryPoint

/**
 * 병원 리스트를 확인하는 화면
 * @author jungspin
 * @since 2023/08/13 3:16 PM
 */
@AndroidEntryPoint
class HospitalListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val diseaseName = intent.getStringExtra(SymptomActivity.INTENT_KEY_DISEASE_NAME) ?: ""
        setContent {
            RootSurface {
                ListView(
                    diseaseName = diseaseName,
                    backButtonAction = { finish() },
                    isNeedBottomButton = false
                ) {
                    HospitalList(hospitalList = sampleData)
                }
            }
        }
    }


    companion object {
        val sampleData = listOf(
            SampleHospital(
                hospitalName = "부산대 병원",
                hospitalTel = "051-000-0000",
                distanceFromHospital = 30,
                hospitalLocation = LatLng(35.3282205, 129.0056385),
                hospitalAddress = "경상남도 양산시 물금읍 금오로 20"
            ),
            SampleHospital(
                hospitalName = "부산백병원",
                hospitalTel = "051-000-0000",
                distanceFromHospital = 45,
                hospitalLocation = LatLng(35.1734932, 129.1819789),
                hospitalAddress = "부산광역시 해운대구 해운대로 875"
            ),
            SampleHospital(
                hospitalName = "서울대 병원",
                hospitalTel = "02-0000-0000",
                distanceFromHospital = 300,
                hospitalLocation = LatLng(37.5795427, 126.9990602),
                hospitalAddress = "서울특별시 종로구 대학로 101"
            ),
        )
    }
}

@Composable
fun HospitalList(hospitalList: List<SampleHospital>) {
    LazyColumn {
        items(hospitalList) { hospital ->
            HospitalItem(hospital = hospital)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalItem(hospital: SampleHospital) {
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
            SymptomText(text = hospital.hospitalName)
            SymptomText(
                text = "${hospital.distanceFromHospital}km",
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
                    tel = "tel:${hospital.hospitalTel}"
                )
            }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // TODO: 따로 빼기
            Text(
                text = hospital.hospitalTel,
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
fun HospitalDetailDialog(setShowDialog: (Boolean) -> Unit, hospital: SampleHospital) {
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
                        position = CameraPosition(hospital.hospitalLocation, 11.0)
                    )
                ) {
                    Marker(
                        state = MarkerState(position = hospital.hospitalLocation)
                    )
                }
                Column(verticalArrangement = Arrangement.Center) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = hospital.hospitalName)
                        Text(text = hospital.hospitalTel, color = Color.Blue)
                    }
                    Text(text = hospital.hospitalAddress)
                }
            }
        }
    }
}


/**
 * 병원 정보 샘플 데이터 클래스
 *
 * @property hospitalName 병원 이름
 * @property hospitalTel 병원 전화번호
 * @property distanceFromHospital 현재 내 위치 - 병원 간 거리
 * @property hospitalLocation 병원 위치
 * @property hospitalAddress 병원 주소
 */
data class SampleHospital(
    val hospitalName: String,
    val hospitalTel: String,
    val distanceFromHospital: Int,
    val hospitalLocation: LatLng = LatLng(0.0, 0.0),
    val hospitalAddress: String = "",
)

@Preview(showBackground = true)
@Composable
fun HospitalListPreview() {
    RootSurface {
        ListView(diseaseName = "심근경색", backButtonAction = {}, isNeedBottomButton = false) {
            HospitalList(hospitalList = HospitalListActivity.sampleData)
        }
    }
}