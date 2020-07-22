package nl.fifthpostulate.shortener.store

import com.fasterxml.jackson.annotation.JsonUnwrapped
import nl.fifthpostulate.shortener.domain.DataSheet
import nl.fifthpostulate.shortener.result.*
import nl.fifthpostulate.shortener.service.couchdb.*
import org.springframework.stereotype.Component

@Component
class CouchDB(val service: CouchDBService) : Store {
    override fun claim(candidate: String): Result<Unit, Unit> {
        return service.save(Claim(candidate))
    }

    override fun store(dataSheet: DataSheet): Result<String, Unit> {
        return when (val result = service.save(Created(dataSheet))) {
            is Success -> Success(result.data)
            is Failure -> Failure("could not store ${dataSheet}")
        }
    }

    override fun retrieve(short: String): Result<String, DataSheet> {
        // TODO make ResultSet return specific data.
        val result = service.specialView("_design/short/_view/events", Query("startkey", short), Query("endkey", short))
        return when (result) {
            is Success -> {
                // TODO send retrieve event
                val url = result.data.rows.filter { it.value?.type == "created" }.firstOrNull()?.value?.url
                        ?: "http://todo.com"
                val accessed = result.data.rows.filter { it.value?.type == "retrieved" }.count()
                Success(DataSheet(short, url, accessed))
            }
            is Failure -> Failure("no events found for ${short}")
        }
    }
}

data class Claim(val short: String) : Document()

sealed class EventInput(val type: String, @JsonUnwrapped val dataSheet: DataSheet) : Document()
class Created(dataSheet: DataSheet) : EventInput("created", dataSheet)
class Retrieved(dataSheet: DataSheet) : EventInput("retrieved", dataSheet)

class Event {
    var type: String? = null
    var url: String? = null
}
