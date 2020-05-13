package br.eti.rafaelcouto.temporada.view.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.eti.rafaelcouto.temporada.R
import br.eti.rafaelcouto.temporada.databinding.ActivityShowListBinding
import br.eti.rafaelcouto.temporada.network.config.INetworkAPI
import br.eti.rafaelcouto.temporada.network.service.ShowListService
import br.eti.rafaelcouto.temporada.router.ShowListRouter
import br.eti.rafaelcouto.temporada.view.general.BaseActivity
import br.eti.rafaelcouto.temporada.view.general.OnItemClickListener
import br.eti.rafaelcouto.temporada.viewModel.ShowListViewModel
import com.google.android.material.snackbar.Snackbar

class ShowListActivity : BaseActivity() {
    // properties
    private lateinit var binding: ActivityShowListBinding
    private lateinit var mViewModel: ShowListViewModel

    private val numberOfColumns: Int
        get() = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> 2
            else -> 3
        }

    // lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_list)
        mViewModel = ShowListViewModel(
            ShowListRouter(this),
            ShowListService(INetworkAPI.baseApi)
        )

        binding.apply {
            lifecycleOwner = this@ShowListActivity

            loaderVisibility = Transformations.map(mViewModel.isLoading) {
                if (it) View.VISIBLE else View.GONE
            }
        }

        setupRecyclerView()
        observe()

        mViewModel.loadTvShows()
    }

    // base
    override fun setupRecyclerView() {
        val context = this

        binding.aShowListRvTvShows.apply {
            layoutManager = GridLayoutManager(context, numberOfColumns)
            itemAnimator = DefaultItemAnimator()
            adapter = TvShowsAdapter(context, mViewModel.tvShows).apply {
                setOnItemClickListener(object :
                    OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        mViewModel.onShowSelected(position)
                    }
                })
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val mngr = recyclerView.layoutManager as? GridLayoutManager ?: return

                    mViewModel.shouldPaginate(
                        mngr.childCount,
                        mngr.itemCount,
                        mngr.findFirstVisibleItemPosition(),
                        dy
                    ).takeIf { it }?.run {
                        mViewModel.loadTvShows()
                    }
                }
            })
        }
    }

    override fun observe() {
        mViewModel.apply {
            tvShows.observe(this@ShowListActivity, Observer {
                binding.aShowListRvTvShows.adapter?.notifyDataSetChanged()
            })

            hasError.observe(this@ShowListActivity, Observer {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).apply {
                    setAction(R.string.default_retry) { mViewModel.loadTvShows() }
                    show()
                }
            })
        }
    }
}
