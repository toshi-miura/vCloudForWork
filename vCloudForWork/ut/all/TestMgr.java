package all;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestMgr {

	public static Injector injector;
	static {
		TestModule module = new TestModule();
		injector = Guice.createInjector(module);

	}

	public static <T> T create(Class<T> cls) {
		return injector.getInstance(cls);
	}

}
