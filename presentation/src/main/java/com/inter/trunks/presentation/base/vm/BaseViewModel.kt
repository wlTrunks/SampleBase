package com.inter.trunks.presentation.base.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inter.trunks.domain.common.exception.BaseError
import com.inter.trunks.domain.common.result.BaseSuccess
import com.inter.trunks.domain.common.util.toLogcat
import com.inter.trunks.presentation.base.component.DispatchersProviderInterface
import com.inter.trunks.presentation.base.component.DataLoadedState
import com.inter.trunks.presentation.base.component.DefaultProvider
import com.inter.trunks.presentation.base.component.LoadingState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(
    protected val dispatchersProvider: DispatchersProviderInterface = DefaultProvider()
) : ViewModel(), CoroutineScope {

    val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    val messageData: MutableLiveData<String> = MutableLiveData()
    val dataLoadedState: MutableLiveData<DataLoadedState> = MutableLiveData()

    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = dispatchersProvider.provideDefault() + job

    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancelChildren()
        coroutineContext.cancel()
    }


    protected fun handleFailure(error: BaseError) {
        "Error $error".toLogcat("handleFailure")
        runOnUI {
            loadingState.postValue(LoadingState.DONE)
            dataLoadedState.postValue(DataLoadedState.ERROR)
            if (error is BaseError.FeatureError) {
                messageData.postValue(error.msg)
            }
        }
    }

    protected fun handleSuccess(msg: BaseSuccess) {
        "Success $msg".toLogcat("handleSuccess")
        runOnUI {
            msg.message?.let {
                messageData.postValue(msg.message)
            }
            dataLoadedState.postValue(DataLoadedState.SUCCESS)
        }
    }

    protected fun <T> runOnBackground(bg: suspend () -> T) {
        loadingState.postValue(LoadingState.ON_START)
        coroutineContext + GlobalScope.async(dispatchersProvider.provideDefault()) {
            bg()
        }
    }

    protected fun runOnUI(front: () -> Unit) {
        coroutineContext + GlobalScope.launch(dispatchersProvider.provideUI()) {
            front()
            dataLoadedState.postValue(DataLoadedState.SUCCESS)
            loadingState.postValue(LoadingState.DONE)
        }
    }
}