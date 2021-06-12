package com.epam.prejap.tetris.game;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Marks skipped tests as failed
 */
public class FailListener extends TestListenerAdapter {
    @Override
    public void onTestSkipped(ITestResult tr) {
        tr.setStatus(ITestResult.FAILURE);
    }
}
