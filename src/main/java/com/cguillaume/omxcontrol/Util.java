package com.cguillaume.omxcontrol;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.File;

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
