package models

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    var bookTitle: String,
    var bookId: Int,
    var bookDesc: String,
    var isBookArchived: Boolean,
    /* `val authors: MutableList<Author> = mutableListOf()` is declaring a property named `authors` of type
    `MutableList<Author>` with an initial value of an empty mutable list. This property is immutable (`val` keyword)
    which means that once it is initialized, it cannot be reassigned to a new value. The `MutableList` allows for
    adding, removing, and modifying elements in the list. */
    val authors: MutableList<Author> = mutableListOf()
)
{
    private var lastItemId = 0
    private fun getItemId() = lastItemId++

    fun numberOfItems() = authors.size
}