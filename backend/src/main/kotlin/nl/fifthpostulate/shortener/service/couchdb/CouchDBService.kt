package nl.fifthpostulate.shortener.service.couchdb

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.util.Base64Utils.encodeToString
import org.springframework.web.client.*
import nl.fifthpostulate.shortener.result.*
import org.springframework.core.ParameterizedTypeReference

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
        val url = "${properties.url}/${viewName}?${query}"
        val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), ResultSet::class.java)
        return response.toResultSet()
    }
}

private fun ResponseEntity<ResultSet>.toResultSet(): Result<Unit, ResultSet> {
    return if (this.body != null) {
        Success(this.body!!)
    } else {
        Failure(Unit)
    }
}


private fun ResponseEntity<Response>.toResult(): Result<Unit, Unit> {
    return if (this.body?.ok ?: false) {
        Success(Unit)
    } else {
        Failure(Unit)
    }
}

class Response(
        val ok:Boolean?,
        val id: String?,
        val rev: String?,
        val error: String?,
        val reason: String?
)

data class Query(val key: String, val value: QueryValue){
    constructor(key: String, value: String): this(key, StringValue(value)){}
    constructor(key: String, value: Int): this(key, IntValue(value)){}
    constructor(key: String, value: Boolean): this(key, BooleanValue(value)){}
    override fun toString(): String {
        return "$key=$value"
    }
}

sealed class QueryValue() {}
data class StringValue(val value: String): QueryValue() {
    override fun toString(): String {
        return "\"$value\""
    }
}
data class IntValue(val value: Int): QueryValue() {
    override fun toString(): String {
        return "$value"
    }
}
data class BooleanValue(val value: Boolean): QueryValue() {
    override fun toString(): String {
        return "$value"
    }
}
