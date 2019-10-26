package katae.fractions;

/**
 * Represent fractions symbolically rather than numerically.
 * TODO: Get this to pass all the tests, or at least most of the tests.
 * @author Alonso del Arte, unless indicated otherwise.
 */
public class Fraction implements Comparable<Fraction> {

    private final long fractNumer;
    private final long fractDenom;

    public long getNumerator() {
        return this.fractNumer;
    }

    public long getDenominator() {
        return this.fractDenom;
    }

    /**
     * Provides numerators and a denominator for cross-multiplied fractions.
     * @param operand1 The first fraction to cross-multiply
     * @param operand2 The second fraction to cross multiply
     * @return Two cross-multiplied numerators and a denominator as an array of
     * long.
     * @author Kunle Oshiyoye and Chris Delduco, October 16, 2019, at Java TDD
     * Crash Course.
     */
    private long[] crossMultiply(Fraction operand1, Fraction operand2) {
        long newDenom = operand1.fractDenom * operand2.fractDenom;
        long newCurrentNumer = operand1.fractNumer * operand2.fractDenom;
        long newOperandNumer = operand2.fractNumer * operand1.fractDenom;
        return new long[]{newCurrentNumer, newOperandNumer, newDenom};
    }

    public Fraction plus(Fraction addend) {
        return new Fraction(0);
    }

    // STUB TO FAIL FIRST TEST
    public Fraction plus(int summand) {
        return new Fraction(0);
    }

    public Fraction negate() {
        return this;
    }

    // STUB TO FAIL FIRST TEST
    public Fraction minus(Fraction subtrahend) {
        return new Fraction(0);
    }

    // STUB TO FAIL FIRST TEST
    public Fraction minus(int subtrahend) {
        return new Fraction(0);
    }

    // STUB TO FAIL FIRST TEST
    public Fraction times(Fraction multiplicand) {
        return new Fraction(0);
    }

    // STUB TO FAIL FIRST TEST
    public Fraction times(int multiplicand) {
        return new Fraction(0);
    }

    // STUB TO FAIL FIRST TEST
    public Fraction reciprocal() {
        return new Fraction(0);
    }

    // STUB TO FAIL FIRST TEST
    public Fraction divides(Fraction divisor) {
        return new Fraction(0);
    }

    // STUB TO FAIL FIRST TEST
    public Fraction divides(int divisor) {
        return new Fraction(0);
    }

    // STUB TO FAIL TEST
    public double getNumericApproximation() {
        return 0.0;
    }

    /**
     * Gives a hash code. The hash codes are not unique for all possible
     * Fraction objects. Hopefully, though, the hash codes will be distinct
     * enough for most use cases.
     * @return The hash code. The sign of the hash code may or may not match the
     * sign of the fraction (e.g., a negative fraction could get a positive hash
     * code).
     * @author Alonso del Arte
     */
    @Override
    public int hashCode() {
        return (int) this.fractDenom * 65536 + (int) this.fractNumer;
    }

    /**
     * Determines if a Fraction object is equal to another object.
     * @param obj The object to be tested for equality.
     * @return True if both objects are of class Fraction and they represent the
     * same arithmetical fraction (regardless of what numerators and
     * denominators were used at the time of construction), false otherwise. For
     * example, 3/4 and 9/12 should be found to be equal. 3/4 and 3/5 should not
     * be found to be equal. A Fraction object should not be found to be equal
     * to an {@link Integer} object even if their values are arithmetically
     * equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Fraction other = (Fraction) obj;
        if (this.fractNumer != other.fractNumer) {
            return false;
        }
        return (this.fractDenom == other.fractDenom);
    }

    /**
     * Gives a representation of this fraction as a String, using ASCII
     * characters only.
     * @return A String with the numerator, followed by the character "/" and
     * then the denominator. However, if this fraction is an integer, the
     * characters "/1" will be omitted. For example, if this fraction is
     * &minus;1/2, the result will be "-1/2".
     */
    @Override
    public String toString() {
        if (this.fractDenom == 1) {
            return Long.toString(this.fractNumer);
        } else {
            return (this.fractNumer + "/" + this.fractDenom);
        }
    }

    // STUB TO FAIL FIRST TEST
    public String toHTMLString() {
        return "Not implemented yet";
    }

    // STUB TO FAIL FIRST TEST
    public String toTeXString() {
        return "Not implemented yet";
    }

    // STUB TO FAIL FIRST TEST
    public boolean isUnitFraction() {
        return false;
    }

    // STUB TO FAIL FIRST TEST
    @Override
    public int compareTo(Fraction other) {
        return -1;
    }

    public Fraction(long numerator) {
        this.fractNumer = numerator;
        this.fractDenom = 1;
    }

    // PRIMARY CONSTRUCTOR STUB
    public Fraction(long numerator, long denominator) {
        this.fractNumer = numerator;
        this.fractDenom = denominator;
    }

}
