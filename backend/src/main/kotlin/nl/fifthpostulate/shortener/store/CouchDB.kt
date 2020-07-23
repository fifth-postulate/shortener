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
        return service.save(Created(dataSheet))
                .mapError { "could not store $dataSheet" }
    }

    override fun retrieve(short: String): Result<String, DataSheet> {
        return service.view<Event>("_design/short/_view/events", Query("startkey", short), Query("endkey", short))
                .mapError { "no events found for $short" }
                .map { it.rows }
                .map {rows -> rows.map {it.value}}
                .map(toDataSheet(short))
                .use { service.save(Retrieved(it)) }
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

fun toDataSheet(short: String): (List<Event?>) -> DataSheet {
    return { events ->
        val url = events.filter { it?.type == "created" }.firstOrNull()?.url ?: "http://todo.com"
        val accessed = events.filter { it?.type == "retrieved" }.count()
        DataSheet(short, url, accessed)
    }
}
