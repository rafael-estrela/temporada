package br.eti.rafaelcouto.temporada.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import br.eti.rafaelcouto.temporada.R
import br.eti.rafaelcouto.temporada.model.TvShow
import br.eti.rafaelcouto.temporada.model.TvShowDetails
import br.eti.rafaelcouto.temporada.network.service.ShowDetailsService
import br.eti.rafaelcouto.temporada.router.ShowListRouter
import br.eti.rafaelcouto.temporada.toDate
import br.eti.rafaelcouto.temporada.toDateString
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ShowDetailsViewModel(
    router: ShowListRouter,
    private val service: ShowDetailsService
) : BaseViewModel(router) {
    // static
    companion object {
        const val SHOW_TITLE = "details_show_title"
        const val SHOW_ID = "details_show_id"
    }

    // show id
    private var id: Long = 0

    // livedata
    private val mShowDetails = MutableLiveData<TvShowDetails>()
    val showDetails: LiveData<TvShowDetails>
        get() = mShowDetails

    // transformations
    val showName = Transformations.map(showDetails) { it.name }
    val showOverview = Transformations.map(showDetails) { it.overview }
    val showFirstYear = Transformations.map(showDetails) { it.firstAirDate.toDate().toDateString("yyyy") }
    val showProgress = Transformations.map(showDetails) { it.progressAverage }
    val showAverage = Transformations.map(showProgress) { it.toInt().toString() }
    val similarShows = Transformations.map(showDetails) { it.similarShows }
    val showGenres = Transformations.map(showDetails) { details -> details.genres.joinToString(", ") { it.name } }

    val showLastYear = Transformations.map(showDetails) {
        if (it.inProduction) "Atualmente" else it.lastAirDate.toDate().toDateString("yyyy")
    }

    // api requests
    fun loadDetails(id: Long? = null) {
        val mId = id ?: this.id
        this.id = mId

        Single.zip(
            service.loadShowDetails(mId, defaultLanguage)
                .subscribeOn(Schedulers.newThread()),
            service.loadSimilarShows(mId, page, defaultLanguage)
                .map {
                    maxPage = it.totalPages

                    it.results
                }.subscribeOn(Schedulers.newThread()),
            BiFunction { details: TvShowDetails, similar: List<TvShow> ->
                details.apply { similarShows = similar }
            }
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mIsLoading.value = true
            }.doFinally {
                mIsLoading.value = false
            }.subscribeBy(onError = {
                mHasError.value = R.string.show_details_error
            }, onSuccess = {
                page++

                mShowDetails.value = it
            }
        ).addTo(disposeBag)
    }

    fun loadSimilarShows() {
        service.loadSimilarShows(id, page, defaultLanguage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { mIsLoading.value = true }
            .doFinally { mIsLoading.value = false }
            .map {
                maxPage = it.totalPages

                it.results
            }.subscribeBy(onError = {
                mHasError.value = R.string.show_details_error
            }, onSuccess = {
                page++

                mShowDetails.value?.let { details ->
                    mShowDetails.value = TvShowDetails(details, it)
                }
            }
        ).addTo(disposeBag)
    }

    // base
    override fun onShowSelected(position: Int) {
        similarShows.value?.let {
            val selected = it[position]

            router.proceedToShowDetails(selected.name, selected.id)
        }
    }
}
