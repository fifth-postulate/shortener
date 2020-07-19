package nl.fifthpostulate.shortener.store

import nl.fifthpostulate.shortener.result.Result

interface Store {
    fun claim(candidate: String): Result<Unit, Unit>
}
