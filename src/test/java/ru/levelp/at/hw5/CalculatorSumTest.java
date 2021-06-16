package ru.levelp.at.hw5;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CalculatorSumTest extends CalculatorBaseTest {

    @DataProvider
    private Object[][] getLongArgs() {
        return new Object[][]{
            {2, 5, 7}, {-3, -7, -10}, {-2, 9, 7}, {5, 0, 5}, {0, 0, 0}
        };
    }

    @DataProvider
    private Object[][] getDoubleArgs() {
        return new Object[][]{
            {2.5, 5.6, 8.1}, {-3.88, -7.656, -11.536}, {-2.0, 9.21, 7.21}, {5.54, 0.0, 5.54}, {0.0, 0.0, 0.0}
        };
    }

    @Test(dataProvider = "getLongArgs")
    public void testLongSumPositive(long a, long b, long expectedResult) {
        assertEquals(calculator.sum(a, b), expectedResult);
    }

    @Test(dataProvider = "getDoubleArgs")
    public void testDoubleSumPositive(double a, double b, double expectedResult) {
        assertEquals(calculator.sum(a, b), expectedResult, doubleComparisonDelta);
    }

    @Test
    public void testLongSumOverflow() {
        assertThrows(Exception.class, () -> calculator.sum(Long.MAX_VALUE, Long.MAX_VALUE));
    }

    @Test
    public void testDoubleSumOverflow() {
        assertThrows(Exception.class, () -> calculator.sum(Double.MAX_VALUE, Double.MAX_VALUE));
    }


}
