package org.example.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String ERROR_PATH = "https://culinaryexchange.com/errors/";

    @ExceptionHandler({EntityNotFoundException.class, AlreadyExistsException.class})
    public ProblemDetail handleCustomExceptions(RuntimeException ex, WebRequest request) {
        HttpStatus status = ex instanceof EntityNotFoundException ? HttpStatus.NOT_FOUND : HttpStatus.CONFLICT;

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problemDetail.setTitle("Business Rule Violation");
        problemDetail.setType(URI.create(ERROR_PATH + "business-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getDescription(false)
                .replace("uri=", ""));

        log.warn("Custom exception: {} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    public ProblemDetail handleOperationNotAllowed(OperationNotAllowedException ex, WebRequest request) {
        log.warn("Operation not allowed: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problemDetail.setTitle("Operation Not Allowed");
        problemDetail.setType(URI.create(ERROR_PATH + "operation-not-allowed"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getDescription(false)
                .replace("uri=", ""));
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public Object handleAllExceptions(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        problemDetail.setTitle("Server Error");
        problemDetail.setType(URI.create(ERROR_PATH + "server-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getDescription(false)
                .replace("uri=", ""));

        log.error("Unhandled exception: {}", ex.getMessage(), ex);

        if (isAjaxRequest(request)) {
            return problemDetail;
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("error");
            modelAndView.addObject("error", problemDetail);
            return modelAndView;
        }
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ex.getBody();
        problemDetail.setType(URI.create(ERROR_PATH + "validation-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getDescription(false)
                .replace("uri=", ""));
        log.warn("Validation error: {}", ex.getMessage());
        return new ResponseEntity<>(problemDetail, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, "Malformed request");
        problemDetail.setTitle("Invalid Request Format");
        problemDetail.setType(URI.create(ERROR_PATH + "invalid-request"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getDescription(false)
                .replace("uri=", ""));
        log.warn("Invalid request format: {}", ex.getMessage());
        return new ResponseEntity<>(problemDetail, headers, status);
    }

    private boolean isAjaxRequest(WebRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        return "XMLHttpRequest".equals(requestedWith) ||
                (accept != null && accept.contains(MediaType.APPLICATION_JSON_VALUE));
    }
}
