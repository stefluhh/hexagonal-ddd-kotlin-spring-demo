package de.stefluhh.hexagonaldemo.singlemodule.domain.model.book

interface ReturnCheckStrategy {

    fun performReturnCheck(book: Book, currentBorrowing: Borrowing, returnCondition: BookCondition): ReturnCheckResult

}