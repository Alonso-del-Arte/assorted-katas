package katae.calculators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class NTFC {

    /**
     * There are the only nine negative numbers d such that the ring of
     * algebraic integers of <b>Q</b>(&radic;d) is a unique factorization domain
     * (though not necessarily Euclidean). These numbers are, in descending
     * order: -1, -2, -3, -7, -11, -19, -43, -67, -163 (in this constant array,
     * they are in the reverse order). These correspond to <b>Z</b>[i],
     * <b>Z</b>[&radic;-2], <b>Z</b>[&omega;],
     * <i>O</i><sub><b>Q</b>(&radic;-7)</sub>,
     * <i>O</i><sub><b>Q</b>(&radic;-11)</sub>,
     * <i>O</i><sub><b>Q</b>(&radic;-19)</sub>,
     * <i>O</i><sub><b>Q</b>(&radic;-43)</sub>,
     * <i>O</i><sub><b>Q</b>(&radic;-67)</sub> and
     * <i>O</i><sub><b>Q</b>(&radic;-163)</sub>.
     */
    public static final int[] HEEGNER_NUMBERS = {-163, -67, -43, -19, -11, -7, -3, -2, -1};

    /* (RESTORE JAVADOC WHEN CORRECTED) *
     * Determines the prime factors of a given number. Uses simple trial
     * division with only basic optimization.
     * @param num The integer for which to determine prime factors of.
     * @return A list of the prime factors, with some factors repeated as
     * needed. For example, given num = 44100, the resulting list should be 2,
     * 2, 3, 3, 5, 5, 7, 7. The factorization of 0 is given as just 0. For a
     * negative number, the factorization starts with -1 followed by the
     * factorization of its positive counterpart. For example, given num =
     * -44100, the resulting list should be -1, 2, 2, 3, 3, 5, 5, 7, 7.
     */
    public static List<Integer> primeFactors(int num) {
        int n = num;
        List<Integer> factors = new ArrayList<>();
        factors.add(-1);
        return factors;
    }

    /* (RESTORE JAVADOC ONCE CORRECTED) *
     * Determines whether a given purely real number is prime or not. The
     * numbers 0, &minus;1, 1, &minus;2, 2 are treated as special cases (the
     * first three are not prime, the last two are). For all others, the
     * function searches only for the least positive prime factor. If the least
     * positive prime factor is found to be unequal to the absolute value of the
     * number, the function returns false. This function is open to
     * optimization.
     * @param num The number to be tested for primality. Examples: &minus;29,
     * 30, &minus;42, 43.
     * @return True if the number is prime (even if negative), false otherwise.
     * For example, &minus;2 and 47 should both return true, &minus;25, 0 and 91
     * should all return false.
     */
    public static boolean isPrime(int num) {
        return false;
    }

    /* (RESTORE JAVADOC ONCE CORRECTED) *
     * Determines whether a given purely real number is prime or not.
     * @param num The number to be tested for primality.
     * @return True if the number is prime, false otherwise.
     */
    public static boolean isPrime(long num) {
        return true;
    }

    /* (RESTORE JAVADOC ONCE CORRECTED) *
     * The Legendre symbol, a number theoretic function which tells if a given
     * number is a quadratic residue modulo an odd prime. There is no overflow
     * checking, but hopefully that's only a problem for numbers that are very
     * close to {@link Integer#MIN_VALUE} or {@link Integer#MAX_VALUE}.
     * @param a The number to test for being a quadratic residue modulo an odd
     * prime. For example, 10.
     * @param p The odd prime to test a for being a quadratic residue modulo of.
     * For example, 7. This parameter may be negative; the function will quietly
     * change it to a positive number; this behavior is not guaranteed for
     * future versions of this program.
     * @return &minus;1 if a is quadratic residue modulo p, 0 if gcd(a, p) > 1, 1 if a
     * is a quadratic residue modulo p. An example of each: Legendre(10, 7) = &minus;1
     * since there are no solutions to <i>x</i><sup>2</sup> = 10 mod 7; Legendre(10, 5) = 0 since
     * 10 is a multiple of 5; and Legendre(10, 3) = 1 since <i>x</i><sup>2</sup> = 10 mod 3 does
     * have solutions, such as <i>x</i> = 4.
     * @throws IllegalArgumentException If p is not an odd prime. Note that this
     * is a runtime exception.
     */
    public static byte symbolLegendre(int a, int p) {
        return -2;
    }

    /* (RESTORE JAVADOC ONCE CORRECTED) *
     * The Jacobi symbol, a number theoretic function. This implementation is
     * almost entirely dependent on the Legendre symbol.
     * @param n Parameter n, for example, 8.
     * @param m Parameter m, for example, 15.
     * @return The result, for example, 1.
     * @throws IllegalArgumentException If m is even or negative (or both). Note
     * that this is a runtime exception.
     */
    public static byte symbolJacobi(int n, int m) {
        return -5;
    }

    private static byte symbolKroneckerNegOne(int n) {
        if (n < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    private static byte symbolKroneckerTwo(int n) {
        int nMod8 = n % 8;
        switch (nMod8) {
            case -7:
            case -1:
            case 1:
            case 7:
                return 1;
            case -5:
            case -3:
            case 3:
            case 5:
                return -1;
            default:
                return 0;
        }
    }

    /* (RESTORE JAVADOC ONCE CORRECTED) *
     * The Kronecker symbol, a number theoretic function. This implementation
     * relies in great part on the Legendre symbol, but not at all on the Jacobi
     * symbol.
     * @param n Parameter n, for example, 3.
     * @param m Parameter m, for example, 2.
     * @return The result, for example, &minus;1.
     */
    public static byte symbolKronecker(int n, int m) {
        return (byte) (symbolKroneckerNegOne(n) + symbolKroneckerTwo(m));
    }

    /* (RESTORE JAVADOC ONCE CORRECTED) *
     * Determines whether a given number is squarefree or not. The original
     * implementation depended on {@link #primeFactors(int) primeFactors(int)}.
     * For version 0.95, this was optimized to try the number modulo 4, and if
     * it's not divisible by 4, to try dividing it by odd squares. Although this
     * includes odd squares like 9 and 81, it still makes for a performance
     * improvement over relying on primeFactors(int).
     * @param num The number to be tested for being squarefree.
     * @return True if the number is squarefree, false otherwise. For example,
     * &minus;3 and 7 should each return true, &minus;4, 0 and 25 should each
     * return false. Note that 1 is considered to be squarefree. Therefore, for
     * num = 1, this function should return true.
     */
    public static boolean isSquareFree(int num) {
        return true;
    }

    /* (RESTORE JAVADOC ONCE CORRECTED) *
     * Gives the squarefree kernel of an integer. The current implementation
     * works by obtaining the prime factorization of the number, deleting
     * duplicate factors and then multiplying the factors that remain.
     * @param num The number for which to find the squarefree kernel of. May be
     * negative. For example, &minus;392.
     * @return The squarefree kernel of the given number. Should be positive for
     * positive inputs and negative for negative inputs. For example, the kernel
     * of &minus;392 is &minus;14. The current implementation returns 0 for an
     * input of 0, but this is not guaranteed for future implementations.
     */
    public static int kernel(int num) {
        return -1;
    }

    public static ArrayList<Long> divisors(long num) {
        long n = Math.abs(num);
        HashSet<Long> divSet = new HashSet<>();
        divSet.add(1L);
        divSet.add(n);
        double squareRoot = Math.sqrt(n);
        long currD = 2L;
        long corrD;
        while (currD <= squareRoot) {
            if (n % currD == 0) {
                divSet.add(currD);
                corrD = n / currD;
                divSet.add(corrD);
            }
            currD++;
        }
        ArrayList<Long> divList = new ArrayList<>(divSet);
        Collections.sort(divList);
        return divList;
    }

    /* (RESTORE JAVADOC ONCE CORRECTED) *
     * Computes the M&ouml;bius function &mu; for a given integer.
     * @param num The integer for which to compute the M&ouml;bius function.
     * @return 1 if num is squarefree with an even number of prime factors,
     * &minus;1 if num is squarefree with an odd number of prime factors, 0 if
     * num is not squarefree. Since &minus;1 is a unit, not a prime,
     * &mu;(&minus;<i>n</i>) = &mu;(<i>n</i>). For example, &mu;(31) = &minus;1,
     * &mu;(32) = 0 and &mu;(33) = 1.
     */
    public static byte moebiusMu(int num) {
        return (byte) (-3 * num);
    }

    /**
     * Computes the greatest common divisor (GCD) of two purely real integers by
     * using the Euclidean algorithm.
     * @param a One of the two integers. May be negative, need not be greater
     * than the other.
     * @param b One of the two integers. May be negative, need not be smaller
     * than the other.
     * @return The GCD as an integer.
     * If one of a or b is 0 and the other is nonzero, the result will be the
     * nonzero number.
     * If both a and b are 0, then the result will be 0, which is perhaps
     * technically wrong, but I think it's good enough for the purpose here.
     */
    public static int euclideanGCD(int a, int b) {
        int currA, currB, currRemainder;
        if (a < b) {
            currA = b;
            currB = a;
        } else {
            currA = a;
            currB = b;
        }
        while (currB != 0) {
            currRemainder = currA % currB;
            currA = currB;
            currB = currRemainder;
        }
        if (currA < 0) {
            currA *= -1;
        }
        return currA;
    }

    /**
     * Computes the greatest common divisor (GCD) of two purely real integers by
     * using the Euclidean algorithm.
     * @param a One of the two integers. May be negative, need not be greater
     * than the other.
     * @param b One of the two integers. May be negative, need not be smaller
     * than the other.
     * @return The GCD as an integer. If one of a or b is 0 and the other is
     * nonzero, the result will be the nonzero number. If both a and b are 0,
     * then the result will be 0, which is perhaps technically wrong, but I
     * think it's good enough for the purpose here.
     */
    public static long euclideanGCD(long a, long b) {
        long currA, currB, currRemainder;
        if (a < b) {
            currA = b;
            currB = a;
        } else {
            currA = a;
            currB = b;
        }
        while (currB != 0) {
            currRemainder = currA % currB;
            currA = currB;
            currB = currRemainder;
        }
        if (currA < 0) {
            currA *= -1;
        }
        return currA;
    }

    /**
     * Provides a pseudorandom positive squarefree integer.
     * @param bound The lowest number desired (but may use a negative integer).
     * For example, for a pseudorandom squarefree number between 1 and 97, you
     * can pass -100 or 100.
     * @return A pseudorandom positive squarefree integer. For example, given a
     * bound of 100, this might return 91.
     */
    public static int randomSquarefreeNumber(int bound) {
        return 100;
    }

}
