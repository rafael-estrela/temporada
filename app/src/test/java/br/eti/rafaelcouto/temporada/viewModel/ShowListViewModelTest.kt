package br.eti.rafaelcouto.temporada.viewModel

import android.os.Build
import br.eti.rafaelcouto.temporada.R
import br.eti.rafaelcouto.temporada.SynchronousTestRule
import br.eti.rafaelcouto.temporada.model.PageWrapper
import br.eti.rafaelcouto.temporada.model.TvShow
import br.eti.rafaelcouto.temporada.network.service.ShowListService
import br.eti.rafaelcouto.temporada.router.ShowListRouter
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.nullValue
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
class ShowListViewModelTest {
    // rule
    @Rule
    @JvmField
    val testRule = SynchronousTestRule()

    // delayer
    private val delayer = PublishSubject.create<Boolean>()

    // sut
    private lateinit var sut: ShowListViewModel

    // mocks
    @Mock
    private lateinit var mockRouter: ShowListRouter
    @Mock
    private lateinit var mockService: ShowListService

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
            name = "dummy $id"
            voteAverage = 9.5
            firstAirDate = "2020-01-01"
            posterPath = "dummy"
        }

    private var dummyId: Long = 0

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        sut = ShowListViewModel(mockRouter, mockService)
        dummyId = 0
    }

    @Test
    fun `when initial show list requested then should update list`() {
        val expected = dummyResult

        whenever(
            mockService.loadTvShows(anyInt(), anyString())
        ) doReturn Single.just(expected).delaySubscription(delayer)

        assertThat(sut.tvShows.value, nullValue())
        assertThat(sut.isLoading.value, nullValue())
        assertThat(sut.hasError.value, nullValue())

        // when

        sut.loadTvShows()

        // then

        verify(mockService).loadTvShows(1, "pt-BR")
        assertThat(sut.isLoading.value, equalTo(true))

        delayer.onComplete()

        assertThat(sut.isLoading.value, equalTo(false))
        assertThat(sut.hasError.value, nullValue())

        assertThat(sut.tvShows.value, equalTo(expected.results))
    }

    @Test
    fun `when initial show list requested then should display error`() {
        whenever(
            mockService.loadTvShows(anyInt(), anyString())
        ) doReturn Single.error<PageWrapper<TvShow>>(
            Throwable("dummy exception")
        ).delaySubscription(delayer)

        assertThat(sut.tvShows.value, nullValue())
        assertThat(sut.isLoading.value, nullValue())
        assertThat(sut.hasError.value, nullValue())

        // when

        sut.loadTvShows()

        // then

        verify(mockService).loadTvShows(1, "pt-BR")
        assertThat(sut.isLoading.value, equalTo(true))
        assertThat(sut.hasError.value, nullValue())

        delayer.onComplete()

        assertThat(sut.isLoading.value, equalTo(false))
        assertThat(sut.hasError.value, equalTo(R.string.show_list_error))

        assertThat(sut.tvShows.value, nullValue())
    }

    @Test
    fun `given initial show list when new page requested then should request new page`() {
        val firstResult = dummyResult
        val secondResult = dummyResult

        whenever(
            mockService.loadTvShows(1, "pt-BR")
        ) doReturn Single.just(firstResult)

        whenever(
            mockService.loadTvShows(2, "pt-BR")
        ) doReturn Single.just(secondResult).delaySubscription(delayer)

        assertThat(sut.tvShows.value, nullValue())
        assertThat(sut.isLoading.value, nullValue())
        assertThat(sut.hasError.value, nullValue())

        // given

        sut.loadTvShows()
        verify(mockService).loadTvShows(1, "pt-BR")
        assertThat(sut.isLoading.value, equalTo(false))
        assertThat(sut.hasError.value, nullValue())

        assertThat(sut.tvShows.value, equalTo(firstResult.results))

        // when

        sut.loadTvShows()

        // then

        verify(mockService).loadTvShows(2, "pt-BR")

        assertThat(sut.isLoading.value, equalTo(true))
        assertThat(sut.hasError.value, nullValue())

        delayer.onComplete()

        assertThat(sut.isLoading.value, equalTo(false))
        assertThat(sut.hasError.value, nullValue())

        assertThat(
            sut.tvShows.value,
            Matchers.contains(
                *firstResult.results.toTypedArray(),
                *secondResult.results.toTypedArray()
            )
        )
    }

    @Test
    fun `given a failed list request when requested again then should request same page`() {
        whenever(
            mockService.loadTvShows(anyInt(), anyString())
        ) doReturn Single.error(Throwable("dummy error"))

        assertThat(sut.tvShows.value, nullValue())
        assertThat(sut.isLoading.value, nullValue())
        assertThat(sut.hasError.value, nullValue())

        // given

        sut.loadTvShows()
        verify(mockService).loadTvShows(1, "pt-BR")
        assertThat(sut.isLoading.value, equalTo(false))
        assertThat(sut.hasError.value, equalTo(R.string.show_list_error))

        assertThat(sut.tvShows.value, nullValue())

        // when

        sut.loadTvShows()

        // then

        verify(mockService, times(2)).loadTvShows(1, "pt-BR")
        verify(mockService, times(0)).loadTvShows(2, "pt-BR")
    }

    @Test
    fun `given a positive pagination scenario when checking if should paginate then it should be true`() {
        // given

        whenever(
            mockService.loadTvShows(anyInt(), anyString())
        ) doReturn Single.just(dummyResult)

        sut.loadTvShows()

        val visibleItems = 9
        val totalItems = 20
        val firstVisiblePosition = 12
        val dy = 10

        // when

        val actual = sut.shouldPaginate(visibleItems, totalItems, firstVisiblePosition, dy)

        // then

        assertThat(actual, equalTo(true))
    }

    @Test
    fun `given an initial list and a full scroll to bottom when checking if should paginate then it should be false`() {
        // given

        whenever(
            mockService.loadTvShows(anyInt(), anyString())
        ) doReturn Single.just(dummyResult)

        sut.loadTvShows()

        val visibleItems = 9
        val totalItems = 20
        val firstVisiblePosition = 12
        val dy = 0 // bottom of page

        // when

        val actual = sut.shouldPaginate(visibleItems, totalItems, firstVisiblePosition, dy)

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

        val dummyResult = dummyResult.apply {
            totalPages = 1
        }

        whenever(
            mockService.loadTvShows(anyInt(), anyString())
        ) doReturn Single.just(dummyResult)

        val visibleItems = 9
        val totalItems = 20
        val firstVisiblePosition = 12
        val dy = 10

        sut.loadTvShows()

        // when

        val actual = sut.shouldPaginate(visibleItems, totalItems, firstVisiblePosition, dy)

        // then

        assertThat(actual, equalTo(false))
    }

    @Test
    fun `given loading in progress when checking if should paginate then it should be false`() {
        // given

        whenever(
            mockService.loadTvShows(anyInt(), anyString())
        ) doReturn Single.just(dummyResult).delaySubscription(delayer)

        sut.loadTvShows()

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
            mockService.loadTvShows(anyInt(), anyString())
        ) doReturn Single.just(dummyResult)

        sut.loadTvShows()

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
            mockService.loadTvShows(anyInt(), anyString())
        ) doReturn Single.just(dummyResult)

        sut.loadTvShows()

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
    fun `given an initial list when details requested then should go to details`() {
        whenever(
            mockService.loadTvShows(anyInt(), anyString())
        ) doReturn Single.just(dummyResult)

        // given

        sut.loadTvShows()

        // when

        sut.onShowSelected(4)

        // then

        verify(mockRouter).proceedToShowDetails("dummy 5", 5)
    }

    @Test
    fun `given no initial list when details requested then should not go to details`() {
        whenever(
            mockService.loadTvShows(anyInt(), anyString())
        ) doReturn Single.error(Throwable("dummy error"))

        // given

        sut.loadTvShows()

        // when

        sut.onShowSelected(4)

        // then

        verifyNoMoreInteractions(mockRouter)
    }
}
