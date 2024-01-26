package de.stefluhh.hexagonaldemo.singlemodule.domain.model.book

import org.springframework.stereotype.Service
import java.math.BigDecimal

/**
 * Domain service for checking the return condition of a book.
 * The condition is checked against the condition when the book was issued and if the condition worsened, a penalty may apply.
 *
 * This service could be extended in a way that it also checks if the borrower has returned several books in bad condition in the past and if so,
 * ban him from borrowing books, increase penalty, etc.
 */
@Service
class ConditionClassReturnCheckStrategy : ReturnCheckStrategy {

    override fun performReturnCheck(book: Book, currentBorrowing: Borrowing, returnCondition: BookCondition): ReturnCheckResult {
        val conditionBefore = currentBorrowing.issuanceCondition

        return if (isConditionWorsened(conditionBefore, returnCondition)) {
            val (penalty, customerMessage) = calculatePenalty(issuanceCondition = conditionBefore, returnCondition = returnCondition, book.bookPrice)
            ReturnCheckResult.withPenalty(customerMessage = customerMessage.message, penalty = penalty)
        } else ReturnCheckResult.noPenalty(customerMessage = CustomerMessageKeys.THANK_YOU_FOR_RETURNING.message)
    }

    private fun calculatePenalty(issuanceCondition: BookCondition, returnCondition: BookCondition, bookPrice: BigDecimal): Pair<BigDecimal, CustomerMessageKeys> {
        return when (issuanceCondition.ordinal - returnCondition.ordinal) {
            -2 -> BigDecimal.valueOf(5.00) to CustomerMessageKeys.SLIGHT_PENALTY // 5 € penalty if the condition deteriorated by 2 classes
            -3 -> BigDecimal.valueOf(15.00) to CustomerMessageKeys.SIGNIFICANT_PENALTY // 15 € penalty if the condition deteriorated by 3 classes (significant damage caused by the borrower)
            -4 -> bookPrice to CustomerMessageKeys.PLESE_DONT_BORROW_AGAIN // 100% of the book price if the condition deteriorated by 4 classes (which means it was new and now it is completely broken)
            /**
             * No penalty for returning a book that was returned in almost same condition.
             */
            else -> BigDecimal.ZERO to CustomerMessageKeys.THANK_YOU_FOR_RETURNING
        }
    }

    private fun isConditionWorsened(conditionBefore: BookCondition, conditionAfter: BookCondition): Boolean = conditionBefore.ordinal < conditionAfter.ordinal
}