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

    fun view(viewName: String, vararg queries: Query): Result<Unit,ResultSet> {
        val base64UsernameAndPassword =
                encodeToString("${properties.username}:${properties.password}".toByteArray())
        val headers = HttpHeaders()
        headers["content-type"] = MediaType.APPLICATION_JSON_VALUE
        headers["Authorization"] = "Basic $base64UsernameAndPassword"

        val query = queries.joinToString("&")
        val response = restTemplate.exchange("${properties.url}/${viewName}", HttpMethod.GET, HttpEntity(null, headers), ResultSet::class.java)
        return response.toResultSet()
    }
}

private fun ResponseEntity<ResultSet>.toResultSet(): Result<Unit, ResultSet> {
    if (this.body != null) {
        return Success(this.body!!)
    } else {
        return Failure(Unit)
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

data class Query(val key: String, val value: String){
    override fun toString(): String {
        return "$key=$value)"
    }
}
