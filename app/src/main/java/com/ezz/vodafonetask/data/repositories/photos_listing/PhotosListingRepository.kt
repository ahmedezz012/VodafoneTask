package com.ezz.vodafonetask.data.repositories.photos_listing

import javax.inject.Inject
import io.reactivex.Single
import com.ezz.vodafonetask.ui.base.BaseRepository
import com.ezz.vodafonetask.data.remote.IRemoteDataSource
import com.ezz.vodafonetask.data.local.ILocalDataSource
import com.ezz.vodafonetask.data.models.dto.PhotoItem
import com.ezz.vodafonetask.data.models.Status
import com.ezz.vodafonetask.data.models.dto.APIResponse
import com.ezz.vodafonetask.retrofit.APICallable
import com.ezz.vodafonetask.utils.ConnectionUtils
import java.util.concurrent.Callable

class PhotosListingRepository @Inject constructor(
    private val connectionUtils: ConnectionUtils,
    private val mIRemoteDataSource: IRemoteDataSource,
    private val mILocalDataSource: ILocalDataSource
) : BaseRepository(connectionUtils, mIRemoteDataSource),
    IPhotosListingRepository {

    override fun getPhotosList(
        pageIndex: Int,
        pageSize: Int,
        tag: String
    ): Single<Status<APIResponse<ArrayList<PhotoItem>?>>> {
        return callAPI(tag, object : APICallable<APIResponse<ArrayList<PhotoItem>?>> {
            override fun call(timestampedTag: String): APIResponse<ArrayList<PhotoItem>?>? {
                return mIRemoteDataSource.getPhotosList(
                    pageIndex,
                    pageSize,
                    timestampedTag
                )
            }
        })
    }

    override fun getOfflinePhotosList(): Single<ArrayList<PhotoItem>> {
        return createSingle { mILocalDataSource.getAllPhotos() }
    }

    override fun deleteAllPhotos(): Single<Int> {
        return createSingle { mILocalDataSource.deleteAllPhotos() }
    }

    override fun insertPhotos(photosList: ArrayList<PhotoItem>): Single<List<Long>> {
        return createSingle { mILocalDataSource.insertPhotos(photosList) }
    }

}