package com.ezz.vodafonetask.data.models

import com.ezz.vodafonetask.utils.Constants


open class PagingModel(
    var pageIndex: Int = Constants.PAGING.DEFAULT_PAGE_INDEX,
    var pageSize: Int = Constants.PAGING.DEFAULT_PAGE_SIZE
) {

    fun resetPageIndex() {
        pageIndex = Constants.PAGING.DEFAULT_PAGE_INDEX
    }

    fun increasePageIndex() {
        pageIndex++
    }
}
