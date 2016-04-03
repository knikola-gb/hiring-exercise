package com.gb.test.rest;

import com.gb.app.Application;
import com.gb.app.rest.service.VatRestConsumerService;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 *
 * @author Nikola Kocic
 * @since 02.04.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port:8097")
public class ApplicationServiceTests {

    @Autowired
    private VatRestConsumerService vatRestConsumerService;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Value("${server.port}")
    private String port;

    @Before
    public void setUp() throws Exception {
    }

    /**
     * Service tests
     */
    @Test
    public void testService() {

        String restUrl = "http://localhost:" + port + "/rest/vat";
        VatRestConsumerService.ConsumerCalculationResult calculationResult = vatRestConsumerService.calculate(restUrl, 10, 10d, null, null);

        Assert.assertEquals(calculationResult.getVatAmount(), 10d, 0);
        Assert.assertEquals(calculationResult.getNetAmount(), 100d, 0);
        Assert.assertEquals(calculationResult.getGrossAmount(), 110d, 0);
        //System.out.println(calculationResult.toString());
    }
}