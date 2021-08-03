package com.ezz.vodafonetask.ui.photo_details

import dagger.Subcomponent

@Subcomponent
interface PhotoDetailsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PhotoDetailsComponent
    }

    fun inject(mPhotoDetailsViewModelFactory: PhotoDetailsViewModelFactory)
}