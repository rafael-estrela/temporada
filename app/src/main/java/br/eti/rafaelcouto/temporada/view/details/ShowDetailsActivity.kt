package br.eti.rafaelcouto.temporada.view.details

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.eti.rafaelcouto.temporada.BuildConfig
import br.eti.rafaelcouto.temporada.R
import br.eti.rafaelcouto.temporada.databinding.ActivityShowDetailsBinding
import br.eti.rafaelcouto.temporada.network.config.INetworkAPI
import br.eti.rafaelcouto.temporada.network.service.ShowDetailsService
import br.eti.rafaelcouto.temporada.router.ShowListRouter
import br.eti.rafaelcouto.temporada.view.general.BaseActivity
import br.eti.rafaelcouto.temporada.view.general.OnItemClickListener
import br.eti.rafaelcouto.temporada.viewModel.ShowDetailsViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class ShowDetailsActivity : BaseActivity() {
    // properties
    private lateinit var binding: ActivityShowDetailsBinding
    private lateinit var mViewModel: ShowDetailsViewModel

    // lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_details)
        mViewModel = ShowDetailsViewModel(
            ShowListRouter(this),
            ShowDetailsService(INetworkAPI.baseApi)
        )

        binding.apply {
            lifecycleOwner = this@ShowDetailsActivity

            loaderVisibility = Transformations.map(mViewModel.isLoading) {
                if (it) View.VISIBLE else View.GONE
            }

            viewModel = mViewModel
        }

        observe()
        setupDetails()
        setupRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.itemId.takeIf { it == android.R.id.home }?.let {
            onBackPressed()

            true
        } ?: super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_ltr, R.anim.slide_out_ltr)
    }

    // private functions
    private fun setupDetails() {
        title = intent.getStringExtra(ShowDetailsViewModel.SHOW_TITLE)

        mViewModel.loadDetails(intent.getLongExtra(ShowDetailsViewModel.SHOW_ID, 0))
    }

    // base
    override fun setupRecyclerView() {
        val context = this

        binding.aShowDetailsRvSimilarShows.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            itemAnimator = DefaultItemAnimator()
            adapter = SimilarTvShowsAdapter(
                context,
                mViewModel.similarShows
            ).apply {
                setOnItemClickListener(object :
                    OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        mViewModel.onShowSelected(position)
                    }
                })
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val mngr = recyclerView.layoutManager as? LinearLayoutManager ?: return

                    mViewModel.shouldPaginate(
                        mngr.childCount,
                        mngr.itemCount,
                        mngr.findFirstVisibleItemPosition(),
                        dx
                    ).takeIf { it }?.run {
                        mViewModel.loadSimilarShows()
                    }
                }
            })
        }
    }

    override fun observe() {
        mViewModel.showDetails.observe(this, Observer {
            binding.aShowDetailsNsvContent.visibility = View.VISIBLE

            Picasso.with(this)
                .load(BuildConfig.POSTER_ENDPOINT + BuildConfig.POSTER_LARGE_WIDTH + it.posterPath)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(binding.aShowDetailsIvPoster)
        })

        mViewModel.showProgress.observe(this, Observer {
            binding.aShowDetailsCpbVotes.progress = it
        })

        mViewModel.similarShows.observe(this, Observer {
            binding.aShowDetailsRvSimilarShows.adapter?.notifyDataSetChanged()
        })

        mViewModel.hasError.observe(this, Observer {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).apply {
                setAction(R.string.default_retry) { mViewModel.loadDetails() }
                show()
            }
        })
    }
}
