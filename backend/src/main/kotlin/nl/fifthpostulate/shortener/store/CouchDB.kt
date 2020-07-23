package nl.fifthpostulate.shortener.store

import com.fasterxml.jackson.annotation.JsonUnwrapped
import nl.fifthpostulate.shortener.domain.DataSheet
import nl.fifthpostulate.shortener.result.*
import nl.fifthpostulate.shortener.service.couchdb.*
import org.springframework.stereotype.Component

@Component
class CouchDB(val service: CouchDBService) : Store {
    override fun claim(candidate: String): Result<Unit, Unit> {
        val document = Claim(candidate)
        return service.save(document)
    }

    override fun store(dataSheet: DataSheet): Result<String, Unit> {
        return when (val result = service.save(Created(dataSheet))) {
            is Success -> Success(result.data)
            is Failure -> Failure("could not store ${dataSheet}")
        }
    }

    override fun retrieve(short: String): Result<String, DataSheet> {
        // TODO make ResultSet return specific data.
        val result = service.view<Event>("_design/short/_view/events", Query("startkey", short), Query("endkey", short))
        return when (result) {
            is Success -> {
                val url = result.data.rows.filter { it.value?.type == "created" }.firstOrNull()?.value?.url
                        ?: "http://todo.com"
                val accessed = result.data.rows.filter { it.value?.type == "retrieved" }.count()
                val dataSheet = DataSheet(short, url, accessed)
                service.save(Retrieved(dataSheet))
                Success(dataSheet)
            }
            is Failure -> Failure("no events found for ${short}")
        }
    }
}

data class Claim(val short: String) : Document(id="claim:${short}")

sealed class EventInput(val type: String, @param:JsonUnwrapped @get:JsonUnwrapped val dataSheet: DataSheet) : Document()
class Created(dataSheet: DataSheet) : EventInput("created", dataSheet)
class Retrieved(dataSheet: DataSheet) : EventInput("retrieved", dataSheet)

class Event {
    var type: String? = null
    var url: String? = null
}
