package nl.fifthpostulate.shortener.config

import nl.fifthpostulate.shortener.domain.DataSheet
import nl.fifthpostulate.shortener.repository.Chained
import nl.fifthpostulate.shortener.repository.InMemory
import nl.fifthpostulate.shortener.repository.ShortRepository
import nl.fifthpostulate.shortener.repository.Suggestion
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
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties
class Configuration {

    @Bean
    fun repository(properties: RepositoryProperties): ShortRepository<DataSheet> {
        val repository = when (RepositoryType.valueOf(properties.type)) {
            RepositoryType.InMemory -> InMemory()
        }
        return Chained<DataSheet>(repositories = *arrayOf(repository), fallback = Suggestion(properties.suggestionUrl))
    }

    @Bean
    fun short(properties: ShortenStrategyProperties, context: ApplicationContext): ShortenStrategy {
        val strategy = when (ShortenStrategyType.valueOf(properties.type)) {
            ShortenStrategyType.InOrder -> Sequential("aaa")
            ShortenStrategyType.StoreBacked -> {
                val service = context.getBean(CouchDBService::class.java)
                val last = lastShort(service).withDefault("aaa")
                StoreBacked(Sequential(successor(last)), CouchDB(service))
            }
        }
        return strategy
    }
}

enum class RepositoryType {
    InMemory
}

enum class ShortenStrategyType {
    InOrder,
    StoreBacked
}

fun lastShort(service: CouchDBService): Result<Unit, String> {
    val resultSet = service.view("_design/short/_view/shorts", Query("descending", "true"), Query("limit", "1"))
    return when(resultSet) {
        is Success -> Success(resultSet.data.rows[0].key)
        is Failure -> Failure(Unit)
    }
}
class ShortDocument(val id:String, val key: String, val value: Int)
