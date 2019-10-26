package katae.calculators;

import org.junit.Test;

import static katae.calculators.ToyCalculator.*;

import static org.junit.Assert.*;

public class ToyCalculatorTest {

    @Test
    public void testToyAdd() {
        System.out.println("toyAdd");
        int expected = 2;
        int actual = toyAdd(1, 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testToyAddRandomNumbers() {
        int randomA = (int) Math.floor(Math.random() * 1048576);
        int randomB = (int) Math.floor(Math.random() * 16384 - 4096);
        int expected = randomA + randomB;
        System.out.println(randomA + " + " + randomB + " = " + expected);
        int actual = toyAdd(randomA, randomB);
        assertEquals(expected, actual);
    }

    @Test
    public void testToySubtract() {
        System.out.println("toySubtract");
        int expected = -10;
        int actual = toySubtract(43, 53);
        assertEquals(expected, actual);
    }

    @Test
    public void testToySubtractRandomNumbers() {
        int randomA = (int) Math.floor(Math.random() * 1048576);
        int randomB = (int) Math.floor(Math.random() * 16384 - 4096);
        int expected = randomA - randomB;
        System.out.println(randomA + " - " + randomB + " = " + expected);
        int actual = toySubtract(randomA, randomB);
        assertEquals(expected, actual);
    }

    @Test
    public void testToyMultiply() {
        System.out.println("toyMultiply");
        int expected = 12;
        int actual = toyMultiply(4, 3);
        assertEquals(expected, actual);
    }

    @Test
    public void testToyMultiplyRandomNumbers() {
        int randomA = (int) Math.floor(Math.random() * 65536);
        int randomB = (int) Math.floor(Math.random() * 512 - 128);
        int expected = randomA * randomB;
        System.out.println(randomA + " \u00D7 " + randomB + " = " + expected);
        int actual = toyMultiply(randomA, randomB);
        assertEquals(expected, actual);
    }

    @Test
    public void testToyReciprocal() {
        System.out.println("toyReciprocal");
        double expected = 0.14285714;
        double actual = toyReciprocal(7);
        assertEquals(expected, actual, 0.00000001);
    }

    @Test
    public void testToyReciprocalRandomNumber() {
        int randomN = 1 + (int) Math.floor(Math.random() * 128);
        double expected = 1 / (double) randomN;
        System.out.println("1/" + randomN + " = " + expected);
        double actual = toyReciprocal(randomN);
        assertEquals(expected, actual, 0.00000001);
    }

}