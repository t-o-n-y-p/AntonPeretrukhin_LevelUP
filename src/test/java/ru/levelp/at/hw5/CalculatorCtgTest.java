package ru.levelp.at.hw5;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import java.util.stream.IntStream;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CalculatorCtgTest extends CalculatorBaseTest {

    @DataProvider
    private Object[][] getDoubleArgs() {
        return new Object[][]{
            {Math.PI / 2, 0.0}, {-Math.PI / 2, 0.0}, {Math.PI / 4, 1.0}, {-Math.PI / 4, -1.0},
            {0.3, 3.232728}, {-0.1, -9.9666444}
        };
    }

    @DataProvider
    private Object[] getInvalidAnglesCoefficient() {
        return IntStream.range(-5, 4)
                        .boxed()
                        .toArray();
    }

    @Test(dataProvider = "getDoubleArgs")
    public void testCtgPositiveCases(double a, double expectedResult) {
        assertEquals(calculator.ctg(a), expectedResult, doubleComparisonDelta);
    }

    @Test(dataProvider = "getInvalidAnglesCoefficient")
    public void testCtgNegativeCases(int coefficient) {
        assertThrows(Exception.class, () -> calculator.ctg(coefficient * Math.PI));
    }

}
