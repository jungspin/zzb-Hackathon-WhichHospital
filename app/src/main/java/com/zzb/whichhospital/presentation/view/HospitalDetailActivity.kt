package com.zzb.whichhospital.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.zzb.whichhospital.presentation.ui.theme.WhichHospitalTheme
import dagger.hilt.android.AndroidEntryPoint
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState

/**
 * 병원 상세 정보를 확인하는 화면
 * @author jungspin
 * @since 2023/08/13
 */
@AndroidEntryPoint
class HospitalDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HospitalDetailView(true)
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun HospitalDetailView(isVisible: Boolean) {
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.INVALID
    }
    if (isVisible) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.6f)
                .border(
                    border = ButtonDefaults.outlinedButtonBorder,
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(12.dp),
        ) {
            NaverMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f),
                cameraPositionState = cameraPositionState
            )
        }
    }


}


@OptIn(ExperimentalNaverMapApi::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RootSurface {
//        HospitalDetailView()
        Column {
            NaverMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
    }
}