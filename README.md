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
* Business logic is testable as a standalone component
* Following a well-known and accepted architecture paradigm creates a team-mindset of thoughtful decision-making and software quality, thus creating reusable knowledge for all team members

Hexagonal hereby is basically just a toolset of ideas and methodologies, including: 

* A couple of rules concerning project structure, e.g. modules and some clear guidelines for naming packages
* Inversion of Control by Dependency Injection
* 2 Anti Corruption Layers, 1 between the client and your domain and 1 between your domain and upstream contexts
* SOLID principles, especially _Single Responsibility_, _Interface Segregation_ and _Dependency Injection_

## Implementation approaches

There are different ways to implement Hexagonal in a project. Choosing one mainly depends on the degree of web application framework flexibility you need on the one hand, and the
amount of strictness you want to apply in order to comply with Hexagonal on the other hand.

There are 2 ways of implementation that we talk about in this article:

* Single Module (Hexagonal)
* Multi Module (Onion Architecture)

### Single Module (Hexagonal)

This approach is the simplest to setup and gives most freedom of using features of your Web Application framework. 
Since __infrastructure__ and __core__ reside in the same maven module, there is no hard seperation of these 2 concerns, allowing to use 
Spring features, such as `@Autowired` for DI and `@Value` to load application config, with no workarounds or additional glue code required. This approach is called the Hexagonal Architecture (as first drafted by Alistar Cockburn). 
However, this implementation approach also requires more discipline to restrain yourself within the boundries of the hexagonal. 

### Multi Module (Onion architecture)

This approach separates the __core__ and __infrastructure__ in a hard way by putting them into dedicated maven modules as well as a very specific rule how these maven 
modules depend on each other: The fundamental rule is that all code can depend on the core module, but no code can depend on code in the infrastructure module. 
When adding this small level of detail to the Single module approach you get the [Onion Architecture (Jeffrey Palermo)](https://jeffreypalermo.com/2008/07/the-onion-architecture-part-1/). 
It basically tweaks the Hexagonal Architecture to have a dependency-graph constraint in it, that __tremendously supports__ developers to "act right out of the box". 
With this architecture style it is quite hard to circumvent the general idea of Hexagonal to rely on dependency injection rather than direct coupling to implementation classes.

## Summary

All described and demo'ed approaches have the same goal in common: They rely heavily on the Dependency Injection principle to separate your business logic in the core, from your technology-specific infrastructure.
The way to accomplish it is fairly simple, but there are a couple of paths available from which you could choose one that suits you and your use case best. 
I would personally tend to go with the "Multi Module (Onion Architecture)" approach, because it gets you the "best of both worlds": 

1. High flexibility and usage of Spring capabilities and 2. High amount of safety that the fundamentals of Hexagonal Architecture are respected out of the box.

In addition its very quickly set up.
