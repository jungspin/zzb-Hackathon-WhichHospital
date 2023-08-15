package com.zzb.whichhospital.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diseases")
data class Disease(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("disease_id") val diseaseId: Long,
    @ColumnInfo("disease_name") val diseaseName: String,
)
