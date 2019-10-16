/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package katae.fractions;

/**
 *
 * @author TBD
 */
public class FractionRange {

    private Fraction beginFract, finishFract;

    // STUB TO FAIL THE FIRST TEST
    public int length() {
        return 0;
    }

    // STUB TO FAIL THE FIRST TEST
    public Fraction apply(int index) {
        return new Fraction(0);
    }

    // STUB TO FAIL THE FIRST TEST
    public FractionRange by(Fraction interval) {
        return this;
    }

    public FractionRange(Fraction start, Fraction end) {
        this.beginFract = start;
        this.finishFract = end;
    }

}
