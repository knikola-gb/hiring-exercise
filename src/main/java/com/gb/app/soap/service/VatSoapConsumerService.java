package com.gb.app.soap.service;

import com.gb.hiring_exercise.GetVatResponse;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 *
 * @author Nikola Kocic
 * @since 02.04.2016
 */
public interface VatSoapConsumerService {

    String calculate(String url, String xmlMessage);

    GetVatResponse calculate(String url, Integer vatRate, Double vatAmount, Double netAmount, Double grossAmount);

}