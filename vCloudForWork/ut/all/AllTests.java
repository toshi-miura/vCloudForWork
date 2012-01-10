package all;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import util.UtilAllTests;
import work.WorkAllTests;
import work.bat.BatAllTests;

@RunWith(Suite.class)
@SuiteClasses({ UtilAllTests.class, WorkAllTests.class, BatAllTests.class })
public class AllTests {

}
