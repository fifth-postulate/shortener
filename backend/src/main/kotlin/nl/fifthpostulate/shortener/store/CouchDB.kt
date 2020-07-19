package nl.fifthpostulate.shortener.store

import com.fasterxml.jackson.annotation.JsonProperty
import nl.fifthpostulate.shortener.service.couchdb.*
import nl.fifthpostulate.shortener.result.*

class CouchDB(val service: CouchDBService) : Store {
    override fun claim(candidate: String): Result<Unit, Unit> {
        return service.save(Claim(candidate))
    }
}

class Claim(val short: String) : Document() {
    @JsonProperty("_id")
    override val id: String? = "claim:${short}"
    override val revision: String? = null
    override val attachments: Map<String, Attachment>? = null
}
