package com.elicius.timr;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Testklasse der zentralen Klasse Timer
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
    public void testCompareTo() {
        Timer t1 = new Timer(1,1,1);
        // t1 größer
        assertEquals("compareTo-Methode funktioniert nicht", t1.compareTo(new Timer(1,1,0)), 1);
        assertEquals("compareTo-Methode funktioniert nicht", t1.compareTo(new Timer(1,0,1)), 1);
        assertEquals("compareTo-Methode funktioniert nicht", t1.compareTo(new Timer(0,1,1)), 1);

        // t1 gleich
        assertEquals("compareTo-Methode funktioniert nicht", t1.compareTo(new Timer(1,1,1)), 0);

        // t1 kleiner
        assertEquals("compareTo-Methode funktioniert nicht", t1.compareTo(new Timer(1,1,2)), -1);
        assertEquals("compareTo-Methode funktioniert nicht", t1.compareTo(new Timer(1,2,1)), -1);
        assertEquals("compareTo-Methode funktioniert nicht", t1.compareTo(new Timer(2,1,1)), -1);
    }

    @Test
    public void testToString() {
        Timer timer = new Timer(1, 2, 3);
        assertEquals("toString-Methode funktioniert nicht", timer.toString(), "03:02:01");
        Timer timer1 = new Timer(10, 9, 8);
        assertEquals("toString-Methode funktioniert nicht", timer1.toString(), "08:09:10");
        Timer timer2 = new Timer(11,11,11);
        assertEquals("toString-Methode funktioniert nicht", timer2.toString(), "11:11:11");
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