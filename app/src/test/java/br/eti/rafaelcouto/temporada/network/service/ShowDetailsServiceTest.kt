package br.eti.rafaelcouto.temporada.network.service

import android.os.Build
import br.eti.rafaelcouto.temporada.SynchronousTestRule
import br.eti.rafaelcouto.temporada.model.Genre
import br.eti.rafaelcouto.temporada.model.PageWrapper
import br.eti.rafaelcouto.temporada.model.TvShow
import br.eti.rafaelcouto.temporada.model.TvShowDetails
import br.eti.rafaelcouto.temporada.network.config.INetworkAPI
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ShowDetailsServiceTest {
    // rules
    @Rule
    @JvmField
    val testRule = SynchronousTestRule()

    // sut
    private lateinit var sut: ShowDetailsService

    // mocks
    @Mock
    private lateinit var mockApi: INetworkAPI

    // dummies
    private val dummyShow: TvShowDetails
        get() = TvShowDetails().apply {
            id = ++dummyId
            name = "dummy"
            voteAverage = 9.5
            firstAirDate = "2020-01-01"
            posterPath = "dummy"
            inProduction = false
            lastAirDate = "2020-05-01"
            overview = "dummy"
            genres = listOf(Genre().apply { name = "dummy" })
            similarShows = dummySimilarList
        }

    private val dummySimilarResult: PageWrapper<TvShow>
        get() = PageWrapper<TvShow>().apply {
            page = 0
            results = dummySimilarList
            totalPages = 5
        }

    private val dummySimilarList: List<TvShow>
        get() = listOf(
            dummySimilarShow,
            dummySimilarShow,
            dummySimilarShow,
            dummySimilarShow,
            dummySimilarShow
        )

    private val dummySimilarShow: TvShow
        get() = TvShow().apply {
            id = ++dummyId
            name = "dummy"
            voteAverage = 9.5
            firstAirDate = "2020-01-01"
            posterPath = "dummy"
        }

    private var dummyId: Long = 0

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        this.sut = ShowDetailsService(mockApi)
        this.dummyId = 0
    }

    @Test
    fun `given a show id when load details requested then should load successfully`() {
        val expected = dummyShow

        whenever(mockApi.getShowDetails(anyLong(), anyString())) doReturn Single.just(expected)

        // given

        val stubId = 10L

        // when

        val result = sut.loadShowDetails(stubId, "pt-BR").test()

        // then

        verify(mockApi).getShowDetails(10L, "pt-BR")
        result.assertNoErrors().assertValue(expected)
    }

    @Test
    fun `given a show id when load details requested then should fail`() {
        val expected = Throwable("dummy exception")

        whenever(mockApi.getShowDetails(anyLong(), anyString())) doReturn Single.error(expected)

        // given

        val stubId = 10L

        // when

        val result = sut.loadShowDetails(stubId, "pt-BR").test()

        // then

        verify(mockApi).getShowDetails(10L, "pt-BR")
        result.assertError(expected)
    }

    @Test
    fun `given a show id when load similar shows requested then should load successfully`() {
        val expected = dummySimilarResult

        whenever(mockApi.getSimilarShows(anyLong(), anyInt(), anyString())) doReturn Single.just(expected)

        // given

        val stubId = 10L

        // when

        val result = sut.loadSimilarShows(stubId, 0, "pt-BR").test()

        // then

        verify(mockApi).getSimilarShows(10L, 0, "pt-BR")
        result.assertNoErrors().assertValue(expected)
    }

    @Test
    fun `given a show id when load similar shows requested then should fail`() {
        val expected = Throwable("dummy exception")

        whenever(mockApi.getSimilarShows(anyLong(), anyInt(), anyString())) doReturn Single.error(expected)

        // given

        val stubId = 10L

        // when

        val result = sut.loadSimilarShows(stubId, 0, "pt-BR").test()

        // then

        verify(mockApi).getSimilarShows(10L, 0, "pt-BR")
        result.assertError(expected)
    }
}
