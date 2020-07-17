package nl.fifthpostulate.shortener.controller

import nl.fifthpostulate.shortener.config.RepositoryProperties
import nl.fifthpostulate.shortener.domain.DataSheet
import nl.fifthpostulate.shortener.repository.ShortRepository
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
class Shortener(val repository: ShortRepository, val repositoryProperties: RepositoryProperties) {
    @GetMapping("/{short}")
    fun redirect(@PathVariable short: String, response: HttpServletResponse) {
        val dataSheet = repository.load(short)
        val url = dataSheet?.url ?: repositoryProperties.fallBackUrl
        response.sendRedirect(url)
    }

    @PutMapping("/{short}")
    fun store(@PathVariable short: String, @RequestBody shortenRequest: ShortenRequest) {
        val dataSheet = DataSheet(short, shortenRequest.url)
        repository.save(dataSheet)
    }
}

data class ShortenRequest(val url: String){}
