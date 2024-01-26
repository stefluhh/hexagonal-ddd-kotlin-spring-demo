package de.stefluhh.hexagonaldemo.singlemodule.infrastructure.adapter.api

import de.stefluhh.hexagonaldemo.singlemodule.domain.model.book.ReturnCheckResult
import de.stefluhh.hexagonaldemo.singlemodule.infrastructure.adapter.api.dto.BookReturnResponseDto

object BookReturnAcl {
    fun toDto(returnCheckResult: ReturnCheckResult): BookReturnResponseDto {
        return BookReturnResponseDto(
            customerMessage = returnCheckResult.customerMessage,
            penalty = returnCheckResult.penalty
        )
    }

}
