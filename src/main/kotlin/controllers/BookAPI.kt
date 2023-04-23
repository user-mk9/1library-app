package controllers

import models.Author
import models.Book
import persistance.Serializer

class BookAPI(serializerType: Serializer) {

    private var books = ArrayList<Book>()
    private var serializer: Serializer = serializerType
    // Load books
    @Throws(Exception::class)
    fun load() {
        books = serializer.read() as ArrayList<Book>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(books)
    }

    private fun formatListString(booksToFormat: List<Book>): String {
        val sb = StringBuilder()
        for ((index, book) in booksToFormat.withIndex()) {
            sb.append("Title: ${book.bookTitle}, ")
            sb.append("ID: ${book.bookId}, ")
            sb.append("Description: ${book.bookDesc}, ")
            sb.append("Authors: ")
            for ((authorIndex, author) in book.authors.withIndex()) {
                sb.append("${author.authorName}")
                if (authorIndex < book.authors.size - 1) {
                    sb.append(", ")
                }
            }
            sb.append("\n")
        }
        return sb.toString()
    }

    fun add(book: Book): Boolean {
        return books.add(book)
    }

    fun listAllBooks(): String =
        if (books.isEmpty()) "no books stroed"
        else formatListString(books)

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

    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, books)
    }
    /*
   * First check if the Book arraylist is empty or not
   * Then loop through the ArrayList also checking the isBookArchived variable
   * If isBookArchived is false then add that Book to the list that will be returned.
   * */
    fun listActiveBooks(): String =
        if (numberOfActiveBooks() == 0) "No active Book stored"
        else formatListString(books.filter { Book -> !Book.isBookArchived })

    // method for listing all the Archived books
    /*
    * First check if the books arraylist is empty or not; if so return No books stored
    * Then loop through the ArrayList also checking the isBookArchived variable
    * If isBookArchived is true then add that book to the list that will be returned.
    * */
    fun listArchivedBooks(): String =
        if (numberOfArchivedBooks() == 0) "No archived books stored"
        else formatListString(books.filter { book -> book.isBookArchived })

    fun listBooksBySelectedId(Id: Int): String =
        if (books.isEmpty()) "No books stored"
        else {
            val listOfBooks = formatListString(books.filter { book -> book.bookId == Id })
            if (listOfBooks.equals("")) "No books stored with Id: $Id"
            else "${numberOfBooksById(Id)} books with ID ($Id): \n $listOfBooks\""
        }
    fun numberOfArchivedBooks(): Int = books.count { book: Book -> book.isBookArchived }

    fun numberOfActiveBooks(): Int = books.count { book: Book -> !book.isBookArchived }

    fun numberOfBooksById(Id: Int): Int =
        if (books.isEmpty()) 0
        else books.count { it.bookId == Id }

    fun deleteBook(indexToDelete: Int): Book? {
        return if (isValidListIndex(indexToDelete, books)) {
            books.removeAt(indexToDelete)
        } else null
    }

    fun updateBook(indexToUpdate: Int, book: Book?): Boolean {
        // find the book object by the index number
        val foundBook = findBook(indexToUpdate)

        // if the book exists, use the book details passed as parameters to update the found book in the ArrayList.
        if ((foundBook != null) && (book != null)) {
            foundBook.bookTitle = book.bookTitle
            foundBook.bookId = book.bookId
            foundBook.bookDesc = book.bookDesc
            return true
        }
        // if the book was not found, return false, indicating that the update was not successful
        return false
    }
    /*
Archive a book
get index of book passed, get book by the index and set isBookArchived = true
 */
    fun archiveBookByIndex(indexToArchive: Int): Boolean {
        val bookToArchive = findBook(indexToArchive)
        if (bookToArchive != null) {
            bookToArchive.isBookArchived = true
            return true
        }
        return false
    }

    fun searchByTitle(searchString: String) =
        formatListString(
            books.filter { book -> book.bookTitle.contains(searchString, ignoreCase = true) }
        )

    fun addAuthorToBook(bookTitle: String, author: Author) {
        val book = books.find { it.bookTitle == bookTitle }
        if (book != null) {
            book.authors.add(author)
            println("Author added successfully to book: $bookTitle")
        } else {
            println("Book not found with title: $bookTitle")
        }
    }
}
