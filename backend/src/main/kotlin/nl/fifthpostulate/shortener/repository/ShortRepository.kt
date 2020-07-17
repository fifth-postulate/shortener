package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet

interface ShortRepository {
    fun load(short: String): DataSheet?
    fun save(dataSheet: DataSheet)
}
