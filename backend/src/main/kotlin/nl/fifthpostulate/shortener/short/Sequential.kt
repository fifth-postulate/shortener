package nl.fifthpostulate.shortener.short

class Sequential(var next: String = "a", val last: Char = 'z') : ShortenStrategy {
    override fun of(url: String): String {
        val short = next
        next = successor(next, last)
        return short
    }
}

fun successor(current: String, last: Char = 'z'): String {
    var characters = current.toCharArray()
    var index = 0
    while (index < characters.size && characters[index] == last) {
        characters[index] = 'a'
        index++
    }
    if (index < characters.size) {
        characters[index] = characters[index].inc()
    } else {
        val copy = characters.copyOf(characters.size + 1)
        copy[index] = 'a'
        characters = copy
    }
    return characters.joinToString("")
}
