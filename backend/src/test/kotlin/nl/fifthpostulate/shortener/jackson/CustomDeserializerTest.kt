package nl.fifthpostulate.shortener.jackson

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class CustomDeserializerTest {
    @ParameterizedTest
    @MethodSource("data")
    fun `deserializing a custom class is possible`(json: String, keys: Array<KeyType>, expected: CompoundKey) {
        val module = SimpleModule()
        module.addDeserializer(CompoundKey::class.java, CompoundKeyDeserializer(*keys))
        val objectMapper = ObjectMapper().registerModule(module)

        val compoundKey = objectMapper.readValue(json, CompoundKey::class.java)

        assertThat(compoundKey).isNotNull()
        assertThat(compoundKey).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun data(): Stream<Arguments> {
            return Stream.of(
                    Arguments.of("[\"aaa\",1]", arrayOf(StringKey(), IntKey()), CompoundKey().register(0, "aaa").register(1, 1)),
                    Arguments.of("[\"bbb\",2]", arrayOf(StringKey(), IntKey()), CompoundKey().register(0, "bbb").register(1, 2)),
                    Arguments.of("[3,\"ccc\"]", arrayOf(IntKey(), StringKey()), CompoundKey().register(0, 3).register(1, "ccc")),
                    Arguments.of("[\"ddd\"]", arrayOf(StringKey()), CompoundKey().register(0, "ddd"))
            )
        }
    }
}
