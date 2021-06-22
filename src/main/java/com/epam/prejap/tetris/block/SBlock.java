package com.epam.prejap.tetris.block;

/**
 * @author Nikita Pochapynskyi
 */
final class SBlock extends Block {

    /**
     * Byte array represents "S" block.
     * In game will be displayed as (between lines):
     *  -------------------
     *       ##
     *      ##
     *  -------------------
     */
    private static final byte[][] S_IMAGE = {
            {0, 1, 1},
            {1, 1, 0}
    };

    SBlock() {
        super(S_IMAGE);
    }
}
