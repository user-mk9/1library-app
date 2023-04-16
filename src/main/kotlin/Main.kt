import utils.ScannerInput
import java.lang.System.exit

fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu(): Int {
    return ScannerInput.readNextInt("""
         > ——————————————————————————————————
         > |       LIBRARY MANAGER APP      |
         > ——————————————————————————————————
         > | BOOK MENU                      |
         > |   1) Add a Book                |
         > |   2) List Books                |
         > |   3) Update a Book             |
         > |   4) Delete a Book             |
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
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}

fun addBook(){
    println("You chose Add Book")
}

fun listBooks(){
    println("You chose List Books")
}

fun updateBook(){
    println("You chose Update Book")
}

fun deleteBook(){
    println("You chose Delete Book")
}

fun exitApp(){
    println("Exiting...bye")
    exit(0)
}
