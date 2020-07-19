package nl.fifthpostulate.shortener.short

import nl.fifthpostulate.shortener.result.*
import nl.fifthpostulate.shortener.store.Store

class StoreBacked(val sequential: Sequential, val store: Store) : ShortenStrategy {
    override fun of(url: String): String {
        var candidate: String
        do {
            candidate = sequential.of(url)
            val result = store.claim(candidate)
        } while (result is Failure)
        return candidate
    }
}
