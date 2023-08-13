package com.zzb.whichhospital.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName="symptoms", foreignKeys = [
    ForeignKey(
        entity = Disease::class,
        parentColumns = ["diseaseId"],
        childColumns = ["disease_id"],
        onDelete = ForeignKey.CASCADE
    )
] )
data class Symptom(
    @PrimaryKey(autoGenerate = true)
    val symptomId : Long,
    val symptomContent : String,
    @ColumnInfo(name = "disease_id") val diseaseId : Long,
)
