package com.inter.trunks.presentation.base.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inter.trunks.domain.common.exception.BaseError
import com.inter.trunks.domain.common.result.BaseSuccess
import com.inter.trunks.domain.common.util.toLogcat
import com.inter.trunks.presentation.base.component.DataLoadedState
import com.inter.trunks.presentation.base.component.DefaultProvider
import com.inter.trunks.presentation.base.component.DispatchersProviderInterface
import com.inter.trunks.presentation.base.component.LoadingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

open class BaseViewModel(
    protected val dispatchersProvider: DispatchersProviderInterface = DefaultProvider()
) : ViewModel() {

    val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    val messageData: MutableLiveData<String> = MutableLiveData()
    val errorData: MutableLiveData<BaseError> = MutableLiveData()
    val dataLoadedState: MutableLiveData<DataLoadedState> = MutableLiveData()

    protected val superJob: Job = SupervisorJob()

    protected val scope = CoroutineScope(
        superJob + Dispatchers.Default
    )

    override fun onCleared() {
        scope.cancel()
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
        scope.launch(dispatchersProvider.provideUI()) {
            loadingState.postValue(LoadingState.ON_START)
        }

        return scope.async(Dispatchers.IO) {
            bg()
        }
    }

    protected fun runOnUI(front: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch(Dispatchers.Main) {
            loadingState.postValue(LoadingState.DONE)
        }
    }
}