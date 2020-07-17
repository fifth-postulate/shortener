package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet

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

    override fun save(dataSheet: DataSheet): Result {
        return repositories.firstOrNull()?.save(dataSheet) ?: Failure("no repositories to save to")
    }
}
