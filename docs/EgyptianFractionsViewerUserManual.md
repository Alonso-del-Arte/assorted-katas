# EgyptianFractionsViewer User Manual

Assuming the Fraction class is passing all the tests, this program should function correctly. If not, it may be necessary to force quit the program.

If everything's in order, the program should display a pie chart for the Egyptian fractions of 39/40 by the greedy algorithm.

## Entry fields

Above the pie chart, there is an entry field for the numerator and another one for the denominator. To change the fraction for the chart, enter a new numerator in the Numerator field, then Tab over to the Denominator field, type a new number in there and press Enter. The chart will not update until you press Enter.

Below the pie chart, there are three read-only fields: the displayed fractions, the fractions with slices too narrow to draw (those less than 1/360) and a fraction not broken down into unit fractions because they are too small and might take too long to calculate.

## Menus

### File

Save chart as... (Ctrl-Shift-S or Shift-Command-S) Saves the currently displayed chart to a PNG file. The file will have different colors than what's displayed on screen.

Close (Ctrl-W or Command-W) Closes the window.

Exit or Quit (Ctrl-Q or Command-Q) Exit the program. This is under the application menu in Mac OS X.

### Edit

Copy readouts to clipboard (Ctrl-Shift-C or Shift-Command-C) Copies the readout fields' aggregate text to the clipboard.

Copy chart to the clipboard (Ctrl-Alt-C or Option-Command-C) Copies the chart to the clipboard. The colors will be different.

### Options

Use greedy algorithm (Ctrl-G or Command-G) This is a checkbox to toggle using the greedy algorithm or using the divisors of the denominator algorithm. For example, for 7/8 with the greedy algorithm, the result would be 1/2 + 1/3 + 1/24; with the divisors of the denominator algorithm, the result would be 1/2 + 1/4 + 1/8.

### Help

Search (Mac OS X only) Not that there are too many menu items in this program, but if you can't remember where a command is, you can search for it.

User Manual... Open this document in the default Web browser.

About... Show a box with the version number and copyright information. This also gets printed to the console when applicable.