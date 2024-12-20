# Ports and Adapters Architecture + Domain Driven Design

This repo contains a demo project showcasing a Hexagonal Architecture (aka Ports and Adapters) and Domain Driven Design.
To do so this repo makes use of these frameworks:

* Kotlin
* Spring Boot
* Maven
* OpenAPI Generator
* MongoDB

## Scenario

The scenario is a simple library application. The library has books and customers. Customers can borrow books from the library and return them.
If the returned book is in a worse condition than before, the customer may have to pay a penalty. 

You can find the OpenAPI specification in the `src/main/resources/api-specs` folder.
A Postman collection is also provided in the `postman` folder.

## Basic concept of hexagonal

Hexagonals' first goal is to aim for a codebase that effectively seperates business logic from infrastructure concerns.

Examples for infrastructure concerns could be:

* Database technology
* Caching framework
* Event broker
* 3rd party REST dependencies
* or even the entire Web Application framework

The concepts of hexagonal create a project setup that decouples businesslogic from infrastructure very effectively. This adds a couple of 
advantages to your codebase:

* Changes or upgrades in infrastructure cause less friction, as your business logic is not directly coupled to specific infrastructure implementations anymore
* Infrastructure components are easily replaceable due to inversion of control
* Changes in infrastructure, such as consumed REST APIs, do not cascade through the codebase by using the Anti Corruption Layer pattern
* Business logic is testable as a standalone component without having to spin up a web application context
* Following a well-known and accepted architecture paradigm raises team spirit and code quality awareness, building reusable knowledge for all team members

Hexagonal hereby is basically just a toolset of ideas and methodologies, including: 

* A couple of rules concerning project structure, e.g. modules, clearly named packages
* Inversion of Control by Dependency Injection
* At least 2 Anti Corruption Layers
* SOLID principles, especially _Single Responsibility_, _Interface Segregation_ and _Dependency Injection_

## Implementation approaches

There are different ways to implement Hexagonal in a project. Choosing one mainly depends on the degree of web appliation framework flexibility you need on the one hand, and the
amount of strictness you want to apply in order to comply with Hexagonal on the other hand.

There are 3 example projects provided:

* Single Module
* Multi Module (Onion Architecture)
* Multi Module (strict)

### Single Module (Hexagonal Architecture)

This approach is the simplest to setup and gives most flexibility of using Spring. 
Since __infrastructure__ and __core__ reside in the same maven module, there is no hard seperation of these 2 concerns, allowing to use 
Spring dependency injection with no workarounds or extra glue code required. This approach is called the Hexagonal Architecture (Alistar Cockburn). 
However, this implementation approach also requires most knowledge on Hexagonal Architecture since there is a higher chance for breaking rules of Hexagonal. 
With this approach it is technically possible to inject concrete repository classes and adapters directly from the applications core, which should not be done if Hexagonal 
is supposed to be respected.

### Multi Module (Onion architecture)

This approach separates the __core__ and __infrastructure__ in a hard way by putting them into dedicated maven modules as well as a very specific rule how these maven 
modules depend on each other: The fundamental rule is that all code can depend on the core module, but no code can depend on code in the infrastructure module. 
When adding this small level of detail to the Single module approach you get the [Onion Architecture (Jeffrey Palermo)](https://jeffreypalermo.com/2008/07/the-onion-architecture-part-1/). 
It basically tweaks the Hexagonal Architecture to have a dependency-graph constraint in it, that __tremendously supports__ developers to "act right out of the box". 
With this architecture style it is quite hard to circumvent the general idea of Hexagonal to rely on dependency injection rather than direct coupling to implementation classes.


In the above example I tried to autowire the concrete MongoDB implementation of the `BookRepository` directly into the `SearchService`. However, 
since `SearchService` is located in the core module and `BookMongoRepository` in infrastructure, there is a compilation error. There is still a (small) chance 
to circumvent the basic rule though. Developers could simply locate the `BookMongoRepository` in the core module and autowire it. However, this would require the 
developer to add `Spring Data MongoDB` to the core module, if its not already present. 

If you aim for a high amount of safety that your architecture approach is followed, I recommend the third approach below.

### Strict Multi Module (Onion architecture, "but strict")

The Onion Architecture as described above is able to significantly increase the chance that your architecture is followed correctly OOTB. What the approach above doesn't solve however, 
is to have a technology-agnostic core module: 

```xml
<project
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns="http://maven.apache.org/POM/4.0.0"
        xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <artifactId>onion-core</artifactId>

    <parent>
        <groupId>de.stefluhh</groupId>
        <artifactId>onion-parent</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-mongodb</artifactId>
        </dependency>
    </dependencies>
</project>
```

The core module depends on Spring, MongoDB and maybe a ton of JPA annotations and Hibernate if you're using SQL. 
The approach described below shows a solution that is able to keep out Spring and Spring Data annotations from the core module. 
But here is a disclaimer: This is an approach only recommended if you're using MongoDB as your primary database. Because MongoDB is schemaless (your model is the schema), your 
model classes are able to work completely without any Spring Data annotations. This enables you to eliminate all Spring dependencies from the core module, creating a highly reusable, 
full technology-agnostic setup that is future-proof and usable with other web application frameworks like Quarkus.

> But when removing Spring, what is then with dependency injection?

The good news is: It's still possible to use Dependency Injection. The bad news is: You need 10 minutes of extra effort and a bit of documentation to make it possible. 
Spring allows you to configure your own, custom annotations to denote Spring beans, i.e. you just need to create a custom annotation and annotate your services with it and tell Spring 
to treat such classes as Spring beans. Spring will then load these classes into the application context and everything just works like normal: 

```kotlin
/**
 * Instead of using @Service you'd use your own annotation to make this class a Spring Bean. 
 */
@MyOwnAnnotation
class SearchService(private val bookRepository: BookRepository) {

    fun searchBooks(searchTerm: String): List<Book> {
        return bookRepository.findBooks(searchTerm)
    }

}

/**
 * Configure Spring to load classes annotated with @MyOwnAnnotation into the application context for dependency injection support.
 */
@SpringBootApplication
@ComponentScan(includeFilters = [ ComponentScan.Filter(type = FilterType.ANNOTATION, value = [MyOwnAnnotation::class])])
class Application
```

If this approach suits you depends on a number of factors. If you're using PostgreSQL and need Hibernate and JPA annotations, or Spring Data OpenSearch, this approach is probably not right for you.
JPA and OpenSearch models usually require a lot more annotations and there is no (easy) way to remove them from the model, or it is possible, but only with a painful amount of overhead and glue code. 
It's not worth it, I would say. 

## Summary

All described and demo'ed approaches have the same goal in common: They rely heavily on the Dependency Injection principle to separate your business logic in the core, from your technology-specific infrastructure.
The way to accomplish it is fairly simple, but there are a couple of paths available from which you should choose one that suits you and your use case best. 
I would personally usually tend to go with the "Multi Module (Onion Architecture)" approach, because you get the "best of both worlds": 
1. High flexibility and usage of Spring capabilities and 2. High amount of safety that the fundamentals of Hexagonal Architecture are respected out of the box.

While the third approach adds most safety and maximizes for portability (you can lift and shift the entire core module and put it into a Quarkus context for instance), it will also add necessity for some custom 
solutions that could become tricky sometimes and would require extra documentation and training for the team.
