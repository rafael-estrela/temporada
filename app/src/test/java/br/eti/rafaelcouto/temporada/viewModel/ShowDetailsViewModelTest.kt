package br.eti.rafaelcouto.temporada.viewModel

import android.os.Build
import br.eti.rafaelcouto.temporada.R
import br.eti.rafaelcouto.temporada.SynchronousTestRule
import br.eti.rafaelcouto.temporada.model.Genre
import br.eti.rafaelcouto.temporada.model.PageWrapper
import br.eti.rafaelcouto.temporada.model.TvShow
import br.eti.rafaelcouto.temporada.model.TvShowDetails
import br.eti.rafaelcouto.temporada.network.service.ShowDetailsService
import br.eti.rafaelcouto.temporada.router.ShowListRouter
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.hamcrest.Matchers.nullValue
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
class ShowDetailsViewModelTest {
    // rule
    @Rule
    @JvmField
    val testRule = SynchronousTestRule()

    // delayer
    private val delayer = PublishSubject.create<Boolean>()

    // sut
    private lateinit var sut: ShowDetailsViewModel

    // mocks
    @Mock
    private lateinit var mockRouter: ShowListRouter
    @Mock
    private lateinit var mockService: ShowDetailsService

    // dummies
    // dummies
    private val dummyShow: TvShowDetails
        get() = TvShowDetails().apply {
            id = 100L
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
            name = "dummy $id"
            voteAverage = 9.5
            firstAirDate = "2020-01-01"
            posterPath = "dummy"
        }

    private var dummyId: Long = 0

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        sut = ShowDetailsViewModel(mockRouter, mockService)
        dummyId = 0

