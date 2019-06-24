package com.inter.trunks.presentation.base.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inter.trunks.domain.common.exception.BaseError
import com.inter.trunks.domain.common.exception.Failure
import com.inter.trunks.domain.common.result.BaseSuccess
import com.inter.trunks.domain.common.util.toLogcat
import com.inter.trunks.presentation.BuildConfig
import com.inter.trunks.presentation.base.component.DataLoadedState
import com.inter.trunks.presentation.base.component.LoadingState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(
    protected val UI: CoroutineDispatcher = Dispatchers.Main,
    protected val DEFAULT: CoroutineDispatcher = Dispatchers.Default
) : ViewModel(), CoroutineScope {

    val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    val messageData: MutableLiveData<String> = MutableLiveData()
    val dataLoadedState: MutableLiveData<DataLoadedState> = MutableLiveData()

    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = DEFAULT + job

    override fun onCleared() {
        super.onCleared()
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

    protected fun <T> runOnBackgound(bg: suspend () -> T) {
        loadingState.postValue(LoadingState.ON_START)
        coroutineContext + GlobalScope.async(DEFAULT) {
            bg()
        }
    }

    protected fun runOnUI(front: () -> Unit) {
        coroutineContext + GlobalScope.launch(UI) {
            front()
            dataLoadedState.postValue(DataLoadedState.SUCCESS)
            loadingState.postValue(LoadingState.DONE)
        }
    }
}