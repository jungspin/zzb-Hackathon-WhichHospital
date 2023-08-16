package com.zzb.whichhospital.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.zzb.whichhospital.data.local.entity.Disease
import com.zzb.whichhospital.data.local.entity.DiseaseWithSymptoms

@Dao
interface DiseaseDao {
    @Query("SELECT * FROM diseases")
    suspend fun getDiseases() : List<Disease>

    @Query("SELECT * FROM diseases WHERE disease_id = :id")
    suspend fun getDiseaseById(id : Long): DiseaseWithSymptoms

    @Transaction
    @Query("SELECT * FROM diseases")
    suspend fun getDiseasesWithSymptoms(): List<DiseaseWithSymptoms>
}