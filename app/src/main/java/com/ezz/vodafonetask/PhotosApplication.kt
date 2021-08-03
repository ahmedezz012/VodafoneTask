package com.ezz.vodafonetask

import android.app.Application
import com.ezz.vodafonetask.di.AppComponent
import com.ezz.vodafonetask.di.DaggerAppComponent
import com.ezz.vodafonetask.utils.Utils
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins

class PhotosApplication : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        initDaggerAppComponent()
        handleRXJavaErrors()
    }

    private fun initDaggerAppComponent() {
        appComponent = DaggerAppComponent.factory()
            .create(this)
    }

    /**
     * Handles the RX Undeliverable exception for when RX fires events after a dispose.
     *
     * For more info check [https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#error-handling]
     */
    private fun handleRXJavaErrors() {
        RxJavaPlugins.setErrorHandler { throwable: Throwable ->
            if (throwable is UndeliverableException)
                return@setErrorHandler
            Utils.printStackTrace(throwable)
            throw Exception(throwable)
        }
    }
}