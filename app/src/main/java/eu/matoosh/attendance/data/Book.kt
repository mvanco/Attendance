package eu.matoosh.attendance.data

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val publishedDate: String,
    val description: String? = null,
    val thumbnailUrl: String? = null,
    val imageUrl: String? = null,
    val googlePlayLink: String? = null,
    val webReaderLink: String? = null
)
