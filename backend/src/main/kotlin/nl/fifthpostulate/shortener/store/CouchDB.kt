package nl.fifthpostulate.shortener.store

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import nl.fifthpostulate.shortener.domain.DataSheet
import nl.fifthpostulate.shortener.service.couchdb.*
import nl.fifthpostulate.shortener.result.*
import org.springframework.stereotype.Component

@Component
class CouchDB(val service: CouchDBService) : Store {
    override fun claim(candidate: String): Result<Unit, Unit> {
        return service.save(Claim(candidate))
    }

    override fun store(dataSheet: DataSheet): Result<String, Unit> {
        val event = Created(dataSheet)
        return when (val result = service.save(event)) {
            is Success -> Success(result.data)
            is Failure -> Failure("could not store ${dataSheet}")
        }
    }

    override fun retrieve(short: String): Result<String, DataSheet> {
        // TODO make ResultSet return specific data.
        val result = service.specialView("_design/short/_view/events", Query("startkey", short), Query("endkey", short))
        return when(result) {
            is Success -> {
                // TODO send retrieve event
                val url = result.data.rows.filter { it.value?.type == "created" }.firstOrNull()?.value?.url ?: "http://todo.com"
                val accessed = result.data.rows.filter {it.value?.type == "retrieved"}.count()
                Success(DataSheet(short, url, accessed))
            }
            is Failure -> Failure("no events found for ${short}")
        }
    }
}

data class Claim(val short: String) : Document() {
    @JsonProperty("_id")
    override val id: String? = "claim:${short}"
    override val revision: String? = null
    override val attachments: Map<String, Attachment>? = null
}

sealed class EventInput(val type: String, @JsonUnwrapped val dataSheet: DataSheet): Document() {
    @JsonProperty("_id")
    override val id: String? = null
    override val revision: String? = null
    override val attachments: Map<String, Attachment>? = null
}

class Created(dataSheet: DataSheet): EventInput("created", dataSheet){}
class Retrieved(dataSheet: DataSheet): EventInput("retrieved", dataSheet){}

class Event() {
    var type: String? = null
    var url: String? = null
}
