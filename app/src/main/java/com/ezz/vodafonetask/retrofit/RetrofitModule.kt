package com.ezz.vodafonetask.retrofit

import com.ezz.vodafonetask.BuildConfig
import com.ezz.vodafonetask.utils.Constants

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideOkHttp(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okhttp = OkHttpClient.Builder()
                .connectTimeout(Constants.Network.CONNECT_TIMEOUT, TimeUnit.MINUTES)
                .readTimeout(Constants.Network.READ_TIMEOUT, TimeUnit.MINUTES)
                .writeTimeout(Constants.Network.WRITE_TIMEOUT, TimeUnit.MINUTES)
        if (BuildConfig.DEBUG)
            okhttp.addInterceptor(httpLoggingInterceptor)
        return okhttp.build()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constants.URL.BASE_NETWORK_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
}
