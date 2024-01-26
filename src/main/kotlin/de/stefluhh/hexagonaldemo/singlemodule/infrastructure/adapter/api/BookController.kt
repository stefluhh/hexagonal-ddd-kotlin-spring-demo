package de.stefluhh.hexagonaldemo.singlemodule.infrastructure.adapter.api

import de.stefluhh.hexagonaldemo.singlemodule.domain.applicationservice.BookingService
import de.stefluhh.hexagonaldemo.singlemodule.domain.applicationservice.SearchService
import de.stefluhh.hexagonaldemo.singlemodule.domain.model.book.BookCondition
import de.stefluhh.hexagonaldemo.singlemodule.infrastructure.adapter.api.dto.BookReturnResponseDto
import de.stefluhh.hexagonaldemo.singlemodule.infrastructure.adapter.api.dto.SearchResultDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class BookController(
    private val searchService: SearchService,
    private val bookingService: BookingService
) : BooksApi {

    override fun searchBooks(searchTerm: String): ResponseEntity<SearchResultDto> {
        val searchResult = searchService.searchBooks(searchTerm)
        return ResponseEntity.ok(SearchResultMapper.toDto(searchResult))
    }

    override fun borrowBook(bookId: String, borrowerName: String): ResponseEntity<Unit> {
        bookingService.borrowBook(bookId = bookId, borrower = borrowerName)
        return ResponseEntity.ok().build()
    }

    override fun returnBook(bookId: String, bookCondition: String): ResponseEntity<BookReturnResponseDto> {
        val returnCheckResult = bookingService.returnBook(bookId = bookId, condition = BookCondition.safeValueOf(bookCondition))
        return ResponseEntity.ok(BookReturnAcl.toDto(returnCheckResult))
    }



}