package de.stefluhh.hexagonaldemo.singlemodule.infrastructure.adapter.api

import de.stefluhh.hexagonaldemo.singlemodule.domain.model.book.Book
import de.stefluhh.hexagonaldemo.singlemodule.infrastructure.adapter.api.dto.BookDto
import de.stefluhh.hexagonaldemo.singlemodule.infrastructure.adapter.api.dto.SearchResultDto

internal object SearchResultMapper {

    fun toDto(searchResult: List<Book>): SearchResultDto {
        return SearchResultDto(
            hitCount = searchResult.size,
            hits = mapSearchHits(searchResult)
        )
    }

    private fun mapSearchHits(searchResult: List<Book>): List<BookDto> {
        return searchResult.map {
            BookDto(
                id = it.id,
                title = it.title,
                author = it.author,
                pageCount = it.pageCount
            )
        }
    }

}
