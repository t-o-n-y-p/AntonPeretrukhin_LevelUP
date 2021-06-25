package ru.levelp.at.hw5;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CalculatorPowTest extends CalculatorBaseTest {

    @DataProvider
    private Object[][] getLongArgs() {
        return new Object[][]{
            {7, 5, 16807.0}, {2, -5, 0.03125}, {-5, 4, 625.0}, {-6, 0, 1.0}, {13, 1, 13.0}
        };
    }

    @Test(dataProvider = "getLongArgs")
    public void testPowPositiveCases(long a, long b, double expectedResult) {
        assertEquals(calculator.pow(a, b), expectedResult, doubleComparisonDelta);
    }

    @Test
    public void testPowZeroToThePowerZero() {
        assertThrows(Exception.class, () -> calculator.pow(0, 0));
    }

    @Test
    public void testPowZeroToTheNegativePower() {
        assertThrows(Exception.class, () -> calculator.pow(0, -5));
    }

    @Test
    public void testPowOverflow() {
        assertThrows(Exception.class, () -> calculator.pow(Long.MAX_VALUE, Long.MAX_VALUE));
    }


}
