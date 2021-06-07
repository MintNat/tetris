package com.epam.prejap.tetris.game;

import com.epam.prejap.tetris.block.Block;
import com.epam.prejap.tetris.block.BlockFeed;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;

public class Playfield {

    private final byte[][] grid;
    private final int rows;
    private final int cols;
    private final Printer printer;
    private final BlockFeed feed;

    private Block block;
    private int row;
    private int col;

    public Playfield(int rows, int cols, BlockFeed feed, Printer printer) {
        this.rows = rows;
        this.cols = cols;
        this.feed = feed;
        this.printer = printer;
        grid = new byte[this.rows][this.cols];
    }

    public void nextBlock() {
        block = feed.nextBlock();
        row = 0;
        col = (cols - block.cols()) / 2;
        show();
    }

    public boolean move(Move move) {
        hide();
        boolean moved;
            switch (move) {
                case LEFT -> moveLeft();
                case RIGHT -> moveRight();
            }
            moved = moveDown();
        show();
        return moved;
    }

    /**
     * Method is used to check for complete lines in grid and remove it.
     * Lines that are above it will be moved down one position.
     */
    public void checkCompleteLines() {
        int numberOfFilledLine = hasCompleteLine();
        while (numberOfFilledLine >= 0) {
            removeLine(numberOfFilledLine);
            numberOfFilledLine = hasCompleteLine();
        }
    }

    /**
     * Allocates filled line in a grid.
     * @return number of line that is filled or -1 if none was found.
     */
    private int hasCompleteLine() {
        int line = 0;
        for (byte[] bytes : grid) {
            boolean lineIsFilled = true;
            for (byte aByte : bytes) {
                if (aByte == 0) {
                    lineIsFilled = false;
                    break;
                }
            }
            if (lineIsFilled) return line;
            line++;
        }
        return -1;
    }

    /**
     * Removes line with given number.
     * Lines that are above it will be moved down one position.
     * @param numberOfLine the index of line, which should be removed.
     */
    private void removeLine(int numberOfLine) {
        for (int i = numberOfLine; i > 0; i--) {
            grid[i] = Arrays.copyOf(grid[i-1], cols);
        }
        Arrays.fill(grid[0], (byte)0);
    }

    /**
     * Moves current block right on 1 column.
     */
    private void moveRight() {
        move(0, 1);
    }

    /**
     * Moves current block left on 1 column.
     */
    private void moveLeft() {
        move(0, -1);
    }

    /**
     * Moves current block down on 1 line.
     * @return true if such move was maid.
     */
    private boolean moveDown() {
        return move(1, 0);
    }

    /**
     * Making block move with specified offset of rows and columns.
     * In case if such move not valid - leaves it without change and returns false.
     * @param rowOffset row offset.
     * @param colOffset column offset.
     * @return true if move was maid.
     */
    private boolean move(int rowOffset, int colOffset) {
        boolean moved = false;
        if (isValidMove(block, rowOffset, colOffset)) {
            doMove(rowOffset, colOffset);
            moved = true;
        }
        return moved;
    }

