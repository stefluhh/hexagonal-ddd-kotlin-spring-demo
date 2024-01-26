package de.stefluhh.hexagonaldemo.singlemodule.infrastructure.repository

import de.stefluhh.hexagonaldemo.singlemodule.domain.model.book.Book
import de.stefluhh.hexagonaldemo.singlemodule.domain.model.book.BookRepository
import de.stefluhh.hexagonaldemo.singlemodule.infrastructure.repository.testdata.TestBookFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.TextIndexDefinition
import org.springframework.data.mongodb.core.indexOps
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.data.mongodb.core.query.TextQuery
import org.springframework.stereotype.Repository

@Profile("mongodb")
@Repository
class BookMongoRepository(private val mongoTemplate: MongoTemplate) : BookRepository {

    @EventListener(ApplicationReadyEvent::class)
    fun initDatabase() {
        ensureIndices()
        TestBookFactory.createTestBooks()
            .forEach(mongoTemplate::save)
    }

    override fun findById(bookId: String): Book? = mongoTemplate.findById(bookId, Book::class.java)

    override fun findBooks(searchTerm: String): List<Book> {
        val criteria = TextCriteria.forDefaultLanguage().matchingPhrase(searchTerm)
        val query = TextQuery.query(criteria)
        return mongoTemplate.find(query, Book::class.java)
    }

    override fun save(borrowedBook: Book) {
        mongoTemplate.save(borrowedBook)
    }

    override fun delete(returnedBook: Book) {
        mongoTemplate.remove(returnedBook)
    }

    private fun ensureIndices() {
        val indexOps = mongoTemplate.indexOps<Book>()
        indexOps.ensureIndex(TextIndexDefinition.builder().onField("title").onField("author").build())
    }

}