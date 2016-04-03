package com.gb.test.rest;

import com.gb.app.Application;
import com.gb.app.exception.VatCalculationException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 *
 * @author Nikola Kocic
 * @since 02.04.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ApplicationTests {

    @Autowired
    private MessageSource messageSource;

    private MockMvc mockMvc;
    private RestTemplate restTemplate;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        restTemplate = new TestRestTemplate();
    }

    @Test
    public void calculateVatWithNoParams() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/vat")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test(expected = NestedServletException.class)
    public void calculateVatWithNoVatRate() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/vat")
                        .param("vatRate", "")
                        .param("vatAmount", "10")
                        .param("netAmount", "10")
                        .param("grossAmount", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test(expected = NestedServletException.class)
    public void calculateVatNotOnlyOneNull_1() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/vat")
                        .param("vatRate", "10")
                        .param("vatAmount", "10")
                        .param("netAmount", "10")
                        .param("grossAmount", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void calculateVatNotOnlyOneNull_2() {
        try {
            this.mockMvc
                    .perform(MockMvcRequestBuilders.get("/rest/vat")
                            .param("vatRate", "10")
                            .param("vatAmount", "10")
                            .param("netAmount", "")
                            .param("grossAmount", "10")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        } catch (Exception e) {
            Assert.assertThat(e.getCause(), CoreMatchers.instanceOf(VatCalculationException.class));
            Assert.assertThat(e.getCause().getLocalizedMessage(), CoreMatchers.equalTo(messageSource.getMessage("error.only.one.input.param.can.be.not.null", null, LocaleContextHolder.getLocale())));
        }
    }

    @Test
    public void calculateVatNotOnlyOneNull_3() throws Exception {
        try {
            this.mockMvc
                    .perform(MockMvcRequestBuilders.get("/rest/vat")
                            .param("vatRate", "10")
                            .param("vatAmount", "")
                            .param("netAmount", "10")
                            .param("grossAmount", "10")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        } catch (Exception ex) {
            Assert.assertThat(ex.getCause(), CoreMatchers.instanceOf(VatCalculationException.class));
            Assert.assertThat(ex.getCause().getLocalizedMessage(), CoreMatchers.equalTo(messageSource.getMessage("error.only.one.input.param.can.be.not.null", null, LocaleContextHolder.getLocale())));
        }
    }

    @Test
    public void calculateVatNotOnlyOneNull_4() throws Exception {
        try {
            this.mockMvc
                    .perform(MockMvcRequestBuilders.get("/rest/vat")
                            .param("vatRate", "10")
                            .param("vatAmount", "")
                            .param("netAmount", "")
                            .param("grossAmount", "")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        } catch (Exception ex) {
            Assert.assertThat(ex.getCause(), CoreMatchers.instanceOf(VatCalculationException.class));
        }
    }

    @Test(expected = NestedServletException.class)
    public void calculateVatNotAllPositive() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/vat")
                        .param("vatRate", "12")
                        .param("vatAmount", "")
                        .param("netAmount", "-35")
                        .param("grossAmount", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void calculateVatWithVatRate12() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/vat")
                        .param("vatRate", "12")
                        .param("vatAmount", "")
                        .param("netAmount", "35")
                        .param("grossAmount", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vatRate").value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vatAmount").value(4.2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.grossAmount").value(39.2))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void calculateVatWithNotExistingVatRate() throws Exception {
        try {
            this.mockMvc
                    .perform(MockMvcRequestBuilders.get("/rest/vat")
                            .param("vatRate", "8")
                            .param("vatAmount", "")
                            .param("netAmount", "35")
                            .param("grossAmount", "")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        } catch (Exception ex) {
            Assert.assertThat(ex.getCause(), CoreMatchers.instanceOf(VatCalculationException.class));
            Assert.assertThat(ex.getCause().getLocalizedMessage(), CoreMatchers.equalTo(messageSource.getMessage("error.not.existing.vat.rate", null, LocaleContextHolder.getLocale())));
        }
    }

    @Test
    public void calculateVatWithVatRate13() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/vat")
                        .param("vatRate", "13")
                        .param("vatAmount", "15")
                        .param("netAmount", "")
                        .param("grossAmount", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vatRate").value(13))
                .andExpect(MockMvcResultMatchers.jsonPath("$.netAmount").value(115.38))
                .andExpect(MockMvcResultMatchers.jsonPath("$.grossAmount").value(130.38))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void calculateVatWithVatRate20() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/vat")
                        .param("vatRate", "20")
                        .param("vatAmount", "")
                        .param("netAmount", "")
                        .param("grossAmount", "11.3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vatRate").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vatAmount").value(1.88))
                .andExpect(MockMvcResultMatchers.jsonPath("$.netAmount").value(9.42));
    }

    @Test
    public void calculateVatException() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/rest/vat")
                        .param("vatRate", "wrong_value")
                        .param("vatAmount", "10")
                        .param("netAmount", "10")
                        .param("grossAmount", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; nested exception is java.lang.NumberFormatException: For input string: \"wrong_value\""));
    }
}