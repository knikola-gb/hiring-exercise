//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.04 at 12:55:01 AM CEST 
//


package com.gb.hiring_exercise;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.gb.hiring_exercise package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.gb.hiring_exercise
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetVatRequest }
     * 
     */
    public GetVatRequest createGetVatRequest() {
        return new GetVatRequest();
    }

    /**
     * Create an instance of {@link GetVatResponse }
     * 
     */
    public GetVatResponse createGetVatResponse() {
        return new GetVatResponse();
    }

    /**
     * Create an instance of {@link Vat }
     * 
     */
    public Vat createVat() {
        return new Vat();
    }

}
