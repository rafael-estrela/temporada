package br.eti.rafaelcouto.temporada.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.eti.rafaelcouto.temporada.router.ShowListRouter
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(
    protected val router: ShowListRouter
) : ViewModel() {
    // livedata
    protected val mIsLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = mIsLoading

    protected val mHasError = MutableLiveData<Int>()
    val hasError: LiveData<Int>
        get() = mHasError

    // pagination properties
    protected var page = 1
    protected var maxPage = 0

    // custom language
    val defaultLanguage: String = "pt-BR"

    // rx
    protected val disposeBag = CompositeDisposable()

    // lifecycle
    override fun onCleared() {
        disposeBag.dispose()

        super.onCleared()
    }

    // pagination verification
    fun shouldPaginate(
        visibleItems: Int,
        totalItems: Int,
        firstVisibleItemPosition: Int,
        offset: Int
    ): Boolean {
        return takeIf {
            offset > 0
        }?.takeIf {
            isLoading.value?.let { !it } ?: true
        }?.takeIf {
            visibleItems + firstVisibleItemPosition >= totalItems
        }?.takeIf {
            firstVisibleItemPosition >= 0
        }?.takeIf {
            page < maxPage
        } != null
    }

    // on item selected
    abstract fun onShowSelected(position: Int)
}
