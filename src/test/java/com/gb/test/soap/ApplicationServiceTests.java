package com.gb.test.soap;

import com.gb.app.Application;
import com.gb.app.soap.service.VatSoapConsumerService;
import com.gb.hiring_exercise.GetVatRequest;
import com.gb.hiring_exercise.GetVatResponse;
import com.gb.hiring_exercise.Vat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ClassUtils;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 *
 * @author Nikola Kocic
 * @since 03.04.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port:8097")
public class ApplicationServiceTests {

    private Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

    @Autowired
    private VatSoapConsumerService vatSoapConsumerService;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Value("${server.port}")
    private String port;

    @Before
    public void init() throws Exception {
        marshaller.setPackagesToScan(ClassUtils.getPackageName(GetVatRequest.class));
        marshaller.afterPropertiesSet();
    }

    /**
     * Service tests
     */
    @Test
    public void testServiceWithRequestXmlMessage() {

        /**
         * NOTE: XML content generated running {@link ApplicationTests#generateRequestXmlMessage} method
         */
        String requestXmlMessage = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><getVatRequest xmlns=\"http://gb.com/hiring-exercise\"><vatRate>12</vatRate><vatAmount xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"/><netAmount>35.0</netAmount><grossAmount xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\"/></getVatRequest>";

        String url = "http://localhost:" + port + "/soap/vat";
        String response = vatSoapConsumerService.calculate(url, requestXmlMessage);

        Assert.assertNotNull(response);
        GetVatResponse vatResponse = (GetVatResponse) marshaller.unmarshal(new StreamSource(new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8))));
        Assert.assertNotNull(vatResponse);
        Vat vat = vatResponse.getVat();
        Assert.assertNotNull(vat);
        Assert.assertEquals(Double.valueOf(vat.getVatAmount()), Double.valueOf(4.2d));
        Assert.assertEquals(Double.valueOf(vat.getNetAmount()), Double.valueOf(35d));
        Assert.assertEquals(Double.valueOf(vat.getGrossAmount()), Double.valueOf(39.2d));
    }

    @Test
    public void testService() {

        String url = "http://localhost:" + port + "/soap/vat";
        GetVatResponse vatResponse = vatSoapConsumerService.calculate(url, 20, null, 111.88d, null);

        Assert.assertNotNull(vatResponse);
        Vat vat = vatResponse.getVat();
        Assert.assertNotNull(vat);
        Assert.assertEquals(Double.valueOf(vat.getVatAmount()), Double.valueOf(22.38d));
        Assert.assertEquals(Double.valueOf(vat.getNetAmount()), Double.valueOf(111.88d));
        Assert.assertEquals(Double.valueOf(vat.getGrossAmount()), Double.valueOf(134.26d));
    }
}