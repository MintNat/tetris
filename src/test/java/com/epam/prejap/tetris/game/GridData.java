package com.epam.prejap.tetris.game;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

public class GridData {
    static int rows = 10;
    static int cols = 20;

    static Field getAccessToLinesFieldInGrid() throws ReflectiveOperationException {
        Field lines = Grid.class.getDeclaredField("lines");
        lines.setAccessible(true);
        return lines;
    }

    static Field getAccessToRowFieldInRowClass() throws ReflectiveOperationException {
        Field row = Grid.Row.class.getDeclaredField("row");
        row.setAccessible(true);
        return row;
    }

    static List<Grid.Row> generateNewListRowsFilledWithZeros() {
        List<Grid.Row> linesTmp = new ArrayList<>();
        for (int i = 0; i < rows; i++) linesTmp.add(i, new Grid.Row(cols));
        return linesTmp;
    }


    @DataProvider
    public static Object[][] rowIsFilled() throws ReflectiveOperationException {
        Field row = getAccessToRowFieldInRowClass();
        Grid.Row notFilledRow1 = new Grid.Row(cols);
        row.set(notFilledRow1, List.of(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1));
        Grid.Row notFilledRow2 = new Grid.Row(cols);
        row.set(notFilledRow2, List.of(1, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0));
        String msg = "Should return false, when the line is not filled";
        return new Object[][]{
                {notFilledRow1, msg},
                {notFilledRow2, msg}
        };
    }

    @DataProvider
    public static Object[][] gridWithFilledLines() throws ReflectiveOperationException {
        Field row = getAccessToRowFieldInRowClass();
        Field lines = getAccessToLinesFieldInGrid();
        Grid.Row notFilledRow1 = new Grid.Row(cols);
        row.set(notFilledRow1, List.of(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1));
        Grid.Row notFilledRow2 = new Grid.Row(cols);
        row.set(notFilledRow2, List.of(1, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0));
        Grid.Row filledRow = new Grid.Row(cols);
        Integer[] tmpLine = new Integer[cols];
        Arrays.fill(tmpLine, 1);
        row.set(filledRow, asList(tmpLine));

        Grid grid1 = new Grid(rows, cols);
        List<Grid.Row> linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(7, notFilledRow1);
        linesTmp.set(8, notFilledRow2);
        linesTmp.set(9, filledRow);
        lines.set(grid1, linesTmp);
        Grid expectedGrid1 = new Grid(rows, cols);
        linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(8, notFilledRow1);
        linesTmp.set(9, notFilledRow2);
        lines.set(expectedGrid1, linesTmp);

        Grid grid2 = new Grid(rows, cols);
        linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(5, notFilledRow1);
        linesTmp.set(6, filledRow);
        linesTmp.set(7, notFilledRow2);
        linesTmp.set(8, notFilledRow1);
        linesTmp.set(9, filledRow);
        lines.set(grid2, linesTmp);
        Grid expectedGrid2 = new Grid(rows, cols);
        linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(7, notFilledRow1);
        linesTmp.set(8, notFilledRow2);
        linesTmp.set(9, notFilledRow1);
        lines.set(expectedGrid2, linesTmp);

        Grid grid3 = new Grid(rows, cols);
        linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(3, notFilledRow2);
        linesTmp.set(4, notFilledRow1);
        linesTmp.set(5, filledRow);
        linesTmp.set(6, notFilledRow2);
        linesTmp.set(7, filledRow);
        linesTmp.set(8, notFilledRow1);
        linesTmp.set(9, filledRow);
        lines.set(grid3, linesTmp);
        Grid expectedGrid3 = new Grid(rows, cols);
        linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(6, notFilledRow2);
        linesTmp.set(7, notFilledRow1);
        linesTmp.set(8, notFilledRow2);
        linesTmp.set(9, notFilledRow1);
        lines.set(expectedGrid3, linesTmp);

        Grid grid4 = new Grid(rows, cols);
        linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(9, filledRow);
        lines.set(grid4, linesTmp);
        Grid expectedGrid4 = new Grid(rows, cols);

        Grid grid5 = new Grid(rows, cols);
        linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(0, filledRow);
        lines.set(grid5, linesTmp);
        Grid expectedGrid5 = new Grid(rows, cols);

        Grid grid6 = new Grid(rows, cols);
        linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(6, notFilledRow1);
        linesTmp.set(7, notFilledRow2);
        linesTmp.set(8, filledRow);
        linesTmp.set(9, filledRow);
        lines.set(grid6, linesTmp);
        Grid expectedGrid6 = new Grid(rows, cols);
        linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(8, notFilledRow1);
        linesTmp.set(9, notFilledRow2);
        lines.set(expectedGrid6, linesTmp);

        return new Object[][]{
                {grid1, expectedGrid1, "Expected one filled line (row number: 9) to be removed, lines above moved down"},
                {grid2, expectedGrid2, "Expected two filled lines (row number: 6, 9) to be removed, row 5 moved to 7, rows 7 -> 8, 8 -> 9"},
                {grid3, expectedGrid3, "Expected three filled lines (row number: 5, 7, 9) to be removed and rows 3 moved to 6, 4 -> 7, row 6 -> 8, row 8 -> 9 "},
                {grid4, expectedGrid4, "Expected one filled line (row number: 9) to be removed"},
                {grid5, expectedGrid5, "Expected one filled line (row number: 0) to be removed"},
                {grid6, expectedGrid6, "Expected two filled lines (row number: 8,9) to be removed, row 6 moved to 8, rows 7 -> 9"}
        };
    }

    @DataProvider
    public static Object[][] gridWithNotFullyFilledLines() throws ReflectiveOperationException {
        Field row = getAccessToRowFieldInRowClass();
        Field lines = getAccessToLinesFieldInGrid();
        Grid.Row notFilledRow = new Grid.Row(cols);
        row.set(notFilledRow, List.of(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1));

        Grid grid1 = new Grid(rows, cols);
        Grid expectedGrid1 = new Grid(rows, cols);

        Grid grid2 = new Grid(rows, cols);
        List<Grid.Row> linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(8, notFilledRow);
        linesTmp.set(9, notFilledRow);
        lines.set(grid2, linesTmp);
        Grid expectedGrid2 = new Grid(rows, cols);
        linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(8, notFilledRow);
        linesTmp.set(9, notFilledRow);
        lines.set(expectedGrid2, linesTmp);

        Grid grid3 = new Grid(rows, cols);
        List<Grid.Row> grid3Tmp = generateNewListRowsFilledWithZeros();
        IntStream.rangeClosed(0, 9).forEach(i -> grid3Tmp.set(i, notFilledRow));
        lines.set(grid3, grid3Tmp);
        Grid expectedGrid3 = new Grid(rows, cols);
        List<Grid.Row> expectedGrid3Tmp = generateNewListRowsFilledWithZeros();
        IntStream.rangeClosed(0, 9).forEach(i -> expectedGrid3Tmp.set(i, notFilledRow));
        lines.set(expectedGrid3, expectedGrid3Tmp);

        return new Object[][]{
                {grid1, expectedGrid1},
                {grid2, expectedGrid2},
                {grid3, expectedGrid3}
        };
    }
}
