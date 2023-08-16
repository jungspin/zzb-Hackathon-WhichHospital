package com.zzb.whichhospital.data.local.mapper

import com.zzb.whichhospital.data.local.entity.Disease
import com.zzb.whichhospital.data.local.entity.DiseaseWithSymptoms

/**
 * DiseaseWithSymptoms Entity 를
 * presentation 계층의 Disease 클래스로 변환
 */
fun DiseaseWithSymptoms.toPresentationDisease(): com.zzb.whichhospital.presentation.model.Disease {
    val disease = this.disease
    val symptoms = this.symptoms.map { it.symptomContent }

    return com.zzb.whichhospital.presentation.model.Disease(
        disease.diseaseId,
        disease.diseaseName,
        symptoms
    )
}

/**
 * Disease Entity 를
 * presentation 계층의 Disease 클래스로 변환
 */
fun Disease.toPresentationDisease(): com.zzb.whichhospital.presentation.model.Disease {
    return com.zzb.whichhospital.presentation.model.Disease(
        this.diseaseId,
        this.diseaseName,
        null
    )
}

