package com.ezz.vodafonetask.ui.base

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ezz.vodafonetask.data.models.LoadingModel
import com.ezz.vodafonetask.data.models.ProgressTypes
import com.ezz.vodafonetask.data.models.StringModel
import com.ezz.vodafonetask.data.models.view_state.BaseViewStateModel
import com.ezz.vodafonetask.utils.view_state.ViewStateHelper
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel(
    private var mSavedStateHandle: SavedStateHandle,
    private vararg var mUseCases: IBaseUseCase
) : ViewModel() {

    val loadingObservable: BehaviorSubject<LoadingModel> = BehaviorSubject.create()
    val errorViewObservable: PublishSubject<Boolean> = PublishSubject.create()
    val showToastObservable: PublishSubject<StringModel> = PublishSubject.create()
    val mClearDataObservable: PublishSubject<Boolean> = PublishSubject.create()

    protected fun clearData(clear: Boolean) {
        mClearDataObservable.onNext(clear)
    }

    protected fun showProgress(
        shouldShow: Boolean,
        progressType: ProgressTypes = ProgressTypes.MAIN_PROGRESS
    ) {
        loadingObservable.onNext(LoadingModel(shouldShow, progressType))
    }

    protected fun shouldShowError(shouldShow: Boolean) {
        errorViewObservable.onNext(shouldShow)
    }

    protected fun showToastMessage(message: Any?, vararg args: Any?) {
        if (message is StringModel)
            showToastObservable.onNext(message)
        else
            showToastObservable.onNext(StringModel(message, *args))
    }

    private fun <T> getStateLiveData(key: String, initialValue: T? = null): MutableLiveData<T?>? {
        if (!mSavedStateHandle.contains(key)) return null

        val liveData = mSavedStateHandle.getLiveData(key, initialValue)
        mSavedStateHandle.remove<T>(key)
        return liveData
    }

    /**
     * saves the views states using the viewModel SavedStateHandle.
     */
    fun saveStates(vararg views: View) {
        ViewStateHelper.saveViewState(mSavedStateHandle, *views)
    }

    /**
     * @param owner The View owning this viewModel.
     * @param views The views we want to restore it's state.
     * @param onNoSavedState Will be invoked in case there is no saved state associated to this view,
     * set it in case there is an action to be taken with this view if there is no saved state,
     * leave it null otherwise.
     */
    fun restoreViewState(
        owner: LifecycleOwner,
        vararg views: View,
        onNoSavedState: ((tag: String) -> Unit)? = null
    ) {
        for (view in views) {
            val stateLiveData = getStateLiveData<Any>(view.tag as String)
            if (stateLiveData != null)
                stateLiveData.observe(owner,
                    androidx.lifecycle.Observer {
                        restoreViewState(view, it as BaseViewStateModel)
                    })
            else
                onNoSavedState?.invoke(view.tag as String)
        }
    }

    private fun restoreViewState(view: View, value: BaseViewStateModel) {
        ViewStateHelper.restoreView(view, value)
    }

    override fun onCleared() {
        mUseCases.forEach {
            it.onCleared()
        }
        super.onCleared()
    }
}
