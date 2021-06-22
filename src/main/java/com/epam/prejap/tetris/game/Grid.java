package com.epam.prejap.tetris.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Grid for the Playfield class.
 * Grid's matrix is represented here as a List of rows.
 * Introduces nested class Row.
 */
class Grid {
    List<Row> lines;
    final int rowsNumber;
    final int columnsNumber;

    public Grid(int rows, int columns) {
        this.rowsNumber = rows;
        this.columnsNumber = columns;
        this.lines = new ArrayList<>(this.rowsNumber);
        for (int i = 0; i < this.rowsNumber; i++) lines.add(i, new Row(this.columnsNumber));
    }

    /**
     * Checks if there is a dot at the position with given numbers of row and column.
     *
     * @param rowNumber row number
     * @param colNumber column number
     * @return true if there is a value at given position > 0
     */
    boolean checkDotPresenceAtPosition(int rowNumber, int colNumber) {
        return lines.get(rowNumber).row.get(colNumber) > 0;
    }

    /**
     * Replaces value of the element at the specified position with the new specified value.
     *
     * @param row      row number
     * @param column   column number
     * @param newValue new value to be stored at the specified position
     */
    void replaceValue(int row, int column, Byte newValue) {
        lines.get(row).replace(column, newValue);
    }

    /**
     * Checks if the grid has filled lines (all row doesn't contain 0).
     *
     * @return true if there are at least one filled line
     */
    public boolean hasFilledLines() {
        return lines.stream().map(Row::isFilled).reduce(false, (a, b) -> a || b);
    }


    /**
     * Represents line of a grid.
     */
    class Row {
        List<Byte> row;

        public Row(int columns) {
            Byte[] line = new Byte[columns];
            Arrays.fill(line, Byte.valueOf("0"));
            this.row = asList(line);
        }

        /**
         * Replaces value at the given position in the row list with new specified value.
         *
         * @param position index of the element to replace
         * @param newValue new value to be stored at the specified position
         */
        public void replace(int position, Byte newValue) {
            row.set(position, newValue);
        }

        /**
         * Checks if this row contains a filled line.
         *
         * @return true if the row doesn't contain 0 ay any position
         */
        public boolean isFilled() {
            return !row.contains(Byte.valueOf("0"));
        }
    }
}
