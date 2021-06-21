package com.epam.prejap.tetris.block;

/**
 * Represents Y block in BlockFeed.
 *
 * @author Anna Silenko
 * @see BlockFeed
 */
final class YBlock extends Block {

    private static final byte[][] IMAGE = {
            {1, 0, 1},
            {0, 1, 0},
            {0, 1, 0}
    };

    public YBlock() {
        super(IMAGE);
    }
}
