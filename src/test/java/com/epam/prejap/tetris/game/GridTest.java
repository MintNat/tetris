package com.epam.prejap.tetris.game;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static com.epam.prejap.tetris.game.GridData.*;
import static java.util.Arrays.asList;

/**
 * Tests for the Grid class, its inner class Row.
 * In case if the field "row" is not present in the Row class or the field "lines" in the Grid class,
 * FailListener will mark skipped tests as failed
 *
 * @author Miatowicz Natalia
 */
@Test(groups = {"Remove filled lines tests", "gridTests"})
@Listeners(FailListener.class)
public class GridTest {
    public void methodHasFilledLinesReturnsTrueWhenCompleteRowPresent() throws ReflectiveOperationException {
        Field row = getAccessToRowFieldInRowClass();
        Field lines = getAccessToLinesFieldInGrid();
        Grid gridWithFilledRow = new Grid(rows, cols);
        Grid.Row filledRow = new Grid.Row(cols);
        Integer[] tmpLine = new Integer[cols];
        Arrays.fill(tmpLine, 1);
        row.set(filledRow, asList(tmpLine));
        List<Grid.Row> linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(2, filledRow);
        lines.set(gridWithFilledRow, linesTmp);

        Assert.assertTrue(gridWithFilledRow.hasFilledLines(), "Should return true, when the complete line present");
    }

    public void methodIsFilledReturnsTrueIfRowIsComplete() throws ReflectiveOperationException {
        Field row = getAccessToRowFieldInRowClass();
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
    public void removesCompleteLinesFromGrid(Grid grid, Grid expectedGrid, String msg) throws ReflectiveOperationException {
        Field lines = getAccessToLinesFieldInGrid();
        grid.removeFilledLine();
        Assert.assertEquals(lines.get(grid), lines.get(expectedGrid), msg);
    }

    @Test(dataProvider = "gridWithNotFullyFilledLines", dataProviderClass = GridData.class)
    public void noCompleteLinesToRemoveGrid(Grid grid, Grid expectedGrid) throws ReflectiveOperationException {
        Field lines = getAccessToLinesFieldInGrid();
        grid.removeFilledLine();
        Assert.assertEquals(lines.get(grid), lines.get(expectedGrid), "No lines should be removed");
    }
}
