package ru.levelp.at.hw5;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CalculatorCosTest extends CalculatorBaseTest {

    @DataProvider
    private Object[][] getDoubleArgs() {
        return new Object[][]{
            {0.0, 1.0}, {Math.PI / 2, 0.0}, {-Math.PI / 2, 0.0}, {Math.PI, -1.0}, {9.7, -0.96236487}, {-8.0, -0.1455}
        };
    }

    @Test(dataProvider = "getDoubleArgs")
    public void testCosPositiveCases(double a, double expectedResult) {
        assertEquals(calculator.cos(a), expectedResult, doubleComparisonDelta);
    }

}
