package br.eti.rafaelcouto.temporada.view.list

import br.eti.rafaelcouto.temporada.BuildConfig
import br.eti.rafaelcouto.temporada.R
import br.eti.rafaelcouto.temporada.databinding.ItemTvShowBinding
import br.eti.rafaelcouto.temporada.view.general.BaseViewHolder
import com.squareup.picasso.Picasso

class TvShowsViewHolder(
    val binding: ItemTvShowBinding
) : BaseViewHolder<ItemTvShowBinding>(binding) {
    override fun displayImage() {
        Picasso.with(binding.root.context)
            .load(BuildConfig.POSTER_ENDPOINT + BuildConfig.POSTER_DEFAULT_WIDTH + binding.item?.posterPath)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(binding.iTvShowIvPoster)
    }
}
