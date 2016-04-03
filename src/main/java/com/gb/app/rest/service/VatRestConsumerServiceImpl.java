package com.gb.app.rest.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 * <p>
 * This service will behave as a WS consumer and will make HTTP call to REST API for VAT calculation.
 *
 * @author Nikola Kocic
 * @since 02.04.2016
 */
@Service
public class VatRestConsumerServiceImpl implements VatRestConsumerService {

    private static final Logger _logger = LogManager.getLogger(VatRestConsumerServiceImpl.class);

    @Override
    public ConsumerCalculationResult calculate(String url, Integer vatRate, Double vatAmount, Double netAmount, Double grossAmount) {

        // url and vatRate must not be null.
        Assert.notNull(url, "url");
        Assert.notNull(vatRate, "vatRate");

        RestTemplate restTemplate = new RestTemplate();
        try {
            _logger.info("Making Http request to REST API.");

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("vatRate", vatRate)
                    .queryParam("vatAmount", vatAmount)
                    .queryParam("netAmount", netAmount)
                    .queryParam("grossAmount", grossAmount);

            HttpEntity<String> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, new HttpEntity<>(headers), String.class);

            _logger.info("Http response arrived.");

            _logger.info("JSON Response to Object Model mapping starts.");
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
            ConsumerCalculationResult jsonObjects = mapper.readValue(response.getBody(), new TypeReference<ConsumerCalculationResult>() {
            });
            _logger.info("JSON Response to Object Model mapping is ready.");

            return jsonObjects;
        } catch (Exception e) {
            throw new RuntimeException(/*HttpStatus.INTERNAL_SERVER_ERROR, */e.getMessage());
        }
    }
}