package com.epam.prejap.tetris.game;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

/**
 * Tests for the Grid class, its inner class Row.
 * In case if the field "row" is not present in Row class, FailListener will mark skipped tests as failed
 *
 * @author Miatowicz Natalia
 */

@Test(groups = {"Remove filled lines tests", "gridTests"})
@Listeners(FailListener.class)
public class GridTest {
    static int rows = 10;
    static int cols = 20;

    public void methodHasFilledLinesReturnsTrueWhenCompleteRowPresent() throws ReflectiveOperationException {
        Field row = getAccessToRowField();
        Grid gridWithFilledRow = new Grid(rows, cols);
        Integer[] tmpLine = new Integer[cols];
        Arrays.fill(tmpLine, 1);
        row.set(gridWithFilledRow.lines.get(2), asList(tmpLine));
        Assert.assertTrue(gridWithFilledRow.hasFilledLines(), "Should return true, when the complete line present");
    }

    public void methodIsFilledReturnsTrueIfRowIsComplete() throws ReflectiveOperationException {
        Field row = getAccessToRowField();
        Grid.Row filledRow = new Grid.Row(cols);
        Integer[] tmpLine = new Integer[cols];
        Arrays.fill(tmpLine, 1);
        row.set(filledRow, asList(tmpLine));
        Assert.assertTrue(filledRow.isFilled(), "Should return true, when the line is filled");
    }

    @Test(dataProvider = "rowIsFilled")
    public void returnFalseIfRowIsNotComplete(Grid.Row row, String msg) {
        Assert.assertFalse(row.isFilled(), msg);
    }

    @DataProvider
    public static Object[][] rowIsFilled() throws ReflectiveOperationException {
        Field row = getAccessToRowField();
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

    @Test(dataProvider = "gridWithFilledLines")
    public void removesCompleteLinesFromGrid(Grid grid, Grid expectedGrid, String msg) {
        grid.removeFilledLine();
        Assert.assertEquals(grid.lines, expectedGrid.lines, msg);
    }

    @DataProvider
    public static Object[][] gridWithFilledLines() throws ReflectiveOperationException {
        Field row = getAccessToRowField();
        Grid.Row notFilledRow1 = new Grid.Row(cols);
        row.set(notFilledRow1, List.of(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1));
        Grid.Row notFilledRow2 = new Grid.Row(cols);
        row.set(notFilledRow2, List.of(1, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0));
        Grid.Row filledRow = new Grid.Row(cols);
        Integer[] tmpLine = new Integer[cols];
        Arrays.fill(tmpLine, 1);
        row.set(filledRow, asList(tmpLine));

        Grid grid1 = new Grid(rows, cols);
        grid1.lines.set(7, notFilledRow1);
        grid1.lines.set(8, notFilledRow2);
        grid1.lines.set(9, filledRow);
        Grid expectedGrid1 = new Grid(rows, cols);
        expectedGrid1.lines.set(8, notFilledRow1);
        expectedGrid1.lines.set(9, notFilledRow2);

        Grid grid2 = new Grid(rows, cols);
        grid2.lines.set(5, notFilledRow1);
        grid2.lines.set(6, filledRow);
        grid2.lines.set(7, notFilledRow2);
        grid2.lines.set(8, notFilledRow1);
        grid2.lines.set(9, filledRow);
        Grid expectedGrid2 = new Grid(rows, cols);
        expectedGrid2.lines.set(7, notFilledRow1);
        expectedGrid2.lines.set(8, notFilledRow2);
        expectedGrid2.lines.set(9, notFilledRow1);

        Grid grid3 = new Grid(rows, cols);
        grid3.lines.set(3, notFilledRow2);
        grid3.lines.set(4, notFilledRow1);
        grid3.lines.set(5, filledRow);
        grid3.lines.set(6, notFilledRow2);
        grid3.lines.set(7, filledRow);
        grid3.lines.set(8, notFilledRow1);
        grid3.lines.set(9, filledRow);
        Grid expectedGrid3 = new Grid(rows, cols);
        expectedGrid3.lines.set(6, notFilledRow2);
        expectedGrid3.lines.set(7, notFilledRow1);
        expectedGrid3.lines.set(8, notFilledRow2);
        expectedGrid3.lines.set(9, notFilledRow1);

        Grid grid4 = new Grid(rows, cols);
        grid4.lines.set(9, filledRow);
        Grid expectedGrid4 = new Grid(rows, cols);

        Grid grid5 = new Grid(rows, cols);
        grid5.lines.set(0, filledRow);
        Grid expectedGrid5 = new Grid(rows, cols);

        Grid grid6 = new Grid(rows, cols);
        grid6.lines.set(6, notFilledRow1);
        grid6.lines.set(7, notFilledRow2);
        grid6.lines.set(8, filledRow);
        grid6.lines.set(9, filledRow);
        Grid expectedGrid6 = new Grid(rows, cols);
        expectedGrid6.lines.set(8, notFilledRow1);
        expectedGrid6.lines.set(9, notFilledRow2);

        return new Object[][]{
                {grid1, expectedGrid1, "Expected one filled line (row number: 9) to be removed, lines above moved down"},
                {grid2, expectedGrid2, "Expected two filled lines (row number: 6, 9) to be removed, row 5 moved to 7, rows 7 -> 8, 8 -> 9"},
                {grid3, expectedGrid3, "Expected three filled lines (row number: 5, 7, 9) to be removed and rows 3 moved to 6, 4 -> 7, row 6 -> 8, row 8 -> 9 "},
                {grid4, expectedGrid4, "Expected one filled line (row number: 9) to be removed"},
                {grid5, expectedGrid5, "Expected one filled line (row number: 0) to be removed"},
                {grid6, expectedGrid6, "Expected two filled lines (row number: 8,9) to be removed, row 6 moved to 8, rows 7 -> 9"}
        };
    }

    @Test(dataProvider = "gridWithNotFullyFilledLines")
    public void noCompleteLinesToRemoveGrid(Grid grid, Grid expectedGrid) {
        grid.removeFilledLine();
        Assert.assertEquals(grid.lines, expectedGrid.lines, "No lines should be removed");
    }

    @DataProvider
    public static Object[][] gridWithNotFullyFilledLines() throws ReflectiveOperationException {
        Field row = getAccessToRowField();
        Grid.Row notFilledRow = new Grid.Row(cols);
        row.set(notFilledRow, List.of(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1));

        Grid grid1 = new Grid(rows, cols);
        Grid expectedGrid1 = new Grid(rows, cols);

        Grid grid2 = new Grid(rows, cols);
        grid2.lines.set(8, notFilledRow);
        grid2.lines.set(9, notFilledRow);
        Grid expectedGrid2 = new Grid(rows, cols);
        expectedGrid2.lines.set(8, notFilledRow);
        expectedGrid2.lines.set(9, notFilledRow);

        Grid grid3 = new Grid(rows, cols);
        IntStream.rangeClosed(0, 9).forEach(i -> grid3.lines.set(i, notFilledRow));
        Grid expectedGrid3 = new Grid(rows, cols);
        IntStream.rangeClosed(0, 9).forEach(i -> expectedGrid3.lines.set(i, notFilledRow));

        return new Object[][]{
                {grid1, expectedGrid1},
                {grid2, expectedGrid2},
                {grid3, expectedGrid3}
        };
    }

    private static Field getAccessToRowField() throws ReflectiveOperationException {
        Field row = Grid.Row.class.getDeclaredField("row");
        row.setAccessible(true);
        return row;
    }
}
