package com.leothosthoren.go4lunch;

import com.leothosthoren.go4lunch.utils.HttpRequestTools;
import com.leothosthoren.go4lunch.utils.DataConvertHelper;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest implements HttpRequestTools, DataConvertHelper {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    //==========================
    // STRING FORMAT OPERATIONS
    //==========================

    @Test
    public void setValuesIntoString () throws Exception {
        assertEquals("48.7927684,2.3591994999999315", setLocationIntoString(48.7927684, 2.3591994999999315));
    }

    @Test
    public void splitStringValues () throws Exception {
        assertEquals("Vador", formatFullName("Vador Dark"));

    }

    @Test
    public void formatAddress () throws Exception {
        assertEquals("48 Pirrama Rd", formatAddress("48 Pirrama Rd, Pyrmont NSW 2009, Australie"));

    }

    //==========================
    // NUMBER FORMAT OPERATIONS
    //==========================

    @Test
    public void computeRating() throws Exception {
        assertEquals(2.0f, formatRating(4.0d), 0.0);
        assertEquals(1.0f, formatRating(2.0d), 0.0);

    }
}