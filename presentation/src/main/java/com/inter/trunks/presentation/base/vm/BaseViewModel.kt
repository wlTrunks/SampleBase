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
    val errorData: MutableLiveData<BaseError> = MutableLiveData()
    val dataLoadedState: MutableLiveData<DataLoadedState> = MutableLiveData()

    private val _superJob: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatchersProvider.provideDefault() + _superJob

    override fun onCleared() {
        super.onCleared()
        cancel()
    }

    protected fun handleFailure(error: BaseError) {
        "Error $error".toLogcat("handleFailure")
        runOnUI {
            loadingState.postValue(LoadingState.DONE)
            dataLoadedState.postValue(DataLoadedState.ERROR)
            if (error is BaseError.FeatureError) {
                messageData.postValue(error.msg)
                errorData.postValue(error)
            }
        }
    }

    protected fun handleSuccess(msg: BaseSuccess) {
        "Success $msg".toLogcat("handleSuccess")
        runOnUI {
            if (!msg.message.isNullOrEmpty()) {
                messageData.postValue(msg.message)
            }
            dataLoadedState.postValue(DataLoadedState.SUCCESS)
        }
    }

    protected fun runOnBackground(bg: suspend CoroutineScope.() -> Unit): Deferred<Unit> {
        loadingState.postValue(LoadingState.ON_START)
        val job = this.async(dispatchersProvider.provideIO()) {
            bg()
        }
        coroutineContext + job
        return job
    }

    protected fun runOnUI(front: suspend CoroutineScope.() -> Unit) {
        coroutineContext + this.launch(dispatchersProvider.provideUI()) {
            front()
            dataLoadedState.postValue(DataLoadedState.SUCCESS)
            loadingState.postValue(LoadingState.DONE)
        }
    }
}