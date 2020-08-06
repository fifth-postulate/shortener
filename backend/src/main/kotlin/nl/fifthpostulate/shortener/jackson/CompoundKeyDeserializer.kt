package nl.fifthpostulate.shortener.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

class CompoundKeyDeserializer(vararg val keyTypes: KeyType): StdDeserializer<CompoundKey>(CompoundKey::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): CompoundKey {
        val node: JsonNode? = p?.getCodec()?.readTree(p)
        val compoundKey = CompoundKey()
        keyTypes.forEachIndexed {index, keyType ->
            compoundKey.register(index, keyType.value(node?.get(index)))

        }
        return compoundKey
    }
}

sealed class KeyType {
    abstract fun value(node: JsonNode?): Any?
}
class StringKey: KeyType() {
    override fun value(node: JsonNode?): Any? {
        return node?.asText()
    }
}

class IntKey: KeyType() {
    override fun value(node: JsonNode?): Any? {
        return node?.asInt()
    }
}
