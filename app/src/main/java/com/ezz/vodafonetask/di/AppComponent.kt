package com.ezz.vodafonetask.di

import com.ezz.vodafonetask.ui.photo_details.PhotoDetailsComponent
import com.ezz.vodafonetask.ui.photos_listing.PhotosListingComponent

import android.content.Context
import androidx.viewbinding.ViewBinding
import com.ezz.vodafonetask.retrofit.RetrofitModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class, RetrofitModule::class, SubComponentsModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance applicationContext: Context): AppComponent
    }
    
    fun photosListingComponent(): PhotosListingComponent.Factory

    fun photoDetailsComponent(): PhotoDetailsComponent.Factory
}
