package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet
import nl.fifthpostulate.shortener.result.*
import nl.fifthpostulate.shortener.store.Store

class Store(val store: Store): ShortRepository<DataSheet?> {
    override fun load(short: String): DataSheet? {
        return store.retrieve(short)
                .map(::poorIdentity)
                .withDefault(null)
    }

    override fun save(dataSheet: DataSheet): Result<String, DataSheet> {
        return store.store(dataSheet)
                .map { dataSheet }
    }
}

fun poorIdentity(dataSheet: DataSheet): DataSheet? {
    return dataSheet
}
