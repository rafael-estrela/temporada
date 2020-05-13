package br.eti.rafaelcouto.temporada.view.list

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import br.eti.rafaelcouto.temporada.R
import br.eti.rafaelcouto.temporada.model.TvShow
import br.eti.rafaelcouto.temporada.view.general.BaseAdapter

class TvShowsAdapter(
    context: Context,
    items: LiveData<List<TvShow>>
) : BaseAdapter<TvShow, TvShowsViewHolder>(context, items, R.layout.item_tv_show) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowsViewHolder {
        return TvShowsViewHolder(
            DataBindingUtil.inflate(inflater, viewType, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TvShowsViewHolder, position: Int) {
        holder.apply {
            binding.item = getItemAtPosition(position)
            binding.iTvShowCpbVotes.progress = binding.item?.progressAverage ?: 0f
            setOnItemClickListener(onItemClick)
            displayImage()
        }
    }
}
