package com.gb.app.soap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.server.endpoint.SoapFaultAnnotationExceptionResolver;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 *
 * @author Nikola Kocic
 * @since 03.04.2016
 */
@Component
public class SoapFaultDefinitionExceptionResolver extends SoapFaultAnnotationExceptionResolver {

    public final static Logger logger = Logger.getLogger(SoapFaultDefinitionExceptionResolver.class);

    public SoapFaultDefinitionExceptionResolver() {
        super();
    }

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        // log caught exception
        logger.debug("Caught exception:", ex);
        //throw new RuntimeException(ex);
    }
}