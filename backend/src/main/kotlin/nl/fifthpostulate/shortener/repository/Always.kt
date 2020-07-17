package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet
import org.springframework.stereotype.Component

class Always(val url: String) : ShortRepository {
    override fun load(short: String): DataSheet? {
        return DataSheet(short, url)
    }

    override fun save(dataSheet: DataSheet) {
        /* It would not be always the same if we could change it, so do nothing */
    }
}
