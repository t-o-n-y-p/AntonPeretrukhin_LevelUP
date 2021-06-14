import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CalculatorSubtractTest extends CalculatorBaseTest {

    @DataProvider
    private Object[][] getLongArgs() {
        return new Object[][]{
            {2, 5, -3}, {-3, -7, 4}, {-2, 9, -11}, {5, 0, 5}, {0, 0, 0}
        };
    }

    @DataProvider
    private Object[][] getDoubleArgs() {
        return new Object[][]{
            {2.5, 5.6, -3.1}, {-3.88, -7.656, 3.776}, {-2.0, 9.21, -11.21}, {5.54, 0.0, 5.54}, {0.0, 0.0, 0.0}
        };
    }

    @Test(dataProvider = "getLongArgs")
    public void testLongSubtractPositive(long a, long b, long expectedResult) {
        assertEquals(calculator.sub(a, b), expectedResult);
    }

    @Test(dataProvider = "getDoubleArgs")
    public void testDoubleSubtractPositive(double a, double b, double expectedResult) {
        assertEquals(calculator.sub(a, b), expectedResult, doubleComparisonDelta);
    }

    @Test
    public void testLongSubtractOverflow() {
        assertThrows(Exception.class, () -> calculator.sub(Long.MAX_VALUE, Long.MIN_VALUE));
    }

    @Test
    public void testDoubleSubtractOverflow() {
        assertThrows(Exception.class, () -> calculator.sub(Double.MAX_VALUE, -Double.MAX_VALUE));
    }

}
