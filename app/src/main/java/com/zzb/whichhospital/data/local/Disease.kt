package com.zzb.whichhospital.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diseases")
data class Disease(
    @PrimaryKey(autoGenerate = true)
    val diseaseId: Long,
    val diseaseName: String,
)
