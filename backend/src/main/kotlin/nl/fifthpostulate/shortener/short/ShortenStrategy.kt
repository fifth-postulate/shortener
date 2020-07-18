package nl.fifthpostulate.shortener.short

interface ShortenStrategy {
    fun of(url: String): String
}
