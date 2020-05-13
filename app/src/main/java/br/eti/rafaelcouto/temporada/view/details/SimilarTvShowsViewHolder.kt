package br.eti.rafaelcouto.temporada.view.details

import br.eti.rafaelcouto.temporada.BuildConfig
import br.eti.rafaelcouto.temporada.R
import br.eti.rafaelcouto.temporada.databinding.ItemSimilarTvShowBinding
import br.eti.rafaelcouto.temporada.view.general.BaseViewHolder
import com.squareup.picasso.Picasso

class SimilarTvShowsViewHolder(
    val binding: ItemSimilarTvShowBinding
) : BaseViewHolder<ItemSimilarTvShowBinding>(binding) {
    override fun displayImage() {
        Picasso.with(binding.root.context)
            .load(BuildConfig.POSTER_ENDPOINT + BuildConfig.POSTER_DEFAULT_WIDTH + binding.item?.posterPath)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(binding.iTvShowIvPoster)
    }
}
