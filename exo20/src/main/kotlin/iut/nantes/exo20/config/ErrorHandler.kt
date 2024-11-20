package iut.nantes.exo20.config

import com.fasterxml.jackson.databind.ObjectMapper
import iut.nantes.exo20.errors.ImATeapotException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.*
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

data class ExceptionResponse(val status: Int, val message: String?)

@ControllerAdvice
class ErrorHandler: ResponseEntityExceptionHandler() {

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        return ResponseEntity.badRequest().body(ExceptionResponse(HttpStatus.BAD_REQUEST.value(), ex.message))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(e: ConstraintViolationException) = ResponseEntity.badRequest().body("Failure: ${e.message}")
    
    @ExceptionHandler(Exception::class)
    fun fallback(e: Exception) = ResponseEntity.internalServerError().body("Failure: ${e.message}")

    @ExceptionHandler(ImATeapotException::class)
    fun handleImATeapot(e: ImATeapotException) =
        ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(e.message)
}