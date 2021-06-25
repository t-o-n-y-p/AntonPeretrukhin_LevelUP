package ru.levelp.at.hw5;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import java.util.stream.IntStream;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CalculatorTgTest extends CalculatorBaseTest {

    @DataProvider
    private Object[][] getDoubleArgs() {
        return new Object[][]{
            {0.0, 0.0}, {Math.PI, 0.0}, {Math.PI / 4, 1.0}, {-Math.PI / 4, -1.0}, {1.2, 2.572151622}, {-4.5, -4.637332}
        };
    }

    @DataProvider
    private Object[] getInvalidAnglesCoefficient() {
        return IntStream.range(-5, 4)
                        .mapToObj(i -> 2 * i + 1)
                        .toArray();
    }

    @Test(dataProvider = "getDoubleArgs")
    public void testTgPositiveCases(double a, double expectedResult) {
        assertEquals(calculator.tg(a), expectedResult, doubleComparisonDelta);
    }

    @Test(dataProvider = "getInvalidAnglesCoefficient")
    public void testTgNegativeCases(int coefficient) {
        assertThrows(Exception.class, () -> calculator.tg(coefficient * Math.PI / 2));
    }

}
