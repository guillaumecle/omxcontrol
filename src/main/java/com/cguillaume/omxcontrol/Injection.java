package com.cguillaume.omxcontrol;

import com.google.inject.Injector;

public class Injection {

	private static Injector injector;

	public static void init(Injector injector) {
		Injection.injector = injector;
	}

	public static Injector getInjector() {
		return injector;
	}

}
