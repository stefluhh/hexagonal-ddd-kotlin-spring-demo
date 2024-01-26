package de.stefluhh.hexagonaldemo.singlemodule.infrastructure.repository

import de.stefluhh.hexagonaldemo.singlemodule.domain.model.book.Book
import de.stefluhh.hexagonaldemo.singlemodule.domain.model.book.BookRepository
import de.stefluhh.hexagonaldemo.singlemodule.infrastructure.repository.testdata.TestBookFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository

@Repository
@Profile("default")
class BookInMemoryRepository : BookRepository {

    private val books: MutableSet<Book> = TestBookFactory.createTestBooks().toMutableSet()

    override fun findById(bookId: String): Book? {
        return books.find { it.id == bookId }
    }

    override fun findBooks(searchTerm: String): List<Book> {
        return books.filter { it.title.contains(searchTerm) || it.author.contains(searchTerm) }
    }

    override fun save(borrowedBook: Book) {
        books.removeIf { it.id == borrowedBook.id }
        books.add(borrowedBook)
    }

    override fun delete(returnedBook: Book) {
        books.removeIf { it.id == returnedBook.id }
    }
}


