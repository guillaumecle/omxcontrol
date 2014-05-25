package com.cguillaume.omxcontrol;

import com.google.inject.Singleton;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class Config {

	private Map<String, String> map = new HashMap<>();
	public Config() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader("conf"));
		String line;
		String[] tab;
		while ((line = bufferedReader.readLine()) != null) {
			tab = line.split("=");
			map.put(tab[0].trim(), tab.length > 1 ? tab[1].trim() : null);
		}
	}

	public String getLibraryLocation() {
		return map.get("lib");
	}

	public String getStaticFilesLocation() {
		return map.get("static");
	}
}