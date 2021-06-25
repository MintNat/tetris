package com.epam.prejap.tetris.block;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.Iterator;

import static org.testng.Assert.assertEquals;

/**
 * "Generic" test class, to test shapes.
 * Define block class and its expected image in {@link BlocksShapesData} class
 * and it will be tested automatically
 *
 * @author Nikita Pochapynskyi
 */
@Test(groups = "blockShapes")
public class BlocksShapesTest {

    private final Block block;
    private final byte[][] image;

    @Factory(dataProviderClass = BlocksShapesData.class, dataProvider = "blocks")
    public BlocksShapesTest(Block block, byte[][] image) {
        this.block = block;
        this.image = image;
    }

    @DataProvider
    public Iterator<Object[]> dotAtData() {
        return BlocksShapesData.getDotAtDataFor(block.getClass());
    }

    public void shouldBeRightQtOfRows() {
        assertEquals(block.rows(), image.length,
                String.format("%s: Nr of rows for is incorrect.", block.getClass().getSimpleName()));
    }

    public void shouldBeRightQtOfColumns() {
        assertEquals(block.cols(), image[0].length,
                String.format("%s: Nr of columns for is incorrect.", block.getClass().getSimpleName()));
    }

    @Test(dataProvider = "dotAtData", dependsOnMethods = {"shouldBeRightQtOfRows", "shouldBeRightQtOfColumns"})
    public void shouldReturnRightBytesFromDotAtMethod(int i, int j, byte expectedDot) {
        assertEquals(block.dotAt(i, j), expectedDot,
                String.format("%s: incorrect dotAt(%s, %s)", block.getClass().getSimpleName(), i, j));
    }
}
