package nl.fifthpostulate.shortener.config

import nl.fifthpostulate.shortener.domain.DataSheet
import nl.fifthpostulate.shortener.repository.*
import org.springframework.boot.context.properties.EnableConfigurationProperties
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
        return Chained<DataSheet>(repositories = *arrayOf(repository), fallback=Suggestion(properties.suggestionUrl))
    }
}

enum class RepositoryType {
    InMemory
}


