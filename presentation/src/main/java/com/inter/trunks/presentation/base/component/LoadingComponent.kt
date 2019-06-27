package com.inter.trunks.presentation.base.component

import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


open class LoaderComponent : LoadingStateListener {
    override val responseLD: MutableLiveData<ResponseMessage> = MutableLiveData()

    private val responseMessage: ResponseMessage = ResponseMessage()

    override fun setResponseCodeMessage(code: Int, message: String) {
        responseMessage.errorCode = code
        responseMessage.message = message
    }

    override val loadingStateLD = MutableLiveData<LoadingState>()

    protected var _loadingState = LoadingState.DONE
        set(value) {
            field = value
            loadingStateLD.value = value
        }

    override fun getLoadingState(): LoadingState = _loadingState

    fun subscribeToLoadingState(@NonNull owner: LifecycleOwner, componentHolder: UIComponentHolder) {
        loadingStateLD.observe(owner, Observer {
        })
    }

    override fun loadingDone(isSuccess: Boolean) {
        _loadingState = LoadingState.DONE
    }

    override fun startLoading() {
        _loadingState = LoadingState.ON_START
    }
}

class PaginationLoaderComponent : LoaderComponent() {

    private val page = Page()

    private var pagingState = PagingState.REFRESH

    fun proceedPagingState(
        isSuccess: Boolean,
        onReload: (() -> Unit)? = null,
        onPaging: (() -> Unit)? = null
    ) {
        if (isSuccess) {
            when (pagingState) {
                PagingState.REFRESH -> onReload?.invoke()
                PagingState.PAGING -> onPaging?.invoke()
            }
        }
        loadingDone(isSuccess)
    }

    override fun loadingDone(isSuccess: Boolean) {
        if (isSuccess)
            page.increment()
        super.loadingDone(isSuccess)
    }

    fun getPageNum(): Int = page.num

    fun refreshData() {
        page.reset()
        pagingState = PagingState.REFRESH
    }

    fun isPaginationEnable() = _loadingState == LoadingState.DONE
}

class Page {
    var size: Int = DEFAULT_SIZE
        set(size) {
            field = size
            if (field < 0) {
                field = DEFAULT_SIZE
            }
        }

    var num: Int = DEFAULT_NUM
        set(value) {
            field = value
            if (field < 0) {
                field = DEFAULT_NUM
            }
        }

    fun reset() {
        num = DEFAULT_NUM
    }

    fun increment() {
        ++num
    }

    fun decrement() {
        if (num <= 0)
            num = 0
        else
            num--
    }

    companion object {

        const val DEFAULT_SIZE = 10
        const val DEFAULT_NUM = 1

    }
}


interface LoadingStateListener {

    fun setResponseCodeMessage(code: Int, message: String)

    fun getLoadingState(): LoadingState

    fun loadingDone(isSuccess: Boolean)

    fun startLoading()

    val loadingStateLD: MutableLiveData<LoadingState>

    val responseLD: MutableLiveData<ResponseMessage>

}

enum class PagingState {
    REFRESH,
    PAGING;
}

enum class LoadingState {
    ON_START,
    DONE;
}

enum class DataLoadedState {
    SUCCESS,
    ERROR;
}