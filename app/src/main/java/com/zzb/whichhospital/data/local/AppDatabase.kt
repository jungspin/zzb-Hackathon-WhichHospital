package com.zzb.whichhospital.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zzb.whichhospital.data.local.dao.DiseaseDao
import com.zzb.whichhospital.data.local.entity.Disease
import com.zzb.whichhospital.data.local.entity.Symptom

@Database(entities = [Disease::class, Symptom::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diseaseDao(): DiseaseDao
}