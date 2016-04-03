package com.gb.app.rest.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 *
 * @author Nikola Kocic
 * @since 02.04.2016
 */
public interface VatRestConsumerService {

	ConsumerCalculationResult calculate(String url, Integer vatRate, Double vatAmount, Double netAmount, Double grossAmount);

	/**
	 * This class is used internally
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	class ConsumerCalculationResult {

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

		@Override
		public String toString() {
			return "ConsumerCalculationResult{" +
					"vatRate=" + vatRate +
					", vatAmount=" + vatAmount +
					", netAmount=" + netAmount +
					", grossAmount=" + grossAmount +
					'}';
		}
	}
}