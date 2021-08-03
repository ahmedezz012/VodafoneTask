package com.ezz.vodafonetask.ui.photos_listing

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.SavedStateHandle
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.Swatch
import com.ezz.vodafonetask.R
import com.ezz.vodafonetask.data.models.*
import com.ezz.vodafonetask.data.models.dto.PhotoItem
import com.ezz.vodafonetask.data.models.navigation_component_dto.PhotoDetailsData
import com.ezz.vodafonetask.domain.use_cases.photos_listing.IPhotosListingUseCase
import com.ezz.vodafonetask.ui.base.BasePagingViewModel
import com.ezz.vodafonetask.utils.Constants
import com.ezz.vodafonetask.utils.Utils
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.util.*


class PhotosListingViewModel(
    private val mPhotosListingUseCase: IPhotosListingUseCase,
    handle: SavedStateHandle
) : BasePagingViewModel(handle, mPhotosListingUseCase) {
    private var mPagingModel: PagingModel = PagingModel()
    private var mPhotosListStatus: Status<ArrayList<PhotosListItem>>? = null
    private var mPhotosListObservable =
        BehaviorSubject.create<Status<ArrayList<PhotosListItem>>>()

    /**
     * In case it's first call the request should be merged with the subject field.
     *
     * @return the Subject to be subscribed on for the data.
     */
    internal fun getPhotosList(): Observable<Status<ArrayList<PhotosListItem>>> {
        return if (mPhotosListStatus == null) {
            mPhotosListObservable.mergeWith(getPhotosList(ProgressTypes.MAIN_PROGRESS))
        } else {
            updatePhotosList(mPhotosListStatus!!)
            mPhotosListObservable.hide()
        }
    }

    private fun isCurrentPhotosListEmpty(): Boolean =
        mPhotosListStatus?.data == null || mPhotosListStatus!!.data!!.isEmpty()

    internal fun onPhotosListScroll(): Completable {
        return if (mPhotosListStatus != null && !mPhotosListStatus!!.isOfflineData() && shouldLoadMore && !isLoading)
            getPhotosList(ProgressTypes.PAGING_PROGRESS)
        else
            Completable.complete()
    }

    internal fun onRetryErrorBtnClick(): Completable {
        return getPhotosList(ProgressTypes.MAIN_PROGRESS)
    }

    /**
     * Why Completable, ignoreElement?
     *
     * This method calls the repository to get the list and returns completable,
     * As the wanted behaviour is to emit the data in map, and this method should only notify onComplete without onNext.
     *
     * Why cancel?
     *
     * In some cases We must cancel the previous requests before making a new one,
     * In this case consider the scenario in which the user scrolls to load more and then quickly refreshes the list,
     * If we don't cancel the load more request:
     *
     * 1- The refresh will clear the list.
     *
     * 2- The 2nd page from load more will be put at first.
     *
     * 3- The 1st page from refresh will be put at second.
     *
     * @param progressType The [ProgressTypes]
     * @return a completable to complete the request RX cycle.
     */
    private fun getPhotosList(
        progressType: ProgressTypes,
        shouldClear: Boolean = false
    ): Completable {
        return mPhotosListingUseCase.getPhotosList(
            mPagingModel.pageIndex,
            mPagingModel.pageSize,
            Constants.APIsRequestsTags.PHOTOS_LISTING_TAG
        )
            .doOnSubscribe { onGetPhotosSubscribe(progressType) }
            .map { mapPhotosResponse(it, shouldClear) }
            .doOnError { throwable -> onGetPhotosError(throwable, progressType) }
            .doOnSuccess { onGetPhotosSuccess(progressType) }
            .onErrorReturn { onPhotosErrorReturn(it, progressType) }
            .ignoreElement()
    }

    private fun mapPhotosResponse(
        photosListResponseStatus: Status<ArrayList<PhotoItem>?>,
        shouldClear: Boolean
    ) {
        val photosListStatus: Status<ArrayList<PhotosListItem>> =
            when (photosListResponseStatus) {
                is Status.Success -> {
                    onPhotosValid(photosListResponseStatus, shouldClear)
                }
                is Status.OfflineData -> {
                    onOfflineData(photosListResponseStatus)
                }
                else -> {
                    Status.CopyStatus(photosListResponseStatus, null)
                }
            }

        if (photosListStatus.isSuccess() || photosListStatus.isOfflineData()) {
            updatePhotosList(photosListStatus)
        } else if (isCurrentPhotosListEmpty()) {
            setPhotosListStatus(photosListStatus)
            updatePhotosList(photosListStatus)
        } else {
            val errorTitle = photosListStatus.errorModel?.errorTitle
            showToastMessage(errorTitle)
        }
    }

    private fun onPhotosValid(
        photosListResponseStatus: Status<ArrayList<PhotoItem>?>,
        shouldClear: Boolean
    ): Status<ArrayList<PhotosListItem>> {
        val newPhotosList = photosListResponseStatus.data
        val currentList = mPhotosListStatus?.data ?: ArrayList()


        val newPhotosListItem = convertPhotosListToPhotosListItems(newPhotosList)

        if (shouldClear) {
            currentList.clear()
        }

        clearData(shouldClear)


        if (!newPhotosListItem.isNullOrEmpty()) {
            newPhotosListItem.add(5, PhotosListItem(ListItemType.AD_ITEM, null))
            newPhotosListItem.add(PhotosListItem(ListItemType.AD_ITEM, null))
        }


        currentList.addAll(newPhotosListItem)

        increasePageIndex()

        if (newPhotosList.isNullOrEmpty())
            shouldLoadMore = false

        setPhotosListStatus(Status.Success(currentList))

        return Status.Success(newPhotosListItem)
    }

    private fun convertPhotosListToPhotosListItems(newPhotosList: ArrayList<PhotoItem>?): java.util.ArrayList<PhotosListItem> {
        val newPhotosListItem: ArrayList<PhotosListItem> = arrayListOf()
        newPhotosList?.forEach {
            newPhotosListItem.add(PhotosListItem(photoItem = it))
        }
        return newPhotosListItem
    }

    private fun onOfflineData(photosListResponseStatus: Status<ArrayList<PhotoItem>?>): Status<ArrayList<PhotosListItem>> {
        val newPhotosList = photosListResponseStatus.data!!
        val newPhotosListItem = convertPhotosListToPhotosListItems(newPhotosList)

        setPhotosListStatus(Status.OfflineData(newPhotosListItem))
        return Status.OfflineData(newPhotosListItem)
    }

    private fun increasePageIndex() {
        mPagingModel.increasePageIndex()
    }

    private fun updatePhotosList(status: Status<ArrayList<PhotosListItem>>) {
        mPhotosListObservable.onNext(status)
    }

    private fun setPhotosListStatus(status: Status<ArrayList<PhotosListItem>>) {
        mPhotosListStatus = status
    }

    private fun onGetPhotosSubscribe(progressType: ProgressTypes) {
        isLoading = true
        showProgress(true, progressType)
        shouldShowError(false)
    }

    private fun onGetPhotosSuccess(progressType: ProgressTypes) {
        showProgress(false, progressType)
        isLoading = false
    }

    private fun onGetPhotosError(throwable: Throwable, progressType: ProgressTypes) {
        isLoading = false
        showProgress(false, progressType)
        Utils.printStackTrace(throwable)
    }

    private fun onPhotosErrorReturn(throwable: Throwable, progressType: ProgressTypes) {
        Utils.printStackTrace(throwable)
        showProgress(false, progressType)
        setPhotosListStatus(Status.Error(errorModel = ErrorModel.Error()))
        updatePhotosList(mPhotosListStatus!!)
    }

    fun refresh(): Completable {
        shouldLoadMore = true
        isLoading = false
        mPagingModel.resetPageIndex()
        return getPhotosList(ProgressTypes.PULL_TO_REFRESH_PROGRESS, true)
    }

    fun onPhotosItemClick(
        context: Context,
        item: PhotosListItem
    ): Single<Status<PhotoDetailsData>> {
        return if (item.photoItem?.downloadUrl.isNullOrEmpty()) {
            Single.just(Status.Error(errorModel = ErrorModel.Error()))
        } else {
            Single.just(
                Status.Success(
                    PhotoDetailsData(
                        item.photoItem?.id!!, item.photoItem.downloadUrl, getDominantColor(
                            context, item.bitmap
                        )
                    )
                )
            )
        }
    }

    private fun getDominantColor(context: Context, bitmap: Bitmap?): Int {
        val swatchesTemp = Palette.from(bitmap!!).generate().swatches
        val swatches: List<Swatch> = ArrayList(swatchesTemp)
        Collections.sort(
            swatches
        ) { swatch1, swatch2 -> swatch2.population - swatch1.population }
        return if (swatches.isNotEmpty()) swatches[0].rgb else ContextCompat.getColor(
            context,
            R.color.defaultColor
        )
    }
}
