package com.ai.aif.osp.jstorm.util;

import java.nio.charset.Charset;

public class Charsets {

    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    public static final Charset UTF_16 = Charset.forName("UTF-16");
    public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
    public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset GBK = Charset.forName("GBK");

    /**
     * Converts a digit into an uppercase hexadecimal character or the null character if invalid.
     *
     * @param digit a number 0 - 15
     * @return the hex character for that digit or '\0' if invalid
     */
    public static char getUpperCaseHex(final int digit) {
        if (digit < 0 || digit >= 16) {
            return '\0';
        }
        return digit < 10 ? getNumericalDigit(digit) : getUpperCaseAlphaDigit(digit);
    }

    /**
     * Converts a digit into an lowercase hexadecimal character or the null character if invalid.
     *
     * @param digit a number 0 - 15
     * @return the hex character for that digit or '\0' if invalid
     */
    public static char getLowerCaseHex(final int digit) {
        if (digit < 0 || digit >= 16) {
            return '\0';
        }
        return digit < 10 ? getNumericalDigit(digit) : getLowerCaseAlphaDigit(digit);
    }

    private static char getNumericalDigit(final int digit) {
        return (char) ('0' + digit);
    }

    private static char getUpperCaseAlphaDigit(final int digit) {
        return (char) ('A' + digit - 10);
    }

    private static char getLowerCaseAlphaDigit(final int digit) {
        return (char) ('a' + digit - 10);
    }

    private Charsets() {
    }
}
