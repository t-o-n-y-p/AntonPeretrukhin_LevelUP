import org.testng.Assert;
import org.testng.Assert.ThrowingRunnable;
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
        Assert.assertEquals(calculator.sum(a, b), expectedResult);
    }

    @Test(dataProvider = "getDoubleArgs")
    public void testDoubleSumPositive(double a, double b, double expectedResult) {
        Assert.assertEquals(calculator.sum(a, b), expectedResult, doubleComparisonDelta);
    }

    @Test
    public void testLongSumOverflow() {
        Assert.assertThrows(Exception.class, () -> calculator.sum(9223372036854775807L, 9223372036854775807L));
    }

    @Test
    public void testDoubleSumOverflow() {
        Assert.assertThrows(Exception.class, () -> calculator.sum(1.7e+308, 1.7e+308));
    }


}
