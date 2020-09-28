package com.elicius.timr;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestTimr {
    @Test
    public void testEquals() {
        Timer timer1 = new Timer(1, 2, 3);
        Timer timer2 = new Timer(1, 2, 3);
        Timer timer3 = new Timer(1, 2, 4);
        assertTrue(timer1.equals(timer2));
        assertFalse(timer1.equals(timer3));
    }

    @Test
    public void testToString() {
        Timer timer = new Timer(1, 2, 3);
        assertEquals("toString-Methode funktioniert nicht", timer.toString(), "3 : 2 : 1");
    }

    @Test
    public void testGetSeconds() {
        Timer t = new Timer(1, 2, 3);
        assertEquals("Methode getSeconds liefert falschen Wert", t.getSeconds(), 1);
    }

    @Test
    public void testGetMinutes() {
        Timer t = new Timer(1, 2, 3);
        assertEquals("Methode getMinutes liefert falschen Wert", t.getMinutes(), 2);
    }

    @Test
    public void testGetHours() {
        Timer t = new Timer(1, 2, 3);
        assertEquals("Methode getHours liefert falschen Wert", t.getHours(), 3);
    }
}