package nl.fifthpostulate.shortener.config

import com.fasterxml.jackson.databind.ObjectMapper
import nl.fifthpostulate.shortener.service.couchdb.ConnectionProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.util.Base64Utils
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class RestTemplateConfiguration @Autowired constructor(
        private val objectMapper: ObjectMapper,
        @Value("\${http.timeout}") val httpTimeout: Int,
        val properties: ConnectionProperties
) {
    @Bean
    fun restTemplate(): RestTemplate {
        val converters = listOf(
                MappingJackson2HttpMessageConverter(objectMapper)
        )
        val base64UsernameAndPassword =
                Base64Utils.encodeToString("${properties.username}:${properties.password}".toByteArray())

        return RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(httpTimeout.toLong()))
                .setReadTimeout(Duration.ofMillis(httpTimeout.toLong()))
                .additionalMessageConverters(converters)
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("content-type", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Basic $base64UsernameAndPassword")
                .build()
    }
}
