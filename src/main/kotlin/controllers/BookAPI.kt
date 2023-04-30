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

    /**
     * The function formats a list of books into a string with their titles, IDs, descriptions, and authors.
     *
     * @param booksToFormat A list of Book objects that need to be formatted into a string.
     * @return a formatted string that contains information about a list of books. The string includes the title, ID,
     * description, and authors of each book in the list.
     */
    private fun formatListString(booksToFormat: List<Book>): String {
        val sb = StringBuilder()
        for ((index, book) in booksToFormat.withIndex()) {
            sb.append("${index + 0}: ")
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

    /**
     * The function adds a book to a list of books and returns a boolean value indicating whether the addition was
     * successful.
     *
     * @param book The parameter "book" is of type "Book", which means it is an object of the "Book" class. This function
     * adds the "book" object to a list of books and returns a boolean value indicating whether the addition was successful
     * or not.
     * @return A boolean value is being returned. The method `add` is adding a `Book` object to a collection of books and
     * returning `true` if the addition was successful, and `false` otherwise.
     */
    fun add(book: Book) = books.add(book)

    /**
     * The function lists all books stored or returns "no books stored" if the list is empty.
     */
    fun listAllBooks(): String =
        if (books.isEmpty()) "no books stroed"
        else formatListString(books)

    /**
     * The function returns the number of books in a collection.
     *
     * @return The function `numberOfBooks()` is returning an integer value which represents the number of books in the
     * `books` array.
     */
    fun numberOfBooks(): Int {
        return books.size
    }

    /**
     * The function "findBook" returns a book at a given index if it exists in the list of books, otherwise it returns
     * null.
     *
     * @param index The index parameter is an integer value representing the index of the book to be retrieved from the
     * books list.
     * @return The function `findBook` is returning a `Book` object if the index provided is valid and the `books` list
     * contains an element at that index. If the index is invalid or the list is empty, the function returns `null`.
     */
    fun findBook(index: Int): Book? {
        return if (isValidListIndex(index, books)) {
            books[index]
        } else null
    }

    // method to check if an index is valid in a list.
    /**
     * The function checks if a given index is a valid index for a given list.
     *
     * @param index The index parameter is an integer value representing the index of an element in the list.
     * @param list The "list" parameter is a List of type Any, which means it can contain elements of any data type.
     * @return The function `isValidListIndex` returns a boolean value indicating whether the given index is a valid index
     * for the given list.
     */
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    /**
     * The function checks if a given index is valid for a list of books.
     *
     * @param index The `index` parameter is an integer value representing the index of an element in a list.
     * @return The function `isValidIndex` is returning a boolean value. It is returning `true` if the `index` parameter is
     * a valid index for the `books` list, and `false` otherwise. The validity of the index is determined by the
     * `isValidListIndex` function, which is not shown in the code snippet provided.
     */
    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, books)
    }
    /*
   * First check if the Book arraylist is empty or not
   * Then loop through the ArrayList also checking the isBookArchived variable
   * If isBookArchived is false then add that Book to the list that will be returned.
   * */
    /**
     * This function returns a formatted string of active books or a message if there are no active books.
     */
    fun listActiveBooks(): String =
        if (numberOfActiveBooks() == 0) "No active Book stored"
        else formatListString(books.filter { Book -> !Book.isBookArchived })

    // method for listing all the Archived books
    /*
    * First check if the books arraylist is empty or not; if so return No books stored
    * Then loop through the ArrayList also checking the isBookArchived variable
    * If isBookArchived is true then add that book to the list that will be returned.
    * */
    /**
     * This Kotlin function lists archived books if there are any, otherwise it returns a message indicating that there are
     * no archived books stored.
     */
    fun listArchivedBooks(): String =
        if (numberOfArchivedBooks() == 0) "No archived books stored"
        else formatListString(books.filter { book -> book.isBookArchived })

    /**
     * This function lists books by a selected ID.
     *
     * @param Id Id is an integer parameter representing the unique identifier of a book. The function uses this parameter
     * to filter the list of books and return a formatted string containing the books with the specified Id.
     */
    fun listBooksBySelectedId(Id: Int): String =
        if (books.isEmpty()) "No books stored"
        else {
            val listOfBooks = formatListString(books.filter { book -> book.bookId == Id })
            if (listOfBooks.equals("")) "No books stored with Id: $Id"
            else "${numberOfBooksById(Id)} books with ID ($Id): \n $listOfBooks\""
        }

    /**
     * This Kotlin function returns the number of archived books in a collection of books.
     */
    fun numberOfArchivedBooks(): Int = books.count { book: Book -> book.isBookArchived }

    /**
     * This Kotlin function returns the number of active books by counting the books that are not archived.
     */
    fun numberOfActiveBooks(): Int = books.count { book: Book -> !book.isBookArchived }

    /**
     * This Kotlin function returns the number of books in a list with a specific ID.
     *
     * @param Id Id is an integer parameter representing the unique identifier of a book. The function `numberOfBooksById`
     * takes this parameter and returns the number of books in the `books` collection that have the same bookId as the
     * given Id.
     */
    fun numberOfBooksById(Id: Int): Int =
        if (books.isEmpty()) 0
        else books.count { it.bookId == Id }

    /**
     * This Kotlin function deletes a book from a list of books at a specified index if the index is valid.
     *
     * @param indexToDelete An integer representing the index of the book to be deleted from the list of books.
     * @return The function `deleteBook` is returning a `Book` object if the index to delete is valid and the book is
     * successfully removed from the `books` list. If the index is invalid, it returns `null`.
     */
    fun deleteBook(indexToDelete: Int): Book? {
        return if (isValidListIndex(indexToDelete, books)) {
            books.removeAt(indexToDelete)
        } else null
    }

    /**
     * The function updates a book in an ArrayList based on the index and book details passed as parameters.
     *
     * @param indexToUpdate an integer representing the index of the book in the ArrayList that needs to be updated.
     * @param book Book object containing the updated details for the book to be updated in the ArrayList. It has
     * properties such as bookTitle, bookId, and bookDesc.
     * @return a boolean value indicating whether the update was successful or not. If the book was found and the update
     * was made, the function returns true. If the book was not found, the function returns false.
     */
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

    /**
     * The function archives a book by its index and returns a boolean value indicating whether the operation was
     * successful or not.
     *
     * @param indexToArchive An integer representing the index of the book to be archived in a collection or list of books.
     * @return The function `archiveBookByIndex` returns a Boolean value. It returns `true` if the book with the given
     * index is found and successfully archived, and `false` if the book is not found.
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

    /**
     * The function adds an author to a book if the book exists, otherwise it prints an error message.
     *
     * @param bookTitle A String representing the title of the book to which the author needs to be added.
     * @param author The `author` parameter is an instance of the `Author` class, representing the author that we want to
     * add to a book.
     * @param find search for the book with the specified title
     * @param let the code block where we add the author to the book's list of authors and print a success message
     * @param find  print an error message
     *
     */
    fun addAuthorToBook(bookTitle: String, author: Author) {
        books.find { it.bookTitle == bookTitle }?.let { book ->
            book.authors.add(author)
            println("Author added successfully to book: $bookTitle")
        } ?: run {
            println("Book not found with title: $bookTitle")
        }
    }

    /**
     * This Kotlin function searches for books by author name and returns a formatted list of matching books or a message
     * if no books are found.
     *
     * @param searchString The parameter `searchString` is a string that represents the name of the author that the user
     * wants to search for in the list of books. The function will return a string that contains a list of all the books
     * that have at least one author whose name contains the `searchString`.
     * @return a string that contains either a list of books that match the search criteria (if any are found), or a
     * message indicating that no books were found.
     */
    fun searchByAuthor(searchString: String): String {
        return if (numberOfBooks() == 0) "No books stored"
        else {
            var listOfBooks = ""
            for (book in books) {
                for (item in book.authors) {
                    if (item.authorName.contains(searchString, ignoreCase = true)) {
                        listOfBooks += formatListString(listOf(book))
                    }
                }
            }
            if (listOfBooks == "") "No book found for: $searchString"
            else listOfBooks
        }
    }

    fun countBooksByAuthor(authorName: String): Int {
        var count = 0
        for (book in books) {
            for (item in book.authors) {
                if (item.authorName.equals(authorName, ignoreCase = true)) {
                    count++
                }
            }
        }
        println("There are $count books associated with $authorName.")
        return count
    }
}
