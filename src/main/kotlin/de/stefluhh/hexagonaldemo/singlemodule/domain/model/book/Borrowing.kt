package de.stefluhh.hexagonaldemo.singlemodule.domain.model.book

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

data class Borrowing(
    val borrower: String,
    val issuanceCondition: BookCondition,
    val returnCondition: BookCondition? = null,
) {
    fun recordReturning(condition: BookCondition) : Borrowing = copy(returnCondition = condition)
}

enum class BookCondition {
    AS_NEW, GOOD, FAIR, POOR, UNUSABLE;

    companion object {

        private val map = entries.associateBy(BookCondition::name)

        fun safeValueOf(bookCondition: String): BookCondition {
            return map[bookCondition] ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No BookCondition with name $bookCondition exists.")
        }
    }
}
