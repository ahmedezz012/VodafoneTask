package com.ezz.vodafonetask.di

import com.ezz.vodafonetask.ui.photo_details.PhotoDetailsComponent
import com.ezz.vodafonetask.ui.photos_listing.PhotosListingComponent
import dagger.Module

@Module(
    subcomponents = [PhotosListingComponent::class,
        PhotoDetailsComponent::class]
)
class SubComponentsModule
