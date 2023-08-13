package com.zzb.whichhospital.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class DiseaseWithSymtoms(
    @Embedded val disease: Disease,
    @Relation(
        parentColumn = "diseaseId",
        entityColumn = "disease_id"
    )
    val symptoms: List<Symptom>
)
