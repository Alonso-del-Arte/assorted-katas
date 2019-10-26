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
package katae.fractions;

import katae.calculators.FractionsCalculator;
import swingaux.clipboardops.ImageSelection;
import swingaux.fileops.FileChooserWithOverwriteGuard;
import swingaux.fileops.PNGFileFilter;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Event;
import java.awt.Graphics2D;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

/**
 * A program to display charts of Egyptian fractions for fractions in the unit
 * interval. Can use either the greedy algorithm or the divisors of denominator
 * algorithm.
 * @author Alonso del Arte
 */
public final class EgyptianFractionViewer extends JFrame implements ActionListener {

    private Fraction inputFraction;

    private static final Fraction ZERO_FRACTION = new Fraction(0);

    private Fraction unprocAmount;

    public static final Fraction SMALLEST_PROCESSABLE_FRACTION = new Fraction(1, 1440);

    private ArrayList<Fraction> egyptianFractions;
    private ArrayList<Fraction> undisplayedFractions;

    private static final boolean MAC_OS_FLAG = System.getProperty("os.name").equals("Mac OS X");

    private static int maskCtrlCommand = Event.CTRL_MASK;

    private static final int DEFAULT_ENTRY_FIELD_COLUMNS = 5;

    private static final int DEFAULT_READOUT_FIELD_COLUMNS = 20;

    private JTextField entryNumerator, entryDenominator;

    private JTextField readoutDispFracts, readoutNonDispFracts, readoutUnprocFract;

    private final FractionPieChartCanvas pieChart;

    private boolean useGreedyAlgorithm = true;

    protected static boolean haveSavedBefore = false;

    private static String prevSavePathName;

    private void updateEntries() {
        this.entryNumerator.setText(Long.toString(this.inputFraction.getNumerator()));
        this.entryDenominator.setText(Long.toString(this.inputFraction.getDenominator()));
    }

    private void updateReaouts() {
        StringBuilder readoutText;
        if (this.egyptianFractions.isEmpty()) {
            readoutText = new StringBuilder("None wide enought to display");
        } else {
            readoutText = new StringBuilder(this.egyptianFractions.get(0).toString());
            for (int i = 1; i < this.egyptianFractions.size(); i++) {
                readoutText.append(" + ").append(this.egyptianFractions.get(i));
            }
        }
        this.readoutDispFracts.setText(readoutText.toString());
        if (this.undisplayedFractions.isEmpty()) {
            readoutText = new StringBuilder("None");
        } else {
            readoutText = new StringBuilder(this.undisplayedFractions.get(0).toString());
            for (int j = 1; j < this.undisplayedFractions.size(); j++) {
                readoutText.append(" + ").append(this.undisplayedFractions.get(j));
            }
        }
        this.readoutNonDispFracts.setText(readoutText.toString());
        if (this.unprocAmount.compareTo(ZERO_FRACTION) == 0) {
            readoutText = new StringBuilder("None");
        } else {
            readoutText = new StringBuilder(this.unprocAmount.toString());
        }
        this.readoutUnprocFract.setText(readoutText.toString());
    }

    private void processFraction() {
        this.inputFraction = FractionsCalculator.placeInUnitInterval(this.inputFraction);
        System.out.println("About to process fraction " + this.inputFraction.toString() + "...");
        if (this.useGreedyAlgorithm) {
            System.out.println("Using the greedy algorithm...");
            this.egyptianFractions = FractionsCalculator.getEgyptianFractionsGreedyCapped(this.inputFraction);
        } else {
            System.out.println("Using the divisors of denominator algorithm...");
            this.egyptianFractions = FractionsCalculator.getEgyptianFractionsByDenomDivisors(this.inputFraction);
        }
        this.undisplayedFractions = new ArrayList<>();
        Fraction currFract;
        for (int i = this.egyptianFractions.size() - 1; i > -1; i--) {
            currFract = this.egyptianFractions.get(i);
            if (currFract.compareTo(FractionPieChartCanvas.SMALLEST_DISPLAYABLE_FRACTION) < 0) {
                this.egyptianFractions.remove(currFract);
                this.undisplayedFractions.add(currFract);
            }
        }
        this.unprocAmount = null;
        if (this.undisplayedFractions.size() > 0) {
            if (this.undisplayedFractions.get(0).getNumerator() != 1) {
                this.unprocAmount = this.undisplayedFractions.get(0);
                this.undisplayedFractions.remove(0);
            }
        }
        if (this.unprocAmount == null) {
            this.unprocAmount = ZERO_FRACTION;
            System.out.println("Have successfully calculated fractions for " + this.inputFraction.toString() + ".");
        } else {
            System.out.println("Was not able to break down " + this.unprocAmount.toString() + " in a timely manner...");
        }
    }

