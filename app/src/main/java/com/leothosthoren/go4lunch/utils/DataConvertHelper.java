package com.leothosthoren.go4lunch.utils;

import com.leothosthoren.go4lunch.model.detail.Period;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
            } else {
                for (Period period : listPeriod) {
                    if (period.getClose().getDay().equals(dayOfWeek()))
                        return "Open until " + period.getClose().getTime() + "pm";
                }
            }

        }
        return "Closed Soon";
    }

    default String formatTime() {

        return "";
    }

    default Integer dayOfWeek() {
        Date DATE = new Date();
        SimpleDateFormat DAY_OF_WEEK = new SimpleDateFormat("u", Locale.getDefault());
        return Integer.valueOf(DAY_OF_WEEK.format(DATE));
    }

    // Change marker id into integer id
    default int convertStringIdIntoInteger(String s) {
        String tmp = s.substring(1);
        return Integer.valueOf(tmp)-1;
    }
}
