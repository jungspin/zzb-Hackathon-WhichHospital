package com.zzb.whichhospital.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface DiseaseDao {
    @Query("SELECT * FROM diseases")
    suspend fun getDiseases() : List<Disease>

    @Query("SELECT * FROM diseases WHERE diseaseId = :id")
    suspend fun getDiseaseById(id : Long): DiseaseWithSymtoms

    @Transaction
    @Query("SELECT * FROM diseases")
    suspend fun getDiseasesWithSymptoms(): List<DiseaseWithSymtoms>
}