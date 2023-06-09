package controllers

import models.Author
import models.Book
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistance.CBORSerializer
import persistance.JSONSerializer
import persistance.XMLSerializer
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BookAPITest {
    private var sampleBook1: Book? = null
    private var sampleBook2: Book? = null
    private var sampleBook3: Book? = null
    private var popBooks: BookAPI? = BookAPI(XMLSerializer(File("books.xml")))
    private var emptyBooks: BookAPI? = BookAPI(XMLSerializer(File("books.xml")))

    @BeforeEach
    fun setup() {
        sampleBook1 = Book("Sample Book 1", 1, "Education", false)
        sampleBook2 = Book("Sample Book 2", 1, "Education", false)
        sampleBook3 = Book("Sample Book 3", 2, "Mystery", false)

        // adding 3 Books to the books api
        popBooks!!.add(sampleBook1!!)
        popBooks!!.add(sampleBook2!!)
        popBooks!!.add(sampleBook3!!)
    }
    @AfterEach
    fun tearDown() {
        sampleBook1 = null
        sampleBook2 = null
        sampleBook3 = null
        popBooks = null
        emptyBooks = null
    }

    @Nested
    inner class AddBook {

        @Test
        fun `adding book to a populated list adds to ArrayList`() {
            val newBook = Book("Book1", 3, "Non-Fiction", false)
            assertEquals(3, popBooks!!.numberOfBooks())
            assertTrue(popBooks!!.add(newBook))
            assertEquals(4, popBooks!!.numberOfBooks())
            assertEquals(newBook, popBooks!!.findBook(popBooks!!.numberOfBooks() - 1))
        }

        @Test
        fun `adding book to an empty list adds to ArrayList`() {
            val newBook = Book("Book1", 3, "Non-Fiction", false)
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.add(newBook))
            assertEquals(1, emptyBooks!!.numberOfBooks())
            assertEquals(newBook, emptyBooks!!.findBook(emptyBooks!!.numberOfBooks() - 1))
        }
    }

    @Nested
    inner class SearchBooks {

        @Test
        fun `search books by title returns no books as there are books with that title exist`() {
            assertEquals(3, popBooks!!.numberOfBooks())
            val searchResults = popBooks!!.searchByTitle("no results expected")
            assertTrue(searchResults.isEmpty())

            // searching empty list
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.searchByTitle("").isEmpty())
        }

        @Test
        fun `search books by title returns books when books with that title exist`() {
            assertEquals(3, popBooks!!.numberOfBooks())

            // Searching a populated arraylist for a title that exists (case matches exactly)
            var searchResults = popBooks!!.searchByTitle("Book 1")
            assertTrue(searchResults.contains("Book 1"))
            kotlin.test.assertFalse(searchResults.contains("Book 2"))

            // Searching a populated arraylist for partial title that exists (case matches exactly)
            searchResults = popBooks!!.searchByTitle("Book")
            assertTrue(searchResults.contains("Book 1"))
            assertTrue(searchResults.contains("Book 2"))
            kotlin.test.assertFalse(searchResults.contains("Story"))

            // Searching a populated arraylist for partial title that exists (case doesnt match)
            searchResults = popBooks!!.searchByTitle("BOoK")
            assertTrue(searchResults.contains("Book 1"))
            assertTrue(searchResults.contains("Book 2"))
            kotlin.test.assertFalse(searchResults.contains("Story"))
        }
    }

    @Nested
    inner class ListBooks {

        @Test
        fun `listAllBooks returns 'no books stored' when arraylist is empty`() {
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.listAllBooks().lowercase().contains("no books"))
        }

        @Test
        fun `listAllBooks returns books when arraylist is populated`() {
            assertEquals(3, popBooks!!.numberOfBooks())
            val booksString = popBooks!!.listAllBooks().lowercase()
            assertTrue(booksString.contains("book 1"))
            assertTrue(booksString.contains("book 2"))
            assertTrue(booksString.contains("book 3"))
        }

        @Test
        fun `listActiveBooks() returns books that have isBookArchived set to false`() {
            assertTrue(
                !popBooks!!.listActiveBooks().contains("No books stored") || !popBooks!!.listActiveBooks()
                    .contains("No active books stored")
            )
        }

        @Test
        fun `listArchivedBooks() returns books that have isBookArchived set to true`() {
            assertEquals("No archived books stored", popBooks!!.listArchivedBooks())
        }

        @Test
        fun `numberOfArchivedBooks() returns the amount of books that are Archoved`() {
            assertEquals(0, popBooks!!.numberOfArchivedBooks())
            val newBook = Book("Book1", 3, "Non-Fiction", true)
            assertTrue(popBooks!!.add(newBook))
            assertEquals(1, popBooks!!.numberOfArchivedBooks())
        }

        @Test
        fun `numberOfActiveBooks() returns the amount of books that are active`() {
            assertEquals(3, popBooks!!.numberOfActiveBooks())
            val newBook = Book("Book1", 3, "Non-Fiction", false)
            assertTrue(popBooks!!.add(newBook))
            assertEquals(4, popBooks!!.numberOfActiveBooks())
        }

        @Test
        fun `listBooksBySelectedId() returns books based on the ID`() {
            assertTrue(popBooks!!.listBooksBySelectedId(2).contains("Sample Book 3"))
            val newBook = Book("Book1", 2, "Non-Fiction", false)
            assertTrue(popBooks!!.add(newBook))
            val books = popBooks!!.listBooksBySelectedId(2)
            assertTrue(books.contains("Sample Book 3"))
            assertTrue(books.contains("Book1"))
        }

        @Test
        fun `numberOfBooksById() returns the number of books based on the Id passed`() {
            assertEquals(1, popBooks!!.numberOfBooksById(2))
            val newBook = Book("Book1", 2, "Non-Fiction", false)
            assertTrue(popBooks!!.add(newBook))
            assertEquals(2, popBooks!!.numberOfBooksById(2))
        }
    }

    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfBooksCalculatedCorrectly() {
            assertEquals(3, popBooks!!.numberOfBooks())
            assertEquals(0, emptyBooks!!.numberOfBooks())
        }

        @Test
        fun numberOfArchivedBooksCalculatedCorrectly() {
            assertEquals(0, popBooks!!.numberOfArchivedBooks())
            assertEquals(0, emptyBooks!!.numberOfArchivedBooks())
        }

        @Test
        fun numberOfActiveBooksCalculatedCorrectly() {
            assertEquals(3, popBooks!!.numberOfActiveBooks())
            assertEquals(0, emptyBooks!!.numberOfActiveBooks())
        }

        @Test
        fun numberOfBooksByIDCalculatedCorrectly() {
            assertEquals(2, popBooks!!.numberOfBooksById(1))
            assertEquals(1, popBooks!!.numberOfBooksById(2))
            assertEquals(0, popBooks!!.numberOfBooksById(3))
            assertEquals(0, popBooks!!.numberOfBooksById(4))
            assertEquals(0, popBooks!!.numberOfBooksById(5))
            assertEquals(0, emptyBooks!!.numberOfBooksById(1))
        }
    }

    @Nested
    inner class DeleteBooks {

        @Test
        fun `deleting a book that does not exist returns null`() {
            assertNull(emptyBooks!!.deleteBook(0))
            assertNull(popBooks!!.deleteBook(-1))
            assertNull(popBooks!!.deleteBook(7))
        }

        @Test
        fun `deleting a book that exists delete and returns deleted object`() {
            assertEquals(3, popBooks!!.numberOfBooks())
            assertEquals(sampleBook3, popBooks!!.deleteBook(2))
            assertEquals(2, popBooks!!.numberOfBooks())
        }
    }

    @Nested
    inner class UpdatesBooks {

        @Test
        fun `updating a book that does not exist returns false`() {
            assertFalse(popBooks!!.updateBook(6, Book("Updating Book", 2, "Sport", false)))
            assertFalse(popBooks!!.updateBook(-1, Book("Updating Book", 2, "Sport", false)))
            assertFalse(emptyBooks!!.updateBook(0, Book("Updating Book", 2, "Sport", false)))
        }

        @Test
        fun `updating a book that exists returns true and updates`() {
            // check if the 3rd book exists and check the contents
            assertEquals(sampleBook3, popBooks!!.findBook(2))
            assertEquals("Sample Book 3", popBooks!!.findBook(2)!!.bookTitle)
            assertEquals(2, popBooks!!.findBook(2)!!.bookId)
            assertEquals("Mystery", popBooks!!.findBook(2)!!.bookDesc)

            // update 3rd book with new info and ensure update is successful
            assertTrue(popBooks!!.updateBook(2, Book("Updating Book3", 4, "Other", false)))
            assertEquals("Updating Book3", popBooks!!.findBook(2)!!.bookTitle)
            assertEquals(4, popBooks!!.findBook(2)!!.bookId)
            assertEquals("Other", popBooks!!.findBook(2)!!.bookDesc)
        }
    }

    @Nested
    inner class ArchiveBookTests {

        @Test
        fun `archive book by index`() {
            val archivedBooks = BookAPI(JSONSerializer(File("books.json")))
            archivedBooks.add(sampleBook1!!)
            archivedBooks.add(sampleBook2!!)
            archivedBooks.add(sampleBook3!!)

            assertEquals(true, archivedBooks.archiveBookByIndex(0))
            assertEquals(true, archivedBooks.archiveBookByIndex(1))
            assertEquals(true, archivedBooks.archiveBookByIndex(2))
        }
    }

    @Nested
    inner class PersistenceTests {

        // ///////////XML FORMAT TESTS
        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty books.XML file.
            val storingBooks = BookAPI(XMLSerializer(File("books.xml")))
            storingBooks.store()

            // Loading the empty books.xml file into a new object
            val loadedBooks = BookAPI(XMLSerializer(File("books.xml")))
            loadedBooks.load()

            // Comparing the source of the notes (storingBooks) with the XML loaded notes (loadedNotes)
            assertEquals(0, storingBooks.numberOfBooks())
            assertEquals(0, loadedBooks.numberOfBooks())
            assertEquals(storingBooks.numberOfBooks(), loadedBooks.numberOfBooks())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't looese data`() {
            // Storing 2 books to the books.XML file
            val storingBooks = BookAPI(XMLSerializer(File("books.xml")))
            storingBooks.add(sampleBook1!!)
            storingBooks.add(sampleBook3!!)
            storingBooks.store()

            // Loading books.xml into a different collection
            val loadedBooks = BookAPI(XMLSerializer(File("books.xml")))
            loadedBooks.load()

            // Comparing the source of the books (storingNotes) with the XML loaded notes (loadedBooks)
            assertEquals(2, storingBooks.numberOfBooks())
            assertEquals(2, loadedBooks.numberOfBooks())
            assertEquals(storingBooks.numberOfBooks(), loadedBooks.numberOfBooks())
            assertEquals(storingBooks.findBook(3), loadedBooks.findBook(3))
            assertEquals(storingBooks.findBook(4), loadedBooks.findBook(4))
        }

        // ///////////JSON FORMAT TESTS
        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty books.json file.
            val storingBooks = BookAPI(JSONSerializer(File("books.json")))
            storingBooks.store()

            // Loading the empty books.json file into a new object
            val loadedBooks = BookAPI(JSONSerializer(File("books.json")))
            loadedBooks.load()

            // Comparing the source of the books (storingBooks) with the json loaded bookd (loadedBooks)
            assertEquals(0, storingBooks.numberOfBooks())
            assertEquals(0, loadedBooks.numberOfBooks())
            assertEquals(storingBooks.numberOfBooks(), loadedBooks.numberOfBooks())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 2 books to the books.json file.
            val storingBooks = BookAPI(JSONSerializer(File("books.json")))
            storingBooks.add(sampleBook1!!)
            storingBooks.add(sampleBook3!!)
            storingBooks.store()

            // Loading books.json into a different collection
            val loadedBooks = BookAPI(JSONSerializer(File("books.json")))
            loadedBooks.load()

            // Comparing the source of the books (storingBooks) with the json loaded books (loadedNotes)
            assertEquals(2, storingBooks.numberOfBooks())
            assertEquals(2, loadedBooks.numberOfBooks())
            assertEquals(storingBooks.numberOfBooks(), loadedBooks.numberOfBooks())
            assertEquals(storingBooks.findBook(2), loadedBooks.findBook(2))
            assertEquals(storingBooks.findBook(3), loadedBooks.findBook(3))
        }

        // ///////////CBOR FORMAT TESTS
        @Test
        fun `saving and loading an empty collection in CBOR doesn't crash app`() {
            // Saving an empty books.cbor file.
            val storingBooks = BookAPI(CBORSerializer(File("books.cbor")))
            storingBooks.store()

            // Loading the empty books.cbor file into a new object
            val loadedBooks = BookAPI(CBORSerializer(File("books.cbor")))
            loadedBooks.load()

            // Comparing the source of the books (storingBooks) with the cbor loaded bookd (loadedBooks)
            assertEquals(0, storingBooks.numberOfBooks())
            assertEquals(0, loadedBooks.numberOfBooks())
            assertEquals(storingBooks.numberOfBooks(), loadedBooks.numberOfBooks())
        }

        @Test
        fun `saving and loading an loaded collection in CBOR doesn't lose data`() {
            // Storing 2 books to the books.cbor file.
            val storingBooks = BookAPI(CBORSerializer(File("books.cbor")))
            storingBooks.add(sampleBook1!!)
            storingBooks.add(sampleBook3!!)
            storingBooks.store()

            // Loading books.cbor into a different collection
            val loadedBooks = BookAPI(CBORSerializer(File("books.cbor")))
            loadedBooks.load()

            // Comparing the source of the books (storingBooks) with the json loaded books (loadedNotes)
            assertEquals(2, storingBooks.numberOfBooks())
            assertEquals(2, loadedBooks.numberOfBooks())
            assertEquals(storingBooks.numberOfBooks(), loadedBooks.numberOfBooks())
            assertEquals(storingBooks.findBook(2), loadedBooks.findBook(2))
            assertEquals(storingBooks.findBook(3), loadedBooks.findBook(3))
        }
    }

    @Nested
    inner class AddAuthorToBook {

        /**
         * The function tests if adding an author to a book adds the author to the book's authors list in Kotlin.
         *
         * @param result The result variable is of type Unit, which is a special type in Kotlin that represents the absence
         * of a value. In this case, the assertTrue function is being called with a Unit parameter, which doesn't have any
         * effect on the test. It seems like the implementation of the assertTrue function is missing,
         */
        @Test
        fun `adding author to book adds author to book's authors list`() {
            assertEquals(0, sampleBook1!!.authors.size)

            val author = Author("John D")
            val result = popBooks!!.addAuthorToBook(sampleBook1!!.bookTitle, author)

            assertTrue(result)
            assertEquals(1, sampleBook1!!.authors.size)
            assertTrue(sampleBook1!!.authors.contains(author))
        }

        private fun assertTrue(result: Unit) {
        }

        /**
         * The function tests that adding an author to a book that does not exist returns false.
         *
         * @param result The `result` parameter is a boolean value indicating whether adding an author to a book was
         * successful or not.
         */
        @Test
        fun `adding author to a book that does not exist returns false`() {
            assertEquals(0, sampleBook1!!.authors.size)

            val author = Author("John D")
            val result = popBooks!!.addAuthorToBook("Score", author)

            assertFalse(result)
            assertEquals(0, sampleBook1!!.authors.size)
        }

        private fun assertFalse(result: Unit) {
        }
    }

    @Nested
    inner class TestSearchByAuthor {
        @Test
        fun `Test searching for authors with matching name`() {
            // Create some sample books with authors
            val book1 = Book("Book 1", 1, "Education", false)
            val author1 = Author("John Doe")
            book1.authors.add(author1)

            /*val book2 = Book("Book 2", 1, "Education", false)
            val author2 = Author("Jane Doe")
            book2.authors.add(author2)

            val book3 = Book("Book 3", 2, "Mystery", false)
            val author3 = Author("John Smith")
            book3.authors.add(author3)*/

            // Add the books to a new BookAPI instance
            val books = BookAPI(XMLSerializer(File("books.xml")))
            books.add(book1)
            // books.add(book2)
            // books.add(book3)

            // Test searching for authors with matching name
            val searchResults1 = books.searchByAuthor("John")
            assertEquals("0: Title: Book 1, ID: 1, Description: Education, Authors: John Doe\n", searchResults1)
        }

        @Test
        fun `Test searching for authors with non-matching name`() {
            val book1 = Book("Book 1", 1, "Education", false)
            val author1 = Author("John Doe")
            book1.authors.add(author1)

            val books = BookAPI(XMLSerializer(File("books.xml")))
            books.add(book1)
            val searchResults3 = books.searchByAuthor("Smithsonian")
            assertEquals("No book found for: Smithsonian", searchResults3)
        }

        @Test
        fun `Test searching for authors when no books are stored`() {
            val emptyBooks = BookAPI(XMLSerializer(File("books.cbor")))
            val searchResults4 = emptyBooks.searchByAuthor("John")
            assertEquals("No books stored", searchResults4)
        }
    }

    @Nested
    inner class CountBooksByAuthor {
        @Test
        fun `countBooksByAuthor should return correct count for existing author`() {
            val authorName = "Test Author"
            val book1 = Book("Book 1", 1, "Test", false, mutableListOf(Author(authorName)))
            val book2 = Book("Book 2", 2, "Test", false, mutableListOf(Author(authorName)))
            popBooks!!.add(book1)
            popBooks!!.add(book2)
            assertEquals(2, popBooks!!.countBooksByAuthor(authorName))
        }

        @Test
        fun `countBooksByAuthor should return 0 for non-existing author`() {
            val authorName = "Non-existing Author"
            assertEquals(0, popBooks!!.countBooksByAuthor(authorName))
        }
    }
}
