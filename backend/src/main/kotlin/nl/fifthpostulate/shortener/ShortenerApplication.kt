package nl.fifthpostulate.shortener

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("nl.fifthpostulate.shortener")
class ShortenerApplication

fun main(args: Array<String>) {
	runApplication<ShortenerApplication>(*args)
}
