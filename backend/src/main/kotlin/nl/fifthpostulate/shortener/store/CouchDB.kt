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
        val result = service.save(Creation(dataSheet))
        return when (result) {
            is Success -> Success(result.data)
            is Failure -> Failure("could not store ${dataSheet}")
        }
    }
}

class Claim(val short: String) : Document() {
    @JsonProperty("_id")
    override val id: String? = "claim:${short}"
    override val revision: String? = null
    override val attachments: Map<String, Attachment>? = null
}

class Creation(@JsonUnwrapped val dataSheet: DataSheet): Document() {
    @JsonProperty("_id")
    override val id: String? = null
    override val revision: String? = null
    override val attachments: Map<String, Attachment>? = null
    val type: String = "creation"
}
