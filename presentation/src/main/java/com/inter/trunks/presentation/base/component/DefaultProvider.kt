package com.inter.trunks.presentation.base.component

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultProvider : DispatchersProviderInterface {
    override fun provideDefault(): CoroutineDispatcher = Dispatchers.Default
    override fun provideUI(): CoroutineDispatcher = Dispatchers.Main
    override fun provideIO(): CoroutineDispatcher = Dispatchers.IO
}