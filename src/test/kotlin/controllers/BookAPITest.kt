package controllers

import models.Book
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
class BookAPITest {
    private var sampleBook1: Book? = null
    private var sampleBook2: Book? = null
    private var sampleBook3: Book? = null
    private var popBooks: BookAPI? = BookAPI()
    private var emptyBooks: BookAPI? = BookAPI()

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

    @Test
    fun `listAllBooks returns 'no books stored' when arraylist is empty`() {
        assertEquals(0, emptyBooks!!.numberOfBooks())
        assertTrue(emptyBooks!!.listAllBooks().lowercase().contains("no books"))
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
        fun `listArchivedBooks() returns no archived books when ArrayList is empty`() {
            assertEquals(0, emptyBooks!!.numberOfArchivedBooks())
            assertTrue(emptyBooks!!.listArchivedBooks().lowercase().contains("no archived books"))
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


    }
}