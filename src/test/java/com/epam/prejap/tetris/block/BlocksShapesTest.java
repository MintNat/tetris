package com.epam.prejap.tetris.block;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test(groups = "blockShapes")
public class BlocksShapesTest {

    private final int rowsExpected;
    private final int rowsActual;
    private final int colsExpected;
    private final int colsActual;
    private final String testedBlock;
    private final Object[][] dotRepresentation;

    public void numberOfEveryBlockShapeRowsAsExpected() {
        assertEquals(rowsActual, rowsExpected, "Number of rows for " + testedBlock +
                "is incorrect. Expected: " + rowsExpected + ", actual: " + rowsActual);
    }

    public void numberOfEveryBlockShapeColsAsExpected() {
        assertEquals(colsActual, colsExpected, "Number of cols for " + testedBlock +
                "is incorrect. Expected: " + colsExpected + ", actual: " + colsActual);
    }

    @Test(dataProvider = "dotsRepresentation")
    public void dotAtReturnCorrectValue(int actual, int expected, String message) {
        String msg = testedBlock + message;
        assertEquals(actual, expected, msg);
    }

    @DataProvider
    public Object[][] dotsRepresentation() {
        return dotRepresentation;
    }

    @Factory(dataProvider = "blocks")
    public BlocksShapesTest(String testedBlock, int rowsActual, int rowsExpected, int colsActual, int colsExpected, Object[][] dotRepresentation) {
        this.rowsActual = rowsActual;
        this.rowsExpected = rowsExpected;
        this.colsActual = colsActual;
        this.colsExpected = colsExpected;
        this.testedBlock = testedBlock;
        this.dotRepresentation = dotRepresentation;
    }

    @DataProvider
    public static Object[][] blocks() {
        OBlock oBlock = new OBlock();
        int actualRowsForOBlock = oBlock.rows();
        int expectedRowsForOBlock = 2;
        int actualColsForOBlock = oBlock.cols();
        int expectedColsForOBlock = 2;
        var oBlockDotRepresentation = new Object[][]{
                {oBlock.dotAt(0, 0), 1, "incorrect dotAt(0, 0)"},
                {oBlock.dotAt(0, 1), 1, "incorrect dotAt(0, 1)"},
                {oBlock.dotAt(1, 0), 1, "incorrect dotAt(1, 0)"},
                {oBlock.dotAt(1, 1), 1, "incorrect dotAt(1, 1)"}
        };

        TBlock tBlock = new TBlock();
        int actualRowsForTBlock = tBlock.rows();
        int expectedRowsForTBlock = 2;
        int actualColsForTBlock = tBlock.cols();
        int expectedColsForTBlock = 3;
        var TBlockDotRepresentation = new Object[][]{
                {tBlock.dotAt(0, 0), 1, "incorrect dotAt(0, 0)"},
                {tBlock.dotAt(0, 1), 1, "incorrect dotAt(0, 1)"},
                {tBlock.dotAt(0, 2), 1, "incorrect dotAt(0, 2)"},
                {tBlock.dotAt(1, 0), 0, "incorrect dotAt(1, 0)"},
                {tBlock.dotAt(1, 1), 1, "incorrect dotAt(1, 1)"},
                {tBlock.dotAt(1, 2), 0, "incorrect dotAt(1, 2)"},
        };

        YBlock yBlock = new YBlock();
        int actualRowsForYBlock = yBlock.rows();
        int expectedRowsForYBlock = 3;
        int actualColsForYBlock = tBlock.cols();
        int expectedColsForYBlock = 3;
        var YBlockDotRepresentation = new Object[][]{
                {yBlock.dotAt(0, 0), 1, "incorrect dotAt(0, 0)"},
                {yBlock.dotAt(0, 1), 0, "incorrect dotAt(0, 1)"},
                {yBlock.dotAt(0, 2), 1, "incorrect dotAt(0, 2)"},
                {yBlock.dotAt(1, 0), 0, "incorrect dotAt(1, 0)"},
                {yBlock.dotAt(1, 1), 1, "incorrect dotAt(1, 1)"},
                {yBlock.dotAt(1, 2), 0, "incorrect dotAt(1, 2)"},
                {yBlock.dotAt(2, 0), 0, "incorrect dotAt(2, 0)"},
                {yBlock.dotAt(2, 1), 1, "incorrect dotAt(2, 1)"},
                {yBlock.dotAt(2, 2), 0, "incorrect dotAt(2, 2)"}
        };

        IBlock iBlock = new IBlock();
        int actualRowsForIBlock = iBlock.rows();
        int expectedRowsForIBlock = 4;
        int actualColsForIBlock = iBlock.cols();
        int expectedColsForIBlock = 1;
        var IBlockDotRepresentation = new Object[][]{
                {iBlock.dotAt(0, 0), 1, "incorrect dotAt(0, 0)"},
                {iBlock.dotAt(1, 0), 1, "incorrect dotAt(1, 0)"},
                {iBlock.dotAt(2, 0), 1, "incorrect dotAt(2, 0)"},
                {iBlock.dotAt(3, 0), 1, "incorrect dotAt(3, 0)"},
        };

        return new Object[][]{
                {"Tests for OBlock ", actualRowsForOBlock, expectedRowsForOBlock, actualColsForOBlock, expectedColsForOBlock, oBlockDotRepresentation},
                {"Tests for TBlock ", actualRowsForTBlock, expectedRowsForTBlock, actualColsForTBlock, expectedColsForTBlock, TBlockDotRepresentation},
                {"Tests for YBlock ", actualRowsForYBlock, expectedRowsForYBlock, actualColsForYBlock, expectedColsForYBlock, YBlockDotRepresentation},
                {"Tests for IBlock ", actualRowsForIBlock, expectedRowsForIBlock, actualColsForIBlock, expectedColsForIBlock, IBlockDotRepresentation}
        };
    }
}
