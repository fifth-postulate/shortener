package nl.fifthpostulate.shortener.config

import nl.fifthpostulate.shortener.repository.Always
import nl.fifthpostulate.shortener.repository.Chained
import nl.fifthpostulate.shortener.repository.InMemory
import nl.fifthpostulate.shortener.repository.ShortRepository
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties
class Configuration {

    @Bean
    fun repository(properties: RepositoryProperties): ShortRepository {
        val repository = when (RepositoryType.valueOf(properties.type)) {
            RepositoryType.InMemory -> InMemory()
        }
        return Chained(repository, Always(properties.alwaysUrl))
    }
}

enum class RepositoryType {
    InMemory
}


