package com.ezz.vodafonetask.ui.photos_listing.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.ezz.vodafonetask.ui.base.BaseAdapter
import com.ezz.vodafonetask.ui.base.ListItemClickListener
import com.ezz.vodafonetask.databinding.ItemPhotoBinding
import com.ezz.vodafonetask.data.models.ListItemType
import com.ezz.vodafonetask.data.models.PhotosListItem
import com.ezz.vodafonetask.databinding.ItemAdBinding
import com.ezz.vodafonetask.ui.base.BaseViewHolder

class PhotosListingAdapter(
    private val mContext: Context,
    private val mPhotosListingItemClickListener: ListItemClickListener<PhotosListItem>? = null
) : BaseAdapter<PhotosListItem, BaseViewHolder<PhotosListItem>>(itemClickListener = mPhotosListingItemClickListener) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<PhotosListItem> {
        return when (viewType) {
            ListItemType.PHOTO_ITEM.ordinal -> {
                binding = ItemPhotoBinding.inflate(LayoutInflater.from(mContext), viewGroup, false)
                 PhotosListingViewHolder(
                    binding as @NonNull ItemPhotoBinding,
                    mContext,
                    mPhotosListingItemClickListener
                )
            }
            else -> {
                binding = ItemAdBinding.inflate(LayoutInflater.from(mContext),viewGroup,false)
                AdsViewHolder(binding as @NonNull ItemAdBinding,mContext)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].listItemType.ordinal
    }

    override fun clearViewBinding() {
        binding = null
    }
}
