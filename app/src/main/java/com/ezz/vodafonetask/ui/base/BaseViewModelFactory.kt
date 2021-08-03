package com.ezz.vodafonetask.ui.base

import android.content.Context
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.savedstate.SavedStateRegistryOwner

abstract class BaseViewModelFactory(
    context: Context,
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {

    init {
        this.injectDependencies(context)
    }

    abstract fun injectDependencies(context: Context)
}
