package nl.fifthpostulate.shortener.config

import nl.fifthpostulate.shortener.repository.HardCoded
import nl.fifthpostulate.shortener.repository.ShortRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Configuration {
    @Bean
    fun repository() : ShortRepository {
        return HardCoded()
    }
}
