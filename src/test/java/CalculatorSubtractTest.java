import org.testng.Assert;
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
        Assert.assertEquals(calculator.sub(a, b), expectedResult);
    }

    @Test(dataProvider = "getDoubleArgs")
    public void testDoubleSubtractPositive(double a, double b, double expectedResult) {
        Assert.assertEquals(calculator.sub(a, b), expectedResult, doubleComparisonDelta);
    }

    @Test
    public void testLongSubtractOverflow() {
        Assert.assertThrows(Exception.class, () -> calculator.sub(9223372036854775807L, -9223372036854775807L));
    }

    @Test
    public void testDoubleSubtractOverflow() {
        Assert.assertThrows(Exception.class, () -> calculator.sub(1.7e+308, -1.7e+308));
    }

}
