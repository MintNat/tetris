package com.epam.prejap.tetris.block;

/**
 *  It creates I block of tetris
 *
 * @author Kanybek Mukalaev
 * @see BlockFeed
 * @see Block
 */
class IBlock extends Block {
    private static final byte [][] IMAGE = {
            {1},
            {1},
            {1},
            {1},
    };
    public IBlock(){
        super(IMAGE);
    }
}
