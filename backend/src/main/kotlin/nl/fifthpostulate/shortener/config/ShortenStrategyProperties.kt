package nl.fifthpostulate.shortener.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("shortenstrategy")
data class ShortenStrategyProperties(val type: String) {}
