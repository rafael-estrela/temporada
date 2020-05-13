package br.eti.rafaelcouto.temporada.router

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import br.eti.rafaelcouto.temporada.R
import br.eti.rafaelcouto.temporada.view.details.ShowDetailsActivity
import br.eti.rafaelcouto.temporada.viewModel.ShowDetailsViewModel

class ShowListRouter(private val activity: AppCompatActivity) {
    fun proceedToShowDetails(title: String, id: Long) {
        activity.startActivity(
            Intent(activity, ShowDetailsActivity::class.java).apply {
                putExtra(ShowDetailsViewModel.SHOW_TITLE, title)
                putExtra(ShowDetailsViewModel.SHOW_ID, id)
            }
        )

        activity.overridePendingTransition(R.anim.slide_in_rtl, R.anim.slide_out_rtl)
    }
}
