package com.leothosthoren.go4lunch;

import com.leothosthoren.go4lunch.utils.HttpRequestTools;
import com.leothosthoren.go4lunch.utils.StringHelper;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest implements HttpRequestTools, StringHelper {
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
        assertEquals("Vador", displayFirstName("Vador Dark"));

    }
}