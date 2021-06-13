package com.epam.prejap.tetris.game;

import com.epam.prejap.tetris.block.Block;
import com.epam.prejap.tetris.block.BlockFeed;

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
     *
     * @return number of line that is filled or -1 if none was found
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
     *
     * @param numberOfLine the index of line, which should be removed
     */
    private void removeLine(int numberOfLine) {
        for (int i = numberOfLine; i > 0; i--) {
            grid[i] = Arrays.copyOf(grid[i-1], cols);
        }
        Arrays.fill(grid[0], (byte)0);
    }

    /**
     * Moves current block right by 1 column.
     */
    private void moveRight() {
        move(0, 1);
    }

    /**
     * Moves current block left by 1 column.
     */
    private void moveLeft() {
        move(0, -1);
    }

    /**
     * Moves current block down by 1 line.
     *
     * @return true if such move was made
     */
    private boolean moveDown() {
        return move(1, 0);
    }

    /**
     * Moves block with specified offset of rows and columns.
     * In case such move is not valid - leaves it without change and returns false.
     *
     * @param rowOffset row offset
     * @param colOffset column offset
     * @return true if move was made
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
     *
     * @param block the block whose move on playfield is to be tested
     * @param rowOffset row offset
     * @param colOffset column offset
     * @return true if move is valid
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
     *
     * @param rowOffset row offset
     * @param colOffset column offset
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

}
