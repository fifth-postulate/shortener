package nl.fifthpostulate.shortener.domain

data class DataSheet(val short: String, val url: String, private var accessed: Int = 0) {
    fun loaded() {
        accessed += 1
    }
}
