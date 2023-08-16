package com.zzb.whichhospital.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName="symptoms", foreignKeys = [
    ForeignKey(
        entity = Disease::class,
        parentColumns = ["disease_id"],
        childColumns = ["disease_id"]
    )
] )
data class Symptom(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("symptom_id") val symptomId : Long,
    @ColumnInfo("symptom_content") val symptomContent : String,
    @ColumnInfo(name = "disease_id") val diseaseId : Long,
)
