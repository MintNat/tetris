package com.epam.prejap.tetris.block;

final class OBlock extends Block {

    /**
     * Byte array represents "O" block.
     * In game will be displayed as (between lines):
     *  -------------------
     *      ##
     *      ##
     *  -------------------
     */
    private static final byte[][] IMAGE = {
        {1, 1},
        {1, 1},
    };

    public OBlock() {
        super(IMAGE);
    }

}
