package student.projects.st10082949_newsapp.data.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Use the API key from your documentation
    private const val RAPIDAPI_KEY = "c209eff306msh2ff33c99dc8806bp120722jsn2a197f3b175e"

    // Try different base URLs - the API might have changed
    private const val BASE_URL = "https://news-api14.p.rapidapi.com"
    private const val RAPIDAPI_HOST = "news-api14.p.rapidapi.com"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val requestInterceptor = Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("X-RapidAPI-Key", RAPIDAPI_KEY)
            .header("X-RapidAPI-Host", RAPIDAPI_HOST)
            .header("Content-Type", "application/json")
            .method(original.method, original.body)
            .build()

        Log.d("Network", "Making request to: ${original.url}")
        Log.d("Network", "Headers: X-RapidAPI-Key: $RAPIDAPI_KEY")
        Log.d("Network", "Headers: X-RapidAPI-Host: $RAPIDAPI_HOST")
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(requestInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val newsApiService: NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }
}