    private void updateChart() {
        this.processFraction();
        this.pieChart.changeFractionList(this.egyptianFractions);
        String algorithmID;
        if (this.useGreedyAlgorithm) {
            algorithmID = "greedy";
        } else {
            algorithmID = "divisors of denominator";
        }
        String replacementTitle = "Egyptian fraction chart for " + this.inputFraction.toString() + " (" + algorithmID + ")";
        this.setTitle(replacementTitle);
        this.updateReaouts();
    }

    public void changeFraction(Fraction fraction) {
        this.inputFraction = fraction;
        this.updateChart();
    }

    public void changeFraction() {
        try {
            long replaceNumer = Long.parseLong(this.entryNumerator.getText());
            long replaceDenom = Long.parseLong(this.entryDenominator.getText());
            Fraction replaceFract = new Fraction(replaceNumer, replaceDenom);
            this.inputFraction = replaceFract;
            this.updateChart();
        } catch (NumberFormatException nfe) {
            String msg = "Invalid number for fraction\n\"" + nfe.getMessage() + "\"";
            JOptionPane.showMessageDialog(this, msg);
        } catch (IllegalArgumentException iae) {
            String msg = "Bad denominator\n\"" + iae.getMessage() + "\"";
            JOptionPane.showMessageDialog(this, msg);
        }
        this.entryNumerator.setText(Long.toString(this.inputFraction.getNumerator()));
        this.entryDenominator.setText(Long.toString(this.inputFraction.getDenominator()));
    }

    public void toggleGreedy() {
        this.useGreedyAlgorithm = !this.useGreedyAlgorithm;
        this.updateChart();
    }

    public void saveChartAs() {
        int imageSize = FractionPieChartCanvas.PREFERRED_SIZE.height; // Should be same as width
        BufferedImage chart = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D graph = chart.createGraphics();
        this.pieChart.paint(graph);
        String suggestedFilename = "EgyptFractsN" + this.inputFraction.getNumerator() + "D" + this.inputFraction.getDenominator() + ".png";
        File chartFile = new File(suggestedFilename);
        FileChooserWithOverwriteGuard fileChooser = new FileChooserWithOverwriteGuard();
        FileFilter pngFilter = new PNGFileFilter();
        fileChooser.addChoosableFileFilter(pngFilter);
        if (haveSavedBefore) {
            fileChooser.setCurrentDirectory(new File(prevSavePathName));
        }
        fileChooser.setSelectedFile(chartFile);
        int fcRet = fileChooser.showSaveDialog(this.pieChart);
        String notificationString;
        switch(fcRet) {
            case JFileChooser.APPROVE_OPTION:
                chartFile = fileChooser.getSelectedFile();
                String filePath = chartFile.getAbsolutePath();
                prevSavePathName = filePath.substring(0, filePath.lastIndexOf(File.separator));
                haveSavedBefore = true;
                try {
                    ImageIO.write(chart, "PNG", chartFile);
                } catch (IOException ioe) {
                    notificationString = "Image input/output exception occurred:\n " + ioe.getMessage();
                    JOptionPane.showMessageDialog(this, notificationString);
                }
                break;
            case JFileChooser.CANCEL_OPTION:
                notificationString = "File save canceled.";
                JOptionPane.showMessageDialog(this, notificationString);
                break;
            case JFileChooser.ERROR_OPTION:
                notificationString = "An error occurred trying to choose a file to save to.";
                JOptionPane.showMessageDialog(this, notificationString);
                break;
            default:
                notificationString = "Unexpected option " + fcRet + " from file chooser.";
                JOptionPane.showMessageDialog(this, notificationString);
        }
    }

    public void copyReadoutsToClipboard() {
        String agregReadouts = "Egyptian fractions for " + this.inputFraction.toString();
        agregReadouts = agregReadouts + ": " + this.readoutDispFracts.getText();
        agregReadouts = agregReadouts + " Too narrow to draw: " + this.readoutNonDispFracts.getText();
        agregReadouts = agregReadouts + " Unprocessed: " + this.readoutUnprocFract.getText();
        StringSelection ss = new StringSelection(agregReadouts);
        this.getToolkit().getSystemClipboard().setContents(ss, ss);
    }

    public void copyChartToClipboard() {
        int imageSize = FractionPieChartCanvas.PREFERRED_SIZE.height;
        BufferedImage chart = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D graph = chart.createGraphics();
        this.pieChart.paint(graph);
        ImageSelection imgSel = new ImageSelection(chart);
        this.getToolkit().getSystemClipboard().setContents(imgSel, imgSel);
    }

