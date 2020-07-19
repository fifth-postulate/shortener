package nl.fifthpostulate.shortener.service.couchdb

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("couchdb")
data class ConnectionProperties(
        val url: String,
        val username: String,
        val password: String
)
