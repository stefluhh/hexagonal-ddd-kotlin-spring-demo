package de.stefluhh.hexagonaldemo.singlemodule.domain.model.book

interface BookRepository {

    fun findById(bookId: String): Book?
    fun findBooks(searchTerm: String): List<Book>
    fun save(borrowedBook: Book)
    fun delete(returnedBook: Book)
}