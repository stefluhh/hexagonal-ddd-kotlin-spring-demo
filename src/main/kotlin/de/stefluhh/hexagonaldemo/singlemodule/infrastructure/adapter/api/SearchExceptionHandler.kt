package de.stefluhh.hexagonaldemo.singlemodule.infrastructure.adapter.api

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * Spring Boot 3 ships a RFC 7807 compliant application/problem+json implementation
 */
@ControllerAdvice
class SearchExceptionHandler : ResponseEntityExceptionHandler()