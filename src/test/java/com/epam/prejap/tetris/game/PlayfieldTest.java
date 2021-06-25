package com.epam.prejap.tetris.game;

import com.epam.prejap.tetris.block.BlockFeed;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Tests for feature that removes filled lines
 * If the field "grid" is not present in the Playfield class or the field "lines" is not present in the Grid class
 * or the field "row" is not present in the Row class, then FailListener will mark skipped tests as failed
 *
 * @author Miatowicz Natalia
 */
@Test(groups = {"Remove filled lines tests", "playfieldClassTests"})
@Listeners(FailListener.class)
public class PlayfieldTest {
    static int rows = 10;
    static int cols = 20;

    public void removeCompleteLinesFromPlayfieldGrid() throws ReflectiveOperationException {
        Field gridField = getAccessToGridFieldInPlayfield();
        Field rowField = getAccessToRowFieldInRowClass();
        Field lines = getAccessToLinesFieldInGrid();

        Playfield playfield = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
        Grid.Row notFilledRow = new Grid.Row(cols);
        rowField.set(notFilledRow, List.of(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1));
        Grid.Row filledRow = new Grid.Row(cols);
        Integer[] tmpLine = new Integer[cols];
        Arrays.fill(tmpLine, 1);
        rowField.set(filledRow, asList(tmpLine));
        Grid expectedGrid = new Grid(rows, cols);
        List<Grid.Row> linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(8, notFilledRow);
        linesTmp.set(9, notFilledRow);
        lines.set(expectedGrid, linesTmp);

        Grid grid = new Grid(rows, cols);
        linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(7, notFilledRow);
        linesTmp.set(8, notFilledRow);
        linesTmp.set(9, filledRow);
        lines.set(grid, linesTmp);
        gridField.set(playfield, grid);

        playfield.findAndRemoveFilledLines();

        Assert.assertEquals(gridField.get(playfield), expectedGrid, "Expected one filled line (row number: 9) to be removed, lines from above moved down");
    }

    public void noLinesRemovedFromPlayfieldGrid() throws ReflectiveOperationException {
        Field gridField = getAccessToGridFieldInPlayfield();
        Field rowField = getAccessToRowFieldInRowClass();
        Field lines = getAccessToLinesFieldInGrid();

        Playfield playfield = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
        Grid.Row notFilledRow = new Grid.Row(cols);
        rowField.set(notFilledRow, List.of(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1));
        Grid expectedGrid = new Grid(rows, cols);
        List<Grid.Row> linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(8, notFilledRow);
        linesTmp.set(9, notFilledRow);
        lines.set(expectedGrid, linesTmp);

        Grid grid = new Grid(rows, cols);
        linesTmp = generateNewListRowsFilledWithZeros();
        linesTmp.set(8, notFilledRow);
        linesTmp.set(9, notFilledRow);
        lines.set(grid, linesTmp);
        gridField.set(playfield, grid);

        playfield.findAndRemoveFilledLines();

        Assert.assertEquals(gridField.get(playfield), expectedGrid, "Expected one filled line (row number: 9) to be removed, lines from above moved down");
    }

    private List<Grid.Row> generateNewListRowsFilledWithZeros() {
        List<Grid.Row> linesTmp = new ArrayList<>();
        for (int i = 0; i < rows; i++) linesTmp.add(i, new Grid.Row(cols));
        return linesTmp;
    }

    private Field getAccessToRowFieldInRowClass() throws ReflectiveOperationException {
        Field rowField = Grid.Row.class.getDeclaredField("row");
        rowField.setAccessible(true);
        return rowField;
    }

    private Field getAccessToGridFieldInPlayfield() throws ReflectiveOperationException {
        Field gridField = Playfield.class.getDeclaredField("grid");
        gridField.setAccessible(true);
        return gridField;
    }

    static Field getAccessToLinesFieldInGrid() throws ReflectiveOperationException {
        Field lines = Grid.class.getDeclaredField("lines");
        lines.setAccessible(true);
        return lines;
    }
}
