package work.util;

import all.TestModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class InjMgr {

	public static Injector injector;
	static {
		TestModule module = new TestModule();
		injector = Guice.createInjector(module);
	}

	/**
	 * モジュールの定義する。
	 * @param m
	 */
	public static void setModule(Module m) {
		injector = Guice.createInjector(m);
	}

	public static <T> T create(Class<T> cls) {
		return injector.getInstance(cls);
	}

}