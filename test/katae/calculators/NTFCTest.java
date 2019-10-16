package katae.calculators;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for a collection of number theoretic functions, including basic
 * primality testing and the Euclidean GCD algorithm. Some of these tests use a
 * finite list of small primes and another finite list of non-prime numbers, as
 * well as a small list of Fibonacci numbers.
 * <p>The most relevant entries in Sloane's On-Line Encyclopedia of Integer
 * Sequences (OEIS) are:</p>
 * <ul>
 * <li><a href="http://oeis.org/A000040">A000040</a>: The prime numbers</li>
 * <li><a href="http://oeis.org/A000045">A000040</a>: The Fibonacci numbers</li>
 * <li><a href="http://oeis.org/A002808">A002808</a>: The composite numbers</li>
 * <li><a href="http://oeis.org/A018252">A018252</a>: 1 and the composite
 * numbers</li>
 * </ul>
 * @author Alonso del Arte
 */
public class NTFCTest {
    /**
     * {@link #setUpClass() setUpClass()} will generate a List of the first few 
     * consecutive primes. This constant determines how long that list will be. 
     * For example, if it's 1000, setUpClass() will generate a list of the 
     * primes between 1 and 1000. It should not be greater than {@link
     * Integer#MAX_VALUE}.
     */
    private static final int PRIME_LIST_THRESHOLD = 1000;

    /**
     * A List of the first few prime numbers, to be used in some of the tests. 
     * It will be populated during {@link #setUpClass() setUpClass()}.
     */
    private static List<Integer> primesList;

    /**
     * The size of primesList, to be determined during {@link #setUpClass()
     * setUpClass()}. This value will be reported on the console before the 
     * tests begin.
     */
    private static int primesListLength;

    /**
     * A List of composite numbers, which may or may not include 
     * {@link #PRIME_LIST_THRESHOLD PRIME_LIST_THRESHOLD}. It will be populated 
     * during {@link #setUpClass() setUpClass()}.
     */
    private static List<Integer> compositesList;

    /**
     * A List of Fibonacci numbers. It will be populated during {@link
     * #setUpClass() setUpClass()}.
     */
    private static List<Integer> fibonacciList;

    /**
     * An array of what I'm calling, for lack of a better term, "Heegner 
     * companion primes." For each Heegner number <i>d</i>, the Heegner 
     * companion prime <i>p</i> is a number that is prime in <b>Z</b> and is 
     * also prime in the ring of integers of 
     * <i>O</i><sub><b>Q</b>(&radic;<i>d</i>)</sub>. This array will be 
     * populated during setUpClass().
     */
    private static final int[] HEEGNER_COMPANION_PRIMES = new int[9];

