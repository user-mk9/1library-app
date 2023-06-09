import controllers.BookAPI
import models.Author
import models.Book
import mu.KotlinLogging
import persistance.CBORSerializer
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit

private val logger = KotlinLogging.logger {}
// private val bookAPI = BookAPI(XMLSerializer(File("books.xml")))
// private val bookAPI = BookAPI(JSONSerializer(File("books.json")))
private val bookAPI = BookAPI(CBORSerializer(File("books.cbor")))

fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu(): Int {
    return readNextInt(
        """
         > ——————————————————————————————————
         > |      LIBRARY MANAGER APP       |
         > ——————————————————————————————————
         > |           BOOK MENU            |
         > |   1) Add a Book                |
         > |   2) Update a Book             |
         > |   3) Delete a Book             |
         > |   4) Archive a Book            |
         > |   5) Search title of a Book    |
         > |   6) More Options              |
         > |   20) Save books               |
         > |   21) Load books               |
         > ——————————————————————————————————
         > |   0) Exit                      |
         > ——————————————————————————————————
         >  Enter Option: """.trimMargin(">")
    )
}

fun subMenu(): Int {
    return readNextInt(
        """ 
         > —————————————————————————————————
         > |       MORE OPTIONS MENU        |
         > —————————————————————————————————
         > |      LIST BOOK SUB-MENU        |
         > |   1) List all books            |
         > |   2) List active books         |
         > |   3) List archived books       |
         > |   4) List books by ID          |
         > —————————————————————————————————
         > |        EXTRA FEATURES          |
         > |   5) Add author to book        |
         > |   6) Search by author to book  |
         > |   7) Counts books by author    |
         > |   0) Exit                      |
         > —————————————————————————————————
         >     Enter Option: """.trimMargin(">")

    )
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1 -> addBook()
            2 -> updateBook()
            3 -> deleteBook()
            4 -> archiveBook()
            5 -> SearchBooks()
            6 -> moreOptionsSubmenu()
            0 -> exitApp()
            20 -> save()
            21 -> load()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}

fun moreOptionsSubmenu() {
    do {
        val option = subMenu()
        when (option) {
            1 -> listBooks()
            2 -> listActiveBooks()
            3 -> listArchivedBooks()
            4 -> println(bookAPI.listBooksBySelectedId(readNextInt("Please Enter a Book ID to List: ")))
            5 -> addAuthor()
            6 -> searchAuthor()
            7 -> countAuthorBooks()
            0 -> runMenu()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}

fun addBook() {
    val bookTitle = readNextLine("Enter the book title : ")
    val bookId = readNextInt("Enter book ID (1(Education), 2(Sports), 3(Fiction), 4(Other)) : ")
    if (bookId < 1 || bookId > 4) {
        println("Invalid book ID entered: $bookId. Please enter a number between 1 and 4.")
        return
    }
    val bookDesc = readNextLine("Enter the book Description : ")
    val newAdd = bookAPI.add(Book(bookTitle, bookId, bookDesc, false))

    if (newAdd) {
        println("Add Successful")
    } else {
        println("Add failed")
    }
}

fun listBooks() {
    println(bookAPI.listAllBooks())
}

/**
 * The function updates a book in a list of books by taking user input for the book's title, ID, and description.
 */
fun updateBook() {
    listBooks()
    if (bookAPI.numberOfBooks() > 0) {
        // only ask the user to choose the note if books exist
        val indexToUpdate = readNextInt("Enter the index of the book to update: ")
        if (bookAPI.isValidIndex(indexToUpdate)) {
            val bookTitle = readNextLine("Enter a book title : ")
            val bookId = readNextInt("Enter book ID (1(Education), 2(Sports), 3(Fiction), 4(Other)) : ")
            val bookDesc = readNextLine("Enter the book description : ")

            // pass the index of the book and the new book details to BookAPI for updating and check for success.
            if (bookAPI.updateBook(indexToUpdate, Book(bookTitle, bookId, bookDesc, false))) {
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no books for this index number")
        }
    }
}

fun deleteBook() {
    listBooks()
    if (bookAPI.numberOfBooks() > 0) {
        // only ask the user to choose the book to delete if books exist
        val indexToDelete = readNextInt("Enter the index of the book to delete: ")
        // pass the index of the book to BookAPI for deleting and check for success.
        val bookToDelete = bookAPI.deleteBook(indexToDelete)
        if (bookToDelete != null) {
            println("Delete Successful! Deleted book: ${bookToDelete.bookTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun exitApp() {
    println("Exiting...bye")
    exit(0)
}

fun save() {
    try {
        bookAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        bookAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

/**
 * The function archives an active book by its index if it exists.
 */
fun archiveBook() {
    // only list active books, as we don't need to archive books that have been done so already
    println(bookAPI.listActiveBooks())
    if (bookAPI.numberOfActiveBooks() > 0) {
        // The index of the book to be archived
        var indexToArchive: Int = readNextInt("Please enter an index of the book: ")
        if (bookAPI.isValidIndex(indexToArchive)) {
            bookAPI.archiveBookByIndex(indexToArchive)
            println("The book has successfully been archived")
        } else {
            println("Index of book is invalid")
        }
    } else {
        println("There is no active books")
    }
}

fun listActiveBooks() {
    println(bookAPI.listActiveBooks())
}

fun listArchivedBooks() {
    println(bookAPI.listArchivedBooks())
}

fun SearchBooks() {
    val searchTitle = readNextLine("Enter the book title to search by: ")
    val searchResults = bookAPI.searchByTitle(searchTitle)
    if (searchTitle.isEmpty()) {
        println("No books found")
    } else {
        println(searchResults)
    }
}

/**
 * The function searches for books by author and displays the results.
 */
fun searchAuthor() {
    val searchAuthor = readNextLine("Enter the author to search by: ")
    val searchResults = bookAPI.searchByAuthor(searchAuthor)
    if (searchResults.isEmpty()) {
        println("No author found")
    } else {
        println(searchResults)
    }
}

/**
 * The function adds an author to a book in a book API.
 */
fun addAuthor() {
    val bookTitle = readNextLine("Please enter the book title: ")
    val authorName = readNextLine("Please enter the author name: ")
    val author = Author(authorName) // Create an Author object with the entered author namee
    bookAPI.addAuthorToBook(bookTitle, author)
}

/**
 * This function prompts the user to enter an author name and then calls a book API to count the number of books written by
 * that author.
 */
fun countAuthorBooks() {
    val authorName = readNextLine("Please enter the author name: ")
    bookAPI.countBooksByAuthor(authorName)
}
