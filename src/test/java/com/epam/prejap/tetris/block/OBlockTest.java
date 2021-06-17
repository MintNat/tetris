package com.epam.prejap.tetris.block;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author Anna Silenko
 */

@Test(groups = {"blockShapes", "blocks"})
public class OBlockTest {
    OBlock oBlock = new OBlock();

    public void numberOfOBlockRowsEqualsTwo(){
        int actual = oBlock.rows();
        int expected = 2;
        assertEquals(actual, expected, "Number of rows is incorrect. Expected: " + expected + ", actual: " + actual);
    }

    public void numberOfOBlockColsEqualsTwo(){
        int actual = oBlock.cols();
        int expected = 2;
        assertEquals(actual, expected, "Number of cols is incorrect. Expected: " + expected + ", actual: " + actual);
    }

    @Test(dataProvider = "dotsRepresentation")
    public void dotAtReturnCorrectValue(int i, int j, int expected, String message) {
        int actual = oBlock.dotAt(i, j);
        assertEquals(actual, expected, message);
    }

    @DataProvider
    public static Object[][] dotsRepresentation() {
        return new Object[][]{
                {0, 0, 1, "Incorrect dotAt(0, 0)"},
                {0, 1, 1, "Incorrect dotAt(0, 1)"},
                {1, 0, 1, "Incorrect dotAt(1, 0)"},
                {1, 1, 1, "Incorrect dotAt(1, 1)"}
        };
    }
}
