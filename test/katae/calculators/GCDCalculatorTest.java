package katae.calculators;

import org.junit.Test;

import static katae.calculators.GCDCalculator.gcd;

import static org.junit.Assert.*;

public class GCDCalculatorTest {

    @Test
    public void testGCDOnSameNumber() {
        int expected = 10;
        int actual = gcd(10, 10);
        assertEquals(expected, actual);
    }

    @Test
    public void testGCDAdditiveInverse() {
        int expected = 12;
        int actual = gcd(-12, 12);
        assertEquals(expected, actual);
    }

    @Test
    public void testGCDMultiplesOf103() {
        int expected = 103;
        int a, b, actual;
        for (int n = 108; n < 206; n++) {
            a = -103 * n;
            b = 103 * n + 11021;
            actual = gcd(a, b);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testGCDWithZero() {
        int expected = 109;
        int actual = gcd(0, expected);
        assertEquals(expected, actual);
        actual = gcd(expected, 0);
        assertEquals(expected, actual);
    }

}