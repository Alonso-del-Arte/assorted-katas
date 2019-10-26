package katae.calculators;

import katae.fractions.Fraction;

class ToyCalculator {

    static int toyAdd(int a, int b) {
        return a + b;
    }

    static int toySubtract(int a, int b) {
        return a - b;
    }

    static int toyMultiply(int a, int b) {
        return a * b;
    }

    static double toyReciprocal(int n) {
        Fraction recip = new Fraction(1, n);
        return recip.getNumericApproximation();
    }

}
