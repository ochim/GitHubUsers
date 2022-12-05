package com.example.githubusers.api

import com.example.githubusers.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val GITHUB_API_URL = "https://api.github.com/"
private const val GITHUB_TOKEN =
    "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"

object GitHubService {
    var retrofit: Retrofit

    init {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        this.retrofit = Retrofit.Builder()
            .baseUrl(GITHUB_API_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(getHttpClient())
            .build()
    }

    private fun getHttpClient() = OkHttpClient.Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        .addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/vnd.github+json")
                .addHeader("Authorization", "Bearer $GITHUB_TOKEN")
                .build()
            return@Interceptor chain.proceed(request)
        })
        .build()
}