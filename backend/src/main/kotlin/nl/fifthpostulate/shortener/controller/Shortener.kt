package nl.fifthpostulate.shortener.controller

import nl.fifthpostulate.shortener.domain.DataSheet
import nl.fifthpostulate.shortener.repository.ShortRepository
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
class Shortener(val repository: ShortRepository) {
    @GetMapping("/{short}")
    fun redirect(@PathVariable short: String, response: HttpServletResponse) {
        val dataSheet = repository.load(short)
        if (dataSheet != null) {
            response.sendRedirect(dataSheet.url)
        } else {
            response.sendRedirect("https://shorten.fifth-postulate.nl/${short}")
        }
    }

    @PutMapping("/{short}")
    fun store(@PathVariable short: String, @RequestBody shortenRequest: ShortenRequest) {
        val dataSheet = DataSheet(short, shortenRequest.url)
        repository.save(dataSheet)
    }
}

data class ShortenRequest(val url: String){}
