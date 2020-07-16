package nl.fifthpostulate.shortener.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("repository")
data class RepositoryProperties(val type: String, val alwaysUrl: String) {
}
