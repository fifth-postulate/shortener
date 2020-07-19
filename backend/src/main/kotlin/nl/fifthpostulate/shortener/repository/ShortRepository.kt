package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet
import nl.fifthpostulate.shortener.result.Result

interface ShortRepository<T> {
    fun load(short: String): T
    fun save(dataSheet: DataSheet): Result<String, DataSheet>
}
