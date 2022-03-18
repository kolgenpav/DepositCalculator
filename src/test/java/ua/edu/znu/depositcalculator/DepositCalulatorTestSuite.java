package ua.edu.znu.depositcalculator;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({InitTest.class, DepositCalculatorTest.class, PrintSaveTest.class})
public class DepositCalulatorTestSuite {
}
