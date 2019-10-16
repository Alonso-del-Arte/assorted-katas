/*
 * Copyright (C) 2019 Alonso del Arte
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package katae.calculators;

import katae.fractions.Fraction;

import java.util.ArrayList;

/**
 * Performs calculations pertaining to fractions. Such as providing two
 * different algorithms for computing the Egyptian fractions for a given
 * fraction.
 * @author Alonso del Arte
 */
public class FractionsCalculator {

    private static final int GREEDY_CAP_DENOM = 1441;

    private static final Fraction ZERO_FRACTION = new Fraction(0);
    private static final Fraction ONE_FRACTION = new Fraction(1);

    /**
     * Places a fraction into the unit interval, so that it is no greater than 1
     * but greater than 0.
     * @param fraction The fraction to be placed into the unit interval, such as
     * &minus;1/2, 3/2, 5/2, 7/2.
     * @return The fraction in the unit interval. For example, given any of
     * &minus;1/2, 3/2, 5/2, 7/2, the result will be 1/2. Given 1/2, the result
     * will also be 1/2.
     */
    public static Fraction placeInUnitInterval(Fraction fraction) {
        Fraction placed = fraction;
        while (placed.compareTo(ONE_FRACTION) > 0) {
            placed = placed.minus(ONE_FRACTION);
        }
        while (placed.compareTo(ZERO_FRACTION) <= 0) {
            placed = placed.plus(ONE_FRACTION);
        }
        return placed;
    }

    /**
     * Gives a list of Egyptian fractions using the greedy algorithm. However,
     * computation is in some cases capped at 1/1440, so that the program is not
     * stuck in a seemingly endless loop. This means that this function may in
     * some cases fail to find unit fractions with large denominators.
     * @param fraction The fraction for which to compute the Egyptian fractions.
     * Two examples, 7/8, 3079/4096.
     * @return A list of Egyptian fractions, from largest to smallest (so the
     * fractions with the smaller denominators will be given first). Given 7/8,
     * the result will be a list containing 1/2, 1/3 and 1/24. In the case of
     * 3079/4096, the list will consist of 1/2, 1/4, 1/586 and 3/1200128, the
     * last of which was obviously left unprocessed.
     */
    public static ArrayList<Fraction> getEgyptianFractionsGreedyCapped(Fraction fraction) {
        ArrayList<Fraction> eFs = new ArrayList<>();
        Fraction currFract = fraction;
        int currDenom = 0;
        Fraction currUnitFract, currDiff;
        boolean keepGoing = true;
        do {
            if (currFract.isUnitFraction()) {
                eFs.add(currFract);
                currDiff = ZERO_FRACTION;
                keepGoing = false;
            } else {
                boolean unitFound;
                do {
                    currDenom++;
                    currUnitFract = new Fraction(1, currDenom);
                    currDiff = currFract.minus(currUnitFract);
                    unitFound = (currDiff.compareTo(ZERO_FRACTION) >= 0);
                } while (!unitFound && currDenom < 1025);
                if (unitFound) {
                    eFs.add(currUnitFract);
                    currFract = currDiff;
                } else {
                    currDiff = currFract;
                }
            }
            keepGoing = keepGoing && (currDenom < GREEDY_CAP_DENOM);
        } while (keepGoing);
        if (!currDiff.equals(ZERO_FRACTION)) {
            eFs.add(currDiff);
        }
        return eFs;
    }

    /**
     * Gives a list of Egyptian fractions in which each denominator is a divisor
     * of the denominator of the given fraction. In some cases, the result will
     * be the same as that of the greedy algorithm, but in many cases it will be
     * different.
     * @param fraction The fraction for which to compute the Egyptian fractions.
     * Two examples, 7/8, 3079/4096.
     * @return A list of Egyptian fractions, from largest to smallest (so the
     * fractions with the smaller denominators will be given first). Given 7/8,
     * the result will be a list containing 1/2, 1/4 and 1/8. In the case of
     * 3079/4096, the list will consist of 1/2, 1/4, 1/1024, 1/2048 and 1/4096.
     */
    public static ArrayList<Fraction> getEgyptianFractionsByDenomDivisors(Fraction fraction) {
        ArrayList<Long> denomDivs = NTFC.divisors(fraction.getDenominator());
        int currIndex = 0;
        Fraction currFract = fraction;
        Fraction currUnitFract, currDiff;
        ArrayList<Fraction> eFs = new ArrayList<>();
        while (currIndex < denomDivs.size()) {
            currUnitFract = new Fraction(1, denomDivs.get(currIndex));
            do {
                currDiff = currFract.minus(currUnitFract);
                if (currDiff.compareTo(ZERO_FRACTION) >= 0) {
                    eFs.add(currUnitFract);
                    currFract = currDiff;
                }
            } while (currDiff.compareTo(ZERO_FRACTION) > 0);
            currIndex++;
        }
        return eFs;
    }

}
