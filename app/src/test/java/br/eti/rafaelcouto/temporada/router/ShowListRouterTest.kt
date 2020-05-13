package br.eti.rafaelcouto.temporada.router

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import br.eti.rafaelcouto.temporada.view.details.ShowDetailsActivity
import br.eti.rafaelcouto.temporada.viewModel.ShowDetailsViewModel
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ShowListRouterTest {
    // sut
    private lateinit var sut: ShowListRouter

    // mocks
    private lateinit var activity: AppCompatActivity

    @Before
    fun setUp() {
        this.activity = Robolectric.buildActivity(AppCompatActivity::class.java).create().get()
        this.sut = ShowListRouter(activity)
    }

    @Test
    fun `when go to details requested then should go to details`() {
        val expectedIntent = Intent(activity, ShowDetailsActivity::class.java)

        sut.proceedToShowDetails("dummy", 100)

        val actualIntent = shadowOf(activity).nextStartedActivity

        assertThat(expectedIntent.filterEquals(actualIntent), equalTo(true))

        assertThat(actualIntent.extras, notNullValue())
        assertThat(actualIntent.getStringExtra(ShowDetailsViewModel.SHOW_TITLE), equalTo("dummy"))
        assertThat(actualIntent.getLongExtra(ShowDetailsViewModel.SHOW_ID, 0L), equalTo(100L))
    }
}
