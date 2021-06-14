package com.epam.prejap.tetris.block;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test(groups = "blockShapes")
public class TBlockTest {

    TBlock tBlock = new TBlock();

    public void rows_ShouldReturn2() {
        int expected = 2;
        int actual = tBlock.rows();
        assertEquals(actual, expected,
                String.format("Number of rows is incorrect.%nExpected: %d, actual: %d", expected, actual));
    }

    public void cols_shouldReturn3() {
        int expected = 3;
        int actual = tBlock.cols();
        assertEquals(actual, expected,
                String.format("Number of cols is incorrect.%nExpected: %d, actual: %d", expected, actual));
    }

    @Test(dataProvider = "dotsPositions")
    public void dotAtShouldReturnCorrectValue(int row, int col, int expected, String message) {
        int actual = tBlock.dotAt(row, col);
        assertEquals(actual, expected, message);
    }

    @DataProvider
    public static Object[][] dotsPositions() {
        return new Object[][]{
                {0, 0, 1, "Incorrect dotAt(0, 0)"},
                {0, 1, 1, "Incorrect dotAt(0, 1)"},
                {0, 2, 1, "Incorrect dotAt(0, 2)"},
                {1, 0, 0, "Incorrect dotAt(1, 0)"},
                {1, 1, 1, "Incorrect dotAt(1, 1)"},
                {1, 2, 0, "Incorrect dotAt(1, 2)"},
        };
    }
}
