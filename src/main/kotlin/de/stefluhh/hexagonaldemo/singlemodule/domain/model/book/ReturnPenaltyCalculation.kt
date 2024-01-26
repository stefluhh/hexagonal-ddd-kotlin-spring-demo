package de.stefluhh.hexagonaldemo.singlemodule.domain.model.book

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode

class ReturnPenaltyCalculation(returnCondition: BookCondition, previousCondition: BookCondition, bookPrice: BigDecimal) {

    val penalty = calculatePenalty(previousCondition, returnCondition, bookPrice)

    private fun calculatePenalty(previousCondition: BookCondition, returnCondition: BookCondition, bookPrice: BigDecimal): BigDecimal {
        return when (previousCondition.ordinal - returnCondition.ordinal) {
            -1 -> ZERO // No penalty for returning a book that was returned in almost same condition
            -2 -> bookPrice * BigDecimal.valueOf(0.33) // 33% of the book price if the condition deteriorated by 2 classes
            -3 -> bookPrice * BigDecimal.valueOf(0.66) // 66% of the book price if the condition deteriorated by 3 classes
            -4 -> bookPrice // 100% of the book price if the condition deteriorated by 4 classes (which means it was new and now it is completely destroyed)
            else -> ZERO
        }.setScale(2, RoundingMode.HALF_UP)
    }

    fun hasPenalty(): Boolean = penalty > ZERO

}
