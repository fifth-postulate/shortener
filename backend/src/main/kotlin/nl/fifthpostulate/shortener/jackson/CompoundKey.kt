package nl.fifthpostulate.shortener.jackson

data class CompoundKey(private val keys: MutableMap<Int, Any?> = mutableMapOf()) {
    fun register(index: Int, value: Any?): CompoundKey {
        keys[index] = value
        return this
    }

    fun get(index: Int): Any? {
        return keys[index]
    }
}
