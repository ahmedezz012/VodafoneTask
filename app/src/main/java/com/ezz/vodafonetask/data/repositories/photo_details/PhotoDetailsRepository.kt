package com.ezz.vodafonetask.data.repositories.photo_details

import com.ezz.vodafonetask.data.local.ILocalDataSource
import com.ezz.vodafonetask.data.remote.IRemoteDataSource
import com.ezz.vodafonetask.ui.base.BaseRepository
import com.ezz.vodafonetask.utils.ConnectionUtils
import javax.inject.Inject

class PhotoDetailsRepository @Inject constructor(
    private val connectionUtils: ConnectionUtils,
    private val mIRemoteDataSource: IRemoteDataSource
) : BaseRepository(connectionUtils, mIRemoteDataSource),
    IPhotoDetailsRepository {


}