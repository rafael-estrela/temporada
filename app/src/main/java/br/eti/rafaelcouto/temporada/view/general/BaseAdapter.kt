package br.eti.rafaelcouto.temporada.view.general

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<Item, VH : RecyclerView.ViewHolder>(
    context: Context,
    private var items: LiveData<List<Item>>,
    @LayoutRes private var layoutId: Int
) : RecyclerView.Adapter<VH>() {
    // inflater
    protected val inflater: LayoutInflater = LayoutInflater.from(context)

    // onitemclick
    var onItemClick: OnItemClickListener? = null

    // override functions
    override fun getItemCount(): Int = items.value.orEmpty().size
    override fun getItemViewType(position: Int): Int = layoutId

    // onitemclick setter
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClick = listener
    }

    // item for position
    protected fun getItemAtPosition(position: Int): Item {
        return items.value?.let {
            it[position]
        } ?: throw IllegalStateException()
    }
}
