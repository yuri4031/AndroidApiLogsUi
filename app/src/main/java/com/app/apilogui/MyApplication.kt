package com.app.apilogui

import android.app.Application
import com.lib.db.ApiHelper
import com.lib.retrofit.log.RetrofitHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ApiHelper.initialize(this, true, 100)

    }

    companion object {
        var sInstance: ApiService? = null
        fun getApiService(): ApiService? {
            if (sInstance == null) {
                synchronized(ApiService::class.java) {
                    if (sInstance == null) {

                        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
                            .readTimeout(120, TimeUnit.SECONDS)
                            .connectTimeout(120, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(false)
                            .addNetworkInterceptor { chain ->
                                val original = chain.request()
                                val request = original.newBuilder()
                                    .header("Accept", "application/json")
                                    .header("Content-Type", "application/json; charset=UTF-8")
                                    .method(original.method(), original.body())
                                    .build()
                                chain.proceed(request)
                            }
                        val logging = HttpLoggingInterceptor()
                        httpClient.addInterceptor(logging)
                        logging.level = HttpLoggingInterceptor.Level.BODY

                        return Retrofit.Builder()
                            .baseUrl("https://eps-net-uat.sadadbh.com/api/v2/mob-cus-sdd/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient.build())
                            .build()
                            .create(ApiService::class.java)
                    }
                }

            }
            return sInstance
        }

    }
}