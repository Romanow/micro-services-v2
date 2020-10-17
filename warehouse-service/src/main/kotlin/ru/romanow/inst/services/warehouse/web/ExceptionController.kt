package ru.romanow.inst.services.warehouse.web

import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.romanow.inst.services.warranty.model.ErrorResponse
import javax.persistence.EntityNotFoundException

@RestControllerAdvice(annotations = [RestController::class])
class ExceptionController {
    private val logger = LoggerFactory.getLogger(ExceptionController::class.java)

    @ApiResponse(responseCode = "400", description = "Wrong data")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun badRequest(exception: MethodArgumentNotValidException): ErrorResponse {
        val validationErrors = prepareValidationErrors(exception.bindingResult.fieldErrors)
        if (logger.isDebugEnabled) {
            logger.debug("Bad Request: {}", validationErrors)
        }
        return ErrorResponse(validationErrors)
    }

    @ApiResponse(responseCode = "404", description = "Requested item not found")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException::class)
    fun notFound(exception: EntityNotFoundException): ErrorResponse {
        return ErrorResponse(exception.message)
    }

    @ApiResponse(responseCode = "500", description = "Server error")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException::class)
    fun handleException(exception: RuntimeException): ErrorResponse {
        logger.error("", exception)
        return ErrorResponse(exception.message)
    }

    private fun prepareValidationErrors(errors: List<FieldError>): String {
        return errors.joinToString(";") { "Field " + it.field + " has wrong value: [" + it.defaultMessage + "]" }
    }
}