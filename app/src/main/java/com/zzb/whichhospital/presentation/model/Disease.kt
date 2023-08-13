package com.zzb.whichhospital.presentation.model

data class Disease(
    val diseaseId: Long,
    val diseaseName: String,
    val symptoms: List<String>
)
