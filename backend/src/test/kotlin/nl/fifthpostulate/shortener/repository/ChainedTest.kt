package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ChainedTest {
    @Test
    fun `a chained repository delegates to first sub repository that loads a datasheet`() {
        val repository = Chained(
                InMemory(),
                InMemory(mapOf("shrt" to DataSheet("shrt", "http://first.nl"))),
                InMemory(mapOf("shrt" to DataSheet("shrt", "http://second.nl")))
        )

        val dataSheet = repository.load("shrt")

        assertThat(dataSheet).isNotNull()
        assertThat(dataSheet).isEqualTo(DataSheet("shrt", "http://first.nl"))
    }
}
