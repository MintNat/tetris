package com.epam.prejap.tetris.block;

/**
 * Implements H shaped block
 * @see com.epam.prejap.tetris.game.Printer#print(byte) 
 * @author Slawomir Kucab
 */
final class HBlock extends Block{

    /**
     * Byte array represents "S" block.
     * In game will be displayed as (between lines):
     *  -------------------
     *       # #
     *       ###
     *       # #
     *  -------------------
     */
    private static final byte[][] IMAGE = {
            {1, 0, 1},
            {1, 1, 1},
            {1, 0, 1}
    };
    public HBlock(){
        super(IMAGE);
    }
}

