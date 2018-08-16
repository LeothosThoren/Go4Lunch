package com.leothosthoren.go4lunch;

import com.leothosthoren.go4lunch.model.detail.Period;
import com.leothosthoren.go4lunch.utils.DataConverterHelper;
import com.leothosthoren.go4lunch.utils.HttpRequestTools;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest implements HttpRequestTools, DataConverterHelper {

    //==========================
    // STRING FORMAT OPERATIONS
    //==========================

    @Test
    public void setValuesIntoString() throws Exception {
        assertEquals("48.7927684,2.3591994999999315", setLocationIntoString(48.7927684, 2.3591994999999315));
    }

    @Test
    public void splitStringValues() throws Exception {
        assertEquals("Vador", formatFullName("Vador Dark"));

    }

    @Test
    public void formatAddress() throws Exception {
        assertEquals("48 Pirrama Rd", formatAddress("48 Pirrama Rd, Pyrmont NSW 2009, Australie"));

    }

    @Test
    public void substringValues() throws Exception {
        //Format weekDayText
        List<String> l = new ArrayList<>();
        l.add("Monday: 10:00 AM – 5:00 PM");
        l.add("Tuesday: 10:00 AM – 5:00 PM");
        l.add("Wednesday: 10:00 AM – 5:00 PM");
        l.add("Thursday: 12:00 – 2:00 PM, 7:30 – 10:00 PM");
        l.add("Friday: 10:00 AM – 5:00 PM");
        l.add("Saturday: 10:00 AM – 5:00 PM");
        l.add("Sunday: 10:00 AM – 5:00 PM");


        assertEquals(12, convertStringIdIntoInteger("m13"));
        assertEquals("12:00 – 2:00 pm,7:30 – 10:00 pm", formatWeekDayText(l));
    }

    //==========================
    // NUMBER FORMAT OPERATIONS
    //==========================

    @Test
    public void computeRating() throws Exception {
        assertEquals(2.0f, formatRating(4.0d), 0.0);
        assertEquals(1.0f, formatRating(2.0d), 0.0);

    }

    @Test
    public void computeDistance() throws Exception {
        //device 48.813326, 2.348383
        //restaurant Sherazade 48.814640, 2.344735
        // maison Doisneau 48.814386, 2.347596

        assertEquals("304m", computeDistance(48.813326, 48.814640, 2.348383, 2.344735));
        assertEquals("131m", computeDistance(48.813326, 48.814386, 2.348383, 2.347596));

    }

    //==========================
    // NUMBER FORMAT OPERATIONS
    //==========================

    @Test
    public void formatDate() throws Exception {
        assertEquals("16/08/2018", formatDate(Calendar.getInstance().getTime()));
    }

    @Test
    public void displayOpeningHour() throws Exception {
        List<Period> periods = new ArrayList<>();

        assertEquals("Closed", formatOpeningTime(false, periods));
//        assertEquals("1730pm", formatOpeningTime(true, periods));
    }

    @Test
    public void getCurrentDayOftheWeek() throws Exception {
        //Change the expected result with the current day
        assertEquals(3, dayOfWeek(), 0.0);
    }
}