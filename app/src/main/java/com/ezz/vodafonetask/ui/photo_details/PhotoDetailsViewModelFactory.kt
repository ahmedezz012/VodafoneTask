package com.ezz.vodafonetask.ui.photo_details

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ezz.vodafonetask.PhotosApplication
import androidx.savedstate.SavedStateRegistryOwner
import com.ezz.vodafonetask.domain.use_cases.photo_details.PhotoDetailsUseCase
import com.ezz.vodafonetask.ui.base.BaseViewModelFactory
import com.ezz.vodafonetask.data.models.navigation_component_dto.PhotoDetailsData
import javax.inject.Inject

class PhotoDetailsViewModelFactory(
    context: Context,
    private val photoDetailsData: PhotoDetailsData, owner: SavedStateRegistryOwner
) : BaseViewModelFactory(context, owner) {

    @Inject
    lateinit var mPhotoDetailsUseCase: PhotoDetailsUseCase

    override fun injectDependencies(context: Context) {
        (context.applicationContext as PhotosApplication).appComponent.photoDetailsComponent()
            .create().inject(this)
    }

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return PhotoDetailsViewModel(mPhotoDetailsUseCase, photoDetailsData, handle) as T
    }
}