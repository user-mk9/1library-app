import controllers.BookAPI
import models.Book
import mu.KotlinLogging
import persistence.JSONSerializer
import persistence.XMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val logger = KotlinLogging.logger {}
//private val bookAPI = BookAPI(XMLSerializer(File("books.xml")))
private val bookAPI = BookAPI(JSONSerializer(File("books.json")))
fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu(): Int {
    return readNextInt("""
         > ——————————————————————————————————
         > |       LIBRARY MANAGER APP      |
         > ——————————————————————————————————
         > | BOOK MENU                      |
         > |   1) Add a Book                |
         > |   2) List Books                |
         > |   3) Update a Book             |
         > |   4) Delete a Book             |
         > |   20) Save books               |
         > |   21) Load books               |
         > ——————————————————————————————————
         > |   0) Exit                      |
         > ——————————————————————————————————
         >   ==>> """.trimMargin(">"))
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addBook()
            2  -> listBooks()
            3  -> updateBook()
            4  -> deleteBook()
            0  -> exitApp()
            20 -> save()
            21 -> load()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}

fun addBook(){
    val bookTitle = readNextLine("Enter the book title : ")
    val bookId = readNextInt("Enter book ID (1(Education), 2(Sports), 3(Fiction), 4(Other)) : ")
    val bookCategory = readNextLine("Enter the book category : ")
    val newAdd = bookAPI.add(Book(bookTitle, bookId, bookCategory, false))

    if(newAdd){
        println("Add Sucessful")
    }
    else{
        println("Add failed")
    }
}

fun listBooks(){
    println(bookAPI.listAllBooks())
}

fun updateBook() {
    listBooks()
    if (bookAPI.numberOfBooks() > 0) {
        // only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the book to update: ")
        if (bookAPI.isValidIndex(indexToUpdate)) {
            val bookTitle = readNextLine("Enter a book title : ")
            val bookId = readNextInt("Enter book ID (1(Education), 2(Sports), 3(Fiction), 4(Other)) : ")
            val bookCategory = readNextLine("Enter the book category : ")

            // pass the index of the book and the new book details to BookAPI for updating and check for success.
            if (bookAPI.updateBook(indexToUpdate, Book(bookTitle, bookId, bookCategory, false))) {
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
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

fun exitApp(){
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