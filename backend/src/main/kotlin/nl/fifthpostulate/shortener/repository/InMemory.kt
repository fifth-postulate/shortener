package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet

class InMemory(val entries: MutableMap<String, DataSheet> = HashMap()) : ShortRepository {
    override fun load(short: String): DataSheet? {
        return entries.get(short)
    }

    override fun save(dataSheet: DataSheet) {
        entries.put(dataSheet.short, dataSheet)
    }
}
