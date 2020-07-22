package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet
import nl.fifthpostulate.shortener.result.Failure
import nl.fifthpostulate.shortener.result.Result
import nl.fifthpostulate.shortener.result.Success
import nl.fifthpostulate.shortener.store.Store

class Store(val store: Store): ShortRepository<DataSheet?> {
    override fun load(short: String): DataSheet? {
        return when(val result = store.retrieve(short)) {
            is Success -> result.data
            is Failure -> null
        }
    }

    override fun save(dataSheet: DataSheet): Result<String, DataSheet> {
        return when(val result = store.store(dataSheet)) {
            is Success -> Success(dataSheet)
            is Failure -> Failure(result.error)
        }
    }
}
