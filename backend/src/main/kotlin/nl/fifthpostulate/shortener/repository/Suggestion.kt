package nl.fifthpostulate.shortener.repository

import nl.fifthpostulate.shortener.domain.DataSheet
import nl.fifthpostulate.shortener.result.Success

class Suggestion(val suggestionUrl: String): ShortRepository<DataSheet> {
    override fun load(short: String): DataSheet {
        return DataSheet(short, "${suggestionUrl}${short}")
    }

    override fun save(dataSheet: DataSheet): Success<String, DataSheet> {
        /* Suggestions are configured and can't be changed programmatically */
        return Success(dataSheet)
    }
}
