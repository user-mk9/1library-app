package persistance

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.*
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import models.Book
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class CBORSerializer(private val file: File) : Serializer {
    @OptIn(ExperimentalSerializationApi::class)
    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val byteArray = Cbor.encodeToByteArray(obj as ArrayList<Book>)
        val file = File("books.cbor")
        val outputStream = FileOutputStream(file)
        outputStream.write(byteArray)
        outputStream.close()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Throws(Exception::class)
    override fun read(): ArrayList<Book> {
        val fileToLoad = File("books.cbor")
        val inputStream = FileInputStream(fileToLoad)
        val byteArray = inputStream.readBytes()
        inputStream.close()
        return Cbor.decodeFromByteArray(byteArray)
    }
}
