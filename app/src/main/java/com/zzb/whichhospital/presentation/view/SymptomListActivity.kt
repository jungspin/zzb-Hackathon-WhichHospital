package com.zzb.whichhospital.presentation.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asLiveData
import com.zzb.whichhospital.base.Status
import com.zzb.whichhospital.presentation.model.Disease
import com.zzb.whichhospital.presentation.ui.theme.WhichHospitalTheme
import com.zzb.whichhospital.presentation.view_model.SymptomListViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * 선택된 질환의 증상 목록을 확인하는 화면
 * @author jungspin
 * @since 2023/08/13 12:10 PM
 */
private const val TAG = "SymptomActivityTest"

@AndroidEntryPoint
class SymptomActivity : ComponentActivity() {

    private val viewModel: SymptomListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val diseaseId = intent.getLongExtra(MainActivity.INTENT_KEY_DISEASE_ID, 0)

        viewModel.getDiseaseById(diseaseId)
        viewModel.diseaseStateFlow.asLiveData().observe(this) { uiState ->
            setContent {
                RootSurface {
                    when (uiState.status) {
                        Status.NONE -> {}
                        Status.LOADING -> LoadingView()
                        Status.SUCCESS -> {
                            uiState.data?.let { disease ->
                                ListView(
                                    diseaseName = disease.diseaseName,
                                    isNeedBottomButton = true,
                                    backButtonAction = { finish() }
                                ) {
                                    SymptomList(disease = disease)
                                }
                            }
                        }

                        Status.FAIL -> {
                            Toast.makeText(
                                LocalContext.current,
                                uiState.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }

                        Status.ERROR -> ErrorView {
                            viewModel.getDiseaseById(diseaseId)
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val INTENT_KEY_DISEASE_NAME = "DiseaseName"
        val sampleDisease = Disease(
            diseaseId = 0L,
            diseaseName = "심근경색",
            symptoms = listOf(
                "운동하거나 빨리 걸을 때 가슴 통증, 압박감, 불쾌감이 느껴진다.",
                "목∙어깨∙팔에 통증과 압박감이 느껴진다.",
                "이유 없이 숨이 차고 가슴이 뛰다가 회복된다.",
                "분명한 원인 없이 발생되는 갑작스럽고 심한 두통이 있다.",
                "어지럽고 졸도할 것 같은 느낌이 있다.",
                "운동하거나 빨리 걸을 때 가슴 통증, 압박감, 불쾌감이 느껴진다.",
                "목∙어깨∙팔에 통증과 압박감이 느껴진다.",
                "이유 없이 숨이 차고 가슴이 뛰다가 회복된다.",
                "분명한 원인 없이 발생되는 갑작스럽고 심한 두통이 있다.",
                "어지럽고 졸도할 것 같은 느낌이 있다.",
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhichHospitalAppBar(title: String, backButtonAction: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, textAlign = TextAlign.Center) },
        navigationIcon = {
            IconButton(onClick = { /* TODO 뒤로가기 구현 */ backButtonAction() }) {
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
fun ListView(
    diseaseName: String,
    backButtonAction: () -> Unit,
    isNeedBottomButton: Boolean,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            WhichHospitalAppBar(title = diseaseName) { backButtonAction() }
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
            Column(Modifier.weight(3f)) {
                content()
            }


            if (isNeedBottomButton) {
                HospitalListButton(diseaseName)
            }
        }
    }
}

@Composable
fun SymptomList(disease: Disease) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
    ) {
        disease.symptoms?.let {
            items(it) { symptom ->
                SymptomItem(symptom = symptom)
            }
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

    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val launcherMultiplePermissions =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { permissionMap ->
            val grantedAll = permissionMap.values.reduce { acc, next -> acc && next }
            // 권한을 모두 동의한 경우
            // 권한 동의 여부를 뷰모델로 전달?
            // 동의한 경우 정보를 요청하고, 동의하지 않은 경우는..다이얼로그로 한번 더 호소? 토스트로 내보내버리기?

            if (grantedAll) {
                val intent = Intent(context, HospitalListActivity::class.java)
                intent.putExtra(SymptomActivity.INTENT_KEY_DISEASE_NAME, diseaseName)
                context.startActivity(intent)
            }

        }

    Button(
        onClick = {

            checkPermission(
                context = context,
                permissions = permissions,
                launcher = launcherMultiplePermissions
            )

        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(15.dp),
    ) {
        SymptomText(text = "병원 확인하기", textAlign = TextAlign.Center)
    }
}

@Preview(showBackground = true)
@Composable
fun SymptomActivityPreview() {
    WhichHospitalTheme {
        RootSurface {
            ListView(SymptomActivity.sampleDisease.diseaseName, {}, true) {
                SymptomList(disease = SymptomActivity.sampleDisease)
            }
        }
    }
}