package com.ezz.vodafonetask.ui.photos_listing.adapter

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import com.ezz.vodafonetask.data.models.PhotosListItem
import com.ezz.vodafonetask.databinding.ItemPhotoBinding
import com.ezz.vodafonetask.ui.base.BaseViewHolder
import com.ezz.vodafonetask.ui.base.ListItemClickListener
import com.ezz.vodafonetask.utils.alternate
import com.ezz.vodafonetask.utils.loadImageUrl


class PhotosListingViewHolder(
    private val binding: ItemPhotoBinding,
    private val mContext: Context,
    private val mPhotosListingItemClickListener: ListItemClickListener<PhotosListItem>? = null
) : BaseViewHolder<PhotosListItem>(binding, mPhotosListingItemClickListener) {

    override fun bind(item: PhotosListItem) {
        val photoItem = item.photoItem
        itemView.setOnClickListener { onPhotoItemClicked(item) }
        bindImage(photoItem?.downloadUrl)
        bindAuthorName(photoItem?.author)
    }

    private fun bindImage(url: String?) {
        binding.imgPhoto.loadImageUrl(url)
    }

    private fun bindAuthorName(author: String?) {
        binding.txtAuthorName.text = author.alternate("-")
    }

    private fun onPhotoItemClicked(item: PhotosListItem) {
        item.bitmap = (binding.imgPhoto.drawable as BitmapDrawable).bitmap
        mPhotosListingItemClickListener?.onItemClick(item, adapterPosition)
    }
}
