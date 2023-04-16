package controllers

import models.Book
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

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

        @Test
        fun `listBooksBySelectedId() returns no books when ArrayList is empty`() {
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.listBooksBySelectedId(1).lowercase().contains("no books"))
        }

        @Test
        fun `listBooksBySelectedId returns no books when no books of that ID exist`() {
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(3, popBooks!!.numberOfBooks())
            val priority2String = popBooks!!.listBooksBySelectedId(4).lowercase()
            assertTrue(priority2String.contains("no books"))
            assertTrue(priority2String.contains("4"))
        }

        /*@Test
        fun `listBooksBySelectedId returns all books that match that ID when books of that ID exist`() {
            //ID 2 books (1 Education), 1 book (2 Sports), none (3 Fiction), none (4 Other)
            assertEquals(3, popBooks!!.numberOfBooks())
            val priority1String = popBooks!!.listBooksBySelectedId(1).lowercase(Locale.getDefault())
            assertTrue(priority1String.contains("2 book"))
            assertTrue(priority1String.contains("id 1"))
            //assertTrue(priority1String.contains("sample Book 1"))
            //assertTrue(priority1String.contains("sample book 2"))
            assertFalse(priority1String.contains("sample book 3"))
        }*/

        @Test
        fun `numberOfBooksById() returns the number of books based on the Id passed`() {
            assertEquals(1, popBooks!!.numberOfBooksById(2))
            val newBook = Book("Book1", 2, "Non-Fiction", false)
            assertTrue(popBooks!!.add(newBook))
            assertEquals(2, popBooks!!.numberOfBooksById(2))
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
            kotlin.test.assertFalse(popBooks!!.updateBook(6, Book("Updating Book", 2, "Sport", false)))
            kotlin.test.assertFalse(popBooks!!.updateBook(-1, Book("Updating Book", 2, "Sport", false)))
            kotlin.test.assertFalse(emptyBooks!!.updateBook(0, Book("Updating Book", 2, "Sport", false)))
        }

        @Test
        fun `updating a book that exists returns true and updates`() {
            // check if the 3rd book exists and check the contents
            assertEquals(sampleBook3, popBooks!!.findBook(2))
            assertEquals("Sample Book 3", popBooks!!.findBook(2)!!.bookTitle)
            assertEquals(2, popBooks!!.findBook(2)!!.bookId)
            assertEquals("Mystery", popBooks!!.findBook(2)!!.bookCategory)

            // update 3rd book with new info and ensure update is successful
            assertTrue(popBooks!!.updateBook(2, Book("Updating Book3", 4, "Other", false)))
            assertEquals("Updating Book3", popBooks!!.findBook(2)!!.bookTitle)
            assertEquals(4, popBooks!!.findBook(2)!!.bookId)
            assertEquals("Other", popBooks!!.findBook(2)!!.bookCategory)
        }
    }
}