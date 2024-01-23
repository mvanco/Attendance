package eu.matoosh.attendance.seznam.data

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val publishedDate: String,
    val description: String? = null,
    val thumbnailUrl: String? = null,
    val imageUrl: String? = null,
    val googlePlayLink: String? = null
)
