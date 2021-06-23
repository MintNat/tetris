package com.epam.prejap.tetris.game;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static com.epam.prejap.tetris.game.GridData.getAccessToRowField;
import static com.epam.prejap.tetris.game.GridData.rows;
import static com.epam.prejap.tetris.game.GridData.cols;
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

    @Test(dataProvider = "rowIsFilled", dataProviderClass = GridData.class)
    public void returnFalseIfRowIsNotComplete(Grid.Row row, String msg) {
        Assert.assertFalse(row.isFilled(), msg);
    }

    @Test(dataProvider = "gridWithFilledLines", dataProviderClass = GridData.class)
    public void removesCompleteLinesFromGrid(Grid grid, Grid expectedGrid, String msg) {
        grid.removeFilledLine();
        Assert.assertEquals(grid.lines, expectedGrid.lines, msg);
    }

    @Test(dataProvider = "gridWithNotFullyFilledLines", dataProviderClass = GridData.class)
    public void noCompleteLinesToRemoveGrid(Grid grid, Grid expectedGrid) {
        grid.removeFilledLine();
        Assert.assertEquals(grid.lines, expectedGrid.lines, "No lines should be removed");
    }
}
