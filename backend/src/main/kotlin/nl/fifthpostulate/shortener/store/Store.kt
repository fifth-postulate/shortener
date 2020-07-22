package nl.fifthpostulate.shortener.store

import nl.fifthpostulate.shortener.domain.DataSheet
import nl.fifthpostulate.shortener.result.Result

interface Store {
    fun claim(candidate: String): Result<Unit, Unit>

    fun store(dataSheet: DataSheet): Result<String, Unit>

    fun retrieve(short: String): Result<String, DataSheet>
}
