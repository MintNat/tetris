package com.epam.prejap.tetris.block;

/**
 * Generates a "T" block in the Tetris game - when "1" in a byte array is present then a "#" is printed,
 * otherwise a whitespace (" ") is printed.
 * @see com.epam.prejap.tetris.game.Printer#print(byte)
 * @author Grzegorz Konopka
 */
final class TBlock extends Block {

    private static final byte[][] IMAGE = {
            {1,1,1},
            {0,1,0}
    };

    TBlock() {
        super(IMAGE);
    }
}

