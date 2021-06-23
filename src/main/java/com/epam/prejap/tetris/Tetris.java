package com.epam.prejap.tetris;

import com.epam.prejap.tetris.block.BlockFeed;
import com.epam.prejap.tetris.game.Move;
import com.epam.prejap.tetris.game.Playfield;
import com.epam.prejap.tetris.game.Printer;
import com.epam.prejap.tetris.game.Waiter;
import com.epam.prejap.tetris.player.HumanPlayer;
import com.epam.prejap.tetris.player.Player;
import com.epam.prejap.tetris.player.RandomPlayer;

class Tetris {
    private final Playfield playfield;
    private final Waiter waiter;
    private final Player player;

    public Tetris(Playfield playfield, Waiter waiter, Player player) {
        this.playfield = playfield;
        this.waiter = waiter;
        this.player = player;
    }

    public Score play() {
        boolean moved;
        int score = 0;
        do {
            moved = false;

            playfield.nextBlock();
            score++;

            boolean nextMove;
            do {
                waiter.waitForIt();
                Move move = player.nextMove().orElse(Move.NONE);
                moved |= (nextMove = playfield.move(move));
                if (move == Move.TO_BOTTOM_NOW) {
                    moved = true;
                }
            } while (nextMove);
            playfield.findAndRemoveFilledLines();
        } while (moved);

        if (player instanceof HumanPlayer)
            ((HumanPlayer) player).killHumanPlayer();

        return new Score(score);
    }

    public static void main(String[] args) {
        int rows = 10;
        int cols = 20;
        int delay = 500;

        Player player = new RandomPlayer();
        if (args.length > 0 && args[0].contains("human"))
            player = new HumanPlayer();

        var feed = new BlockFeed();
        var printer = new Printer(System.out);
        var playfield = new Playfield(rows, cols, feed, printer);
        var game = new Tetris(playfield, new Waiter(delay), player);

        var score = game.play();

        System.out.println("Score: " + score.points());
    }
}