    /**
     * Sets up a List of the first few consecutive primes, the first few 
     * composite numbers, the first few Fibonacci numbers and the Heegner 
     * "companion primes." This provides most of what is needed for the tests.
     */
    @BeforeClass
    public static void setUpClass() {
        /* First, to generate a list of the first few consecutive primes, using 
        the sieve of Eratosthenes. */
        int threshold, halfThreshold;
        if (PRIME_LIST_THRESHOLD < 0) {
            threshold = (-1) * PRIME_LIST_THRESHOLD;
        } else {
            threshold = PRIME_LIST_THRESHOLD;
        }
        halfThreshold = threshold / 2;
        primesList = new ArrayList<>();
        primesList.add(2); // Add 2 as a special case
        boolean[] primeFlags = new boolean[halfThreshold];
        for (int i = 0; i < halfThreshold; i++) {
            primeFlags[i] = true; // Presume all odd numbers prime for now
        }
        int currPrime = 3;
        int twiceCurrPrime, currIndex;
        while (currPrime < threshold) {
            primesList.add(currPrime);
            twiceCurrPrime = 2 * currPrime;
            for (int j = currPrime * currPrime; j < threshold; j += twiceCurrPrime) {
                currIndex = (j - 3)/2;
                primeFlags[currIndex] = false;
            }
            do {
                currPrime += 2;
                currIndex = (currPrime - 3)/2;
            } while (currIndex < (halfThreshold - 1) && !primeFlags[currIndex]);
        }
        primesListLength = primesList.size();
        /* Now to make a list of composite numbers, from 4 up to and perhaps 
           including PRIME_LIST_THRESHOLD. */
        compositesList = new ArrayList<>();
        for (int c = 4; c < PRIME_LIST_THRESHOLD; c += 2) {
            compositesList.add(c);
            if (!primeFlags[c/2 - 1]) {
                compositesList.add(c + 1);
            }
        }
        System.out.println("setUpClass() has generated a list of the first " + primesListLength + " consecutive primes.");
        System.out.println("prime(" + primesListLength + ") = " + primesList.get(primesListLength - 1));
        System.out.println("There are " + (PRIME_LIST_THRESHOLD - (primesListLength + 1)) + " composite numbers up to " + PRIME_LIST_THRESHOLD + ".");
        // And now to make a list of Fibonacci numbers
        fibonacciList = new ArrayList<>();
        fibonacciList.add(0);
        fibonacciList.add(1);
        threshold = (Integer.MAX_VALUE - 3)/4; // Repurposing this variable
        currIndex = 2; // Also repurposing this one
        int currFibo = 1;
        while (currFibo < threshold) {
            currFibo = fibonacciList.get(currIndex - 2) + fibonacciList.get(currIndex - 1);
            fibonacciList.add(currFibo);
            currIndex++;
        }
        currIndex--; // Step one back to index last added Fibonacci number
        System.out.println("setUpClass() has generated a list of the first " + fibonacciList.size() + " Fibonacci numbers.");
        System.out.println("Fibonacci(" + currIndex + ") = " + fibonacciList.get(currIndex));
        /* And last but not least, to make what I'm calling, for lack of a 
           better term, "the Heegner companion primes." */
        int absD, currDiff, currCompCand, currSqrIndex, currSqrDMult;
        boolean numNotFoundYet;
        for (int d = 0; d < NTFC.HEEGNER_NUMBERS.length; d++) {
            absD = (-1) * NTFC.HEEGNER_NUMBERS[d];
            currIndex = 0;
            do {
                currPrime = primesList.get(currIndex);
                currIndex++;
            } while (currPrime <= absD);
            numNotFoundYet = true;
            while (numNotFoundYet) {
                currCompCand = 4 * currPrime;
                currSqrIndex = 1;
                currDiff = absD;
                while (currDiff > 0) {
                    currSqrDMult = absD * currSqrIndex * currSqrIndex;
                    currDiff = currCompCand - currSqrDMult;
                    if (Math.sqrt(currDiff) == Math.floor(Math.sqrt(currDiff))) {
                        currDiff = 0;
                    }
                    currSqrIndex++;
                }
                if (currDiff < 0) {
                    numNotFoundYet = false;
                } else {
                    currIndex++;
                    currPrime = primesList.get(currIndex);
                }
            }
            HEEGNER_COMPANION_PRIMES[d] = currPrime;
        }
        System.out.println("setUpClass() has generated a list of \"Heegner companion primes\": ");
        for (int dReport = 0; dReport < NTFC.HEEGNER_NUMBERS.length; dReport++) {
            System.out.print(HEEGNER_COMPANION_PRIMES[dReport] + " for " + NTFC.HEEGNER_NUMBERS[dReport] + ", ");
        }
        System.out.println();
    }

