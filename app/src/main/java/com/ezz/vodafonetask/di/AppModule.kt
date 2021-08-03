package com.ezz.vodafonetask.di

import android.content.Context
import androidx.room.Room
import com.ezz.vodafonetask.data.local.ILocalDataSource
import com.ezz.vodafonetask.data.local.LocalDataSource
import com.ezz.vodafonetask.data.remote.IRemoteDataSource
import com.ezz.vodafonetask.data.remote.RemoteDataSource
import com.ezz.vodafonetask.database.PhotosDatabase
import com.ezz.vodafonetask.retrofit.ApiInterface
import com.ezz.vodafonetask.utils.ConnectionUtils
import com.ezz.vodafonetask.utils.Constants.Database.DATABASE_NAME
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().serializeNulls().create()
    }

    @Provides
    @Singleton
    fun provideConnectivity(context: Context): ConnectionUtils {
        return ConnectionUtils(context)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(context: Context, photosDatabase: PhotosDatabase): ILocalDataSource {
        return LocalDataSource(context, photosDatabase)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiInterface: ApiInterface): IRemoteDataSource {
        return RemoteDataSource(apiInterface)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): PhotosDatabase {
        return Room.databaseBuilder(
            context,
            PhotosDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

}