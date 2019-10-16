package katae.fractions;

public class Fraction implements Comparable<Fraction> {

    private final long fractNumer;
    private final long fractDenom;

    // STUB TO FAIL FIRST TEST
    public long getNumerator() {
        return 0L;
    }

    // STUB TO FAIL FIRST TEST
    public long getDenominator() {
        return 0L;
    }

    // STUB TO FAIL FIRST TEST
    public Fraction plus(Fraction summand) {
        return new Fraction(0);
    }

    // STUB TO FAIL FIRST TEST
    public Fraction plus(int summand) {
        return new Fraction(0);
    }

    // STUB TO FAIL FIRST TEST
    public Fraction negate() {
        return new Fraction(0);
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

    // STUB TO FAIL FIRST TEST
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
            return (this.fractNumer + " \\ " + this.fractDenom);
            // CHANGE BACKSLASH TO FORWARD SLASH TO PASS TEST
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
        return 0;
    }

    // AUXILIARY CONSTRUCTOR STUB
    public Fraction(long numerator) {
        this.fractNumer = numerator;
        this.fractDenom = 0;
    }

    // PRIMARY CONSTRUCTOR STUB
    public Fraction(long numerator, long denominator) {
        this.fractNumer = numerator;
        this.fractDenom = denominator;
    }

}
