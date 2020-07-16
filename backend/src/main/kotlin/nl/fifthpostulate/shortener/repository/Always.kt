package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet
import org.springframework.stereotype.Component

class Always(val url: String) : ShortRepository {
    override fun load(short: String): DataSheet? {
        return DataSheet(short, url)
    }
}
