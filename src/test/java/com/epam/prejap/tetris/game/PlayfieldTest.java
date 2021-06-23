package com.epam.prejap.tetris.game;

import com.epam.prejap.tetris.block.BlockFeed;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Tests for feature that removes filled lines
 * If the field "grid" is not present in the Playfield class and the field "row" is not present in the Row class,
 * FailListener will mark skipped tests as failed
 *
 * @author Miatowicz Natalia
 */
@Test(groups = {"Remove filled lines tests", "playfieldClassTests"})
@Listeners(FailListener.class)
public class PlayfieldTest {
    static int rows = 10;
    static int cols = 20;

    public void removeCompleteLinesFromPlayfieldGrid() throws ReflectiveOperationException {
        Field gridField = getAccessToGridField();
        Field rowField = getAccessToRowField();

        Playfield playfield = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
        Grid.Row notFilledRow = new Grid.Row(cols);
        rowField.set(notFilledRow, List.of(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1));
        Grid.Row filledRow = new Grid.Row(cols);
        Integer[] tmpLine = new Integer[cols];
        Arrays.fill(tmpLine, 1);
        rowField.set(filledRow, asList(tmpLine));
        Grid expectedGrid = new Grid(rows, cols);
        expectedGrid.lines.set(8, notFilledRow);
        expectedGrid.lines.set(9, notFilledRow);

        Grid grid = new Grid(rows, cols);
        grid.lines.set(7, notFilledRow);
        grid.lines.set(8, notFilledRow);
        grid.lines.set(9, filledRow);
        gridField.set(playfield, grid);
        playfield.findAndRemoveFilledLines();

        Assert.assertEquals(gridField.get(playfield), expectedGrid, "Expected one filled line (row number: 9) to be removed, lines from above moved down");
    }

    public void noLinesRemovedFromPlayfieldGrid() throws ReflectiveOperationException {
        Field gridField = getAccessToGridField();
        Field rowField = getAccessToRowField();

        Playfield playfield = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
        Grid.Row notFilledRow = new Grid.Row(cols);
        rowField.set(notFilledRow, List.of(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1));
        Grid expectedGrid = new Grid(rows, cols);
        expectedGrid.lines.set(8, notFilledRow);
        expectedGrid.lines.set(9, notFilledRow);

        Grid grid = new Grid(rows, cols);
        grid.lines.set(8, notFilledRow);
        grid.lines.set(9, notFilledRow);
        gridField.set(playfield, grid);
        playfield.findAndRemoveFilledLines();

        Assert.assertEquals(gridField.get(playfield), expectedGrid, "Expected one filled line (row number: 9) to be removed, lines from above moved down");
    }

    private Field getAccessToRowField() throws ReflectiveOperationException {
        Field rowField = Grid.Row.class.getDeclaredField("row");
        rowField.setAccessible(true);
        return rowField;
    }

    private Field getAccessToGridField() throws ReflectiveOperationException {
        Field gridField = Playfield.class.getDeclaredField("grid");
        gridField.setAccessible(true);
        return gridField;
    }
}
