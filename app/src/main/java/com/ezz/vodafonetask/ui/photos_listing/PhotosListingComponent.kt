package com.ezz.vodafonetask.ui.photos_listing

import dagger.Subcomponent

@Subcomponent(modules = [PhotosListingModule::class])
interface PhotosListingComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PhotosListingComponent
    }

    fun inject(mPhotosListingViewModelFactory: PhotosListingViewModelFactory)
}