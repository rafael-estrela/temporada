package br.eti.rafaelcouto.temporada.model

import com.google.gson.annotations.SerializedName

class PageWrapper<T> {
    var page: Int = 0
    var results: List<T> = emptyList()
    @SerializedName("total_pages") var totalPages: Int = 0
}
