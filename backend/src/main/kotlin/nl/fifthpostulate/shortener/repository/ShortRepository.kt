package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet

interface ShortRepository<T> {
    fun load(short: String): T
    fun save(dataSheet: DataSheet)
}
