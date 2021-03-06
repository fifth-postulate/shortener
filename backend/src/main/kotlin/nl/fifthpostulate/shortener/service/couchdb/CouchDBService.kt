package nl.fifthpostulate.shortener.service.couchdb

import nl.fifthpostulate.shortener.result.*
import org.springframework.context.annotation.Configuration
import org.springframework.core.*
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.*

@Component
class CouchDBService(val restTemplate: RestTemplate, val properties: ConnectionProperties) {
    fun save(document: Document): Result<Unit, Unit> {
        try {
            val response = restTemplate.exchange(properties.url, HttpMethod.POST, HttpEntity(document), Response::class.java)
            return response.toResult().andThen(::okResponse)
        } catch (e: HttpClientErrorException) {
            if (e.statusCode == HttpStatus.CONFLICT) {
                return Failure(Unit)
            }
            throw e
        }
    }
}

inline fun <reified Key, reified Value> CouchDBService.view(viewName: String, vararg queries: Query): Result<Unit, ResultSet<Key, Value>> {
    val query = queries.joinToString("&")
    val url = "${properties.url}/${viewName}?${query}"
    val type = ResolvableType.forClassWithGenerics(ResultSet::class.java, Key::class.java, Value::class.java)
    val response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, null), ParameterizedTypeReference.forType<ResultSet<Key, Value>>(type.type))
    return response.toResult()
}

fun <T> ResponseEntity<T>.toResult(): Result<Unit, T> {
    return if (this.body != null) {
        Success(this.body!!)
    } else {
        Failure(Unit)
    }
}


fun okResponse(response: Response): Result<Unit, Unit> {
    return if (response.ok ?: false) {
        Success(Unit)
    } else {
        Failure(Unit)
    }
}

class Response(
        val ok: Boolean?,
        val id: String?,
        val rev: String?,
        val error: String?,
        val reason: String?
)

data class Query(val key: String, val value: QueryValue) {
    constructor(key: String, value: String) : this(key, StringValue(value))
    constructor(key: String, value: Int) : this(key, IntValue(value))
    constructor(key: String, value: Boolean) : this(key, BooleanValue(value))
    constructor(key: String, vararg values: QueryValue): this(key, CompoundValue(*values))

    override fun toString(): String {
        return "$key=$value"
    }
}

sealed class QueryValue
data class StringValue(val value: String) : QueryValue() {
    override fun toString(): String {
        return "\"$value\""
    }
}

data class IntValue(val value: Int) : QueryValue() {
    override fun toString(): String {
        return "$value"
    }
}

data class BooleanValue(val value: Boolean) : QueryValue() {
    override fun toString(): String {
        return "$value"
    }
}

class CompoundValue(vararg val values: QueryValue): QueryValue() {
    override fun toString(): String {
        return "[${values.joinToString(",")}]"
    }
}
