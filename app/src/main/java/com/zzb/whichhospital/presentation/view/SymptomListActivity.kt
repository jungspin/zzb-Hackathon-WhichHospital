package com.zzb.whichhospital.presentation.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zzb.whichhospital.presentation.ui.theme.WhichHospitalTheme


/**
 * 선택된 질환의 증상 목록을 확인하는 화면
 * @author jungspin
 * @since 2023/08/13 12:10 PM
 */
private const val TAG = "SymptomActivityTest"

class SymptomActivity : ComponentActivity() {
    private val sampleDisease = SampleDisease(
        id = 0,
        diseaseName = "심근경색",
        diseaseSymptom = SampleSymptom(
            id = 0,
            symptoms = listOf(
                "운동하거나 빨리 걸을 때 가슴 통증, 압박감, 불쾌감이 느껴진다.",
                "목∙어깨∙팔에 통증과 압박감이 느껴진다.",
                "이유 없이 숨이 차고 가슴이 뛰다가 회복된다.",
                "분명한 원인 없이 발생되는 갑작스럽고 심한 두통이 있다.",
                "어지럽고 졸도할 것 같은 느낌이 있다.",
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val diseaseId = intent.getIntExtra(MainActivity.INTENT_KEY_DISEASE_ID, 0)
        setContent {
            RootSurface {
                ListLayout(
                    diseaseName = sampleDisease.diseaseName,
                    isNeedButton = true
                ) {
                    SymptomList(disease = sampleDisease)
                }
            }
        }
    }

    companion object {
        const val INTENT_KEY_DISEASE_NAME = "DiseaseName"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhichHospitalAppBar(title: String) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, textAlign = TextAlign.Center) },
        navigationIcon = {
            IconButton(onClick = { /**/ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "뒤로가기 버튼"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListLayout(diseaseName: String, isNeedButton: Boolean, content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            WhichHospitalAppBar(title = diseaseName)
        },
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
                .padding(
                    PaddingValues(
                        top = it.calculateTopPadding(),
                        end = 8.dp,
                        start = 8.dp,
                        bottom = 8.dp
                    )
                )
        ) {
            content()

            if (isNeedButton) {
                HospitalListButton(diseaseName)
            }
        }
    }
}

@Composable
fun SymptomList(disease: SampleDisease) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
    ) {
        items(disease.diseaseSymptom.symptoms) { symptom ->
            SymptomItem(symptom = symptom)
        }
    }
}

@Composable
fun SymptomItem(symptom: String) {
    Card(
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        SymptomText(text = symptom)
    }
}

@SuppressLint("UnnecessaryComposedModifier")
@Composable
fun SymptomText(
    text: String,
    textAlign: TextAlign = TextAlign.Start,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = Modifier
            .padding(16.dp)
            .composed { modifier },
        style = MaterialTheme.typography.bodyLarge,
        textAlign = textAlign
    )
}

@Composable
fun HospitalListButton(diseaseName: String) {
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(context, HospitalListActivity::class.java)
            intent.putExtra(SymptomActivity.INTENT_KEY_DISEASE_NAME, diseaseName)
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(15.dp),
    ) {
        SymptomText(text = "병원 확인하기", textAlign = TextAlign.Center)
    }
}

/**
 * 화면 출력을 위한 샘플 질환 데이터 클래스
 *
 * @property id id
 * @property diseaseName 질환 이름
 * @property diseaseSymptom 질환 증상
 */
data class SampleDisease(
    val id: Int,
    val diseaseName: String,
    val diseaseSymptom: SampleSymptom,
)

/**
 * 화면 출력을 위한 샘플 증상 데이터 클래스
 *
 * @property id id
 * @property symptoms 증상
 */
data class SampleSymptom(
    val id: Int,
    val symptoms: List<String>,
)

@Preview(showBackground = true)
@Composable
fun SymptomActivityPreview() {
    val sampleDisease = SampleDisease(
        id = 0,
        diseaseName = "심근경색",
        diseaseSymptom = SampleSymptom(
            id = 0,
            symptoms = listOf(
                "운동하거나 빨리 걸을 때 가슴 통증, 압박감, 불쾌감이 느껴진다.",
                "목∙어깨∙팔에 통증과 압박감이 느껴진다.",
                "이유 없이 숨이 차고 가슴이 뛰다가 회복된다.",
                "분명한 원인 없이 발생되는 갑작스럽고 심한 두통이 있다.",
                "어지럽고 졸도할 것 같은 느낌이 있다.",
            )
        )
    )
    WhichHospitalTheme {
        RootSurface {
            ListLayout(sampleDisease.diseaseName, true) {
                SymptomList(disease = sampleDisease)
            }
        }
    }
}