package com.inter.trunks.presentation.base.component

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersProviderInterface {
    fun provideDefault(): CoroutineDispatcher
    fun provideUI(): CoroutineDispatcher
    fun provideIO(): CoroutineDispatcher
}