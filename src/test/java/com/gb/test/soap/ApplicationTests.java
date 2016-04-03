package com.gb.test.soap;

import com.gb.app.Application;
import com.gb.hiring_exercise.GetVatRequest;
import com.gb.hiring_exercise.GetVatResponse;
import com.gb.hiring_exercise.Vat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;

import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 *
 * @author Nikola Kocic
 * @since 03.04.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
public class ApplicationTests {

    private Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

    @Autowired
    private MessageSource messageSource;

    @Value("${local.server.port}")
    private int port = 0;

    @Before
    public void init() throws Exception {
        marshaller.setPackagesToScan(ClassUtils.getPackageName(GetVatRequest.class));
        marshaller.afterPropertiesSet();
    }

    @Test
    public void calculateVatWithVatRate12() throws TransformerException {
        GetVatRequest request = new GetVatRequest();
        request.setVatRate(12);
        request.setVatAmount(null);
        request.setNetAmount(35d);
        request.setGrossAmount(null);

        GetVatResponse vatResponse = (GetVatResponse) new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:" + port + "/soap/vat", request);
        Assert.assertNotNull(vatResponse);
        Vat vat = vatResponse.getVat();
        Assert.assertNotNull(vat);
        Assert.assertEquals(Double.valueOf(vat.getVatAmount()), Double.valueOf(4.2d));
        Assert.assertEquals(Double.valueOf(vat.getNetAmount()), Double.valueOf(35d));
        Assert.assertEquals(Double.valueOf(vat.getGrossAmount()), Double.valueOf(39.2d));
    }

    @Test
    public void calculateVatWithVatRate13() throws TransformerException {
        GetVatRequest request = new GetVatRequest();
        request.setVatRate(13);
        request.setVatAmount(15d);
        request.setNetAmount(null);
        request.setGrossAmount(null);

        GetVatResponse vatResponse = (GetVatResponse) new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:" + port + "/soap/vat", request);
        Assert.assertNotNull(vatResponse);
        Vat vat = vatResponse.getVat();
        Assert.assertNotNull(vat);
        Assert.assertEquals(Double.valueOf(vat.getVatAmount()), Double.valueOf(15d));
        Assert.assertEquals(Double.valueOf(vat.getNetAmount()), Double.valueOf(115.38d));
        Assert.assertEquals(Double.valueOf(vat.getGrossAmount()), Double.valueOf(130.38d));
    }

    @Test
    public void calculateVatWithVatRate20() throws TransformerException {
        GetVatRequest request = new GetVatRequest();
        request.setVatRate(20);
        request.setVatAmount(null);
        request.setNetAmount(111.88d);
        request.setGrossAmount(null);

        GetVatResponse vatResponse = (GetVatResponse) new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:" + port + "/soap/vat", request);
        Assert.assertNotNull(vatResponse);
        Vat vat = vatResponse.getVat();
        Assert.assertNotNull(vat);
        Assert.assertEquals(Double.valueOf(vat.getVatAmount()), Double.valueOf(22.38d));
        Assert.assertEquals(Double.valueOf(vat.getNetAmount()), Double.valueOf(111.88d));
        Assert.assertEquals(Double.valueOf(vat.getGrossAmount()), Double.valueOf(134.26d));
    }

    @Test
    public void calculateVatNotOnlyOneNull_1() throws TransformerException {
        try {

            GetVatRequest request = new GetVatRequest();
            request.setVatRate(20);
            request.setVatAmount(10d);
            request.setNetAmount(111.88d);
            request.setGrossAmount(null);

            new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:" + port + "/soap/vat", request);
        } catch (SoapFaultClientException e) {
            DOMSource domSource = (DOMSource) e.getSoapFault().getSource();

            Assert.assertEquals(messageSource.getMessage("error.only.one.input.param.can.be.not.null", null, LocaleContextHolder.getLocale()),
                    domSource.getNode().getLastChild().getTextContent());
        }
    }

