package com.ezz.vodafonetask.ui.photos_listing

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.ezz.vodafonetask.PhotosApplication
import com.ezz.vodafonetask.domain.use_cases.photos_listing.PhotosListingUseCase
import com.ezz.vodafonetask.ui.base.BaseViewModelFactory
import javax.inject.Inject

class PhotosListingViewModelFactory(
    context: Context,
    owner: SavedStateRegistryOwner
) : BaseViewModelFactory(context, owner) {

    @Inject
    lateinit var mPhotosListingUseCase: PhotosListingUseCase

    override fun injectDependencies(context: Context) {
        (context.applicationContext as PhotosApplication).appComponent.photosListingComponent()
            .create().inject(this)
    }

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return PhotosListingViewModel(mPhotosListingUseCase, handle) as T
    }
}