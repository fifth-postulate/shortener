package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet

class InMemory(val entries: MutableMap<String, DataSheet> = HashMap()) : ShortRepository<DataSheet?> {
    override fun load(short: String): DataSheet? {
        val dataSheet = entries[short]
        dataSheet?.loaded()
        return dataSheet
    }

    override fun save(dataSheet: DataSheet): Result {
        if (!entries.containsKey(dataSheet.short)) {
            entries[dataSheet.short] = dataSheet
            return Success(dataSheet)
        } else {
            return Failure("short '${dataSheet.short}' is taken")
        }
    }
}
