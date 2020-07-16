package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class InMemoryTest {
    @Test
    fun `an empty InMemory repository does not know about any shorts`() {
        val repository = InMemory()

        val dataSheet = repository.load("shrt")

        assertThat(dataSheet).isNull()
    }

    @Test
    fun `an prepopulated InMemory repository knows about the prepopulated shorts`() {
        val repository = InMemory(mapOf("shrt" to DataSheet("shrt", "http://local.test")))

        val dataSheet = repository.load("shrt")

        assertThat(dataSheet).isNotNull()
        assertThat(dataSheet).isEqualTo(DataSheet("shrt", "http://local.test"))
    }
}
