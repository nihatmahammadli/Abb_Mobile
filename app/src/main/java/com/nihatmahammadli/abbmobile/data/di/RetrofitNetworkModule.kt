package com.nihatmahammadli.abbmobile.data.di

import android.util.Log
import com.nihatmahammadli.abbmobile.data.remote.CardApi
import com.nihatmahammadli.abbmobile.data.remote.RetryInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitNetworkModule {

    @Provides
    fun provideBaseUrl() = "https://fakecardapi-1.onrender.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        Log.d("RetrofitNetworkModule", "OkHttpClient working")
        return OkHttpClient.Builder()
            .addInterceptor(RetryInterceptor(maxRetry = 3))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideCardApi(retrofit: Retrofit): CardApi =
        retrofit.create(CardApi::class.java)
}
