package com.gb.app.soap.service;

import com.gb.hiring_exercise.GetVatRequest;
import com.gb.hiring_exercise.GetVatResponse;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 * <p>
 * This service will behave as a WS consumer and will make HTTP call to SOAP API for VAT calculation.
 *
 * @author Nikola Kocic
 * @since 02.04.2016
 */
@Service
public class VatSoapConsumerServiceImpl implements VatSoapConsumerService {

    @Override
    public String calculate(String url, String xmlMessage) {
        try {
            // url and xmlMessage must not be null.
            Assert.notNull(url, "url");
            Assert.notNull(url, "xmlMessage");

            StreamSource source = new StreamSource(new StringReader(xmlMessage));
            StreamResult result = new StreamResult(new StringWriter());

            // call upon SOAP API and get back the response as stream-result
            new WebServiceTemplate().sendSourceAndReceiveToResult(url, source, result);

            return result.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public GetVatResponse calculate(String url, Integer vatRate, Double vatAmount, Double netAmount, Double grossAmount) {

        GetVatRequest request = new GetVatRequest();
        request.setVatRate(vatRate);
        request.setVatAmount(vatAmount);
        request.setNetAmount(netAmount);
        request.setGrossAmount(grossAmount);

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan(ClassUtils.getPackageName(GetVatRequest.class));
        return (GetVatResponse) new WebServiceTemplate(marshaller).marshalSendAndReceive(url, request);
    }
}