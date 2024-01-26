package de.stefluhh.hexagonaldemo.singlemodule

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    SpringApplication(Application::class.java).run(*args)
}
