package work;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import work.VApp4Work.AUTH_STATUS;

public class VApp4WorkTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {

		String name = AUTH_STATUS.AUTH.name();

		System.out.println(name);

		System.out.println(AUTH_STATUS.valueOf(name));

	}

}