    /**
     * Test of isPrime method, of class NTFC. The 
     * numbers listed in Sloane's A000040, as well as those same numbers 
     * multiplied by &minus;1, should all be identified as prime. Likewise, the 
     * numbers listed in Sloane's A018252, as well as those same numbers 
     * multiplied by &minus;1, should all be identified as not prime. As for 0, 
     * I'm not sure; if you like you can uncomment the line for it and perhaps 
     * change assertFalse to assertTrue.
     */
    @Test
    public void testIsPrime() {
        System.out.println("isPrime(int)");
        // assertFalse(NTFC.isPrime(0));
        String assertionMessage;
        for (Integer currPrime : primesList) {
            assertionMessage = currPrime + " should be found to be prime";
            assertTrue(assertionMessage, NTFC.isPrime(currPrime));
            assertionMessage = "\u2212" + assertionMessage;
            assertTrue(assertionMessage, NTFC.isPrime(-currPrime));
        }
        assertionMessage = "1 should not be found to be prime";
        assertFalse(assertionMessage, NTFC.isPrime(1));
        assertionMessage = "\u2212" + assertionMessage;
        assertFalse(assertionMessage, NTFC.isPrime(-1));
        for (Integer compositeNum : compositesList) {
            assertionMessage = compositeNum + " should not be found to be prime";
            assertFalse(assertionMessage, NTFC.isPrime(compositeNum));
            assertionMessage = "\u2212" + assertionMessage;
            assertFalse(assertionMessage, NTFC.isPrime(-compositeNum));
        }
        /* Now we're going to test odd integers greater than the last prime 
        in our List but smaller than the square of that prime. */
        int maxNumForTest = primesList.get(primesListLength - 1) * primesList.get(primesListLength - 1);
        int primeIndex = 1; // Which of course corresponds to 3, not 2, in a zero-indexed array
        boolean possiblyPrime = true; // Presume k to be prime until finding otherwise
        for (int k = primesList.get(primesListLength - 1) + 2; k < maxNumForTest; k += 2) {
            while (primeIndex < primesListLength && possiblyPrime) {
                possiblyPrime = (k % primesList.get(primeIndex) != 0);
                primeIndex++;
            }
            if (possiblyPrime) {
                assertTrue(NTFC.isPrime(k));
                assertTrue(NTFC.isPrime(-k));
            } else {
                assertFalse(NTFC.isPrime(k));
                assertFalse(NTFC.isPrime(-k));
            }
            primeIndex = 1; // Reset for next k
            possiblyPrime = true; // Reset for next k
        }
        /* Next, we're going to test indices of Fibonacci primes greater than 4, 
           which corresponds to 3 */
        for  (int m = 5; m < fibonacciList.size(); m++) {
            if (NTFC.isPrime(fibonacciList.get(m))) {
                assertTrue(NTFC.isPrime(m));
            }
        }
        /* One more thing before moving on to complex UFDs: testing 
           isPrime(long) */
        System.out.println("isPrime(long)");
        long longNum = Integer.MAX_VALUE;
        assertionMessage = "2^31 - 1 should be found to be prime.";
        assertTrue(assertionMessage, NTFC.isPrime(longNum));
        longNum++;
        assertionMessage = "2^31 should not be found to be prime.";
        assertFalse(assertionMessage, NTFC.isPrime(longNum));
        longNum += 11;
        assertionMessage = "2^31 + 11 should be found to be prime.";
        assertTrue(assertionMessage, NTFC.isPrime(longNum));
        int castNum = (int) longNum;
        assertionMessage = "-(2^31) + 11 should not be found to be prime.";
        assertFalse(assertionMessage, NTFC.isPrime(castNum));
    }


