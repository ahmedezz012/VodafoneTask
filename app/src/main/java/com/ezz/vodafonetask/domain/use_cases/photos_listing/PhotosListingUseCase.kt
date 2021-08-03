package com.ezz.vodafonetask.domain.use_cases.photos_listing

import com.ezz.vodafonetask.data.models.*
import com.ezz.vodafonetask.data.models.dto.APIResponse
import com.ezz.vodafonetask.data.models.dto.PhotoItem
import com.ezz.vodafonetask.data.repositories.photos_listing.IPhotosListingRepository
import com.ezz.vodafonetask.ui.base.BaseUseCase
import com.ezz.vodafonetask.data.repositories.photos_listing.PhotosListingRepository
import com.ezz.vodafonetask.utils.Constants
import com.ezz.vodafonetask.utils.Constants.PAGING.DEFAULT_PAGE_INDEX
import com.ezz.vodafonetask.utils.Constants.PAGING.SECOND_PAGE_INDEX
import io.reactivex.Single
import javax.inject.Inject


class PhotosListingUseCase @Inject constructor(
    private val mPhotosListingRepository: IPhotosListingRepository
) : BaseUseCase(mPhotosListingRepository), IPhotosListingUseCase {

    override fun getPhotosList(
        pageIndex: Int,
        pageSize: Int,
        tag: String
    ): Single<Status<ArrayList<PhotoItem>?>> {
        mPhotosListingRepository.cancelAPI(Constants.APIsRequestsTags.PHOTOS_LISTING_TAG)
        return mPhotosListingRepository.getPhotosList(pageIndex, pageSize, tag)
            .map(this::mapPhotosResponse)
            .flatMap { status -> getPhotosListFromLocalStorage(status, pageIndex) }
    }

    private fun mapPhotosResponse(photosListResponseStatus: Status<APIResponse<ArrayList<PhotoItem>?>>): Status<ArrayList<PhotoItem>?> {
        return when (validateResponse(photosListResponseStatus)) {
            StatusCode.VALID ->
                onPhotosValid(photosListResponseStatus)
            StatusCode.NO_NETWORK ->
                Status.NoNetwork(errorModel = ErrorModel.NoNetworkError())
            StatusCode.ERROR ->
                Status.Error(errorModel = ErrorModel.Error())
            else ->
                Status.Error(errorModel = ErrorModel.Error())
        }
    }

    private fun onPhotosValid(photosListResponseStatus: Status<APIResponse<java.util.ArrayList<PhotoItem>?>>): Status<java.util.ArrayList<PhotoItem>?> {
        if ((photosListResponseStatus.data?.result as ArrayList<PhotoItem>?).isNullOrEmpty())
            return Status.NoData(errorModel = ErrorModel.NoDataError())

        return Status.Success(photosListResponseStatus.data?.result)
    }

    private fun getPhotosListFromLocalStorage(
        status: Status<ArrayList<PhotoItem>?>,
        pageIndex: Int
    ): Single<Status<ArrayList<PhotoItem>?>> {
        if (pageIndex == DEFAULT_PAGE_INDEX && status.isSuccess()) {
            return mPhotosListingRepository.deleteAllPhotos().flatMap {
                mPhotosListingRepository.insertPhotos(status.data!!).flatMap {
                    Single.just(status)
                }
            }

        } else if (pageIndex == SECOND_PAGE_INDEX && status.isSuccess()) {
            return mPhotosListingRepository.insertPhotos(status.data!!).flatMap {
                Single.just(status)
            }
        } else if (!status.isNoNetwork())
            return Single.just(status)
        return mPhotosListingRepository.getOfflinePhotosList().flatMap {
            val offlinePhotosList = it
            if (!offlinePhotosList.isNullOrEmpty() && pageIndex == DEFAULT_PAGE_INDEX)
                Single.just(Status.OfflineData(offlinePhotosList))
            else {
                Single.just(Status.NoNetwork(errorModel = ErrorModel.NoNetworkError()))
            }
        }
    }

}