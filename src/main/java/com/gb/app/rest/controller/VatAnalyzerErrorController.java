package com.gb.app.rest.controller;

import com.gb.app.exception.VatCalculationException;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 * <p>
 * This Controller is responsible to send JSON response to the client with all the information regarding
 * any thrown Exception.
 *
 * @author Nikola Kocic
 * @since 02.04.2016
 */
@RestController
@RequestMapping(value = "/error")
public class VatAnalyzerErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    /**
     * The method for handling the error responses.
     *
     * @param request the request which caused the exception
     * @return key value pairs as JSON object
     */
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> error(HttpServletRequest request) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes().getErrorAttributes(requestAttributes, false);
    }

    /**
     * Handles the exceptions to have proper information inside the ErrorAttributes object.
     *
     * @return ErrorAttributes which contains the default information coming from DefaultErrorAttributes, and the
     * custom info which comes from the internally used VatCalculationException
     */
    @Bean
    protected ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {

            @Override
            public Map<String, Object> getErrorAttributes(
                    RequestAttributes requestAttributes,
                    boolean includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
                Throwable error = super.getError(requestAttributes);
                if (error instanceof VatCalculationException) {
                    VatCalculationException vatCalculationException = (VatCalculationException) error;
                    errorAttributes.put("status", vatCalculationException.getStatusCode().value());
                    errorAttributes.put("error", vatCalculationException.getStatusCode().getReasonPhrase());
                }
                return errorAttributes;
            }

        };
    }
}
