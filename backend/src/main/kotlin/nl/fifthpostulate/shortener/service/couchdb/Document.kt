package nl.fifthpostulate.shortener.service.couchdb

import com.fasterxml.jackson.annotation.JsonProperty

abstract class Document {
    abstract val id: String?
    abstract val revision: String?
    abstract val attachments: Map<String, Attachment>?
}

data class Attachment(
        @JsonProperty("content_type")
        val contentType: String,
        val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Attachment

        if (contentType != other.contentType) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contentType.hashCode()
        result = 37 * result + data.contentHashCode()
        return result
    }
}
