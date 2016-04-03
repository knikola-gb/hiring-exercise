package com.gb.app.exception;

import org.springframework.http.HttpStatus;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 *
 * @author Nikola Kocic
 * @since 02.04.2016
 */
public class VatCalculationException extends RuntimeException {

    private HttpStatus statusCode;

    public VatCalculationException() {
        super();
    }

    public VatCalculationException(String message) {
        this(null, message);
    }

    public VatCalculationException(HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
