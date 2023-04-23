package models

import kotlinx.serialization.Serializable

@Serializable
class Book(
    var bookTitle: String,
    var bookId: Int,
    var bookDesc: String,
    var isBookArchived: Boolean,
    val authors: MutableList<Author> = mutableListOf()
)
