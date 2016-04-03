package com.gb.app.util;

import java.io.Serializable;

/**
 * Global Blue's Hiring Exercise for Software Developer position.
 *
 * @author Nikola Kocic
 * @since 02.04.2016
 */
public class Assert implements Serializable {

    /**
     * Assert that passed objects is not null.
     *
     * @param object       - object to be checked for null
     * @param propertyName - property to be defined as a prt of validation message.
     * @throws IllegalArgumentException if issued object is <code>null</code>
     */
    public static void notNull(Object object, String propertyName) {
        if (object == null) {
            throw new IllegalArgumentException("Property '" + propertyName + "' is null and should not be!");
        }
    }

    /**
     * Assert that passed number is positive
     *
     * @param number - number to be issued
     */
    public static boolean positive(Double number) {
        return number == null || number >= 0;
    }

    /**
     * Assert that passed numbers are all positive
     *
     * @param numbers - numbers to be issued
     */
    public static boolean positive(Double... numbers) {
        Assert.notNull(numbers, "numbers");
        for (Double number : numbers) {
            if (!positive(number)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Assert that only one of the objects is not null.
     *
     * @param objects Object list to be checked for being null
     * @throws IllegalArgumentException if more than one of the objects is <code>not null</code>
     */
    public static boolean onlyOneNotNull(Object... objects) {
        boolean alreadyNotNull = false;
        for (Object object : objects) {
            if (alreadyNotNull && object != null) {
                return false;
            }
            if (object != null) {
                alreadyNotNull = true;
            }
        }
        return alreadyNotNull;
    }
}