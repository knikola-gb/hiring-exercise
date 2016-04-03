package com.gb.app.rest.controller;

import com.gb.app.exception.VatCalculationException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 * <p>
 * Advice is responsible for intercepting and logging exceptions defined in corresponding Controller Advice class methods.
 * Can be configured in different ways, e.g. either logging and retrieving ErrorReturnInfo object as exception thrown (suitable for web REST clients for easy handling in UI),
 * or by logging and checking the exception thrown and translating it to the expected business related exception (in our case VAT calculation exception).
 *
 * @author Nikola Kocic
 * @since 02.04.2016
 */
@ControllerAdvice
public class ControllerExceptionAdvice {

    private static final Logger _logger = LogManager.getLogger(ControllerExceptionAdvice.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    ErrorReturnInfo handleNotAcceptableStatusException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        // log exception
        _logger.debug(exception.getMessage());

        String errorMessage = exception.getLocalizedMessage();
        String errorURL = request.getRequestURL().toString();

        return new ErrorReturnInfo(response.getStatus(), errorURL, errorMessage);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, TypeMismatchException.class})
    //@ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    ErrorReturnInfo handleException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        // log exception
        _logger.debug(exception.getMessage());

        String errorMessage = exception.getLocalizedMessage();
        String errorURL = request.getRequestURL().toString();

        return new ErrorReturnInfo(response.getStatus(), errorURL, errorMessage);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    void handleException(HttpServletResponse response, RuntimeException exception) {
        // log exception
        _logger.debug(exception.getMessage());

        if (exception instanceof VatCalculationException) {
            throw exception;
        }
        throw new VatCalculationException(HttpStatus.valueOf(response.getStatus()), exception.getLocalizedMessage());
    }

    /**
     * Helper class holding request failure information
     */
    public class ErrorReturnInfo {

        private Integer status;
        private String url;
        private String message;

        public ErrorReturnInfo(Integer status, String url, String message) {
            this.status = status;
            this.url = url;
            this.message = message;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}