package com.zzb.whichhospital.presentation.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.zzb.whichhospital.R
import com.zzb.whichhospital.presentation.ui.theme.WhichHospitalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootSurface {
                Main()
            }
        }
    }

    companion object {
        const val INTENT_KEY_DISEASE_ID = "DiseaseId"
        val diseaseContentList = listOf(
            DiseaseContent(1, "뇌졸중", R.raw.lottie_medical_6),
            DiseaseContent(2, "심근경색", R.raw.lottie_medical_2),
            DiseaseContent(3, "뇌진탕", R.raw.lottie_medical_3),
            DiseaseContent(4, "녹내장", R.raw.lottie_medical_4),
        )
    }
}

@Composable
fun Main() {
    Column {
        MainText()
        MainGrid()
    }
}

/**
 * 메인화면 상단 타이틀
 */
@Composable
fun MainText() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .padding(PaddingValues(horizontal = 0.dp, vertical = 8.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "어떤 질환을 알아볼까요?",
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

/**
 * 메인화면 하단 질환 그리드
 */
@Composable
fun MainGrid() {
    val context = LocalContext.current
    val intent = Intent(context, SymptomActivity::class.java)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
            .padding(PaddingValues(top = 8.dp)),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(MainActivity.diseaseContentList) { diseaseContent ->
                DiseaseItem(
                    rawId = diseaseContent.diseaseRawId,
                    diseaseName = diseaseContent.diseaseName
                ) {
                    intent.putExtra(MainActivity.INTENT_KEY_DISEASE_ID, diseaseContent.diseaseId)
                    context.startActivity(intent)
                }
            }
        }
    }
}

/**
 * Disease Item
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiseaseItem(rawId: Int, diseaseName: String, clickAction: () -> Unit) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(rawId)
    )
    val lottieAnimatable = rememberLottieAnimatable()
    LaunchedEffect(composition) {
        lottieAnimatable.animate(
            composition = composition,
            clipSpec = LottieClipSpec.Frame(0, 1200),
            reverseOnRepeat = true,
            initialProgress = 0f
        )
    }

    Card(
        onClick = { clickAction() },
        shape = CardDefaults.outlinedShape,
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),

        ) {
        Column(
            modifier = Modifier
                .height(200.dp)
                .padding(4.dp)
        ) {
            LottieAnimation(
                composition = composition,
                progress = { lottieAnimatable.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f),
                contentScale = ContentScale.Fit
            )
            Text(
                text = diseaseName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

/**
 * 질환 매칭 컨텐츠 데이터 클래스
 *
 * @property diseaseName 질환 이름
 * @property diseaseRawId 컨텐츠 id
 */
data class DiseaseContent(
    val diseaseId: Int,
    val diseaseName: String,
    val diseaseRawId: Int
)

@Composable
fun RootSurface(content: @Composable () -> Unit) {
    WhichHospitalTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    RootSurface {
        Main()
    }
}