package com.epam.prejap.tetris.game;

import com.epam.prejap.tetris.block.BlockFeed;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Tests for feature that removes filled lines
 * In case if the field "grid" is not present in Playfield class, FailListener will mark skipped tests as failed
 *
 * @author Miatowicz Natalia
 */
@Listeners(FailListener.class)
public class PlayfieldTest {
    @Test(groups = {"Functions tests", "playfieldClassTests"}, dataProvider = "gridWithNotFullyFilledLines")
    public void noCompleteLinesToRemoveFromPlayfield(Playfield playfield, byte[][] expectedGrid) throws ReflectiveOperationException {
        Field grid = Playfield.class.getDeclaredField("grid");
        grid.setAccessible(true);
        playfield.findAndRemoveFilledLines();
        Assert.assertEquals(grid.get(playfield), expectedGrid, "No lines should be removed");
    }

    @DataProvider
    public static Object[][] gridWithNotFullyFilledLines() throws ReflectiveOperationException {
        Field grid = Playfield.class.getDeclaredField("grid");
        grid.setAccessible(true);
        int rows = 10;
        int cols = 20;
        byte[] notCompleteLine = new byte[]{0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1};

        Playfield playfield1 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
        byte[][] expectedGrid1 = new byte[rows][cols];

        Playfield playfield2 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
        byte[][] grid2 = new byte[rows][cols];
        grid2[8] = Arrays.copyOf(notCompleteLine, cols);
        grid2[9] = Arrays.copyOf(notCompleteLine, cols);
        grid.set(playfield2, grid2);

        byte[][] expectedGrid2 = new byte[rows][cols];
        expectedGrid2[8] = Arrays.copyOf(notCompleteLine, cols);
        expectedGrid2[9] = Arrays.copyOf(notCompleteLine, cols);

        Playfield playfield3 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
        byte[][] grid3 = new byte[rows][cols];
        IntStream.rangeClosed(0, 9).forEach(i -> grid3[i] = Arrays.copyOf(notCompleteLine, cols));
        grid.set(playfield3, grid3);

        byte[][] expectedGrid3 = new byte[rows][cols];
        IntStream.rangeClosed(0, 9).forEach(i -> expectedGrid3[i] = Arrays.copyOf(notCompleteLine, cols));

        return new Object[][]{
                {playfield1, expectedGrid1},
                {playfield2, expectedGrid2},
                {playfield3, expectedGrid3}
        };
    }

    @Test(groups = {"Functions tests", "playfieldClassTests"}, dataProvider = "gridWithFilledLines")
    public void removeCompleteLinesFromPlayfield(Playfield playfield, byte[][] expectedGrid, String msg) throws ReflectiveOperationException {
        Field grid = Playfield.class.getDeclaredField("grid");
        grid.setAccessible(true);
        playfield.findAndRemoveFilledLines();
        Assert.assertEquals(grid.get(playfield), expectedGrid, msg);
    }

