package br.eti.rafaelcouto.temporada.model

import com.google.gson.annotations.SerializedName

class TvShowDetails() : TvShow() {
    @SerializedName("in_production") var inProduction: Boolean = false
    @SerializedName("last_air_date") var lastAirDate: String = ""
    var overview: String = ""
    var genres: List<Genre> = emptyList()
    @Transient var similarShows: List<TvShow> = emptyList()

    constructor(previousDetails: TvShowDetails, additionalSimilarShows: List<TvShow>) : this() {
        name = previousDetails.name
        voteAverage = previousDetails.voteAverage
        firstAirDate = previousDetails.firstAirDate
        posterPath = previousDetails.posterPath
        inProduction = previousDetails.inProduction
        lastAirDate = previousDetails.lastAirDate
        overview = previousDetails.overview
        genres = previousDetails.genres
        similarShows = previousDetails.similarShows + additionalSimilarShows
    }
}
