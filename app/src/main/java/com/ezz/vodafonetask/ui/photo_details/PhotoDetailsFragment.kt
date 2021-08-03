package com.ezz.vodafonetask.ui.photo_details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.ezz.vodafonetask.R
import com.ezz.vodafonetask.data.models.navigation_component_dto.PhotoDetailsData
import com.ezz.vodafonetask.databinding.FragmentPhotoDetailsBinding
import com.ezz.vodafonetask.ui.base.BaseFragment
import com.ezz.vodafonetask.ui.base.IToolbar
import com.ezz.vodafonetask.utils.loadImageUrl
import com.ezz.vodafonetask.utils.view_state.SaveStateLifecycleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PhotoDetailsFragment : BaseFragment<FragmentPhotoDetailsBinding>(), IToolbar {

    private lateinit var mPhotoDetailsViewModel: PhotoDetailsViewModel

    override var mToolbar: Toolbar?
        get() = binding.toolbarView.toolbar
        set(_) {}

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPhotoDetailsBinding
        get() = FragmentPhotoDetailsBinding::inflate


    override fun onActivityReady(savedInstanceState: Bundle?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initViews() {
        setToolbar(mContext, getString(R.string.photo_details), true)
    }

    override fun setListeners() {

    }

    override fun initViewModels(arguments: Bundle?) {
        val args: PhotoDetailsFragmentArgs by navArgs()
        initPhotoDetailsViewModel(arguments, args)
    }

    override fun bindViewModels() {
        addDisposable(bindPhotoDetailsData())
    }

    private fun bindPhotoDetailsData(): Disposable {
        return mPhotoDetailsViewModel.bindPhotoDetailsData().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onGetPhotoDetails(it) }, { onError(it) })
    }

    private fun onGetPhotoDetails(photoDetailsData: PhotoDetailsData) {
        bindImage(photoDetailsData.downloadUrl)
        bindPhotoBackGround(photoDetailsData.dominantColor)
    }

    private fun bindImage(downloadUrl: String?) {
        binding.phvPhoto.loadImageUrl(downloadUrl)
    }

    private fun bindPhotoBackGround(dominantColor: Int) {
        binding.root.setBackgroundColor(dominantColor)
    }

    private fun initPhotoDetailsViewModel(arguments: Bundle?, args: PhotoDetailsFragmentArgs) {
        mPhotoDetailsViewModel =
            ViewModelProvider(
                this,
                PhotoDetailsViewModelFactory(mContext, args.photoDetailsData, this)
            )
                .get(PhotoDetailsViewModel::class.java)
    }

}