    /**
     * Test of symbolLegendre method, of class 
     * NTFC. Per quadratic reciprocity, 
     * Legendre(<i>p</i>, <i>q</i>) = Legendre(<i>q</i>, <i>p</i>) if <i>p</i> 
     * and <i>q</i> are both primes and either one or both of them are congruent 
     * to 1 mod 4. But if both are congruent to 3 mod 4, then Legendre(<i>p</i>, 
     * q) = &minus;Legendre(<i>q</i>, <i>p</i>). And of course 
     * Legendre(<i>p</i>, <i>p</i>) = 0. Some of this assumes that both <i>p</i> 
     * and <i>q</i> are positive. In the case of Legendre(<i>p</i>, 
     * &minus;<i>q</i>) with <i>q</i> being positive, reckon the congruence of 
     * <i>q</i> mod 4 rather than &minus;<i>q</i> mod 4.
     * <p>Another property to test for is that Legendre(<i>ab</i>, <i>p</i>) = 
     * Legendre(<i>a</i>, <i>p</i>) Legendre(<i>b</i>, <i>p</i>). That is to say 
     * that this is a multiplicative function. So here this is tested with 
     * Legendre(2<i>p</i>, <i>q</i>) = Legendre(2, <i>q</i>) Legendre(<i>p</i>, 
     * <i>q</i>).</p>
     * <p>Of course it's entirely possible that in implementing symbolLegendre 
     * the programmer could get dyslexic and produce an implementation that 
     * always gives the wrong result when gcd(<i>a</i>, <i>p</i>) = 1, meaning 
     * that it always returns &minus;1 when it should return 1, and vice-versa, 
     * and yet it passes the tests.</p>
     * <p>For that reason one should not rely only on the identities pertaining 
     * to multiplicativity and quadratic reciprocity. Therefore these tests also 
     * include some computations of actual squares modulo <i>p</i> to check some 
     * of the answers.</p>
     * <p>I chose to use the Fibonacci numbers for this purpose, since they are 
     * already being used in some of the other tests and they contain a good mix 
     * of prime numbers (including the even prime 2) and composite numbers.</p>
     */
    @Test
    public void testLegendreSymbol() {
        System.out.println("symbolLegendre");
        byte expResult, result;
        int p, q;
        // First to test Legendre(Fibonacci(n), p)
        for (int i = 3; i < fibonacciList.size(); i++) {
            for (int j = 1; j < primesListLength; j++) {
                p = primesList.get(j);
                int fiboM = fibonacciList.get(i) % p;
                if (fiboM == 0) {
                    expResult = 0;
                } else {
                    int halfPmark = (p + 1)/2;
                    int[] modSquares = new int[halfPmark];
                    boolean noSolutionFound = true;
                    int currModSqIndex = 0;
                    for (int n = 0; n < halfPmark; n++) {
                        modSquares[n] = (n * n) % p;
                    }
                    while (noSolutionFound && (currModSqIndex < halfPmark)) {
                        noSolutionFound = !(fiboM == modSquares[currModSqIndex]);
                        currModSqIndex++;
                    }
                    if (noSolutionFound) {
                        expResult = -1;
                    } else {
                        expResult = 1;
                    }
                }
                result = NTFC.symbolLegendre(fibonacciList.get(i), p);
                assertEquals(expResult, result);
            }
        }
        // Now to test with both p and q being odd primes
        for (int pindex = 1; pindex < primesListLength; pindex++) {
            p = primesList.get(pindex);
            expResult = 0;
            result = NTFC.symbolLegendre(p, p);
            assertEquals(expResult, result);
            for (int qindex = pindex + 1; qindex < primesListLength; qindex++) {
                q = primesList.get(qindex);
                expResult = NTFC.symbolLegendre(q, p);
                if (p % 4 == 3 && q % 4 == 3) {
                    expResult *= -1;
                }
                result = NTFC.symbolLegendre(p, q);
                assertEquals(expResult, result);
                result = NTFC.symbolLegendre(p, -q);
                assertEquals(expResult, result);
                /* And lastly, to test that Legendre(2p, q) = Legendre(2, q) 
                   Legendre(p, q). */
                expResult = NTFC.symbolLegendre(2, q);
                expResult *= NTFC.symbolLegendre(p, q);
                result = NTFC.symbolLegendre(2 * p, q);
                assertEquals(expResult, result);
            }
        }
        // And lastly to check for exceptions for bad arguments.
        try {
            byte attempt = NTFC.symbolLegendre(7, 2);
            fail("Calling Legendre(7, 2) should have triggered an exception, not given result " + attempt + ".");
        } catch (IllegalArgumentException iae) {
            System.out.println("Calling Legendre(7, 2) correctly triggered IllegalArgumentException. \"" + iae.getMessage() + "\"");
        }
    }

