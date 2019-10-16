/*
 * Copyright (C) 2019 Alonso del Arte
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package katae.fractions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * Displays a list of fractions, totaling at least 0 but not more than 1, as a
 * pie onto a Java AWT Canvas component. This component can then be added to
 * either a Java AWT Frame or a Java Swing JPanel.
 * @author Alonso del Arte
 */
public class FractionPieChartCanvas extends JPanel {

    /**
     * The smallest positive fraction this pie chart can display, 1/360,
     * corresponding to 1 degree (&pi;/180 radians). Even this may be too narrow
     * to see.
     */
    public static final Fraction SMALLEST_DISPLAYABLE_FRACTION = new Fraction(1, 360);

    /**
     * The largest fraction this pie chart can display, 1.
     */
    public static final Fraction LARGEST_DISPLAYABLE_FRACTION = new Fraction(1);

    public static final Color DEFAULT_ZERO_COLOR = new Color(0);

    public static final int DEFAULT_MARGIN = 40;
    public static final int DEFAULT_ZERO_INDENT = 10;
    public static final int DEFAULT_PIE_DIAMETER = 440;

    public static final Dimension PREFERRED_SIZE = new Dimension(2 * DEFAULT_MARGIN + DEFAULT_PIE_DIAMETER,
            2 * DEFAULT_MARGIN + DEFAULT_PIE_DIAMETER);

    private ArrayList<Fraction> fracts;

    private ArrayList<Fraction> dispFracts;

    private int currSliceNumber = 0;

    private int currR = 255;

    private int currG = 160;

    private int currB = 48;

    private static final int R_DEC = 4;

    private static final int G_DEC = 8;

    private static final int B_DEC = 16;

    private final int ALMOST_OPAQUE = 224;

    private Color nextColor() {
        Color color;
        switch (this.currSliceNumber % 3) {
            case 0:
                color = new Color(this.currR, this.currG, this.currB, ALMOST_OPAQUE);
                break;
            case 1:
                color = new Color(this.currG, this.currB, this.currR, ALMOST_OPAQUE);
                break;
            case 2:
                color = new Color(this.currB, this.currR, this.currG, ALMOST_OPAQUE);
                break;
            default:
                color = new Color(0); // Just in case currSliceNumber is negative
        }
        this.currSliceNumber++;
        this.currR -= R_DEC;
        this.currG -= G_DEC;
        this.currB -= B_DEC;
        if (this.currR < R_DEC) {
            this.currR += 254 - R_DEC;
        }
        if (this.currG < G_DEC) {
            this.currG += 254 - G_DEC;
        }
        if (this.currB < B_DEC) {
            this.currB += 254 - B_DEC;
        }
        return color;
    }

    /**
     * Paints a black oval, and then, if applicable, slices of various colors to
     * represent the list of fractions.
     * @param g The <code>Graphics</code> to paint to.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(DEFAULT_ZERO_COLOR);
        g.fillOval(DEFAULT_MARGIN + DEFAULT_ZERO_INDENT,
                DEFAULT_MARGIN + DEFAULT_ZERO_INDENT,
                DEFAULT_PIE_DIAMETER - 2 * DEFAULT_ZERO_INDENT,
                DEFAULT_PIE_DIAMETER - 2 * DEFAULT_ZERO_INDENT);
        int startAngle = 0;
        int arcAngle;
        for (Fraction fraction : this.dispFracts) {
            arcAngle = (int) Math.floor(fraction.getNumericApproximation() * 360);
            g.setColor(this.nextColor());
            g.fillArc(DEFAULT_MARGIN, DEFAULT_MARGIN, DEFAULT_PIE_DIAMETER, DEFAULT_PIE_DIAMETER, startAngle, arcAngle);
            startAngle += arcAngle;
        }
    }

    private void reviewFractionList() {
        this.dispFracts = new ArrayList<>();
        this.fracts.stream().filter((fract) -> (fract.compareTo(LARGEST_DISPLAYABLE_FRACTION) <= 0 && fract.compareTo(SMALLEST_DISPLAYABLE_FRACTION) >= 0)).forEachOrdered((fract) -> {
            this.dispFracts.add(fract);
        });
    }

    /**
     * Change the list of fractions to represent as a pie chart. The list is
     * reviewed to ascertain which fractions are in the displayable range. Then
     * the canvas is repainted accordingly.
     * @param replacement A new list of fractions to represent as a pie chart.
     */
    public void changeFractionList(ArrayList<Fraction> replacement) {
        this.fracts = new ArrayList(replacement);
        this.reviewFractionList();
        this.repaint();
    }

    /**
     * Constructs a new canvas in which to display the pie chart of fractions.
     * The list of fractions can be changed later with {@link
     * #changeFractionList(java.util.ArrayList) changeFractionList()},
     * if necessary.
     * @param fractions A list of fractions to display initially.
     */
    public FractionPieChartCanvas(ArrayList<Fraction> fractions) {
        this.fracts = fractions;
        this.reviewFractionList();
        this.setPreferredSize(PREFERRED_SIZE);
    }

}
