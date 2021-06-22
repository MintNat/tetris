package com.epam.prejap.tetris.player;
/*
 JNativeHook: Global keyboard and mouse hooking for Java.
Copyright (C) 2006-2015 Alexander Barker.  All Rights Received.
https://github.com/kwhat/jnativehook/

JNativeHook is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

JNativeHook is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import com.epam.prejap.tetris.game.Move;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.Optional;
import java.util.logging.Level;

import static java.util.logging.Logger.getLogger;

/**
 * Capture keyboard input with JNativeHook and generate movement based on keystrokes.
 * @author Radosław Piwowarski
 * @see <a href="https://github.com/kwhat/jnativehook">JNativeHook</a>
 * @see Player
 */
public class HumanPlayer implements Player, NativeKeyListener {

    /**
     * @see HumanPlayer#nextMove()
     */
    private int currentMovement;

    /**
     * Instantiate a new <code>HumanPlayer</code> object with a native hook
     *
     * @see GlobalScreen
     * @see <a href="https://github.com/kwhat/jnativehook/blob/2.2/doc/ConsoleOutput.md">ConsoleOutput</a>
     * for how-to suspend verbose logger
     */
    public HumanPlayer() {
        var loggerNH = getLogger(GlobalScreen.class.getPackage().getName()); // just to suspend spammer
        try {
            loggerNH.setLevel(Level.OFF); // disable spam from JNativeHook
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
        } catch (NativeHookException e) {
            System.err.println("Count not register native hook!");
        }
    }

    /**
     * @return Optional of next Move
     * @see Player#nextMove()
     */
    @Override
    public Optional<Move> nextMove() {
        var result = mapMoveToKey(currentMovement);
        currentMovement = 0;
        return Optional.of(result);
    }

    Move mapMoveToKey(int key) {
        return Move.of(key);
    }

    /**
     * Release hook from register
     * HumanPlayer can not take any action without registered hook, so
     * instance is as good as dead
     */
    public void killHumanPlayer() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            System.err.println("Could not unregister hook!");
        }
    }

    /**
     * Capture keys when typed, do not check if key was released or not
     */
    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        if (Move.of(nativeKeyEvent.getKeyChar()) != Move.NONE) {
            currentMovement = nativeKeyEvent.getKeyChar();
        }
    }

    /**
     * Capture pressed keys
     */
    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        /*
         * No need to distinguish between pressed/released/typed
         */
    }

    /**
     * Capture keys when released
     */
    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        /*
         * No need to distinguish between pressed/released/typed
         */
    }
}