    /**
     * Test of symbolJacobi method, of class NTFC. 
     * First, it checks that Legendre(<i>a</i>, <i>p</i>) = Jacobi(<i>a</i>, 
     * <i>p</i>), where <i>p</i> is an odd prime. Next, it checks that 
     * Jacobi(<i>n</i>, <i>pq</i>) = Legendre(<i>n</i>, <i>p</i>) 
     * Legendre(<i>n</i>, <i>q</i>). If the Legendre symbol test fails, the 
     * result of this test is meaningless. Then follows the actual business of 
     * checking Jacobi(<i>n</i>, <i>m</i>).
     */
    @Test
    public void testJacobiSymbol() {
        System.out.println("symbolJacobi");
        System.out.println("Checking overlap with Legendre symbol...");
        byte expResult, result;
        for (int i = 1; i < primesListLength; i++) {
            for (int a = 5; a < 13; a++) {
                expResult = NTFC.symbolLegendre(a, primesList.get(i));
                result = NTFC.symbolJacobi(a, primesList.get(i));
                assertEquals(expResult, result);
            }
        }
        System.out.println("Now checking Jacobi symbol per se...");
        int p, q, m;
        for (int pindex = 1; pindex < primesListLength; pindex++) {
            p = primesList.get(pindex);
            for (int qindex = pindex + 1; qindex < primesListLength; qindex++) {
                q = primesList.get(qindex);
                m = p * q;
                for (int n = 15; n < 20; n++) {
                    expResult = NTFC.symbolLegendre(n, p);
                    expResult *= NTFC.symbolLegendre(n, q);
                    result = NTFC.symbolJacobi(n, m);
                    assertEquals(expResult, result);
                }
            }
        }
        // And lastly to check for exceptions for bad arguments.
        try {
            byte attempt = NTFC.symbolJacobi(7, 2);
            fail("Calling Jacobi(7, 2) should have triggered an exception, not given result " + attempt + ".");
        } catch (IllegalArgumentException iae) {
            System.out.println("Calling Jacobi(7, 2) correctly triggered IllegalArgumentException. \"" + iae.getMessage() + "\"");
        }
    }

    /**
     * Test of symbolKronecker method, of class 
     * NTFC. First, it checks that 
     * Legendre(<i>a</i>, <i>p</i>) = Kronecker(<i>a</i>, <i>p</i>), where 
     * <i>p</i> is an odd prime. Next, it checks that Jacobi(<i>n</i>, <i>m</i>) 
     * = Kronecker(<i>n</i>, <i>m</i>). If either the Legendre symbol test or 
     * the Jacobi symbol test fails, the result of this test is meaningless. 
     * Then follows the actual business of checking Kronecker(<i>n</i>, 
     * &minus;2), Kronecker(<i>n</i>, &minus;1) and Kronecker(<i>n</i>, 2). On 
     * another occasion I might add a few multiplicative tests.
     */
    @Test
    public void testKroneckerSymbol() {
        System.out.println("symbolKronecker");
        byte expResult, result;
        System.out.println("Checking overlap with Legendre symbol...");
        for (int i = 1; i < primesListLength; i++) {
            for (int a = 7; a < 11; a++) {
                expResult = NTFC.symbolLegendre(a, primesList.get(i));
                result = NTFC.symbolKronecker(a, primesList.get(i));
                assertEquals(expResult, result);
            }
        }
        System.out.println("Checking overlap with Jacobi symbol...");
        for (int j = -10; j < 10; j++) {
            for (int b = 5; b < 15; b += 2) {
                expResult = NTFC.symbolJacobi(j, b);
                result = NTFC.symbolKronecker(j, b);
                assertEquals(expResult, result);
            }
        }
        System.out.println("Now checking Kronecker symbol per se...");
        for (int n = 1; n < 50; n++) {
            expResult = -1;
            result = NTFC.symbolKronecker(-n, -1);
            assertEquals(expResult, result);
            expResult = 1;
            result = NTFC.symbolKronecker(n, -1);
            assertEquals(expResult, result);
        }
        for (int m = -24; m < 25; m += 8) {
            expResult = -1;
            result = NTFC.symbolKronecker(m + 3, 2);
            assertEquals(expResult, result);
            result = NTFC.symbolKronecker(m + 5, 2);
            assertEquals(expResult, result);
            if (m < 0) {
                result = NTFC.symbolKronecker(m + 1, -2);
                assertEquals(expResult, result);
                result = NTFC.symbolKronecker(m + 7, -2);
                assertEquals(expResult, result);
            } else {
                result = NTFC.symbolKronecker(m + 3, -2);
                assertEquals(expResult, result);
                result = NTFC.symbolKronecker(m + 5, -2);
                assertEquals(expResult, result);
            }
            expResult = 0;
            result = NTFC.symbolKronecker(m, 2);
            assertEquals(expResult, result);
            result = NTFC.symbolKronecker(m + 2, 2);
            assertEquals(expResult, result);
            result = NTFC.symbolKronecker(m + 4, 2);
            assertEquals(expResult, result);
            result = NTFC.symbolKronecker(m + 6, 2);
            assertEquals(expResult, result);
            expResult = 1;
            result = NTFC.symbolKronecker(m + 1, 2);
            assertEquals(expResult, result);
            result = NTFC.symbolKronecker(m + 7, 2);
            assertEquals(expResult, result);
            if (m < 0) {
                result = NTFC.symbolKronecker(m + 3, -2);
                assertEquals(expResult, result);
                result = NTFC.symbolKronecker(m + 5, -2);
                assertEquals(expResult, result);
            } else {
                result = NTFC.symbolKronecker(m + 1, -2);
                assertEquals(expResult, result);
                result = NTFC.symbolKronecker(m + 7, -2);
                assertEquals(expResult, result);
            }
        }
    }

