package com.gb.app.service;

import com.gb.app.exception.VatCalculationException;
import com.gb.app.util.Assert;
import com.gb.app.util.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 * <p>
 * This service will contain core logic for VAT calculation.
 *
 * @author Nikola Kocic
 * @since 02.04.2016
 */
@Service
public class VatCalculationServiceImpl implements VatCalculationService {

    @Autowired
    private MessageSource messageSource;

    @Override
    public VatCalculationResult calculate(Integer vatRate, Double vatAmount, Double netAmount, Double grossAmount) {

        /**
         * Perform necessary validations as requested:
         *
         * - if passed vatRate is provided (not null)
         * - if passed vatRate is allowed for Country AUT (acceptable values: 10%, 12%, 13%, 20%)
         * - only one from passed values vatAmount, netAmount, grossAmount is not null
         * - passed values vatAmount, netAmount, grossAmount are either null or positive numbers
         */
        if (vatRate == null || vatRate == 0) {
            throw new VatCalculationException(HttpStatus.BAD_REQUEST, messageSource.getMessage("error.vat.rate.not.provided", null, LocaleContextHolder.getLocale()));
        }

        boolean isRatePerCountryValid = VatRate.AUT.isVatRateValid(vatRate);
        if (!isRatePerCountryValid) {
            throw new VatCalculationException(HttpStatus.BAD_REQUEST, messageSource.getMessage("error.not.existing.vat.rate", null, LocaleContextHolder.getLocale()));
        }

        boolean isOnlyOneNotNull = com.gb.app.util.Assert.onlyOneNotNull(vatAmount, netAmount, grossAmount);
        if (!isOnlyOneNotNull) {
            throw new VatCalculationException(HttpStatus.BAD_REQUEST, messageSource.getMessage("error.only.one.input.param.can.be.not.null", null, LocaleContextHolder.getLocale()));
        }

        boolean isAllPositive = com.gb.app.util.Assert.positive(vatAmount, netAmount, grossAmount);
        if (!isAllPositive) {
            throw new VatCalculationException(HttpStatus.BAD_REQUEST, messageSource.getMessage("error.input.params.must.be.positive", null, LocaleContextHolder.getLocale()));
        }

        // perform calculation based on the provided not-null amount/value.
        if (vatAmount != null) {
            netAmount = MathUtil.roundNumber(MathUtil.multiply(MathUtil.divide(vatAmount, vatRate), 100), 2);
            grossAmount = MathUtil.roundNumber(MathUtil.add(netAmount, vatAmount), 2);
        } else if (netAmount != null) {
            vatAmount = MathUtil.roundNumber(MathUtil.divide(MathUtil.multiply(netAmount, vatRate), 100), 2);
            grossAmount = MathUtil.roundNumber(MathUtil.add(netAmount, vatAmount), 2);
        } else {
            netAmount = MathUtil.roundNumber(MathUtil.multiply(MathUtil.divide(grossAmount, MathUtil.add(100, vatRate)), 100), 2);
            vatAmount = MathUtil.roundNumber(MathUtil.subtract(grossAmount, netAmount), 2);
        }

        // prepare the calculation result to be returned to the consumer
        VatCalculationResult vatCalculationResult = new VatCalculationResult();
        vatCalculationResult.setVatRate(vatRate);
        vatCalculationResult.setVatAmount(vatAmount);
        vatCalculationResult.setNetAmount(netAmount);
        vatCalculationResult.setGrossAmount(grossAmount);

        return vatCalculationResult;
    }

    /**
     * Global Blue's Hiring Exercise for Software Developer position.
     * <p>
     * <p>
     * Helper enum. Holds information related to available taxRates per Country.
     * In general, resources should be retrieved from storage/database (for this purpose I used enum).
     *
     * @author Nikola Kocic
     * @since 03.04.2016
     */
    enum VatRate {

        AUT("AUT", Arrays.asList(10, 12, 13, 20)),
        //AUS("AUS, Arrays.asList(10)),
        //UK("UK, Arrays.asList(5, 20))
        ;

        private String code;
        private List<Integer> rates;

        VatRate(String code, List<Integer> rates) {
            this.code = code;
            this.rates = rates;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<Integer> getRates() {
            return rates;
        }

        public void setRates(List<Integer> rates) {
            this.rates = rates;
        }

        public boolean isVatRateValid(Integer rate) {
            Assert.notNull(rate, "rate");

            for (VatRate vatRate : values()) {
                if (vatRate.getCode().equals(code)) {
                    return vatRate.getRates().contains(rate);
                }
            }
            return false;
        }
    }
}