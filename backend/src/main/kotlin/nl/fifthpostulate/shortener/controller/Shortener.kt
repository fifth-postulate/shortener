package nl.fifthpostulate.shortener.controller

import nl.fifthpostulate.shortener.domain.DataSheet
import nl.fifthpostulate.shortener.repository.*
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@CrossOrigin("*")
@RestController
class Shortener(val repository: ShortRepository<DataSheet>) {
    @GetMapping("/{short}")
    fun redirect(@PathVariable short: String, response: HttpServletResponse) {
        val dataSheet = repository.load(short)
        response.sendRedirect(dataSheet.url)
    }

    @PutMapping("/{short}")
    fun store(@PathVariable short: String, @RequestBody command: ShortCommand): Result {
        val dataSheet = DataSheet(short, command.url)
        return repository.save(dataSheet)
    }

    @PostMapping("/")
    fun storeWithGeneratedShort(@RequestBody command: ShortCommand): Result {
        val short = "generatedShort"
        val dataSheet = DataSheet(short, command.url)
        return repository.save(dataSheet)
    }
}

data class ShortCommand(val url: String)