        sut.apply {
            listOf(
                showName,
                showOverview,
                showFirstYear,
                showProgress,
                showAverage,
                similarShows,
                showGenres,
                showLastYear
            ).forEach { it.observeForever { } }
        }
    }

    @Test
    fun `given a show id when load details requested and details request ends first then should update show`() {
        val expected = dummyShow

        dummyId = 0

        val expectedSimilar = dummySimilarResult

        val mDelayer = PublishSubject.create<Boolean>()

        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.just(expected).delaySubscription(delayer)

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.just(expectedSimilar).delaySubscription(mDelayer)

        assertThat(sut.isLoading.value, nullValue())
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, nullValue())
        assertThat(sut.similarShows.value, nullValue())

        // given

        val stubId = 100L

        // when

        sut.loadDetails(stubId)

        // then

        verify(mockService).loadShowDetails(100L, "pt-BR")
        verify(mockService).loadSimilarShows(100L, 1, "pt-BR")

        assertThat(sut.isLoading.value, equalTo(true))
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, nullValue())
        assertThat(sut.similarShows.value, nullValue())

        delayer.onComplete()

        assertThat(sut.isLoading.value, equalTo(true))
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, nullValue())
        assertThat(sut.similarShows.value, nullValue())

        mDelayer.onComplete()

        assertThat(sut.isLoading.value, equalTo(false))
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, equalTo(expected))
        assertThat(sut.similarShows.value, equalTo(expectedSimilar.results))
    }

    @Test
    fun `given a show id when load details requested and similar request ends first then should update show`() {
        val expected = dummyShow

        dummyId = 0

        val expectedSimilar = dummySimilarResult

        val mDelayer = PublishSubject.create<Boolean>()

        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.just(expected).delaySubscription(delayer)

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.just(expectedSimilar).delaySubscription(mDelayer)

        assertThat(sut.isLoading.value, nullValue())
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, nullValue())
        assertThat(sut.similarShows.value, nullValue())

        // given

        val stubId = 100L

        // when

        sut.loadDetails(stubId)

        // then

        verify(mockService).loadShowDetails(100L, "pt-BR")
        verify(mockService).loadSimilarShows(100L, 1, "pt-BR")

        assertThat(sut.isLoading.value, equalTo(true))
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, nullValue())
        assertThat(sut.similarShows.value, nullValue())

        mDelayer.onComplete()

        assertThat(sut.isLoading.value, equalTo(true))
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, nullValue())
        assertThat(sut.similarShows.value, nullValue())

        delayer.onComplete()

        assertThat(sut.isLoading.value, equalTo(false))
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, equalTo(expected))
        assertThat(sut.similarShows.value, equalTo(expectedSimilar.results))
    }

    @Test
    fun `given a show id when load details request fails then should display error`() {
        val mDelayer = PublishSubject.create<Boolean>()

        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.error<TvShowDetails>(
            Throwable("dummy exception")
        ).delaySubscription(delayer)

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.just(dummySimilarResult).delaySubscription(mDelayer)

        assertThat(sut.isLoading.value, nullValue())
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, nullValue())
        assertThat(sut.similarShows.value, nullValue())

        // given

        val stubId = 100L

        // when

        sut.loadDetails(stubId)

        // then

        verify(mockService).loadShowDetails(100L, "pt-BR")
        verify(mockService).loadSimilarShows(100L, 1, "pt-BR")

        assertThat(sut.isLoading.value, equalTo(true))
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, nullValue())
        assertThat(sut.similarShows.value, nullValue())

        mDelayer.onComplete()

        assertThat(sut.isLoading.value, equalTo(true))
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, nullValue())
        assertThat(sut.similarShows.value, nullValue())

        delayer.onComplete()

        assertThat(sut.isLoading.value, equalTo(false))
        assertThat(sut.hasError.value, equalTo(R.string.show_details_error))
        assertThat(sut.showDetails.value, nullValue())
        assertThat(sut.similarShows.value, nullValue())
    }

    @Test
    fun `given a show id when load similar request fails then should display error`() {
        val mDelayer = PublishSubject.create<Boolean>()

        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.just(dummyShow).delaySubscription(mDelayer)

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.error<PageWrapper<TvShow>>(
            Throwable("dummy exception")
        ).delaySubscription(delayer)

        assertThat(sut.isLoading.value, nullValue())
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, nullValue())
        assertThat(sut.similarShows.value, nullValue())

        // given

        val stubId = 100L

        // when

        sut.loadDetails(stubId)

        // then

        verify(mockService).loadShowDetails(100L, "pt-BR")
        verify(mockService).loadSimilarShows(100L, 1, "pt-BR")

        assertThat(sut.isLoading.value, equalTo(true))
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, nullValue())
        assertThat(sut.similarShows.value, nullValue())

        mDelayer.onComplete()

        assertThat(sut.isLoading.value, equalTo(true))
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, nullValue())
        assertThat(sut.similarShows.value, nullValue())

        delayer.onComplete()

        assertThat(sut.isLoading.value, equalTo(false))
        assertThat(sut.hasError.value, equalTo(R.string.show_details_error))
        assertThat(sut.showDetails.value, nullValue())
        assertThat(sut.similarShows.value, nullValue())
    }

    @Test
    fun `given an initial similar list when more shows requested then should load more shows`() {
        val stubId = 100L

        // given

        val expected = dummyShow
        val expectedSimilar = dummySimilarResult

        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.just(expected)

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.just(expectedSimilar)

        sut.loadDetails(stubId)

        verify(mockService).loadShowDetails(100L, "pt-BR")
        verify(mockService).loadSimilarShows(100L, 1, "pt-BR")

        // when

        val secondExpectedSimilar = dummySimilarResult

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.just(secondExpectedSimilar).delaySubscription(delayer)

        assertThat(sut.isLoading.value, equalTo(false))
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, notNullValue())
        assertThat(sut.similarShows.value, notNullValue())

        sut.loadSimilarShows()

        // then

        verify(mockService).loadSimilarShows(100L, 2, "pt-BR")
        assertThat(sut.isLoading.value, equalTo(true))
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, notNullValue())
        assertThat(sut.similarShows.value, notNullValue())

        delayer.onComplete()

        assertThat(sut.isLoading.value, equalTo(false))
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, notNullValue())

        assertThat(
            sut.similarShows.value,
            contains(
                *expectedSimilar.results.toTypedArray(),
                *secondExpectedSimilar.results.toTypedArray()
            )
        )
    }

    @Test
    fun `given an initial similar shows list when more shows requested then should display error`() {
        val stubId = 100L

        // given

        val expected = dummyShow
        val expectedSimilar = dummySimilarResult

        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.just(expected)

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.just(expectedSimilar)

        sut.loadDetails(stubId)

        verify(mockService).loadShowDetails(100L, "pt-BR")
        verify(mockService).loadSimilarShows(100L, 1, "pt-BR")

        // when

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.error<PageWrapper<TvShow>>(
            Throwable("dummy error")
        ).delaySubscription(delayer)

        assertThat(sut.isLoading.value, equalTo(false))
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, notNullValue())
        assertThat(sut.similarShows.value, notNullValue())

        sut.loadDetails()

        // then

        verify(mockService).loadSimilarShows(100L, 2, "pt-BR")
        assertThat(sut.isLoading.value, equalTo(true))
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, notNullValue())
        assertThat(sut.similarShows.value, notNullValue())

        delayer.onComplete()

        assertThat(sut.isLoading.value, equalTo(false))
        assertThat(sut.hasError.value, equalTo(R.string.show_details_error))
        assertThat(sut.showDetails.value, notNullValue())

        assertThat(
            sut.similarShows.value,
            contains(
                *expectedSimilar.results.toTypedArray()
            )
        )
    }

    @Test
    fun `given a failed similar shows request when retry requested then should reload same page`() {
        val stubId = 100L

        // given

        val expected = dummyShow
        val expectedSimilar = dummySimilarResult

        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.just(expected)

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.just(expectedSimilar)

        sut.loadDetails(stubId)

        verify(mockService).loadShowDetails(100L, "pt-BR")
        verify(mockService).loadSimilarShows(100L, 1, "pt-BR")

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.error(Throwable("dummy error"))

        sut.loadSimilarShows()

        verify(mockService).loadSimilarShows(100L, 2, "pt-BR")
        assertThat(sut.hasError.value, equalTo(R.string.show_details_error))

        // when

        sut.loadSimilarShows()

        // then

        verify(mockService, times(2)).loadSimilarShows(100L, 2, "pt-BR")
    }

    @Test
    fun `given a positive pagination scenario when checking if should paginate then it should be true`() {
        // given

        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.just(dummyShow)

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.just(dummySimilarResult)

        sut.loadDetails(100L)

        val visibleItems = 9
        val totalItems = 20
        val firstVisiblePosition = 12
        val dx = 10

        // when

        val actual = sut.shouldPaginate(visibleItems, totalItems, firstVisiblePosition, dx)

        // then

        assertThat(actual, equalTo(true))
    }

    @Test
    fun `given an initial list and a full scroll to bottom when checking if should paginate then it should be false`() {
        // given

        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.just(dummyShow)

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.just(dummySimilarResult)

        sut.loadDetails(100L)

        val visibleItems = 9
        val totalItems = 20
        val firstVisiblePosition = 12
        val dx = 0 // bottom of page

        // when

        val actual = sut.shouldPaginate(visibleItems, totalItems, firstVisiblePosition, dx)

        // then

        assertThat(actual, equalTo(false))
    }

    @Test
    fun `given no initial list when checking if should paginate then it should be false`() {
        // given

        val visibleItems = 9
        val totalItems = 20
        val firstVisiblePosition = 12
        val dy = 10

        // when

        val actual = sut.shouldPaginate(visibleItems, totalItems, firstVisiblePosition, dy)

        // then

        assertThat(actual, equalTo(false))
    }

    @Test
    fun `given last page requested when checking if should paginate then it should be false`() {
        // given

        val dummyResult = dummySimilarResult.apply {
            totalPages = 1
        }

        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.just(dummyShow)

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.just(dummyResult)

        sut.loadDetails(100L)

        val visibleItems = 9
        val totalItems = 20
        val firstVisiblePosition = 12
        val dy = 10

        // when

        val actual = sut.shouldPaginate(visibleItems, totalItems, firstVisiblePosition, dy)

        // then

        assertThat(actual, equalTo(false))
    }

    @Test
    fun `given loading in progress when checking if should paginate then it should be false`() {
        // given

        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.just(dummyShow).delaySubscription(delayer)

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.just(dummySimilarResult)

        sut.loadDetails(100L)

        assertThat(sut.isLoading.value, equalTo(true))

        val visibleItems = 9
        val totalItems = 20
        val firstVisiblePosition = 12
        val dy = 10

        // when

        val actual = sut.shouldPaginate(visibleItems, totalItems, firstVisiblePosition, dy)

        // then

        assertThat(actual, equalTo(false))
    }

    @Test
    fun `given a scroll on initial items when checking if should paginate then it should be false`() {
        // given

        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.just(dummyShow)

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.just(dummySimilarResult)

        sut.loadDetails(100L)

        val visibleItems = 9
        val totalItems = 20
        val firstVisiblePosition = 6 // is not at bottom of screen
        val dy = 10

        // when

        val actual = sut.shouldPaginate(visibleItems, totalItems, firstVisiblePosition, dy)

        // then

        assertThat(actual, equalTo(false))
    }

    @Test
    fun `given initial data changed scroll when checking if should paginate then it should be false`() {
        // given

        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.just(dummyShow)

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.just(dummySimilarResult)

        sut.loadDetails(100L)

        val visibleItems = 9
        val totalItems = 20
        val firstVisiblePosition = 0 // dataSetChanged triggers scroll on fill
        val dy = 10

        // when

        val actual = sut.shouldPaginate(visibleItems, totalItems, firstVisiblePosition, dy)

        // then

        assertThat(actual, equalTo(false))
    }

    @Test
    fun `given a failed details request when reload requested then should reload details`() {
        val stubId = 100L

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.just(dummySimilarResult)

        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.error(Throwable("dummy exception"))

        assertThat(sut.isLoading.value, nullValue())
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.showDetails.value, nullValue())
        assertThat(sut.similarShows.value, nullValue())

        // given

        sut.loadDetails(stubId)

        verify(mockService).loadShowDetails(100L, "pt-BR")
        verify(mockService).loadSimilarShows(100L, 1, "pt-BR")
        assertThat(sut.isLoading.value, equalTo(false))
        assertThat(sut.hasError.value, equalTo(R.string.show_details_error))
        assertThat(sut.similarShows.value, nullValue())

        // when

        sut.loadDetails()

        // then

        verify(mockService, times(2)).loadShowDetails(100L, "pt-BR")
        verify(mockService, times(2)).loadSimilarShows(100L, 1, "pt-BR")
    }

    @Test
    fun `given a failed similar shows request when reload requested then should reload shows`() {
        val stubId = 100L

        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.just(dummyShow)

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.just(dummySimilarResult)

        sut.loadDetails(stubId)

        verify(mockService).loadShowDetails(100L, "pt-BR")
        verify(mockService).loadSimilarShows(100L, 1, "pt-BR")

        // given

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.error(Throwable("dummy exception"))

        assertThat(sut.isLoading.value, equalTo(false))
        assertThat(sut.hasError.value, nullValue())
        assertThat(sut.similarShows.value, notNullValue())

        sut.loadSimilarShows()

        verify(mockService).loadSimilarShows(100L, 2, "pt-BR")

        assertThat(sut.isLoading.value, equalTo(false))
        assertThat(sut.hasError.value, equalTo(R.string.show_details_error))
        assertThat(sut.similarShows.value, notNullValue())

        // when

        sut.loadSimilarShows()

        // then

        verify(mockService, times(2)).loadSimilarShows(100L, 2, "pt-BR")
    }

    @Test
    fun `given an initial list when details requested then should go to details`() {
        val dummyResults = dummySimilarResult

        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.just(dummyShow)

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.just(dummyResults)

        // given

        val stubId = 100L
        sut.loadDetails(stubId)

        // when

        sut.onShowSelected(4)

        // then

        verify(mockRouter).proceedToShowDetails("dummy 5", 5)
    }

    @Test
    fun `given no initial list when details requested then should not go to details`() {
        whenever(
            mockService.loadShowDetails(anyLong(), anyString())
        ) doReturn Single.just(dummyShow)

        whenever(
            mockService.loadSimilarShows(anyLong(), anyInt(), anyString())
        ) doReturn Single.error(Throwable("dummy"))

        // given

        val stubId = 100L
        sut.loadDetails(stubId)

        // when

        sut.onShowSelected(4)

        // then

        verifyNoMoreInteractions(mockRouter)
    }
}
