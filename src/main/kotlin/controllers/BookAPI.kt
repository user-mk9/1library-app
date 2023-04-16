package controllers

import models.Book

class BookAPI {

    private var books = ArrayList<Book>()

    fun add(book: Book): Boolean {
        return books.add(book)
    }

    fun listAllBooks(): String {
        return if (books.isEmpty()) {
            "No books stored"
        } else {
            var listOfBooks = ""
            for (i in books.indices) {
                listOfBooks += "${i}: ${books[i].bookTitle} \n"
            }
            listOfBooks
        }
    }

    fun numberOfBooks(): Int {
        return books.size
    }

    fun findBook(index: Int): Book? {
        return if (isValidListIndex(index, books)) {
            books[index]
        } else null
    }

    // method to check if an index is valid in a list.
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }
}