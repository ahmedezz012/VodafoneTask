package com.ezz.vodafonetask.ui.photo_details

import androidx.lifecycle.SavedStateHandle
import com.ezz.vodafonetask.ui.base.BaseViewModel
import com.ezz.vodafonetask.domain.use_cases.photo_details.IPhotoDetailsUseCase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import com.ezz.vodafonetask.data.models.navigation_component_dto.PhotoDetailsData
import io.reactivex.Single

class PhotoDetailsViewModel(
    val mPhotoDetailsUseCase: IPhotoDetailsUseCase,
    private val photoDetailsData: PhotoDetailsData,
    handle: SavedStateHandle
) : BaseViewModel(handle, mPhotoDetailsUseCase) {

    fun bindPhotoDetailsData(): Single<PhotoDetailsData> {
        return Single.just(photoDetailsData)
    }

}
