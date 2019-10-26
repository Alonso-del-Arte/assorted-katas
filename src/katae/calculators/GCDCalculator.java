package katae.calculators;

class GCDCalculator {

    static int gcd(int a, int b) {
        int currA = a;
        int currB = b;
        int currRem;
        while (currB != 0) {
            currRem = currA % currB;
            currA = currB;
            currB = currRem;
        }
        return Math.abs(currA);
    }

}
