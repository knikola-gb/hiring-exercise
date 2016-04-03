package com.gb.app.rest.controller;

import com.gb.app.service.VatCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 * <p>
 * This Controller represents the core API used for VAT re-calculation.
 * Will relay on the service which will be responsible for VAT re-calculation.
 *
 * @author Nikola Kocic
 * @since 02.04.2016
 */
@Controller
@RequestMapping(value = "/rest")
public class VatCalculationController {

    @Autowired
    private VatCalculationService vatCalculationService;

    @ResponseBody
    @RequestMapping(value = "/vat", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CalculationResult calculate(
            @RequestParam(value = "vatRate") Integer vatRate,
            @RequestParam(value = "vatAmount") Double vatAmount,
            @RequestParam(value = "netAmount") Double netAmount,
            @RequestParam(value = "grossAmount") Double grossAmount) {

        // call upon service method for re-calculating VAT
        VatCalculationService.VatCalculationResult result = vatCalculationService.calculate(vatRate, vatAmount, netAmount, grossAmount);

        return new CalculationResult(result.getVatRate(), result.getVatAmount(), result.getNetAmount(), result.getGrossAmount());
    }

    /**
     * Calculation Result class. Holds information related to VAT calculation
     */
    class CalculationResult {

        private Integer vatRate;
        private Double vatAmount;
        private Double netAmount;
        private Double grossAmount;

        public CalculationResult(Integer vatRate, Double vatAmount, Double netAmount, Double grossAmount) {
            this.vatRate = vatRate;
            this.vatAmount = vatAmount;
            this.netAmount = netAmount;
            this.grossAmount = grossAmount;
        }

        public Integer getVatRate() {
            return vatRate;
        }

        public void setVatRate(Integer vatRate) {
            this.vatRate = vatRate;
        }

        public Double getVatAmount() {
            return vatAmount;
        }

        public void setVatAmount(Double vatAmount) {
            this.vatAmount = vatAmount;
        }

        public Double getNetAmount() {
            return netAmount;
        }

        public void setNetAmount(Double netAmount) {
            this.netAmount = netAmount;
        }

        public Double getGrossAmount() {
            return grossAmount;
        }

        public void setGrossAmount(Double grossAmount) {
            this.grossAmount = grossAmount;
        }
    }
}