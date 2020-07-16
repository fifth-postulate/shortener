package nl.fifthpostulate.shortener.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
class Shortener {
    @GetMapping("/")
    fun redirect(response: HttpServletResponse) {
        response.sendRedirect("https://shrt.fifth-postulate.nl")
    }
}
