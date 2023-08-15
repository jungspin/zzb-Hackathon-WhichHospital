package com.zzb.whichhospital.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class DiseaseWithSymptoms(
    @Embedded val disease: Disease,
    @Relation(
        parentColumn = "disease_id",
        entityColumn = "disease_id"
    )
    val symptoms: List<Symptom>
)
