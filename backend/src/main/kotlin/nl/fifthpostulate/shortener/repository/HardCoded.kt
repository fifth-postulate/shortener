package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet
import org.springframework.stereotype.Component

@Component
class HardCoded : ShortRepository {
    override fun load(short: String): DataSheet? {
        return DataSheet(short, "https://shorten.fifth-postulate.nl")
    }
}
