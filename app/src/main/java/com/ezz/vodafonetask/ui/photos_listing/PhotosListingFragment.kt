package com.ezz.vodafonetask.ui.photos_listing

import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezz.vodafonetask.R
import com.ezz.vodafonetask.data.models.LoadingModel
import com.ezz.vodafonetask.data.models.PhotosListItem
import com.ezz.vodafonetask.data.models.Status
import com.ezz.vodafonetask.data.models.StatusCode
import com.ezz.vodafonetask.data.models.navigation_component_dto.PhotoDetailsData
import com.ezz.vodafonetask.ui.base.BaseFragment
import com.ezz.vodafonetask.ui.base.IErrorViews
import com.ezz.vodafonetask.ui.base.IToolbar
import com.ezz.vodafonetask.databinding.FragmentPhotosListingBinding
import com.ezz.vodafonetask.ui.base.ListItemClickListener
import com.ezz.vodafonetask.ui.photos_listing.adapter.PhotosListingAdapter
import com.ezz.vodafonetask.utils.Constants
import com.ezz.vodafonetask.utils.PagingScrollListener
import com.ezz.vodafonetask.utils.view_state.SaveStateLifecycleObserver
import com.ezz.vodafonetask.utils.view_state.ViewStateHelper
import io.reactivex.disposables.Disposable

class PhotosListingFragment : BaseFragment<FragmentPhotosListingBinding>(),
    IToolbar,
    PagingScrollListener.PagingScrollListenerInteractions,
    ListItemClickListener<PhotosListItem>,
    IErrorViews {

    private lateinit var mPhotosListingViewModel: PhotosListingViewModel
    private lateinit var mPhotosListingAdapter: PhotosListingAdapter
    private lateinit var mPhotosListingLayoutManager: LinearLayoutManager

    companion object {
        const val TAG = "PhotosListingFragment"

        internal fun newInstance(): PhotosListingFragment {
            return PhotosListingFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }

    override var mToolbar: Toolbar?
        get() = binding.toolbarView.toolbar
        set(_) {}

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPhotosListingBinding
        get() = FragmentPhotosListingBinding::inflate


    override fun onActivityReady(savedInstanceState: Bundle?) {
        initViewStateLifecycle()
    }

    override fun initViews() {
        setToolbar(mContext, getString(R.string.app_name), false)
        initPhotosListingRecyclerView()
    }

    override fun setListeners() {
        binding.rvPhotosListing.addOnScrollListener(
            PagingScrollListener(mPhotosListingLayoutManager, this)
        )
        binding.swipeRefresh.setOnRefreshListener {
            onRefresh()
        }

    }

    override fun initViewModels(arguments: Bundle?) {
        initPhotosListingViewModel(arguments)
    }

    override fun bindViewModels() {
        bindLoadingObservable()
        bindToastObservable()
        bindErrorObservable()
        bindClearDataObservable()
        bindPhotosListObservable()
    }

    private fun bindClearDataObservable() {
        addDisposable(
            mPhotosListingViewModel.mClearDataObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onClearDataRetrieved(it) }, { onError(it) })
        )
    }

    private fun onClearDataRetrieved(clear: Boolean) {
        if (clear) {
            mPhotosListingAdapter.clearItems()
        }
    }

    private fun bindPhotosListObservable() {
        addDisposable(
            mPhotosListingViewModel.getPhotosList().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetPhotosList, this::onError)
        )
    }

    private fun onGetPhotosList(status: Status<ArrayList<PhotosListItem>>) {
        when (status.statusCode) {
            StatusCode.SUCCESS, StatusCode.OFFLINE_DATA -> {
                onGetPhotosListSuccess(status)
            }
            StatusCode.NO_DATA, StatusCode.ERROR, StatusCode.NO_NETWORK -> {
                onError(mContext,
                    binding.errorLayout.root,
                    status.errorModel!!,
                    binding.rvPhotosListing,
                    onRetryClick = { onRetryErrorBtnClick() })
            }
            else -> {
            }
        }
    }

    private fun onRetryErrorBtnClick() {
        addDisposable(
            mPhotosListingViewModel.onRetryErrorBtnClick()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    private fun onGetPhotosListSuccess(status: Status<java.util.ArrayList<PhotosListItem>>) {
        mPhotosListingAdapter.addItems(status.data!!)
        mPhotosListingViewModel.restoreViewState(this, binding.rvPhotosListing)
        onSuccess(binding.rvPhotosListing, binding.errorLayout.layoutError)
    }

    override fun showError(shouldShow: Boolean) {
        shouldShowErrorLayout(binding.errorLayout.layoutError, shouldShow)
    }


    private fun saveState() {
        mPhotosListingViewModel.saveStates(binding.rvPhotosListing)
    }

    private fun setViewsTags() {
        ViewStateHelper.setViewTag(
            binding.rvPhotosListing,
            Constants.ViewsTags.RECYCLER_VIEW_PHOTOS
        )
    }

    private fun initViewStateLifecycle() {
        val viewStateLifecycleObserver =
            SaveStateLifecycleObserver(this::saveState, this::setViewsTags)
        viewLifecycleOwner.lifecycle.addObserver(viewStateLifecycleObserver)
    }

    private fun initPhotosListingViewModel(arguments: Bundle?) {
        mPhotosListingViewModel =
            ViewModelProvider(this, PhotosListingViewModelFactory(mContext, this))
                .get(PhotosListingViewModel::class.java)
    }

    private fun bindLoadingObservable() {
        addDisposable(
            mPhotosListingViewModel.loadingObservable
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoadingObserverRetrieved, this::onError)
        )
    }

    private fun onLoadingObserverRetrieved(loadingModel: LoadingModel) {
        loadingModel.loadingProgressView = binding.viewProgress.progressbar
        loadingModel.pagingProgressView = binding.progressViewPaging.progressbarPaging
        loadingModel.pullToRefreshProgressView = binding.swipeRefresh
        showProgress(loadingModel)
    }

    private fun bindToastObservable() {
        addDisposable(
            mPhotosListingViewModel.showToastObservable
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showMessage, this::onError)
        )
    }

    private fun initPhotosListingRecyclerView() {
        mPhotosListingAdapter = PhotosListingAdapter(mContext, this)
        mPhotosListingLayoutManager = LinearLayoutManager(mContext)
        binding.rvPhotosListing.layoutManager = mPhotosListingLayoutManager
        binding.rvPhotosListing.adapter = mPhotosListingAdapter
    }

    override fun onItemClick(item: PhotosListItem, position: Int) {
        addDisposable(onPhotosItemClick(item, position))
    }

    private fun onPhotosItemClick(item: PhotosListItem, position: Int): Disposable {
        return mPhotosListingViewModel.onPhotosItemClick(mContext, item)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onPhotosItemClickRetrieved, this::onError)
    }

    private fun onPhotosItemClickRetrieved(status: Status<PhotoDetailsData>) {
        if (status.isSuccess())
            navigateTo(PhotosListingFragmentDirections.actionPhotosListingToPhotoDetails(status.data!!))
        else
            showMessage(status.errorModel?.errorTitle!!)
    }

    private fun bindErrorObservable() {
        addDisposable(
            mPhotosListingViewModel.errorViewObservable
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showError, this::onError)
        )
    }

    override fun onScroll() {
        addDisposable(
            mPhotosListingViewModel.onPhotosListScroll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    private fun onRefresh() {
        addDisposable(
            mPhotosListingViewModel.refresh()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

}