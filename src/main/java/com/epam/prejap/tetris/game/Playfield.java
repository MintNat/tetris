package com.epam.prejap.tetris.game;

import com.epam.prejap.tetris.block.Block;
import com.epam.prejap.tetris.block.BlockFeed;


public class Playfield {

    private final Grid grid;
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
        grid = new Grid(this.rows, this.cols);
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
            case TO_BOTTOM_NOW -> moveToBottom();
        }
        moved = moveDown();
        show();
        return moved;
    }

    /**
     * Searches and removes the complete lines from a grid if such was found.
     * Lines that are above it, will be moved down on such number of rows however many filled lines were found.
     */
    public void findAndRemoveFilledLines() {
        if (grid.hasFilledLines()) grid.removeFilledLine();
    }

    /**
     * Move immediately to bottom
     * @see Playfield#isValidMove(Block, int, int)
     */
    private boolean moveToBottom() {
        int i = 1;
        while (isValidMove(block, i, 0)) {
            i++;
        }
        return move(i - 1, 0);
    }

    /**
     * Moves a current block right by 1 column.
     */
    private void moveRight() {
        move(0, 1);
    }

    /**
     * Moves a current block left by 1 column.
     */
    private void moveLeft() {
        move(0, -1);
    }

    /**
     * Moves a current block down by 1 line.
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
                    if (newRow >= rows || newCol >= cols || grid.checkDotPresenceAtPosition(newRow, newCol)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Hides a current block.
     */
    private void hide() {
        forEachBrick((i, j, dot) -> grid.replaceValue(row + i, col + j, 0));
    }

    /**
     * Shows block and draws grid.
     */
    private void show() {
        forEachBrick((i, j, dot) -> grid.replaceValue(row + i, col + j, dot));
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
        void act(int i, int j, int dot);
    }

}