    /**
     * Test of isSquareFree method, of class NTFC. 
     * Prime numbers should be found to be squarefree, squares of primes should 
     * not.
     */
    @Test
    public void testIsSquareFree() {
        System.out.println("isSquareFree");
        String assertionMessage;
        int number;
        for (int i = 0; i < primesListLength - 1; i++) {
            number = primesList.get(i) * primesList.get(i + 1); // A squarefree semiprime, pq
            assertionMessage = number + " should have been found to be squarefree";
            assertTrue(assertionMessage, NTFC.isSquareFree(number));
            number *= primesList.get(i); // Repeat one prime factor, (p^2)q
            assertionMessage = number + " should not have been found to be squarefree";
            assertFalse(assertionMessage, NTFC.isSquareFree(number));
            number /= primesList.get(i + 1); // Now this should be p^2
            assertionMessage = number + " should not have been found to be squarefree";
            assertFalse(assertionMessage, NTFC.isSquareFree(number));
        }
    }

    /**
     * Test of kernel method, of class NTFC. This 
     * checks the kernel function with numbers that are the product of two 
     * distinct primes as well as with numbers that are the product of powers of 
     * primes. For now there is no testing of 0 as an input.
     */
    @Test
    public void testKernel() {
        System.out.println("kernel");
        int currPrime, currNum, expResult, result;
        String assertionMessage;
        int currIndex = 1;
        do {
            currPrime = primesList.get(currIndex); // Get p
            currNum = currPrime;
            expResult = currPrime;
            result = NTFC.kernel(currNum);
            assertionMessage = "Kernel of " + currNum + " should be found to be " + currNum + " itself.";
            assertEquals(assertionMessage, expResult, result);
            currNum *= currPrime; // p^2, expResult stays the same
            result = NTFC.kernel(currNum);
            assertionMessage = "Kernel of " + currNum + " should be found to be " + currPrime + ".";
            assertEquals(assertionMessage, expResult, result);
            currPrime = -primesList.get(currIndex - 1); // Get q, a negative prime
            currNum *= currPrime; // p^2 q, which is negative
            expResult *= currPrime; // pq, which is also negative
            result = NTFC.kernel(currNum);
            assertionMessage = "Kernel of " + currNum + " should be found to be " + expResult + ".";
            assertEquals(assertionMessage, expResult, result);
            currNum *= currPrime; // p^2 q^2, which is positive
            expResult *= -1; // -pq, which is also positive
            result = NTFC.kernel(currNum);
            assertionMessage = "Kernel of " + currNum + " should be found to be " + expResult + ".";
            assertEquals(assertionMessage, expResult, result);
            currNum *= currPrime; // p^2 q^3, which is negative
            expResult *= -1; // Back to pq, which is negative
            result = NTFC.kernel(currNum);
            assertionMessage = "Kernel of " + currNum + " should be found to be " + expResult + ".";
            assertEquals(assertionMessage, expResult, result);
            currIndex++;
        } while (currIndex < primesListLength && currPrime > -71);
    }

