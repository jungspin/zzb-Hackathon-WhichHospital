package com.zzb.whichhospital.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface DiseaseDao {
    @Query("SELECT * FROM diseases")
    fun getDiseases() : List<Disease>

    @Query("SELECT * FROM diseases WHERE disease_id = :id")
    fun getDiseaseById(id : Long): DiseaseWithSymptoms

    @Transaction
    @Query("SELECT * FROM diseases")
    fun getDiseasesWithSymptoms(): List<DiseaseWithSymptoms>
}