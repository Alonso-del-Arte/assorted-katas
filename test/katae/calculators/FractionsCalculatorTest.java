/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package katae.calculators;

import katae.fractions.Fraction;

import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests of the FractionsCalculator class.
 * @author Alonso del Arte
 */
public class FractionsCalculatorTest {

    /**
     * Test of placeInUnitInterval method, of class FractionsCalculator.
     */
    @Test
    public void testPlaceInUnitInterval() {
        System.out.println("placeInUnitInterval");
        Fraction fraction = new Fraction(13, 4);
        Fraction expResult = new Fraction(1, 4);
        Fraction result = FractionsCalculator.placeInUnitInterval(fraction);
        assertEquals(expResult, result);
    }

    /**
     * Test of getEgyptianFractionsGreedyCapped method, of class
     * FractionsCalculator.
     */
    @Test
    public void testGetEgyptianFractionsGreedyCapped() {
        System.out.println("getEgyptianFractionsGreedyCapped");
        Fraction fraction = new Fraction(7, 8);
        Fraction oneHalf = new Fraction(1, 2);
        Fraction oneThird = new Fraction(1, 3);
        Fraction one24th = new Fraction(1, 24);
        ArrayList<Fraction> expResult = new ArrayList<>();
        expResult.add(oneHalf);
        expResult.add(oneThird);
        expResult.add(one24th);
        ArrayList<Fraction> result = FractionsCalculator.getEgyptianFractionsGreedyCapped(fraction);
        assertEquals(expResult, result);
    }

    /**
     * Test of getEgyptianFractionsByDenomDivisors method, of class
     * FractionsCalculator.
     */
    @Test
    public void testGetEgyptianFractionsByDenomDivisors() {
        System.out.println("getEgyptianFractionsByDenomDivisors");
        Fraction fraction = new Fraction(7, 8);
        Fraction oneHalf = new Fraction(1, 2);
        Fraction oneQuarter = new Fraction(1, 4);
        Fraction oneEighth = new Fraction(1, 8);
        ArrayList<Fraction> expResult = new ArrayList<>();
        expResult.add(oneHalf);
        expResult.add(oneQuarter);
        expResult.add(oneEighth);
        ArrayList<Fraction> result = FractionsCalculator.getEgyptianFractionsByDenomDivisors(fraction);
        assertEquals(expResult, result);
        expResult.clear();
        Fraction oneThird = new Fraction(1, 3);
        Fraction oneNinth = new Fraction(1, 9);
        expResult.add(oneThird);
        expResult.add(oneNinth);
        expResult.add(oneNinth);
        fraction = new Fraction(5, 9);
        result = FractionsCalculator.getEgyptianFractionsByDenomDivisors(fraction);
        assertEquals(expResult, result);
    }

}
