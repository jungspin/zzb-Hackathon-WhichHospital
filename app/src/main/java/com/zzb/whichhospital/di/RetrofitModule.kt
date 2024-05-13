package com.zzb.whichhospital.di

import com.google.gson.Gson
import com.zzb.whichhospital.BuildConfig
import com.zzb.whichhospital.data.remote.api.HospitalApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Singleton
    @Provides
    fun getRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BuildConfig.HOSPITAL_BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun getOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.MINUTES)
            .connectionPool(ConnectionPool(5, 50, TimeUnit.SECONDS))
            .build()
    }

    @Singleton
    @Provides
    fun getHospitalService(retrofit: Retrofit): HospitalApi {
        return retrofit.create(HospitalApi::class.java)
    }
}