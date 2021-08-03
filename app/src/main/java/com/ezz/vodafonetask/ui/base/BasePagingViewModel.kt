package com.ezz.vodafonetask.ui.base

import androidx.lifecycle.SavedStateHandle
import com.ezz.vodafonetask.data.models.ProgressTypes

abstract class BasePagingViewModel(
    handle: SavedStateHandle,
    vararg mUseCases: IBaseUseCase
) : BaseViewModel(handle, *mUseCases) {

    protected var shouldLoadMore: Boolean = true
    protected var isLoading: Boolean = false

    public override fun onCleared() {
        isLoading = false
        showProgress(false, ProgressTypes.PAGING_PROGRESS)
        super.onCleared()
    }
}