    @DataProvider
    public static Object[][] gridWithFilledLines() throws ReflectiveOperationException {
        Field grid = Playfield.class.getDeclaredField("grid");
        grid.setAccessible(true);
        int rows = 10;
        int cols = 20;
        byte[] notCompleteLine = new byte[]{1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1};
        byte[] completeLine = new byte[cols];
        Arrays.fill(completeLine, (byte) 1);

        Playfield playfield1 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
        byte[][] grid1 = new byte[rows][cols];
        grid1[7] = Arrays.copyOf(notCompleteLine, cols);
        grid1[8] = Arrays.copyOf(notCompleteLine, cols);
        grid1[9] = Arrays.copyOf(completeLine, cols);
        grid.set(playfield1, grid1);

        byte[][] expectedGrid1 = new byte[rows][cols];
        expectedGrid1[8] = Arrays.copyOf(grid1[7], cols);
        expectedGrid1[9] = Arrays.copyOf(grid1[8], cols);

        Playfield playfield2 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
        byte[][] grid2 = new byte[rows][cols];
        grid2[5] = Arrays.copyOf(notCompleteLine, cols);
        grid2[6] = Arrays.copyOf(completeLine, cols);
        grid2[7] = Arrays.copyOf(notCompleteLine, cols);
        grid2[8] = Arrays.copyOf(notCompleteLine, cols);
        grid2[9] = Arrays.copyOf(completeLine, cols);
        grid.set(playfield2, grid2);

        byte[][] expectedGrid2 = new byte[rows][cols];
        expectedGrid2[7] = Arrays.copyOf(grid2[5], cols);
        expectedGrid2[8] = Arrays.copyOf(grid2[7], cols);
        expectedGrid2[9] = Arrays.copyOf(grid2[8], cols);

        Playfield playfield3 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
        byte[][] grid3 = new byte[rows][cols];
        grid3[3] = Arrays.copyOf(notCompleteLine, cols);
        grid3[4] = Arrays.copyOf(notCompleteLine, cols);
        grid3[5] = Arrays.copyOf(completeLine, cols);
        grid3[6] = Arrays.copyOf(notCompleteLine, cols);
        grid3[7] = Arrays.copyOf(completeLine, cols);
        grid3[8] = Arrays.copyOf(notCompleteLine, cols);
        grid3[9] = Arrays.copyOf(completeLine, cols);
        grid.set(playfield3, grid3);

        byte[][] expectedGrid3 = new byte[rows][cols];
        expectedGrid3[6] = Arrays.copyOf(grid3[3], cols);
        expectedGrid3[7] = Arrays.copyOf(grid3[4], cols);
        expectedGrid3[8] = Arrays.copyOf(grid3[6], cols);
        expectedGrid3[9] = Arrays.copyOf(grid3[8], cols);

        Playfield playfield4 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
        byte[][] grid4 = new byte[rows][cols];
        grid4[9] = Arrays.copyOf(completeLine, cols);
        grid.set(playfield4, grid4);

        byte[][] expectedGrid4 = new byte[rows][cols];

        Playfield playfield5 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
        byte[][] grid5 = new byte[rows][cols];
        grid5[0] = Arrays.copyOf(completeLine, cols);
        grid.set(playfield5, grid5);

        byte[][] expectedGrid5 = new byte[rows][cols];

        Playfield playfield6 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
        byte[][] grid6 = new byte[rows][cols];
        grid6[6] = Arrays.copyOf(notCompleteLine, cols);
        grid6[7] = Arrays.copyOf(notCompleteLine, cols);
        grid6[8] = Arrays.copyOf(completeLine, cols);
        grid6[9] = Arrays.copyOf(completeLine, cols);
        grid.set(playfield6, grid6);

        byte[][] expectedGrid6 = new byte[rows][cols];
        expectedGrid6[8] = Arrays.copyOf(grid6[6], cols);
        expectedGrid6[9] = Arrays.copyOf(grid6[7], cols);

        return new Object[][]{
                {playfield1, expectedGrid1, "Expected one filled line (row number: 9) to be removed, lines from above moved down"},
                {playfield2, expectedGrid2, "Expected two filled lines (row number: 6, 9) to be removed, row 5 moved to 7, rows 7 -> 8, 8 -> 9"},
                {playfield3, expectedGrid3, "Expected three filled lines (row number: 5, 7, 9) to be removed and rows 3 moved to 6, 4 -> 7, row 6 -> 8, row 8 -> 9 "},
                {playfield4, expectedGrid4, "Expected one filled line (row number: 9) to be removed"},
                {playfield5, expectedGrid5, "Expected one filled line (row number: 0) to be removed"},
                {playfield6, expectedGrid6, "Expected two filled lines (row number: 8,9) to be removed, row 6 moved to 8, rows 7 -> 9"}
        };
    }
}
