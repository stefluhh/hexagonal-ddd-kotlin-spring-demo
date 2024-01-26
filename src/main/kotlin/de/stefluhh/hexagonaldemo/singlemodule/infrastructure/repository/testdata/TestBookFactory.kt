package de.stefluhh.hexagonaldemo.singlemodule.infrastructure.repository.testdata

import de.stefluhh.hexagonaldemo.singlemodule.domain.model.book.Book
import java.math.BigDecimal

object TestBookFactory {

    fun createTestBooks(): Set<Book> {
        return setOf(
            Book("1", "Implementing Domain-Driven Design", "Vaughn Vernon", 565, BigDecimal.valueOf(49.99)),
            Book("2", "Strategic Monoliths and Microservices: Driving Innovation Using Purposeful Architecture", "Vaughn Vernon", 500, BigDecimal.valueOf(59.99)),
            Book("3", "Clean Code: A Handbook of Agile Software Craftsmanship", "Martin \"Uncle Bob\" C. Martin", 1214, BigDecimal.valueOf(49.99)),
            Book("4", "Effektive Softwarearchitekturen: Ein praktischer Leitfaden", "Gernot Starke", 798, BigDecimal.valueOf(29.99)),
            Book("5", "Roller Coaster - Der Achterbahn-Designer Werner Stengel", "Klaus Schuetzmannsky", 168, BigDecimal.valueOf(119.99))
        )

    }
}