    /**
     * Test of divisors method, of class NTFC.
     */
    @Test
    public void testDivisors() {
        System.out.println("divisors");
        ArrayList<Long> expResult = new ArrayList<>();
        expResult.add(1L);
        expResult.add(2L);
        expResult.add(3L);
        expResult.add(4L);
        expResult.add(6L);
        expResult.add(12L);
        ArrayList<Long> result = NTFC.divisors(12L);
        assertEquals(expResult, result);
        expResult.clear();
        expResult.add(1L);
        expResult.add(13L);
        result = NTFC.divisors(13L);
        assertEquals(expResult, result);
        expResult.clear();
        expResult.add(1L);
        expResult.add(2L);
        expResult.add(4L);
        expResult.add(8L);
        expResult.add(16L);
        result = NTFC.divisors(16L);
        assertEquals(expResult, result);
    }

    /**
     * Another test of divisors method, of class NTFC. Technically, the set of
     * divisors of any integer includes both positive and negative integers.
     * However, for most uses, it is enough to list only the positive divisors.
     * Thus, this function should give positive divisors even if the argument is
     * negative.
     */
    @Test
    public void testDivisorsOfNegativeNumberReportedPositive() {
        ArrayList<Long> expResult = new ArrayList<>();
        expResult.add(1L);
        expResult.add(3L);
        expResult.add(5L);
        expResult.add(15L);
        ArrayList<Long> result = NTFC.divisors(-15L);
        assertEquals(expResult, result);
    }

    /**
     * Another test of divisors method, of class NTFC. Zero is divisible by any
     * integer except itself. However, it would be impractical, and perhaps
     * useless, for the divisors function to return a list of nonzero integer
     * that can be represented by the pertinent data type. So maybe the correct
     * result for the divisors of 0 ought to be an empty list.
     */
    @Test
    public void testDivisorsOfZero() {
        ArrayList<Long> expResult = new ArrayList<>();
        ArrayList<Long> result = NTFC.divisors(0L);
        assertEquals(expResult, result);
    }

    /**
     * Test of moebiusMu method, of class NTFC. I expect that
     * &mu;(&minus;<i>n</i>) = &mu;(<i>n</i>), so this test checks for that. If
     * you wish to limit the test to positive integers, you can just remove some
     * of the assertions.
     */
    @Test
    public void testMoebiusMu() {
        System.out.println("moebiusMu");
        byte expResult = -1;
        byte result;
        // The primes p should all have mu(p) = -1
        for (int i = 0; i < primesListLength; i++) {
            result = NTFC.moebiusMu(primesList.get(i));
            assertEquals(expResult, result);
            assertEquals(result, NTFC.moebiusMu(-primesList.get(i)));
        }
        // Now to test mu(n) = 0 with n being a multiple of 4
        expResult = 0;
        for (int j = 0; j < 97; j += 4) {
            result = NTFC.moebiusMu(j);
            assertEquals(expResult, result);
            assertEquals(result, NTFC.moebiusMu(-j));
        }
        // And lastly, the products of two distinct primes p and q should give mu(pq) = 1
        expResult = 1;
        int num;
        for (int k = 0; k < primesListLength - 1; k++) {
            num = primesList.get(k) * primesList.get(k + 1);
            result = NTFC.moebiusMu(num);
            assertEquals(expResult, result);
            assertEquals(result, NTFC.moebiusMu(-num));
        }
    }

