package com.zzb.whichhospital.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Disease::class, Symptom::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diseaseDao(): DiseaseDao
}