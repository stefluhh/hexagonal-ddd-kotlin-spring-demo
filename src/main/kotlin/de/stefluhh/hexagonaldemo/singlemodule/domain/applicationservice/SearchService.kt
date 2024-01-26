package de.stefluhh.hexagonaldemo.singlemodule.domain.applicationservice

import de.stefluhh.hexagonaldemo.singlemodule.domain.model.book.Book
import de.stefluhh.hexagonaldemo.singlemodule.domain.model.book.BookRepository
import org.springframework.stereotype.Service

/**
 * BookRepository is an interface (a port), that a concrete repository implementation has to implement in the infrastructure package (an adapter).
 * In this example, 2 implementations are given:
 *
 * 1. BookInMemoryRepository
 * 2. BookMongoRepository
 *
 * This is to demo that, with Hexagonal Architecture, it is relatively easy to replace concrete implementations while at the same time gaining the benefit that
 * clients of the bookRepository (like this class) do not need to change anything, as they are simply relying on the interfaces' contract.
 *
 * From a testing perspective, things become smoother as well: The SearchServiceTest just has to mock the interface BookRepository instead of a concrete class. If it would
 * mock a concrete class and we want to change it later (e.g. migrating from PostgreSQL to MongoDB), then a ton of tests won't break either. This is the power of
 * Hexagonal Architecture in combination with IoC.
 */
@Service
class SearchService(private val bookRepository: BookRepository) {

    fun searchBooks(searchTerm: String): List<Book> {
        return bookRepository.findBooks(searchTerm)
    }

}
