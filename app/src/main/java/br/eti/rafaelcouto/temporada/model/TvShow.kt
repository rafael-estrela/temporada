package br.eti.rafaelcouto.temporada.model

import br.eti.rafaelcouto.temporada.toDate
import br.eti.rafaelcouto.temporada.toDateString
import com.google.gson.annotations.SerializedName

open class TvShow {
    var id: Long = 0
    var name: String = ""
    @SerializedName("vote_average") var voteAverage: Double = 0.0
    @SerializedName("first_air_date") var firstAirDate: String = ""
    @SerializedName("poster_path") var posterPath: String? = null

    val firstAirYear: String
        get() = firstAirDate.toDate().toDateString("yyyy")

    val progressAverage: Float
        get() = (voteAverage * 10).toFloat()

    val textAverage: String
        get() = progressAverage.toInt().toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TvShow

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
