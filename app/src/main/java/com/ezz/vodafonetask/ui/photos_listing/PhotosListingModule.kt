package com.ezz.vodafonetask.ui.photos_listing

import com.ezz.vodafonetask.data.repositories.photos_listing.IPhotosListingRepository
import com.ezz.vodafonetask.data.repositories.photos_listing.PhotosListingRepository
import dagger.Binds
import dagger.Module

@Module
abstract class PhotosListingModule {
    @Binds
    abstract fun getIPhotosListingRepository(photosListingRepository: PhotosListingRepository): IPhotosListingRepository
}