    /**
     * Uses the default Web browser to show the user manual for this program. If
     * the URL has bad syntax or the default Web browser can't be accessed, an
     * error message will be displayed. However, if there is no Internet
     * connection, how to handle that is up to the default Web browser.
     */
    public void showUserManual() {
        String urlStr = "https://github.com/Alonso-del-Arte/assorted-katas/blob/master/docs/EgyptianFractionsViewerUserManual.md";
        try {
            URI url = new URI(urlStr);
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(url);
            } else {
                String noDesktopMessage = "Sorry, unable to open URL\n<" + urlStr + ">\nDefault Web browser is not available from this program.";
                JOptionPane.showMessageDialog(this, noDesktopMessage);
                System.err.println(noDesktopMessage);
            }
        } catch (URISyntaxException urise) {
            System.err.println("Unable to open URL " + urlStr);
            System.err.println("\"" + urise.getMessage() + "\"");
        } catch (IOException ioe) {
            String problemMessage = "Sorry, unable to open URL\n<" + urlStr + ">\n\"" + ioe.getMessage() + "\"";
            JOptionPane.showMessageDialog(this, problemMessage);
            System.err.println(problemMessage);
        }
    }

    public void showAboutBox() {
        String aboutMessage = "Egyptian Fraction Viewer\nVersion 0.9\n\u00A9 2019 Alonso del Arte";
        JOptionPane.showMessageDialog(this, aboutMessage, "About", JOptionPane.PLAIN_MESSAGE);
        System.out.println(aboutMessage);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();
        switch (cmd) {
            case "saveDiagramAs":
                this.saveChartAs();
                break;
            case "close":
                this.dispose();
                break;
            case "exit":
                System.exit(0);
                break;
            case "copyReadouts":
                this.copyReadoutsToClipboard();
                break;
            case "copyDiagram":
                this.copyChartToClipboard();
                break;
            case "changeNumer":
            case "changeDenom":
                this.changeFraction();
                break;
            case "toggleGreedy":
                this.toggleGreedy();
                break;
            case "showUserManual":
                this.showUserManual();
                break;
            case "about":
                this.showAboutBox();
                break;
            default:
                System.out.println("Command \"" + cmd + "\" not recognized.");
        }
    }

    private JMenuItem makeMenuItem(String menuItemText, String accessibleDescription, String actionCommand, KeyStroke accelerator) {
        JMenuItem menuItem = new JMenuItem(menuItemText);
        menuItem.getAccessibleContext().setAccessibleDescription(accessibleDescription);
        menuItem.setActionCommand(actionCommand);
        menuItem.setAccelerator(accelerator);
        menuItem.addActionListener(this);
        return menuItem;
    }

    private JMenu makeFileMenu() {
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.getAccessibleContext().setAccessibleDescription("Menu for file operations");
        String accDescr = "Save currently displayed chart to a PNG file";
        KeyStroke accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, maskCtrlCommand + Event.SHIFT_MASK);
        JMenuItem menuItem = this.makeMenuItem("Save chart as...", accDescr, "saveDiagramAs", accelerator);
        menu.add(menuItem);
        menu.addSeparator();
        accDescr = "Close the window";
        accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_W, maskCtrlCommand);
        menuItem = this.makeMenuItem("Close", accDescr, "close", accelerator);
        menu.add(menuItem);
        if (!MAC_OS_FLAG) {
            accDescr = "Exit the program";
            accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_Q, maskCtrlCommand);
            menuItem = this.makeMenuItem("Exit", accDescr, "exit", accelerator);
            menu.add(menuItem);
        }
        return menu;
    }

    private JMenu makeEditMenu() {
        JMenu menu = new JMenu("Edit");
        menu.setMnemonic(KeyEvent.VK_E);
        menu.getAccessibleContext().setAccessibleDescription("Menu for editing");
        String accDescr = "Copy readouts of fractions to the clipboard";
        KeyStroke accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_C, maskCtrlCommand + Event.SHIFT_MASK);
        JMenuItem menuItem = this.makeMenuItem("Copy readouts to clipboard", accDescr, "copyReadouts", accelerator);
        menu.add(menuItem);
        accDescr = "Copy the chart of the currently displayed Egyptian fractions diagram to the clipboard";
        accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_C, maskCtrlCommand + Event.ALT_MASK);
        menuItem = this.makeMenuItem("Copy chart to the clipboard", accDescr, "copyDiagram", accelerator);
        menu.add(menuItem);
        return menu;
    }

    private JMenu makeOptionsMenu() {
        JMenu menu = new JMenu("Options");
        menu.setMnemonic(KeyEvent.VK_O);
        menu.getAccessibleContext().setAccessibleDescription("Menu for changing options");
        JCheckBoxMenuItem greedCheck = new JCheckBoxMenuItem("Use greedy algorithm", true);
        greedCheck.getAccessibleContext().setAccessibleDescription("Toggle whether or not to use the greedy algorithm");
        greedCheck.setActionCommand("toggleGreedy");
        greedCheck.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, maskCtrlCommand));
        greedCheck.addActionListener(this);
        menu.add(greedCheck);
        return menu;
    }

    private JMenu makeHelpMenu() {
        JMenu menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menu.getAccessibleContext().setAccessibleDescription("Menu to provide help and documentation");
        String accDescr = "Use default Web browser to show user manual";
        KeyStroke accelerator;
        if (MAC_OS_FLAG) {
            accelerator = null;
        } else {
            accelerator =  KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
        }
        JMenuItem menuItem = this.makeMenuItem("User Manual...", accDescr, "showUserManual", accelerator);
        menu.add(menuItem);
        accDescr = "Display information about this program";
        menuItem = this.makeMenuItem("About...", accDescr, "about", null);
        menu.add(menuItem);
        return menu;
    }

    private JMenuBar setUpMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(this.makeFileMenu());
        menuBar.add(this.makeEditMenu());
        menuBar.add(this.makeOptionsMenu());
        menuBar.add(this.makeHelpMenu());
        return menuBar;
    }

    private JPanel setUpFractionEntryFields() {
        JPanel entryPanel = new JPanel();
        entryPanel.add(new JLabel("Numerator: "));
        this.entryNumerator = new JTextField(DEFAULT_ENTRY_FIELD_COLUMNS);
        this.entryNumerator.setActionCommand("changeNumer");
        this.entryNumerator.addActionListener(this);
        entryPanel.add(this.entryNumerator);
        entryPanel.add(new JLabel(" / Denominator: "));
        this.entryDenominator = new JTextField(DEFAULT_ENTRY_FIELD_COLUMNS);
        this.entryDenominator.setActionCommand("changeDenom");
        this.entryDenominator.addActionListener(this);
        entryPanel.add(this.entryDenominator);
        return entryPanel;
    }

    private JPanel setUpPieReadouts() {
        JPanel readoutPanel = new JPanel();
        GroupLayout layout = new GroupLayout(readoutPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        JLabel labelDispFract = new JLabel("Displayed fractions: ");
        JLabel labelUndispFract = new JLabel("Too narrow to draw: ");
        JLabel labelUnprocFract = new JLabel("Unprocessed: ");
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(labelDispFract)
                .addComponent(labelUndispFract)
                .addComponent(labelUnprocFract));
        this.readoutDispFracts = new JTextField(DEFAULT_READOUT_FIELD_COLUMNS);
        this.readoutDispFracts.setEditable(false);
        this.readoutNonDispFracts = new JTextField(DEFAULT_READOUT_FIELD_COLUMNS);
        this.readoutNonDispFracts.setEditable(false);
        this.readoutUnprocFract = new JTextField(DEFAULT_READOUT_FIELD_COLUMNS);
        this.readoutUnprocFract.setEditable(false);
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(this.readoutDispFracts)
                .addComponent(this.readoutNonDispFracts)
                .addComponent(this.readoutUnprocFract));
        layout.setHorizontalGroup(hGroup);
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(labelDispFract)
                .addComponent(this.readoutDispFracts));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(labelUndispFract)
                .addComponent(this.readoutNonDispFracts));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(labelUnprocFract)
                .addComponent(this.readoutUnprocFract));
        layout.setVerticalGroup(vGroup);
        readoutPanel.setLayout(layout);
        return readoutPanel;
    }

    public EgyptianFractionViewer(Fraction fraction) {
        if (MAC_OS_FLAG) {
            maskCtrlCommand = Event.META_MASK;
        }
        this.inputFraction = fraction;
        this.processFraction();
        this.setTitle("Egyptian fraction chart for " + this.inputFraction.toString());
        this.setJMenuBar(this.setUpMenuBar());
//        this.setBackground(??? WHICH COLOR ????);
        this.pieChart = new FractionPieChartCanvas(this.egyptianFractions);
        this.add(this.setUpFractionEntryFields(), BorderLayout.PAGE_START);
        this.add(this.pieChart, BorderLayout.CENTER);
        this.add(this.setUpPieReadouts(), BorderLayout.PAGE_END);
        this.updateEntries();
        this.updateReaouts();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        if (MAC_OS_FLAG) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.out.println("Mac OS X detected");
        }
        Fraction initFract = new Fraction(39, 40);
        System.out.println("About to set up viewer with initial fraction " + initFract.toString());
        EgyptianFractionViewer egv = new EgyptianFractionViewer(initFract);
    }

}