    /**
     * Checks if move is valid.
     * @param block block whose move on playfield is to be tested.
     * @param rowOffset raw offset.
     * @param colOffset column offset.
     * @return true if move is valid.
     */
    private boolean isValidMove(Block block, int rowOffset, int colOffset) {
        for (int i = 0; i < block.rows(); i++) {
            for (int j = 0; j < block.cols(); j++) {
                var dot = block.dotAt(i, j);
                if (dot > 0) {
                    int newRow = row + i + rowOffset;
                    int newCol = col + j + colOffset;
                    if (newRow >= rows || newCol >= cols || grid[newRow][newCol] > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Hides current block.
     */
    private void hide() {
        forEachBrick((i, j, dot) -> grid[row + i][col + j] = 0);
    }

    /**
     * Shows block and draws grid.
     */
    private void show() {
        forEachBrick((i, j, dot) -> grid[row + i][col + j] = dot);
        printer.draw(grid);
    }

    /**
     * Implements block's shift with specified offset of rows and columns.
     * @param rowOffset row offset.
     * @param colOffset column offset.
     */
    private void doMove(int rowOffset, int colOffset) {
        row += rowOffset;
        col += colOffset;
    }

    private void forEachBrick(BrickAction action) {
        for (int i = 0; i < block.rows(); i++) {
            for (int j = 0; j < block.cols(); j++) {
                var dot = block.dotAt(i, j);
                if (dot > 0) {
                    action.act(i, j, dot);
                }
            }
        }
    }

    private interface BrickAction {
        void act(int i, int j, byte dot);
    }

    @Test
    public static class PlayfieldRemovingCompleteLinesTest {

        @Test(dataProvider = "gridWithNoFilledLines")
        public void noCompleteLinesToRemoveFromPlayfield(Playfield playfield, byte[][] expectedGrid) {
            playfield.checkCompleteLines();
            Assert.assertEquals(playfield.grid, expectedGrid, "No lines should be removed");
        }

        @DataProvider
        public static Object[][] gridWithNoFilledLines() {
            int rows = 10;
            int cols = 20;
            byte[] notCompleteLine = new byte[] {0,0,1,1,0,0,0,0,0,0,0,1,1,0,0,0,1,1,1,1};

            Playfield playfield1 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
            byte[][] expectedGrid1 = new byte[rows][cols];

            Playfield playfield2 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
            playfield2.grid[8] = Arrays.copyOf(notCompleteLine, cols);
            playfield2.grid[9] = Arrays.copyOf(notCompleteLine, cols);
            byte[][] expectedGrid2 = new byte[rows][cols];
            expectedGrid2[8] = Arrays.copyOf(notCompleteLine, cols);
            expectedGrid2[9] = Arrays.copyOf(notCompleteLine, cols);

            Playfield playfield3 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
            playfield3.grid[0] = Arrays.copyOf(notCompleteLine, cols);
            playfield3.grid[1] = Arrays.copyOf(notCompleteLine, cols);
            playfield3.grid[2] = Arrays.copyOf(notCompleteLine, cols);
            playfield3.grid[3] = Arrays.copyOf(notCompleteLine, cols);
            playfield3.grid[4] = Arrays.copyOf(notCompleteLine, cols);
            playfield3.grid[5] = Arrays.copyOf(notCompleteLine, cols);
            playfield3.grid[6] = Arrays.copyOf(notCompleteLine, cols);
            playfield3.grid[7] = Arrays.copyOf(notCompleteLine, cols);
            playfield3.grid[8] = Arrays.copyOf(notCompleteLine, cols);
            playfield3.grid[9] = Arrays.copyOf(notCompleteLine, cols);
            byte[][] expectedGrid3 = new byte[rows][cols];
            expectedGrid3[0] = Arrays.copyOf(notCompleteLine, cols);
            expectedGrid3[1] = Arrays.copyOf(notCompleteLine, cols);
            expectedGrid3[2] = Arrays.copyOf(notCompleteLine, cols);
            expectedGrid3[3] = Arrays.copyOf(notCompleteLine, cols);
            expectedGrid3[4] = Arrays.copyOf(notCompleteLine, cols);
            expectedGrid3[5] = Arrays.copyOf(notCompleteLine, cols);
            expectedGrid3[6] = Arrays.copyOf(notCompleteLine, cols);
            expectedGrid3[7] = Arrays.copyOf(notCompleteLine, cols);
            expectedGrid3[8] = Arrays.copyOf(notCompleteLine, cols);
            expectedGrid3[9] = Arrays.copyOf(notCompleteLine, cols);

            return new Object[][] {
                    {playfield1, expectedGrid1},
                    {playfield2, expectedGrid2},
                    {playfield3, expectedGrid3}
            };
        }

        @Test(dataProvider = "gridWithFilledLines")
        public void removesCompleteLinesFromPlayfield(Playfield playfield, byte[][] expectedGrid, String msg) {
            playfield.checkCompleteLines();
            Assert.assertEquals(playfield.grid, expectedGrid, msg);
        }

        @DataProvider
        public static Object[][] gridWithFilledLines() {
            int rows = 10;
            int cols = 20;
            byte[] notCompleteLine = new byte[] {1,1,1,1,0,0,1,1,0,0,0,1,1,0,0,0,0,1,1,1};
            byte[] completeLine = new byte[cols];
            Arrays.fill(completeLine, (byte) 1);

            Playfield playfield1 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
            playfield1.grid[7] = Arrays.copyOf(notCompleteLine, cols);
            playfield1.grid[8] = Arrays.copyOf(notCompleteLine, cols);
            playfield1.grid[9] = Arrays.copyOf(completeLine, cols);

            byte[][] expectedGrid1 = new byte[rows][cols];
            expectedGrid1[8] = Arrays.copyOf(playfield1.grid[7], cols);
            expectedGrid1[9] = Arrays.copyOf(playfield1.grid[8], cols);

            Playfield playfield2 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
            playfield2.grid[5] = Arrays.copyOf(notCompleteLine, cols);
            playfield2.grid[6] = Arrays.copyOf(completeLine, cols);
            playfield2.grid[7] = Arrays.copyOf(notCompleteLine, cols);
            playfield2.grid[8] = Arrays.copyOf(notCompleteLine, cols);
            playfield2.grid[9] = Arrays.copyOf(completeLine, cols);

            byte[][] expectedGrid2 = new byte[rows][cols];
            expectedGrid2[7] = Arrays.copyOf(playfield2.grid[5], cols);
            expectedGrid2[8] = Arrays.copyOf(playfield2.grid[7], cols);
            expectedGrid2[9] = Arrays.copyOf(playfield2.grid[8], cols);

            Playfield playfield3 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
            playfield3.grid[3] = Arrays.copyOf(notCompleteLine, cols);
            playfield3.grid[4] = Arrays.copyOf(notCompleteLine, cols);
            playfield3.grid[5] = Arrays.copyOf(completeLine, cols);
            playfield3.grid[6] = Arrays.copyOf(notCompleteLine, cols);
            playfield3.grid[7] = Arrays.copyOf(completeLine, cols);
            playfield3.grid[8] = Arrays.copyOf(notCompleteLine, cols);
            playfield3.grid[9] = Arrays.copyOf(completeLine, cols);

            byte[][] expectedGrid3 = new byte[rows][cols];
            expectedGrid3[6] = Arrays.copyOf(playfield3.grid[3], cols);
            expectedGrid3[7] = Arrays.copyOf(playfield3.grid[4], cols);
            expectedGrid3[8] = Arrays.copyOf(playfield3.grid[6], cols);
            expectedGrid3[9] = Arrays.copyOf(playfield3.grid[8], cols);

            Playfield playfield4 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
            playfield4.grid[9] = Arrays.copyOf(completeLine, cols);

            byte[][] expectedGrid4 = new byte[rows][cols];

            Playfield playfield5 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
            playfield5.grid[0] = Arrays.copyOf(completeLine, cols);

            byte[][] expectedGrid5 = new byte[rows][cols];

            Playfield playfield6 = new Playfield(rows, cols, new BlockFeed(), new Printer(System.out));
            playfield6.grid[6] = Arrays.copyOf(notCompleteLine, cols);
            playfield6.grid[7] = Arrays.copyOf(notCompleteLine, cols);
            playfield6.grid[8] = Arrays.copyOf(completeLine, cols);
            playfield6.grid[9] = Arrays.copyOf(completeLine, cols);

            byte[][] expectedGrid6 = new byte[rows][cols];
            expectedGrid6[8] = Arrays.copyOf(playfield6.grid[6], cols);
            expectedGrid6[9] = Arrays.copyOf(playfield6.grid[7], cols);

            return new Object[][] {
                    {playfield1, expectedGrid1, "Expected one filled line (row number: 9) to be removed, lines from above moved down"},
                    {playfield2, expectedGrid2, "Expected two filled lines (row number: 6, 9) to be removed, row 5 moved to 7, rows 7 -> 8, 8 -> 9"},
                    {playfield3, expectedGrid3, "Expected three filled lines (row number: 5, 7, 9) to be removed and rows 3 moved to 6, 4 -> 7, row 6 -> 8, row 8 -> 9 "},
                    {playfield4, expectedGrid4, "Expected one filled line (row number: 9) to be removed"},
                    {playfield5, expectedGrid5, "Expected one filled line (row number: 0) to be removed"},
                    {playfield6, expectedGrid6, "Expected two filled lines (row number: 8,9) to be removed, row 6 moved to 8, rows 7 -> 9"}
            };
        }
    }
}
