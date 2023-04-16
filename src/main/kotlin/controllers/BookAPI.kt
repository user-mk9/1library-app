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

    /*
   * First check if the Book arraylist is empty or not
   * Then loop through the ArrayList also checking the isBookArchived variable
   * If isBookArchived is false then add that Book to the list that will be returned.
   * */
    fun listActiveBooks(): String {
        return if (numberOfActiveBooks() == 0) {
            "No active Books stored"
        } else {
            var listOfActiveBooks = ""
            for (book in books) {
                if (!book.isBookArchived) {
                    listOfActiveBooks += "${books.indexOf(book)}: $book \n"
                }
            }
            listOfActiveBooks
        }
    }

    // method for listing all the Archived books
    /*
    * First check if the books arraylist is empty or not; if so return No books stored
    * Then loop through the ArrayList also checking the isBookArchived variable
    * If isBookArchived is true then add that book to the list that will be returned.
    * */
    fun listArchivedBooks(): String {
        return if (numberOfArchivedBooks() == 0) {
            "No archived Books stored"
        } else {
            var listOfArchivedBooks = ""
            for (book in books) {
                if (book.isBookArchived) {
                    listOfArchivedBooks += "${books.indexOf(book)}: $books \n"
                }
            }
            listOfArchivedBooks
        }
    }

    fun listBooksBySelectedId(Id: Int): String {
        return if (books.isEmpty()) {
            "No books stored"
        } else {
            var listOfBooks = ""
            for (i in books.indices) {
                if (books[i].bookId == Id) {
                    listOfBooks +=
                        """$i: ${books[i]}
                        """.trimIndent()
                }
            }
            if (listOfBooks.equals("")) {
                "No books with Id: $Id"
            } else {
                "${listBooksBySelectedId(Id)} books with ID $Id: $listOfBooks"
            }
        }
    }

    fun numberOfArchivedBooks(): Int {
        //return Books.stream().filter { obj: Book -> obj.isBookArchived }.count().toInt()
        var counter = 0
        for (book in books) {
            if (book.isBookArchived) {
                counter++
            }
        }
        return counter
    }

    fun numberOfActiveBooks(): Int {
        //return Books.stream().filter { p: Book -> !p.isBookArchived }.count().toInt()
        var counter = 0
        for (book in books) {
            if (!book.isBookArchived) {
                counter++
            }
        }
        return counter
    }

    fun numberOfBooksById(Id: Int): Int {
        //return books.stream().filter { p: Book -> p.bookId == Id }.count().toInt()
        var counter = 0
        for (book in books) {
            if (book.bookId == Id) {
                counter++
            }
        }
        return counter
    }
}