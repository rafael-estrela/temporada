package br.eti.rafaelcouto.temporada.network.service

import br.eti.rafaelcouto.temporada.model.PageWrapper
import br.eti.rafaelcouto.temporada.model.TvShow
import br.eti.rafaelcouto.temporada.model.TvShowDetails
import br.eti.rafaelcouto.temporada.network.config.INetworkAPI
import io.reactivex.Single

class ShowDetailsService(
    private val api: INetworkAPI
) {
    fun loadShowDetails(id: Long, language: String): Single<TvShowDetails> {
        return api.getShowDetails(id, language)
    }

    fun loadSimilarShows(id: Long, page: Int, language: String): Single<PageWrapper<TvShow>> {
        return api.getSimilarShows(id, page, language)
    }
}
