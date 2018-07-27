package com.leothosthoren.go4lunch.utils;

import java.text.DecimalFormat;

public interface DataConvertHelper {

    default String formatFullName(String fullName) {
        String[] firstName = fullName.split(" ");
        return firstName[0];
    }

    default float formatRating(double rating) {
        DecimalFormat df = new DecimalFormat("#.#");
        rating = Math.round(rating * 3) / 5;
        rating = Double.parseDouble(df.format(rating));
        return (float) rating;
    }

    default String formatAddress(String address) {
        String [] newAddress = address.split(",");
        return newAddress[0];
    }
}
