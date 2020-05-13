package br.eti.rafaelcouto.temporada

import android.os.Build
import java.lang.Exception
import java.util.Calendar
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ExtensionsTests {
    @Test
    fun `given a date string when default pattern requested then should be correct date`() {
        val strDate = "2020-01-01"

        val calendar = Calendar.getInstance()
        calendar.time = strDate.toDate()

        assertThat(calendar.get(Calendar.DAY_OF_MONTH), equalTo(1))
        assertThat(calendar.get(Calendar.MONTH), equalTo(0))
        assertThat(calendar.get(Calendar.YEAR), equalTo(2020))
    }

    @Test
    fun `given a date string when custom pattern requested then should be correct date`() {
        val strDate = "01/01/2020"

        val calendar = Calendar.getInstance()
        calendar.time = strDate.toDate("dd/MM/yyyy")

        assertThat(calendar.get(Calendar.DAY_OF_MONTH), equalTo(1))
        assertThat(calendar.get(Calendar.MONTH), equalTo(0))
        assertThat(calendar.get(Calendar.YEAR), equalTo(2020))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `given a non-matching date string when format requested then should throw exception`() {
        val strDate = "01/01/2020"

        try {
            strDate.toDate()
        } catch (e: Exception) {
            assertThat(e.message, equalTo("The string 01/01/2020 does not conform to the pattern yyyy-MM-dd."))
            throw e
        }
    }

    @Test
    fun `given a date when default pattern requested then should be correct string`() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.MONTH, 0)
        calendar.set(Calendar.YEAR, 2020)

        val dateString = calendar.time.toDateString()

        assertThat(dateString, equalTo("01/01/2020"))
    }

    @Test
    fun `given a date when custom pattern requested then should be correct string`() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.MONTH, 0)
        calendar.set(Calendar.YEAR, 2020)

        val dateString = calendar.time.toDateString("yyyy-MM-dd")

        assertThat(dateString, equalTo("2020-01-01"))
    }
}
