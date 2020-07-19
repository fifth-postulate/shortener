package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet
import nl.fifthpostulate.shortener.result.*

class Chained<T>(val fallback : ShortRepository<T>, vararg val repositories : ShortRepository<T?>) : ShortRepository<T> {
    override fun load(short: String): T {
        for (repository in repositories) {
            val dataSheet = repository.load(short)
            if (dataSheet != null) {
                return dataSheet
            }
        }
        return fallback.load(short)
    }

    override fun save(dataSheet: DataSheet): Result<String, DataSheet> {
        for (repository in repositories) {
            val result = repository.save(dataSheet)
            if (result is Success) {
                return result
            }
        }
        return Failure("no repository could save ${dataSheet}")
    }
}
