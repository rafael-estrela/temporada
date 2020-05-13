package br.eti.rafaelcouto.temporada.view.details

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import br.eti.rafaelcouto.temporada.R
import br.eti.rafaelcouto.temporada.model.TvShow
import br.eti.rafaelcouto.temporada.view.general.BaseAdapter

class SimilarTvShowsAdapter(
    context: Context,
    items: LiveData<List<TvShow>>
) : BaseAdapter<TvShow, SimilarTvShowsViewHolder>(context, items, R.layout.item_similar_tv_show) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarTvShowsViewHolder {
        return SimilarTvShowsViewHolder(
            DataBindingUtil.inflate(inflater, viewType, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SimilarTvShowsViewHolder, position: Int) {
        holder.apply {
            binding.item = getItemAtPosition(position)
            setOnItemClickListener(onItemClick)
            displayImage()
        }
    }
}
