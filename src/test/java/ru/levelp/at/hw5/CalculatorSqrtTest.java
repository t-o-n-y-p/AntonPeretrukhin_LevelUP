package ru.levelp.at.hw5;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CalculatorSqrtTest extends CalculatorBaseTest {

    @DataProvider
    private Object[][] getDoubleArgs() {
        return new Object[][]{
            {0.0, 0.0}, {9.0, 3.0}, {13.5, 3.6742346}
        };
    }

    @Test(dataProvider = "getDoubleArgs")
    public void testSqrtPositiveCases(double a, double expectedResult) {
        assertEquals(calculator.sqrt(a), expectedResult, doubleComparisonDelta);
    }

    @Test
    public void testSqrtFromNegative() {
        assertThrows(Exception.class, () -> calculator.sqrt(-1));
    }

}
