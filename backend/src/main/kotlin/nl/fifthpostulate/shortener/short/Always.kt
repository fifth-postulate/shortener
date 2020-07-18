package nl.fifthpostulate.shortener.short

data class Always(val short: String) : ShortenStrategy {
    override fun of(url: String): String {
        return short
    }
}
