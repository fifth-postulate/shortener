package nl.fifthpostulate.shortener.store

interface Store {
    fun claim(candidate: String): StoreResult
}

sealed class StoreResult(){}
class Success(): StoreResult(){}
class Failure(): StoreResult(){}
