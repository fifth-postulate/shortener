package nl.fifthpostulate.shortener.service.couchdb

import com.fasterxml.jackson.annotation.JsonProperty

open class Document(@param:JsonProperty("_id") @get:JsonProperty("_id") val id: String? = null, val revision: String? = null, var attachments: Map<String, Attachment>? = null)

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


class ResultSet<Key, Value>(@JsonProperty("total_rows") val totalRows: Int, val offset: Int, val rows: List<Row<Key, Value>>)
class Row<Key, Value> {
    val id: String? = null
    val key: Key? = null
    val value: Value? = null
}
