import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CalculatorMultiplyTest extends CalculatorBaseTest {

    @DataProvider
    private Object[][] getLongArgs() {
        return new Object[][]{
            {2, 5, 10}, {-3, -7, 21}, {-2, 9, -18}, {5, 0, 0}, {0, 0, 0}, {-8, 1, -8}, {1, 1, 1}
        };
    }

    @DataProvider
    private Object[][] getDoubleArgs() {
        return new Object[][]{
            {2.5, 5.6, 14.0}, {-3.88, -7.656, 29.70528}, {-2.0, 9.21, -18.42}, {5.54, 0.0, 0.0}, {0.0, 0.0, 0.0},
            {-8.87, 1.0, -8.87}, {1.0, 1.0, 1.0}
        };
    }

    @Test(dataProvider = "getLongArgs")
    public void testLongMultiplyPositive(long a, long b, long expectedResult) {
        Assert.assertEquals(calculator.mult(a, b), expectedResult);
    }

    @Test(dataProvider = "getDoubleArgs")
    public void testDoubleMultiplyPositive(double a, double b, double expectedResult) {
        Assert.assertEquals(calculator.mult(a, b), expectedResult, doubleComparisonDelta);
    }

    @Test
    public void testLongMultiplyOverflow() {
        Assert.assertThrows(Exception.class, () -> calculator.mult(9223372036854775807L, 9223372036854775807L));
    }

    @Test
    public void testDoubleMultiplyOverflow() {
        Assert.assertThrows(Exception.class, () -> calculator.mult(1.7e+308, 1.7e+308));
    }

}
