package com.asif.lithiumaktie.network

import com.asif.lithiumaktie.model.SubscribeRequest
import com.asif.lithiumaktie.model.SubscribeResponse
import com.asif.lithiumaktie.utility.Constants.Companion.APP_REQUEST_TIMEOUT
import com.asif.lithiumaktie.utility.Constants.Companion.base_url
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit


private val gson: Gson = GsonBuilder()
    .setLenient()
    .create()

fun provideOkkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    return OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(APP_REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(APP_REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(APP_REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .build()
}

private val retrofit = Retrofit.Builder()
    .baseUrl(base_url)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .client(provideOkkHttpClient())
    .build()

object SubscribeApi {
    val retrofitService: SubscribeApiService by lazy {
        retrofit.create(SubscribeApiService::class.java)
    }
}

interface SubscribeApiService {
    @Headers(
        "Accept: application/" +
                "json"
    )
    @POST("res_api/api/v1/register")
    fun getSubscribeResponse(
        @Body body: RequestBody
    ): Call<SubscribeResponse>
}



