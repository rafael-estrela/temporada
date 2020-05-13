package br.eti.rafaelcouto.temporada.network.service

import br.eti.rafaelcouto.temporada.model.PageWrapper
import br.eti.rafaelcouto.temporada.model.TvShow
import br.eti.rafaelcouto.temporada.network.config.INetworkAPI
import io.reactivex.Single

class ShowListService(
    private val api: INetworkAPI
) {
    fun loadTvShows(page: Int, language: String): Single<PageWrapper<TvShow>> {
        return api.getShowList(page, language)
    }
}
