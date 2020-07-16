package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet

class Chained(vararg val repositories : ShortRepository) : ShortRepository {
    override fun load(short: String): DataSheet? {
        for (repository in repositories) {
            val dataSheet = repository.load(short)
            if (dataSheet != null) {
                return dataSheet
            }
        }
        return null
    }
}
