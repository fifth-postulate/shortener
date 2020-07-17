package nl.fifthpostulate.shortener.repository

import com.fasterxml.jackson.annotation.JsonUnwrapped
import nl.fifthpostulate.shortener.domain.DataSheet

interface ShortRepository<T> {
    fun load(short: String): T
    fun save(dataSheet: DataSheet): Result
}

sealed class Result(val type: String){}
class Success(@JsonUnwrapped val dataSheet: DataSheet): Result("success") {}
class Failure(val reason: String): Result("failure") {}
