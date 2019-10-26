package katae.fractions;

import katae.calculators.NTFC;

import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the Fraction class.
 * @author Alonso del Arte
 */
public class FractionTest {

    /**
     * The test delta, 10<sup>&minus;8</sup>.
     */
    private static final double TEST_DELTA = 0.00000001;

    /**
     * This one should be initialized to 7/8 in setUp().
     */
    private Fraction operandA;

    /**
     * This one should be initialized to 1/3 in setUp().
     */
    private Fraction operandB;

    /**
     * Initializes two Fraction objects that will be used in a lot of the tests.
     */
    @Before
    public void setUp() {
        operandA = new Fraction(7, 8);
        operandB = new Fraction(1, 3);
    }

    /**
     * Test of getNumerator method, of class Fraction.
     */
    @Test
    public void testGetNumerator() {
        System.out.println("getNumerator");
        long expResult = 7L;
        long result = operandA.getNumerator();
        assertEquals(expResult, result);
        expResult = 1L;
        result = operandB.getNumerator();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDenominator method, of class Fraction.
     */
    @Test
    public void testGetDenominator() {
        System.out.println("getDenominator");
        long expResult = 8L;
        long result = operandA.getDenominator();
        assertEquals(expResult, result);
        expResult = 3L;
        result = operandB.getDenominator();
        assertEquals(expResult, result);
    }

    /**
     * Test of plus method, of class Fraction.
     */
    @Test
    public void testPlusSameDenom() {
        System.out.println("plus");
        Fraction addendA = new Fraction(5, 13);
        Fraction addendB = new Fraction(7, 13);
        Fraction expected = new Fraction(12, 13);
        Fraction actual = addendA.plus(addendB);
        assertEquals(expected, actual);
        actual = addendB.plus(addendA);
        assertEquals(expected, actual); // Commutative check
    }

    /**
     * Test of plus method, of class Fraction.
     */
    @Test
    public void testPlusDiffDenom() {
        Fraction addendA = new Fraction(5, 12);
        Fraction addendB = new Fraction(7, 13);
        Fraction expected = new Fraction(149, 156);
        Fraction actual = addendA.plus(addendB);
        assertEquals(expected, actual);
        actual = addendB.plus(addendA);
        assertEquals(expected, actual); // Commutative check
    }

    /**
     * Test of minus method, of class Fraction.
     */
    @Test
    public void testMinusSameDenom() {
        System.out.println("minus");
        Fraction minuend = new Fraction(5, 13);
        Fraction subtrahend = new Fraction(7, 13);
        Fraction expected = new Fraction(-2, 13);
        Fraction actual = minuend.minus(subtrahend);
        assertEquals(expected, actual);
    }

    /**
     * Test of minus method, of class Fraction.
     */
    @Test
    public void testMinusDiffDenom() {
        Fraction minuend = new Fraction(5, 12);
        Fraction subtrahend = new Fraction(7, 13);
        Fraction expected = new Fraction(-19, 156);
        Fraction actual = minuend.minus(subtrahend);
        assertEquals(expected, actual);
    }

    /**
     * Test of times method, of class Fraction.
     */
    @Test
    public void testTimes() {
        System.out.println("times");
        Fraction testMultiplicandA = new Fraction(7, 8);
        Fraction testMultiplicandB = new Fraction(1, 3);
        Fraction expected = new Fraction(7, 24);
        Fraction actual = testMultiplicandA.times(testMultiplicandB);
        assertEquals(expected, actual);
        actual = testMultiplicandB.times(testMultiplicandA); // Commutative test
        assertEquals(expected, actual);
        expected = new Fraction(21, 2);
        actual = testMultiplicandA.times(12);
        assertEquals(expected, actual);
        expected = new Fraction(4);
        actual = testMultiplicandB.times(12);
        assertEquals(expected, actual);
    }

    /**
     * Test of dividedBy method, of class Fraction.
     */
    @Test
    public void testDividedBy() {
        System.out.println("dividedBy");
        Fraction testDividend = new Fraction(7, 8);
        Fraction testDivisor = new Fraction(1, 3);
        Fraction expected = new Fraction(21, 8);
        Fraction actual = testDividend.divides(testDivisor);
        assertEquals(expected, actual);
        expected = new Fraction(7, 96);
        actual = testDividend.divides(12);
        assertEquals(expected, actual);
        expected = new Fraction(1, 36);
        actual = testDivisor.divides(12);
        assertEquals(expected, actual);
    }

    /**
     * Test of dividedBy method, of class Fraction. Trying to divide by zero
     * should cause either IllegalArgumentException or
     * ArithmeticException. Any other exception will fail the test. So will
     * giving any kind of result.
     */
    @Test
    public void testDivisionByZeroCausesException() {
        System.out.println("Division by zero should cause an exception");
        Fraction testDividend = new Fraction(53, 60);
        Fraction zero = new Fraction(0, 1);
        Fraction result;
        try {
            result = testDividend.divides(zero);
            String failMessage = testDividend.toString() + " divided by 0 should have caused an Exception, not given result " + result.toString();
            fail(failMessage);
        } catch (IllegalArgumentException iae) {
            System.out.println("IllegalArgumentException is considered preferable for division by zero.");
            System.out.println("\"" + iae.getMessage() + "\"");
        } catch (ArithmeticException ae) {
            System.out.println("ArithmeticException is deemed acceptable for division by zero.");
            System.out.println("\"" + ae.getMessage() + "\"");
        } catch (Exception e) {
            String failMessage = e.getClass().getName() + " is not an appropriate exception for division by zero.";
            fail(failMessage);
        }
    }

    /**
     * Test of negate method, of class Fraction. Applying negate twice should
     * return the original number.
     */
    @Test
    public void testNegate() {
        System.out.println("negate");
        Fraction testFraction = new Fraction(40, 81);
        Fraction expected = new Fraction(-40, 81);
        Fraction actual = testFraction.negate();
        assertEquals(expected, actual);
        actual = expected.negate();
        assertEquals(testFraction, actual);
    }

    /**
     * Test of toString method, of class Fraction. Spaces are acceptable in the
     * output, so this test strips them out.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Fraction testFraction = new Fraction(13, 12);
        String expected = "13/12";
        String actual = testFraction.toString().replace(" ", "");
        assertEquals(expected, actual);
    }

    /**
     * Test of toHTMLString method, of class Fraction. Spaces are acceptable in
     * the output, so this test strips them out.
     */
    @Test
    public void testToHTMLString() {
        System.out.println("toHTMLString");
        Fraction testFraction = new Fraction(7, 12);
        String expected = "<sup>7</sup>&frasl;<sub>12</sub>";
        String actual = testFraction.toHTMLString().replace(" ", "");
        assertEquals(expected, actual);
    }

    /**
     * Test of toTeXString method, of class Fraction. Spaces are acceptable in
     * the output, so this test strips them out.
     */
    @Test
    public void testToTeXString() {
        System.out.println("toTeXString");
        Fraction testFraction = new Fraction(8, 5);
        String expected = "\\frac{8}{5}";
        String actual = testFraction.toTeXString().replace(" ", "");
        assertEquals(expected, actual);
    }

    /**
     * Test of isUnitFraction method, of class Fraction.
     */
    @Test
    public void testIsUnitFraction() {
        System.out.println("isUnitFraction");
        Fraction testFraction = new Fraction(1, 441);
        String assertionMessage = testFraction.toString() + " should be recognized as a unit fraction";
        assertTrue(assertionMessage, testFraction.isUnitFraction());
        testFraction = new Fraction(8, 5);
        assertionMessage = testFraction.toString() + " should not be recognized as a unit fraction";
        assertFalse(assertionMessage, testFraction.isUnitFraction());
    }

    /**
     * Test of toString, toHTMLString, toTeXString methods of class Fraction.
     * Fractions that are integers should be reported with the denominator of 1
     * as tacit, not explicit.
     */
    @Test
    public void testIntegersHaveTacitDenominator() {
        System.out.println("Integers should be reported with a tacit, not explicit, denominator");
        Fraction actualInt = new Fraction(12, 1);
        assertEquals("12", actualInt.toString());
        assertEquals("12", actualInt.toHTMLString());
        assertEquals("12", actualInt.toTeXString());
    }

    /**
     * Test of hashCode method, of class Fraction.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Fraction testFractionA = new Fraction(43, 20);
        Fraction testFractionB = new Fraction(47, 80);
        int operAHash = testFractionA.hashCode();
        int operBHash = testFractionB.hashCode();
        System.out.println(testFractionA.toString() + " hashed as " + operAHash);
        System.out.println(testFractionB.toString() + " hashed as " + operBHash);
        Fraction operADup = new Fraction(43, 20);
        Fraction operBDup = new Fraction(47, 80);
        int operAHashDup = operADup.hashCode();
        int operBHashDup = operBDup.hashCode();
        System.out.println(operADup.toString() + " hashed as " + operAHashDup);
        System.out.println(operBDup.toString() + " hashed as " + operBHashDup);
        String assertionMessage = testFractionA.toString() + " and " + operADup.toString() + " should hash the same";
        assertEquals(assertionMessage, operAHash, operAHashDup);
        assertionMessage = testFractionB.toString() + " and " + operBDup.toString() + " should hash the same";
        assertEquals(assertionMessage, operBHash, operBHashDup);
        assertionMessage = testFractionA.toString() + " and " + operBDup.toString() + " should hash differently";
        assertNotEquals(assertionMessage, operAHash, operBHashDup);
        assertionMessage = testFractionB.toString() + " and " + operADup.toString() + " should hash differently";
        assertNotEquals(assertionMessage, operBHash, operAHashDup);
    }

    /**
     * Test of hashCode method, of class Fraction. More than 400 instances of
     * Fraction are created, representing distinct fractions with denominators
     * ranging from 2 to 103. They should all get distinct hash codes.
     */
    @Test
    public void testHashCodeOnManyFractions() {
        System.out.println("hashCode on several instances of Fraction");
        HashSet<Integer> fractHashes = new HashSet<>(435);
        Fraction currUnitFract, currFract;
        int currHash, currSize, prevSize;
        currUnitFract = new Fraction(1, 102);
        currHash = currUnitFract.hashCode();
        fractHashes.add(currHash);
        currUnitFract = new Fraction(1, 103);
        currHash = currUnitFract.hashCode();
        fractHashes.add(currHash);
        String assertionMessage = "Set of Fraction hash codes should have two hash codes";
        assertEquals(assertionMessage, 2, fractHashes.size());
        prevSize = 2;
        for (int n = 2; n < 102; n++) {
            if (NTFC.euclideanGCD(n, 102) == 1) {
                currUnitFract = new Fraction(1, n);
                currHash = currUnitFract.hashCode();
                fractHashes.add(currHash);
                currSize = fractHashes.size();
                assertionMessage = "Hash code " + currHash + " for " + currUnitFract.toString() + " should be distinct.";
                assertTrue(assertionMessage, currSize > prevSize);
                prevSize++;
            }
            currFract = new Fraction(n, 102);
            currHash = currFract.hashCode();
            fractHashes.add(currHash);
            currSize = fractHashes.size();
            assertionMessage = "Hash code " + currHash + " for " + currFract.toString() + " should be distinct.";
            assertTrue(assertionMessage, currSize > prevSize);
            prevSize++;
            currFract = new Fraction(-n, 102);
            currHash = currFract.hashCode();
            fractHashes.add(currHash);
            currSize = fractHashes.size();
            assertionMessage = "Hash code " + currHash + " for " + currFract.toString() + " should be distinct.";
            assertTrue(assertionMessage, currSize > prevSize);
            prevSize++;
            currFract = new Fraction(n, 103);
            currHash = currFract.hashCode();
            fractHashes.add(currHash);
            currSize = fractHashes.size();
            assertionMessage = "Hash code " + currHash + " for " + currFract.toString() + " should be distinct.";
            assertTrue(assertionMessage, currSize > prevSize);
            prevSize++;
            currFract = new Fraction(-n, 103);
            currHash = currFract.hashCode();
            fractHashes.add(currHash);
            currSize = fractHashes.size();
            assertionMessage = "Hash code " + currHash + " for " + currFract.toString() + " should be distinct.";
            assertTrue(assertionMessage, currSize > prevSize);
            prevSize++;
        }
        System.out.println("Successfully created " + prevSize + " instances of Fraction with " + prevSize + " distinct hash codes.");
    }

    /**
     * Test of equals method, of class Fraction.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Fraction testFractionA = new Fraction(22, 7);
        Fraction testFractionB = new Fraction(355, 113);
        assertEquals(testFractionA, testFractionA); // Reflexive test
        assertEquals(testFractionB, testFractionB);
        Fraction tempHold = new Fraction(22, 7);
        assertEquals(testFractionA, tempHold);
        assertEquals(tempHold, testFractionA); // Symmetric test
        Fraction transitiveHold = new Fraction(22, 7);
        assertEquals(tempHold, transitiveHold);
        assertEquals(transitiveHold, tempHold); // Transitive test
    }

    /**
     * Another test of equals method, of class Fraction. A Fraction object
     * should not be found to be equal to an object of a different class (except
     * perhaps a class subclassed from Fraction).
     */
    @Test
    public void testDoesNotEqualObjectOtherClass() {
        SQLClientInfoException sqle = new SQLClientInfoException();
        Fraction testFraction = new Fraction(22, 7);
        assertNotEquals(testFraction, sqle);
    }

    /**
     * Test of compareTo method, of class Fraction, implementing
     * Comparable.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        Fraction testFractionA = new Fraction(7, 8);
        Fraction testFractionB = new Fraction(1, 3);
        Fraction negThreeHalves = new Fraction(-3, 2);
        Fraction approxPiFiftieths = new Fraction(157, 50);
        Fraction approxPi113ths = new Fraction(355, 113);
        Fraction approxPiSevenths = new Fraction(22, 7);
        int comparison = negThreeHalves.compareTo(approxPiFiftieths);
        String assertionMessage = negThreeHalves.toString() + " should be found to be less than " + approxPiFiftieths.toString();
        assertTrue(assertionMessage, comparison < 0);
        comparison = testFractionB.compareTo(testFractionA);
        assertionMessage = testFractionB.toString() + " should be found to be less than " + testFractionA.toString();
        assertTrue(assertionMessage, comparison < 0);
        comparison = approxPiFiftieths.compareTo(approxPi113ths);
        assertionMessage = approxPiFiftieths.toString() + " should be found to be less than " + approxPi113ths.toString();
        assertTrue(assertionMessage, comparison < 0);
        comparison = approxPi113ths.compareTo(approxPiSevenths);
        assertionMessage = approxPi113ths.toString() + " should be found to be less than " + approxPiSevenths.toString();
        assertTrue(assertionMessage, comparison < 0);
        comparison = approxPiFiftieths.compareTo(approxPiFiftieths);
        assertEquals(0, comparison);
        comparison = approxPi113ths.compareTo(approxPi113ths);
        assertEquals(0, comparison);
        comparison = approxPiSevenths.compareTo(approxPiSevenths);
        assertEquals(0, comparison);
        comparison = testFractionA.compareTo(testFractionB);
        assertionMessage = testFractionA.toString() + " should be found to be greater than " + testFractionB.toString();
        assertTrue(assertionMessage, comparison > 0);
        comparison = approxPiFiftieths.compareTo(negThreeHalves);
        assertionMessage = approxPiFiftieths.toString() + " should be found to be greater than " + negThreeHalves.toString();
        assertTrue(assertionMessage, comparison > 0);
        comparison = approxPi113ths.compareTo(approxPiFiftieths);
        assertionMessage = approxPi113ths.toString() + " should be found to be greater than " + approxPiFiftieths.toString();
        assertTrue(assertionMessage, comparison > 0);
        comparison = approxPiSevenths.compareTo(approxPi113ths);
        assertionMessage = approxPiSevenths.toString() + " should be found to be greater than " + approxPi113ths.toString();
        assertTrue(assertionMessage, comparison > 0);
        try {
            comparison = approxPiSevenths.compareTo(null);
            String failMsg = "Comparing " + approxPiSevenths.toString() + " to null should have caused an exception, not given result " + comparison + ".";
            fail(failMsg);
        } catch (NullPointerException npe) {
            System.out.println("Comparing " + approxPiSevenths.toString() + " to null correctly triggered NullPointerException.");
            System.out.println("NullPointerException had this message: \"" + npe.getMessage() + "\"");
        } catch (Exception e) {
            String failMsg = e.getClass().getName() + " is the wrong exception to throw for comparing " + approxPiSevenths.toString() + " to null.";
            fail(failMsg);
        }
    }

    /**
     * Another test of compareTo method, of class Fraction. This one tests
     * whether Fraction can distinguish between 1/9223372036854775807 and
     * 1/9223372036854775806.
     */
    @Ignore
    @Test
    public void testCompareToCloseFraction() {
        Fraction numberA = new Fraction(1, Integer.MAX_VALUE);
        Fraction numberB = new Fraction(1, Integer.MAX_VALUE - 1);
        String assertionMessage = numberA.toString() + " should be found to be less than " + numberB.toString();
        assertTrue(assertionMessage, numberA.compareTo(numberB) < 0);
        assertionMessage = numberB.toString() + " should be found to be greater than " + numberA.toString();
        assertTrue(assertionMessage, numberB.compareTo(numberA) > 0);
    }

    /**
     * Yet another test of compareTo method, of class Fraction. This one checks
     * that {@link Collections#sort(java.util.List)} can use compareTo to sort a
     * list of fractions in ascending order.
     */
    @Test
    public void testCompareToThroughCollectionSort() {
        Fraction negThreeHalves = new Fraction(-3, 2);
        Fraction approxPiFiftieths = new Fraction(157, 50);
        Fraction approxPi113ths = new Fraction(355, 113);
        Fraction approxPiSevenths = new Fraction(22, 7);
        List<Fraction> expectedList = new ArrayList<>();
        expectedList.add(negThreeHalves);
        expectedList.add(operandB);
        expectedList.add(operandA);
        expectedList.add(approxPiFiftieths);
        expectedList.add(approxPi113ths);
        expectedList.add(approxPiSevenths);
        List<Fraction> unsortedList = new ArrayList<>();
        unsortedList.add(approxPi113ths);
        unsortedList.add(negThreeHalves);
        unsortedList.add(approxPiSevenths);
        unsortedList.add(operandA);
        unsortedList.add(approxPiFiftieths);
        unsortedList.add(operandB);
        Collections.sort(unsortedList);
        assertEquals(expectedList, unsortedList);
    }

    /**
     * Test of getNumericApproximation method, of class Fraction.
     */
    @Test
    public void testGetNumericApproximation() {
        System.out.println("getNumericApproximation");
        double expResult = 0.875;
        double result = operandA.getNumericApproximation();
        assertEquals(expResult, result, TEST_DELTA);
        expResult = 0.33333333;
        result = operandB.getNumericApproximation();
        assertEquals(expResult, result, TEST_DELTA);
    }

    /**
     * Test of reciprocal method, of class Fraction. Checks that applying the
     * reciprocal function to a reciprocal returns the original number.
     */
    @Test
    public void testReciprocal() {
        System.out.println("reciprocal");
        Fraction expResult = new Fraction(8, 7);
        Fraction result = operandA.reciprocal();
        assertEquals(expResult, result);
        result = expResult.reciprocal();
        assertEquals(operandA, result);
        expResult = new Fraction(3);
        result = operandB.reciprocal();
        assertEquals(expResult, result);
        result = expResult.reciprocal();
        assertEquals(operandB, result);
    }

    /**
     * Another test of reciprocal method, of class Fraction. Checks that trying
     * to take the reciprocal of zero triggers the appropriate exception.
     */
    @Test
    public void testReciprocalOfZero() {
        System.out.println("reciprocal(0)");
        Fraction zero = new Fraction(0, 1);
        try {
            Fraction result = zero.reciprocal();
            String failMessage = "Trying to get reciprocal of 0 should have caused an exception, not given result " + result.toString();
            fail(failMessage);
        } catch (IllegalArgumentException iae) {
            System.out.println("IllegalArgumentException is appropriate for trying to take the reciprocal of 0.");
            System.out.println("\"" + iae.getMessage() + "\"");
        } catch (ArithmeticException ae) {
            System.out.println("ArithmeticException is deemed acceptable for trying to take the reciprocal of 0.");
            System.out.println("\"" + ae.getMessage() + "\"");
        } catch (Exception e) {
            String failMessage = e.getClass().getName() + " is not an appropriate exception for trying to take the reciprocal of 0.";
            fail(failMessage);
        }
    }

    /**
     * Test of Fraction constructor. Even if the constructor parameters are not
     * in lowest terms, the constructor should change them to lowest terms.
     */
    @Test
    public void testFractionsAreInLowestTerms() {
        System.out.println("Fractions should be in lowest terms");
        Fraction fraction = new Fraction(21, 24);
        assertEquals(operandA, fraction);
        assertEquals(7L, fraction.getNumerator());
        assertEquals(8L, fraction.getDenominator());
        fraction = new Fraction(8, 24);
        assertEquals(operandB, fraction);
        assertEquals(1L, fraction.getNumerator());
        assertEquals(3L, fraction.getDenominator());
    }

    /**
     * Test of Fraction constructor. Even if the denominator passed to the
     * constructor is negative, the constructor should change it to a positive
     * integer.
     */
    @Test
    public void testNegativeDenomsQuietlyChanged() {
        System.out.println("Negative denominators should be quietly changed to positive");
        Fraction fraction = new Fraction(12, -29);
        assertEquals(-12L, fraction.getNumerator());
        assertEquals(29L, fraction.getDenominator());
    }

}
