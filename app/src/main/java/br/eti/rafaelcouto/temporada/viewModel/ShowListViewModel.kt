package br.eti.rafaelcouto.temporada.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.eti.rafaelcouto.temporada.R
import br.eti.rafaelcouto.temporada.model.TvShow
import br.eti.rafaelcouto.temporada.network.service.ShowListService
import br.eti.rafaelcouto.temporada.router.ShowListRouter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ShowListViewModel(
    router: ShowListRouter,
    private val service: ShowListService
) : BaseViewModel(router) {
    // livedata
    private val mTvShows = MutableLiveData<List<TvShow>>()
    val tvShows: LiveData<List<TvShow>>
        get() = mTvShows

    // api requests
    fun loadTvShows() {
        service.loadTvShows(page, defaultLanguage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { mIsLoading.value = true }
            .doFinally { mIsLoading.value = false }
            .map {
                maxPage = it.totalPages

                it.results
            }.subscribeBy(onError = {
                mHasError.value = R.string.show_list_error
            }, onSuccess = { data ->
                page++

                mTvShows.value?.let {
                    mTvShows.value = it + data
                } ?: run {
                    mTvShows.value = data
                }
            }
        ).addTo(disposeBag)
    }

    // base
    override fun onShowSelected(position: Int) {
        tvShows.value?.let {
            val selected = it[position]

            router.proceedToShowDetails(selected.name, selected.id)
        }
    }
}
