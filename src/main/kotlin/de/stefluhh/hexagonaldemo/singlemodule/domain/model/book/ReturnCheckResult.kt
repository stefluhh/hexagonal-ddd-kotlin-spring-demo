package de.stefluhh.hexagonaldemo.singlemodule.domain.model.book

import java.math.BigDecimal

data class ReturnCheckResult(val customerMessage: String, val penalty: BigDecimal? = null) {
    companion object {
        fun noPenalty(customerMessage: String): ReturnCheckResult = ReturnCheckResult(customerMessage)
        fun withPenalty(customerMessage: String, penalty: BigDecimal?): ReturnCheckResult = ReturnCheckResult(customerMessage, penalty)
    }
}