    /**
     * Test of euclideanGCD method, of class NTFC.
     * At this time, I choose not to test the case gcd(0, 0). The value of such 
     * a test would be philosophical rather than practical.
     */
    @Test
    public void testEuclideanGCD() {
        System.out.println("euclideanGCD");
        int result;
        int expResult = 1; /* Going to test with consecutive integers, expect 
                              the result to be 1 each time */
        for (int i = -30; i < 31; i++) {
            result = NTFC.euclideanGCD(i, i + 1);
            assertEquals(expResult, result);
        }
        /* Now test with consecutive odd numbers, result should also be 1 each 
           time as well */
        for (int j = -29; j < 31; j += 2) {
            result = NTFC.euclideanGCD(j, j + 2);
            assertEquals(expResult, result);
        }
        /* And now consecutive Fibonacci numbers before moving on to even 
           numbers. This will probably be the longest part of the test. */
        for (int k = 1; k < fibonacciList.size(); k++) {
            result = NTFC.euclideanGCD(fibonacciList.get(k - 1), fibonacciList.get(k));
            assertEquals(expResult, result);
        }
        expResult = 2; /* Now to test with consecutive even integers, result 
                          should be 2 each time */
        for (int m = -30; m < 31; m += 2) {
            result = NTFC.euclideanGCD(m, m + 2);
            assertEquals(expResult, result);
        }
        // And now some of the same tests again but with the long data type
        long expResultLong = 1;
        long resultLong;
        for (long j = (long) Integer.MAX_VALUE; j < ((long) Integer.MAX_VALUE + 32); j++) {
            resultLong = NTFC.euclideanGCD(j, j + 1);
            assertEquals(expResultLong, resultLong);
        }
        expResultLong = 2;
        for (long k = ((long) Integer.MAX_VALUE - 1); k < ((long) Integer.MAX_VALUE + 32); k += 2) {
            resultLong = NTFC.euclideanGCD(k, k + 2);
            assertEquals(expResultLong, resultLong);
        }
    }

    /**
     * Test of randomSquarefreeNumber method, of class 
     * NTFC. This test doesn't check whether the 
     * distribution is random enough, only that the numbers produced are indeed 
     * squarefree, that they don't have repeated prime factors. The test will 
     * keep going until either an assertion fails or a number is produced with 7 
     * for a least significant digit, whichever happens first.
     */
    @Test
    public void testRandomSquarefreeNumber() {
        System.out.println("randomSquarefreeNumber");
        int testBound = primesList.get(primesListLength - 1) * primesList.get(primesListLength - 1);
        int potentialRanSqFreeNum;
        String assertionMessage;
        do {
            potentialRanSqFreeNum = NTFC.randomSquarefreeNumber(testBound);
            System.out.println("Function came up with this pseudorandom squarefree number: " + potentialRanSqFreeNum);
            int squaredPrime, remainder; // TODO: Move initialization to outer scope
            for (int i = 0; i < primesListLength; i++) {
                squaredPrime = primesList.get(i) * primesList.get(i);
                remainder = potentialRanSqFreeNum % squaredPrime;
                assertionMessage = "Since " + potentialRanSqFreeNum + " is said to be squarefree, it should not be divisible by " + squaredPrime;
                assertNotEquals(assertionMessage, 0, remainder);
            }
            assertionMessage = "The number " + potentialRanSqFreeNum + " is not greater than the bound " + testBound;
            assertTrue(assertionMessage, potentialRanSqFreeNum < testBound);
            assertionMessage = "The number " + potentialRanSqFreeNum + " is greater than 0";
            assertTrue(assertionMessage, potentialRanSqFreeNum > 0);
        } while (potentialRanSqFreeNum % 10 != 9);
    }

}
