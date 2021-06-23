package com.epam.prejap.tetris.game;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

/**
 * Grid for the Playfield class usage.
 * Grid's matrix is represented here as a List of rows.
 * Introduces nested class Row.
 *
 * @author Miatowicz Natalia
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
    void replaceValue(int row, int column, Integer newValue) {
        lines.get(row).replace(column, newValue);
    }

    /**
     * Checks if the grid has filled lines (all row doesn't contain 0).
     *
     * @return true if there are at least one filled line
     */
    boolean hasFilledLines() {
        return lines.stream().map(Row::isFilled).reduce(false, (a, b) -> a || b);
    }

    /**
     * Removes filled rows.
     * Appends new rows, filled with 0, at the beginning of the List lines such number of rows, as many lines were removed
     */
     void removeFilledLine() {
        var filledLines = lines.stream().filter(Row::isFilled).collect(Collectors.toList());
        lines.removeAll(filledLines);
        for (int i = 0; i < filledLines.size(); i++) {
            lines.add(i, new Row(columnsNumber));
        }
    }

    /**
     * Represents one line of the grid.
     */
    static class Row {
        private final List<Integer> row;

        Row(int columns) {
            Integer[] line = new Integer[columns];
            Arrays.fill(line, 0);
            this.row = asList(line);
        }

        /**
         * Replaces value at the given position in the row list with new specified value.
         *
         * @param position index of the element to replace
         * @param newValue new value to be stored at the specified position
         */
        void replace(int position, Integer newValue) {
            row.set(position, newValue);
        }

        /**
         * Checks if this row contains a filled line.
         *
         * @return true if the row doesn't contain 0 ay any position
         */
        boolean isFilled() {
            return row.stream().noneMatch(number -> (number == null || number == 0));
        }

        public List<Integer> getRow() {
            return Collections.unmodifiableList(row);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Row row1 = (Row) o;

            return Objects.equals(row, row1.row);
        }

        @Override
        public int hashCode() {
            return row != null ? row.hashCode() : 0;
        }
    }
}
