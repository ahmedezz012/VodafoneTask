package com.ezz.vodafonetask.utils

import com.ezz.vodafonetask.BuildConfig


object Constants {
    object Network {
        const val CONNECT_TIMEOUT: Long = 5
        const val READ_TIMEOUT: Long = 5
        const val WRITE_TIMEOUT: Long = 5
        const val CONTENT_TYPE = "Content-Type"
        const val APPLICATION_JSON = "application/json"
    }

    object URL {
        const val BASE_NETWORK_URL = BuildConfig.BASE_NETWORK_URL
        const val PHOTOS_LISTING = "list"
    }

    object ApiParams {
        const val PAGE_INDEX = "page"
        const val PAGE_SIZE = "limit"
    }

    object APIsRequestsTags {
        const val PHOTOS_LISTING_TAG = "PHOTOS_LISTING_TAG"
    }

    object ApiStatus {
        const val STATUS_SUCCESS = 0
        const val STATUS_FAIL = 1
        const val NO_STATUS = -1
        const val NO_HTTP_CODE = -1
    }

    object PAGING {
        const val DEFAULT_PAGE_INDEX = 1
        const val DEFAULT_PAGE_SIZE = 10

        const val SECOND_PAGE_INDEX = 2
    }

    object ViewsTags {
        const val RECYCLER_VIEW_PHOTOS = "RECYCLER_VIEW_PHOTOS"
    }

    object Database {
        const val DATABASE_NAME = "VodafonePhotosDatabase"
        const val DB_VERSION = 1

        object TableNames {
            const val PHOTOS_TABLE_NAME = "Photos"
        }
    }
}