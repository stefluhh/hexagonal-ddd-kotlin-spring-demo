package de.stefluhh.hexagonaldemo.singlemodule.domain.model.book

import de.stefluhh.hexagonaldemo.singlemodule.domain.model.book.BookCondition.AS_NEW
import de.stefluhh.hexagonaldemo.singlemodule.domain.model.book.BookCondition.UNUSABLE
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal

data class Book(
    val id: BookId,
    val title: String,
    val author: String,
    val pageCount: Int,
    val bookPrice: BigDecimal,
    val borrowHistory: List<Borrowing> = listOf()
) {

    /**
     * Writes into the borrowing history that the book was borrowed by the given borrower with the books' last known condition.
     */
    fun borrow(borrower: BorrowerName): Book {
        if (isCurrentlyBorrowed()) {
            throw ResponseStatusException(BAD_REQUEST, "Book $id is already borrowed!")
        }

        return copy(borrowHistory = borrowHistory + Borrowing(borrower, lastKnownCondition())).also {
            println("Book ${it.id} was borrowed by $borrower with condition ${it.lastKnownCondition()}")
        }
    }

    fun returnBook(returnCheckStrategy: ReturnCheckStrategy, condition: BookCondition): Pair<Book, ReturnCheckResult> {
        val updatedBorrowingHistory = updateBorrowingHistory(condition)
        val returnedBook = copy(borrowHistory = updatedBorrowingHistory)

        val returnCheck = performReturnCheck(returnCheckStrategy, condition)

        println("Book ${returnedBook.id} was returned with condition $condition")

        return returnedBook to returnCheck
    }

    /**
     * Performs a return check by visual inspection.
     * If the condition is not as good as when it was borrowed, a penalty charge may apply for the borrower.
     */
    fun performReturnCheck(returnCheckStrategy: ReturnCheckStrategy, returnCondition: BookCondition): ReturnCheckResult {
        val currentBorrowing = getCurrentBorrowing()

        return returnCheckStrategy.performReturnCheck(this, currentBorrowing, returnCondition)
    }

    /**
     * Takes the last borrowing and updates the return condition.
     * Returns a new, updated list.
     */
    private fun updateBorrowingHistory(returnCondition: BookCondition): List<Borrowing> {
        val currentBorrowing = getCurrentBorrowing()
        val updatedBorrowing = currentBorrowing.recordReturning(returnCondition)
        return borrowHistory.dropLast(1) + updatedBorrowing
    }

    private fun isCurrentlyBorrowed(): Boolean {
        return borrowHistory.any { it.returnCondition == null }
    }

    /**
     * Returns the current (open) borrowing.
     * Throws an exception if the book is not borrowed to anyone currently.
     */
    private fun getCurrentBorrowing() = borrowHistory.firstOrNull { it.returnCondition == null } ?: throw ResponseStatusException(BAD_REQUEST, "Book $id is not borrowed!")

    private fun lastKnownCondition(): BookCondition {
        return borrowHistory.lastOrNull()?.returnCondition ?: AS_NEW // if no borrowings, assume as new
    }

    fun isUsable(): Boolean {
        return lastKnownCondition() != UNUSABLE
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}