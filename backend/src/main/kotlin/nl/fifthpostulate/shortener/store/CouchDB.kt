package nl.fifthpostulate.shortener.store

import com.fasterxml.jackson.annotation.JsonProperty
import nl.fifthpostulate.shortener.service.couchdb.*
import nl.fifthpostulate.shortener.service.couchdb.Failure
import nl.fifthpostulate.shortener.service.couchdb.Success

class CouchDB(val service: CouchDBService) : Store {
    override fun claim(candidate: String): StoreResult {
        return when (service.save(Claim(candidate))) {
            is Success -> nl.fifthpostulate.shortener.store.Success()
            is Failure -> nl.fifthpostulate.shortener.store.Failure()
        }
    }
}

class Claim(val short: String) : Document() {
    @JsonProperty("_id")
    override val id: String? = "claim:${short}"
    override val revision: String? = null
    override val attachments: Map<String, Attachment>? = null

}
