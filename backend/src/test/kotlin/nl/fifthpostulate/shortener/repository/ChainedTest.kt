package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ChainedTest {
    @Test
    fun `a chained repository delegates to the first sub repository that loads a datasheet`() {
        val needle = DataSheet("shrt", "http://first.nl")
        val repository = Chained(
                InMemory(),
                InMemory(mutableMapOf("shrt" to needle)),
                InMemory(mutableMapOf("shrt" to DataSheet("shrt", "http://second.nl")))
        )

        val dataSheet = repository.load("shrt")

        assertThat(dataSheet).isNotNull()
        assertThat(dataSheet).isEqualTo(needle)
    }
}
