package ru.levelp.at.hw5;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CalculatorSinTest extends CalculatorBaseTest {

    @DataProvider
    private Object[][] getDoubleArgs() {
        return new Object[][]{
            {0.0, 0.0}, {Math.PI / 2, 1.0}, {-Math.PI / 2, -1.0}, {Math.PI, 0.0},
            {4.2, -0.8715757724}, {-7.9, -0.998941341}
        };
    }

    @Test(dataProvider = "getDoubleArgs")
    public void testSinPositiveCases(double a, double expectedResult) {
        assertEquals(calculator.sin(a), expectedResult, doubleComparisonDelta);
    }

}
