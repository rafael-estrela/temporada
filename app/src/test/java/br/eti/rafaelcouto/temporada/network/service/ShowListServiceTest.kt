package br.eti.rafaelcouto.temporada.network.service

import android.os.Build
import br.eti.rafaelcouto.temporada.SynchronousTestRule
import br.eti.rafaelcouto.temporada.model.PageWrapper
import br.eti.rafaelcouto.temporada.model.TvShow
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
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ShowListServiceTest {
    // rules
    @Rule
    @JvmField
    val testRule = SynchronousTestRule()

    // sut
    private lateinit var sut: ShowListService

    // mocks
    @Mock
    private lateinit var mockApi: INetworkAPI

    // dummies
    private val dummyResult: PageWrapper<TvShow>
        get() = PageWrapper<TvShow>().apply {
            page = 0
            results = dummyList
            totalPages = 5
        }

    private val dummyList: List<TvShow>
        get() = listOf(
            dummyShow,
            dummyShow,
            dummyShow,
            dummyShow,
            dummyShow
        )

    private val dummyShow: TvShow
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

        this.sut = ShowListService(mockApi)
        this.dummyId = 0
    }

    @Test
    fun `when load shows requested then should load correctly`() {
        val expected = dummyResult

        whenever(mockApi.getShowList(anyInt(), anyString())) doReturn Single.just(expected)

        // when

        val result = sut.loadTvShows(0, "pt-BR").test()

        // then

        verify(mockApi).getShowList(0, "pt-BR")
        result.assertNoErrors().assertValue(expected)
    }

    @Test
    fun `when load shows requested then should fail`() {
        val expected = Throwable("dummy exception")

        whenever(mockApi.getShowList(anyInt(), anyString())) doReturn Single.error(expected)

        // when

        val result = sut.loadTvShows(0, "pt-BR").test()

        // then

        verify(mockApi).getShowList(0, "pt-BR")
        result.assertError(expected)
    }
}
