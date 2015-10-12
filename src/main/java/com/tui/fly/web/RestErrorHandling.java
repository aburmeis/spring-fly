package com.tui.fly.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;

@ControllerAdvice(annotations = RestController.class)
class RestErrorHandling {

    private static final Logger LOG = LoggerFactory.getLogger(RestErrorHandling.class);

    @Autowired
    private ErrorAttributes errorAttributes;

    @ExceptionHandler({NoSuchElementException.class, MissingResourceException.class})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException exception, HttpServletRequest request) {
        return createResponse(HttpStatus.NOT_FOUND, exception, request);
    }

    @ExceptionHandler({IllegalArgumentException.class, TypeMismatchException.class, BindException.class, ConstraintViolationException.class})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleBadRequest(RuntimeException exception, HttpServletRequest request) {
        return createResponse(HttpStatus.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleInternal(Exception exception, HttpServletRequest request) {
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception, request);
    }

    private ResponseEntity<Map<String, Object>> createResponse(HttpStatus status, Throwable exception, HttpServletRequest request) {
        if (status.is5xxServerError()) {
            LOG.error(status.getReasonPhrase(), exception);
        } else {
            LOG.info(status.getReasonPhrase(), exception.getMessage());
        }
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, status.value()); // needed to make 
        return new ResponseEntity<>(errorAttributes.getErrorAttributes(new ServletRequestAttributes(request), status.is5xxServerError()), status);
    }
}
