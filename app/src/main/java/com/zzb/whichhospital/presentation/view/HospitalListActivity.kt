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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
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
                ListLayout(diseaseName = diseaseName, isNeedButton = false) {
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
                distanceFromHospital = 30
            ),
            SampleHospital(
                hospitalName = "부산백병원",
                hospitalTel = "051-000-0000",
                distanceFromHospital = 45
            ),
            SampleHospital(
                hospitalName = "서울대 병원",
                hospitalTel = "02-0000-0000",
                distanceFromHospital = 300
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
    Card(
        onClick = { moveToHospitalDetail(context = context) },
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
                onClick = { moveToHospitalDetail(context = context) },
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "병원 자세히 보기 버튼",
                )
            }

        }

    }
}

fun moveToHospitalDetail(context: Context){
    context.startActivity(Intent(context, HospitalDetailActivity::class.java))
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

data class SampleHospital(
    val hospitalName: String,
    val hospitalTel: String,
    val distanceFromHospital: Int,
)

@Preview(showBackground = true)
@Composable
fun HospitalListPreview() {
    RootSurface {
        ListLayout(diseaseName = "심근경색", isNeedButton = false) {
            HospitalList(hospitalList = HospitalListActivity.sampleData)
        }
    }
}