package ru.levelp.at.hw5;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import ru.levelup.qa.at.calculator.Calculator;

public abstract class CalculatorBaseTest {

    protected Calculator calculator;
    protected final double doubleComparisonDelta = 0.000001;

    @BeforeMethod
    public void setUp() {
        calculator = new Calculator();
    }

    @AfterMethod
    public void tearDown() {
        calculator = null;
    }
}
