package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet

class InMemory(val entries: Map<String, DataSheet> = HashMap()) : ShortRepository {
    override fun load(short: String): DataSheet? {
        return entries.get(short)
    }
}
