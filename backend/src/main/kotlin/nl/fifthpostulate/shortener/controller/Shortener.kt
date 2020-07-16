package nl.fifthpostulate.shortener.controller

import nl.fifthpostulate.shortener.domain.DataSheet
import nl.fifthpostulate.shortener.repository.ShortRepository
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
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
}

@Component
class HardCoded : ShortRepository {
    override fun load(short: String): DataSheet? {
        return DataSheet(short, "https://google.com")
    }
}
