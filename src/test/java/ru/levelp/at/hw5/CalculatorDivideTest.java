package ru.levelp.at.hw5;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CalculatorDivideTest extends CalculatorBaseTest {

    @DataProvider
    private Object[][] getLongArgs() {
        return new Object[][]{
            {5, 2, 2}, {2, 5, 0}, {-20, -7, 2}, {-9, 2, -4}, {-8, 1, -8}, {1, 1, 1}
        };
    }

    @DataProvider
    private Object[][] getDoubleArgs() {
        return new Object[][]{
            {5.6, 2.5, 2.24}, {2.5, 5.6, 0.44642857}, {-20.66, -7.656, 2.698537}, {-9.21, 2.0, -4.605},
            {-8.87, 1.0, -8.87}, {1.0, 1.0, 1.0}
        };
    }

    @Test(dataProvider = "getLongArgs")
    public void testLongDividePositive(long a, long b, long expectedResult) {
        assertEquals(calculator.div(a, b), expectedResult);
    }

    @Test(dataProvider = "getDoubleArgs")
    public void testDoubleDividePositive(double a, double b, double expectedResult) {
        assertEquals(calculator.div(a, b), expectedResult, doubleComparisonDelta);
    }

    @Test
    public void testLongDivideByZero() {
        assertThrows(NumberFormatException.class, () -> calculator.div(2, 0));
    }

    @Test
    public void testDoubleDivideByZero() {
        assertThrows(NumberFormatException.class, () -> calculator.div(2.5, 0.0));
    }

    @Test
    public void testDoubleDivideOverflow() {
        assertThrows(Exception.class, () -> calculator.div(Double.MAX_VALUE, Double.MIN_VALUE));
    }

}
