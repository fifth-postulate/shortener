package nl.fifthpostulate.shortener.short

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SequentialTest {
    @Test
    fun `the sequential strategy per default starts at "a"`() {
        val strategy = Sequential()

        val short = strategy.of("http://any.test")

        assertThat(short).isEqualTo("a")
    }

    @Test
    fun `the sequential strategy can be started at a different short`() {
        val strategy = Sequential("b")

        val short = strategy.of("http://any.test")

        assertThat(short).isEqualTo("b")
    }

    @Test
    fun `shorts are given out consecutively`() {
        val strategy = Sequential()

        assertThat(strategy.of("http://any.test")).isEqualTo("a")
        assertThat(strategy.of("http://any.test")).isEqualTo("b")
        assertThat(strategy.of("http://any.test")).isEqualTo("c")
        assertThat(strategy.of("http://any.test")).isEqualTo("d")
        assertThat(strategy.of("http://any.test")).isEqualTo("e")
        assertThat(strategy.of("http://any.test")).isEqualTo("f")
        assertThat(strategy.of("http://any.test")).isEqualTo("g")
        assertThat(strategy.of("http://any.test")).isEqualTo("h")
    }

    @Test
    fun `shorts are per default rollover to "aa" after "z"`() {
        val strategy = Sequential("z")

        strategy.of("http://any.test")
        val short = strategy.of("http://any.test")

        assertThat(short).isEqualTo("aa")
    }

    @Test
    fun `the rollover character can be set`() {
        val strategy = Sequential(next="c", last = 'c')

        strategy.of("http://any.test")
        val short = strategy.of("http://any.test")

        assertThat(short).isEqualTo("aa")
    }
}
