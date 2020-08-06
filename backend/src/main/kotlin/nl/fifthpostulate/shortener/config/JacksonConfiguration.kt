package nl.fifthpostulate.shortener.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import nl.fifthpostulate.shortener.jackson.*
import org.springframework.context.annotation.*
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfiguration {
    @Bean
    fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.registerModule(Jdk8Module())
        mapper.registerModule(KotlinModule())
        mapper.registerModule(singleCompoundKey())
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return mapper
    }

    private fun singleCompoundKey(): SimpleModule {
        val module = SimpleModule()
        module.addDeserializer(CompoundKey::class.java, CompoundKeyDeserializer(StringKey(), IntKey()))
        return module
    }


}
