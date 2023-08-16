package com.zzb.whichhospital.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzb.whichhospital.base.Status
import com.zzb.whichhospital.domain.usecase.DiseaseUseCase
import com.zzb.whichhospital.presentation.model.Disease
import com.zzb.whichhospital.presentation.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 증상 리스트 화면 ViewModel
 * @author jungspin
 * @since 2023/08/15 4:53 PM
 */
@HiltViewModel
class SymptomListViewModel @Inject constructor(private val diseaseUseCase: DiseaseUseCase) :
    ViewModel() {

    private val _diseaseStateFlow = MutableStateFlow(UiState<Disease>())
    val diseaseStateFlow = _diseaseStateFlow.asStateFlow()

    private val _diseasesStateFlow = MutableStateFlow(UiState<List<Disease>>())
    val diseasesStateFlow = _diseasesStateFlow.asStateFlow()

    /**
     * 질환 목록 불러오기
     */
    fun getDiseases() {
        _diseasesStateFlow.value.apply {
            viewModelScope.launch {
                diseaseUseCase.getDisease().onStart {
                    _diseasesStateFlow.value = UiState(
                        status = Status.LOADING,
                        message = "잠시만 기다려주세요...",
                    )
                }
                    .stateIn(viewModelScope)
                    .collect { data ->
                        if (data.isEmpty()) {
                            _diseasesStateFlow.value = UiState(
                                status = Status.SUCCESS,
                                message = "질환이 존재하지 않습니다.",
                            )
                        } else {
                            _diseasesStateFlow.value = UiState(
                                status = Status.SUCCESS,
                                message = "질환을 모두 불러왔습니다.",
                                data = data,
                            )
                        }

                    }
            }
        }
    }

    /**
     * 선택된 질환 불러오기
     *
     * @param id 질환 id
     */
    fun getDiseaseById(id: Long) {
        _diseaseStateFlow.value.apply {
            viewModelScope.launch {
                diseaseUseCase.getDiseaseById(id).onStart {
                    _diseaseStateFlow.value = UiState(
                        status = Status.LOADING,
                        message = "잠시만 기다려주세요...",
                    )
                }
                    .stateIn(viewModelScope)
                    .collect { disease ->
                        if (disease.diseaseId == -1L) {
                            _diseaseStateFlow.value = UiState(
                                status = Status.FAIL,
                                message = "질환을 불러오는데 실패했습니다.",
                            )
                        } else {
                            _diseaseStateFlow.value = UiState(
                                status = Status.SUCCESS,
                                message = "질환을 불러오는데 성공했습니다.",
                                data = disease

                            )
                        }
                    }
            }
        }
    }
}