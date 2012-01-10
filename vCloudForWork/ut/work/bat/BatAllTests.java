package work.bat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AuthInvalidTaskTest.class, PnoInvalidTaskTest.class,
		SendCostMailTaskTest.class })
public class BatAllTests {

}
