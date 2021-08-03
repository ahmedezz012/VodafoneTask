package com.ezz.vodafonetask.domain.use_cases.photo_details

import com.ezz.vodafonetask.ui.base.BaseUseCase
import com.ezz.vodafonetask.data.repositories.photo_details.PhotoDetailsRepository
import io.reactivex.Single
import javax.inject.Inject


class PhotoDetailsUseCase @Inject constructor(
    private val mPhotoDetailsRepository: PhotoDetailsRepository
) : BaseUseCase(mPhotoDetailsRepository), IPhotoDetailsUseCase {

}