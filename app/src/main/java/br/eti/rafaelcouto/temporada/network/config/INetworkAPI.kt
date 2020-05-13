package br.eti.rafaelcouto.temporada.network.config

import br.eti.rafaelcouto.temporada.BuildConfig
import br.eti.rafaelcouto.temporada.model.PageWrapper
import br.eti.rafaelcouto.temporada.model.TvShow
import br.eti.rafaelcouto.temporada.model.TvShowDetails
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface INetworkAPI {
    companion object {
        val baseApi: INetworkAPI
            get() = Retrofit.Builder()
                .baseUrl(BuildConfig.API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(AuthenticatorClient.build())
                .build()
                .create(INetworkAPI::class.java)
    }

    @GET("3/tv/popular")
    fun getShowList(
        @Query("page") page: Int,
        @Query("language") language: String
    ): Single<PageWrapper<TvShow>>

    @GET("3/tv/{id}")
    fun getShowDetails(
        @Path("id") id: Long,
        @Query("language") language: String
    ): Single<TvShowDetails>

    @GET("3/tv/{id}/similar")
    fun getSimilarShows(
        @Path("id") id: Long,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Single<PageWrapper<TvShow>>
}
