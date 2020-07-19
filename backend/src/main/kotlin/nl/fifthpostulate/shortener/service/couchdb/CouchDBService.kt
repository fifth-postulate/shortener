package nl.fifthpostulate.shortener.service.couchdb

import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils.encodeToString
import org.springframework.web.client.*
import nl.fifthpostulate.shortener.result.*

@Component
class CouchDBService(val restTemplate: RestTemplate, val properties: ConnectionProperties) {
    fun save(document: Document): Result<Unit, Unit> {
        val base64UsernameAndPassword =
                encodeToString("${properties.username}:${properties.password}".toByteArray())
        val headers = HttpHeaders()
        headers["content-type"] = MediaType.APPLICATION_JSON_VALUE
        headers["Authorization"] = "Basic $base64UsernameAndPassword"

        try {
            val response = restTemplate.exchange(properties.url, HttpMethod.POST, HttpEntity(document, headers), Response::class.java)
            return response.toResult()
        } catch (e: HttpClientErrorException) {
            if (e.statusCode == HttpStatus.CONFLICT) {
                return Failure(Unit)
            }
            throw e
        }
    }
}

private fun ResponseEntity<Response>.toResult(): Result<Unit, Unit> {
    if (this.body?.ok ?: false) {
        return Success(Unit)
    } else {
        return Failure(Unit)
    }
}

class Response(
        val ok:Boolean?,
        val id: String?,
        val rev: String?,
        val error: String?,
        val reason: String?
)
