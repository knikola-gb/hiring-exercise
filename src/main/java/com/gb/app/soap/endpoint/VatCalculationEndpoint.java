package com.gb.app.soap.endpoint;

import com.gb.app.service.VatCalculationService;
import com.gb.hiring_exercise.GetVatRequest;
import com.gb.hiring_exercise.GetVatResponse;
import com.gb.hiring_exercise.Vat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 * <p>
 * This WS represents the core API used for VAT re-calculation.
 * Will relay on the service which will be responsible for VAT re-calculation.
 *
 * @author Nikola Kocic
 * @since 03.04.2016
 */
@Endpoint
public class VatCalculationEndpoint {

    @Autowired
    private VatCalculationService vatCalculationService;

    @PayloadRoot(namespace = "http://gb.com/hiring-exercise", localPart = "getVatRequest")
    @ResponsePayload
    public GetVatResponse calculate(@RequestPayload GetVatRequest request) {

        Integer vatRate = request.getVatRate();
        Double vatAmount = request.getVatAmount();
        Double netAmount = request.getNetAmount();
        Double grossAmount = request.getGrossAmount();

        // call upon service method for re-calculating VAT
        VatCalculationService.VatCalculationResult vatCalculationResult = vatCalculationService.calculate(vatRate, vatAmount, netAmount, grossAmount);

        // create vat object to be sent back to the consumer
        Vat vat = new Vat();
        vat.setVatRate(vatCalculationResult.getVatRate());
        vat.setVatAmount(vatCalculationResult.getVatAmount());
        vat.setNetAmount(vatCalculationResult.getNetAmount());
        vat.setGrossAmount(vatCalculationResult.getGrossAmount());

        GetVatResponse response = new GetVatResponse();
        response.setVat(vat);

        return response;
    }
}