package com.cguillaume.omxcontrol;

import java.io.File;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Util {

	private final File file;

	@Inject
	public Util(Config config) {
		file = new File(config.getLibraryLocation());
	}
	public String getFreeSpace() {
		return file.getUsableSpace() / 1024 / 1024 + " Mo";// us = Mb
	}

}
