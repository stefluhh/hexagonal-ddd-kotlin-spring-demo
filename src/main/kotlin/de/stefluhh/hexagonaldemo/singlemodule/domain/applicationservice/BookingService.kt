package de.stefluhh.hexagonaldemo.singlemodule.domain.applicationservice

import de.stefluhh.hexagonaldemo.singlemodule.domain.model.book.*
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class BookingService(
    private val bookRepository: BookRepository,
    private val returnCheckStrategy: ReturnCheckStrategy
) {

    fun borrowBook(bookId: String, borrower: BorrowerName) {
        val borrowedBook = findBook(bookId).borrow(borrower)
        bookRepository.save(borrowedBook)
    }

    fun returnBook(bookId: String, condition: BookCondition): ReturnCheckResult {
        val borrowedBook = findBook(bookId)

        val (returnedBook, returnCheckResult) = borrowedBook.returnBook(returnCheckStrategy = returnCheckStrategy, condition = condition)

        /**
         * If the book has become unusable, we delete it from the repository.
         */
        if (returnedBook.isUsable()) bookRepository.save(returnedBook) else bookRepository.delete(returnedBook)

        return returnCheckResult
    }

    private fun findBook(bookId: String) = bookRepository.findById(bookId) ?: throw ResponseStatusException(NOT_FOUND, "Book not found")

}