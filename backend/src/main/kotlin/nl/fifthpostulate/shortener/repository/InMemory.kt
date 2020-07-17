package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet

class InMemory(val entries: MutableMap<String, DataSheet> = HashMap()) : ShortRepository<DataSheet?> {
    override fun load(short: String): DataSheet? {
        val dataSheet = entries[short]
        dataSheet?.loaded()
        return dataSheet
    }

    override fun save(dataSheet: DataSheet) {
        entries[dataSheet.short] = dataSheet
    }
}