    @Test
    public void calculateVatNotOnlyOneNull_2() throws TransformerException {
        try {

            GetVatRequest request = new GetVatRequest();
            request.setVatRate(20);
            request.setVatAmount(null);
            request.setNetAmount(null);
            request.setGrossAmount(null);

            new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:" + port + "/soap/vat", request);
        } catch (SoapFaultClientException e) {
            DOMSource domSource = (DOMSource) e.getSoapFault().getSource();

            Assert.assertEquals(messageSource.getMessage("error.only.one.input.param.can.be.not.null", null, LocaleContextHolder.getLocale()),
                    domSource.getNode().getLastChild().getTextContent());
        }
    }

    @Test
    public void calculateVatNotOnlyOneNull_3() throws TransformerException {
        try {

            GetVatRequest request = new GetVatRequest();
            request.setVatRate(20);
            request.setVatAmount(null);
            request.setNetAmount(12d);
            request.setGrossAmount(15d);

            new WebServiceTemplate(marshaller).marshalSendAndReceive("http://localhost:" + port + "/soap/vat", request);
        } catch (SoapFaultClientException e) {
            DOMSource domSource = (DOMSource) e.getSoapFault().getSource();

            Assert.assertEquals(messageSource.getMessage("error.only.one.input.param.can.be.not.null", null, LocaleContextHolder.getLocale()),
                    domSource.getNode().getLastChild().getTextContent());
        }
    }

    @Test
    public void calculateVatWithNullVatRate() {
        try {
            /**
             * NOTE: XML content generated running {@link #generateRequestXmlMessage} test
             */
            String requestXmlMessage = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><getVatRequest xmlns=\"http://gb.com/hiring-exercise\"><vatRate></vatRate><vatAmount xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"/><netAmount>35.0</netAmount><grossAmount xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"/></getVatRequest>";

            StreamSource source = new StreamSource(new StringReader(requestXmlMessage));
            StreamResult result = new StreamResult(new StringWriter());
            new WebServiceTemplate().sendSourceAndReceiveToResult("http://localhost:" + port + "/soap/vat", source, result);

        } catch (SoapFaultClientException e) {
            DOMSource domSource = (DOMSource) e.getSoapFault().getSource();

            Assert.assertEquals(messageSource.getMessage("error.vat.rate.not.provided", null, LocaleContextHolder.getLocale()),
                    domSource.getNode().getLastChild().getTextContent());
        }
    }

    @Test
    public void generateRequestXmlMessage() {

        GetVatRequest request = new GetVatRequest();
        request.setVatRate(12);
        request.setVatAmount(null);
        request.setNetAmount(35d);
        request.setGrossAmount(null);

        StreamResult result = new StreamResult(System.out);
        marshaller.marshal(request, result);
    }

    @Test
    public void callUponSoapUsingXMLMessage() throws IOException {
        /**
         * NOTE: XML content generated running {@link #generateRequestXmlMessage} test
         */
        String requestXmlMessage = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><getVatRequest xmlns=\"http://gb.com/hiring-exercise\"><vatRate>12</vatRate><vatAmount xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"/><netAmount>35.0</netAmount><grossAmount xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"/></getVatRequest>";

        StreamSource source = new StreamSource(new StringReader(requestXmlMessage));
        StreamResult result = new StreamResult(new StringWriter());
        new WebServiceTemplate().sendSourceAndReceiveToResult("http://localhost:" + port + "/soap/vat", source, result);

        GetVatResponse vatResponse = (GetVatResponse) marshaller.unmarshal(new StreamSource(new ByteArrayInputStream(result.getWriter().toString().getBytes(StandardCharsets.UTF_8))));
        Assert.assertNotNull(vatResponse);
        Vat vat = vatResponse.getVat();
        Assert.assertNotNull(vat);
        Assert.assertEquals(Double.valueOf(vat.getVatAmount()), Double.valueOf(4.2d));
        Assert.assertEquals(Double.valueOf(vat.getNetAmount()), Double.valueOf(35d));
        Assert.assertEquals(Double.valueOf(vat.getGrossAmount()), Double.valueOf(39.2d));
    }
}