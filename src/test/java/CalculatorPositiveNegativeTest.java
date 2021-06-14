import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class CalculatorPositiveNegativeTest extends CalculatorBaseTest {

    @Test
    public void testPositiveNumber() {
        assertTrue(calculator.isPositive(5));
        assertFalse(calculator.isNegative(5));
    }

    @Test
    public void testNegativeNumber() {
        assertTrue(calculator.isNegative(-20));
        assertFalse(calculator.isPositive(-20));
    }

    @Test
    public void testZero() {
        assertFalse(calculator.isPositive(0));
        assertFalse(calculator.isNegative(0));
    }

}
