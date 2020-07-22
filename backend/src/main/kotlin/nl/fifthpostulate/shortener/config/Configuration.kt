package nl.fifthpostulate.shortener.config

import nl.fifthpostulate.shortener.domain.DataSheet
import nl.fifthpostulate.shortener.repository.*
import nl.fifthpostulate.shortener.result.Failure
import nl.fifthpostulate.shortener.result.Result
import nl.fifthpostulate.shortener.result.Success
import nl.fifthpostulate.shortener.service.couchdb.CouchDBService
import nl.fifthpostulate.shortener.service.couchdb.Query
import nl.fifthpostulate.shortener.short.Sequential
import nl.fifthpostulate.shortener.short.ShortenStrategy
import nl.fifthpostulate.shortener.short.StoreBacked
import nl.fifthpostulate.shortener.short.successor
import nl.fifthpostulate.shortener.store.CouchDB
import nl.fifthpostulate.shortener.store.Event
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties
class Configuration {

    @Bean
    fun repository(properties: RepositoryProperties, context: ApplicationContext): ShortRepository<DataSheet> {
        val repository = when (RepositoryType.valueOf(properties.type)) {
            RepositoryType.InMemory -> InMemory()
            RepositoryType.Store -> {
                val store = context.getBean(CouchDB::class.java)
                Store(store)
            }
        }
        return Chained<DataSheet>(repositories = *arrayOf(repository), fallback = Suggestion(properties.suggestionUrl))
    }

    @Bean
    fun short(properties: ShortenStrategyProperties, context: ApplicationContext): ShortenStrategy {
        val strategy = when (ShortenStrategyType.valueOf(properties.type)) {
            ShortenStrategyType.InOrder -> Sequential("aaa")
            ShortenStrategyType.StoreBacked -> {
                val store = context.getBean(CouchDB::class.java)
                val last = lastShort(store.service).withDefault("zz")
                StoreBacked(Sequential(successor(last)), store)
            }
        }
        return strategy
    }
}

enum class RepositoryType {
    InMemory,
    Store
}

enum class ShortenStrategyType {
    InOrder,
    StoreBacked
}

fun lastShort(service: CouchDBService): Result<Unit, String> {
    val resultSet = service.view("_design/short/_view/shorts", Query("descending", true), Query("limit", 1))
    return when(resultSet) {
        is Success -> {
            val short = if (resultSet.data.rows.size > 0) { resultSet.data.rows[0].key } else { "zz" }
            Success(short)
        }
        is Failure -> Failure(Unit)
    }
}
class ShortDocument(val id:String, val key: String, val value: Int)
class EventDocument(val id:String, val key: String, val value: Event)
