package com.ezz.vodafonetask.ui.base

interface ListItemClickListener<in Item> {
    fun onItemClick(item: Item, position: Int)
}
