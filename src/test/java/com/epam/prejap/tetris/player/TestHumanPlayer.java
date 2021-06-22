package com.epam.prejap.tetris.player;

import com.epam.prejap.tetris.game.Move;
import org.jnativehook.GlobalScreen;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.stream.IntStream;

import static org.testng.Assert.*;

@Test(groups = {"TestPlayfieldAndHumanPlayer"})
public class TestHumanPlayer {

    private static final Map<Character, Move> acceptedKeys = Map.of(
            ' ', Move.NONE, 'h', Move.LEFT, 'l', Move.RIGHT, 'j', Move.TO_BOTTOM_NOW);

    @DataProvider
    public Object[][] provideKeysForMovement() {
        return acceptedKeys.entrySet().stream()
                .map(e -> new Object[]{e.getKey(), e.getValue()})
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "provideKeysForMovement")
    public void testIfExpectedKeysResultsWithProperMovements(int key, Move movement) {
        assertEquals(new HumanPlayer().mapMoveToKey(key), movement);
    }

    @DataProvider
    public Object[][] provideKeysExpectedToNotCauseAnyMovement() {
        return IntStream.rangeClosed('a', 'z').boxed()
                .filter(acceptedKeys::containsKey).map(x -> new Object[]{x}).toArray(Object[][]::new);
    }

    @Test(dataProvider = "provideKeysExpectedToNotCauseAnyMovement")
    public void testIfKeysNotExpectedAreIgnored(int key) {
        assertEquals(new HumanPlayer().mapMoveToKey(key), Move.NONE);
    }

    public void testHumanPlayerRegisterHook() {
        var human = new HumanPlayer();
        assertTrue(GlobalScreen.isNativeHookRegistered());
    }

    public void testKillHumanPlayerUnregisterHook() {
        var human = new HumanPlayer();
        human.killHumanPlayer();
        assertFalse(GlobalScreen.isNativeHookRegistered());
    }
}
