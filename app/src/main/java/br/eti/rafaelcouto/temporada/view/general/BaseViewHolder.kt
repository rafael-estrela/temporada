package br.eti.rafaelcouto.temporada.view.general

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T : ViewDataBinding>(
    private val binding: T
) : RecyclerView.ViewHolder(binding.root) {
    fun setOnItemClickListener(onClick: OnItemClickListener?) {
        onClick?.let { onItemClick ->
            binding.root.setOnClickListener {
                onItemClick.onItemClick(adapterPosition)
            }
        }
    }

    abstract fun displayImage()
}
