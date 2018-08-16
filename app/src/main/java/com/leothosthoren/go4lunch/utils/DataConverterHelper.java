package com.leothosthoren.go4lunch.utils;

import com.leothosthoren.go4lunch.model.detail.Period;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public interface DataConverterHelper {

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
        String[] newAddress = address.split(",");
        return newAddress[0];
    }

    //Todo figure it out if it's working
    default String formatOpeningTime(boolean openNow, List<Period> listPeriod) {
        if (!openNow) {
            return "Closed";
        } else {
            if (listPeriod.size() == 7 || listPeriod.size() == 0) {
                return "Open 24/7";
            } else if (listPeriod.size() <= 6){
                return "Open until " + listPeriod.get(dayOfWeek()).getClose().getTime() + "pm";
            }
        }

        return "Closed Soon";
    }

    default String formatTime() {

        return "";
    }

    default Integer dayOfWeek() {
        Date date = new Date();
        SimpleDateFormat dayOfWeek = new SimpleDateFormat("u", Locale.getDefault());
        //Day number of week (1 = Monday, ..., 7 = Sunday) != Place api 0 = Monday
        return Integer.valueOf(dayOfWeek.format(date)) - 1;
    }

    // Change marker id into integer id
    default int convertStringIdIntoInteger(String s) {
        String tmp = s.substring(1);
        return Integer.valueOf(tmp) - 1;
    }

    default Integer convertStringIntoInteger(String s) {
        return Integer.valueOf(s);
    }

    // Find the distance between two coordinates
    default String distance(double deviceLat, double restaurantLat, double deviceLng, double restaurantLng) {

        final int R = 6371; // Radius of earth
        double latDistance = Math.toRadians(restaurantLat - deviceLat);
        double lonDistance = Math.toRadians(restaurantLng - deviceLng);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(deviceLat)) * Math.cos(Math.toRadians(restaurantLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = 0.0 - 0.0;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        //Rounded
        return Math.round(Math.sqrt(distance)) + "m";
    }

    default String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

}
