package com.example.nicko.turuntanganmalangapps.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by nicko on 8/3/2017.
 */

public class NumberFormatter {
    public static String money(double n) {
        DecimalFormat indonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        indonesia.setDecimalFormatSymbols(formatRp);
        indonesia.setMinimumFractionDigits(0);
        return indonesia.format(n) + ",00";
    }

    public static String number_separator(int n) {
        DecimalFormat number = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatNumber = new DecimalFormatSymbols();

        formatNumber.setCurrencySymbol("");
        formatNumber.setMonetaryDecimalSeparator(',');
        formatNumber.setGroupingSeparator('.');

        number.setDecimalFormatSymbols(formatNumber);
        number.setMinimumFractionDigits(0);
        return number.format(n);
    }
}
