package com.gb.app.service;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 *
 * @author Nikola Kocic
 * @since 02.04.2016
 */
public interface VatCalculationService {

    /**
     * Service method responsible for calculating VAT based on passed input arguments
     *
     * @param vatRate     - VAT Rate
     * @param vatAmount   - VAT Amount
     * @param netAmount   - Net Amount
     * @param grossAmount - Gross Amount
     * @return Calculation Result
     */
    VatCalculationResult calculate(Integer vatRate, Double vatAmount, Double netAmount, Double grossAmount);

    /**
     * This class is used internally
     */
    class VatCalculationResult {

        private Integer vatRate;
        private Double vatAmount;
        private Double netAmount;
        private Double grossAmount;

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