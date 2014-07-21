package com.cguillaume.omxcontrol;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.inject.Singleton;

@Singleton
public class Config {

	private final List<String> keys = new LinkedList<String>() {
		{
			add("lib");
			add("tmp");
		}
	};

	private final Map<String, String> keysMap = new HashMap<String, String>() {
		{
			for (String key : keys) {
				put(key, key);
			}
		}
	};

	private Map<String, String> parsedMap = new HashMap<>();

	public Config() throws Exception {
		readConfig();
		List<String> missingValues = getMissingValues();
		if (missingValues.size() !=0) {
			throw new Exception("Some configurations values are missing:" + missingValues);
		}
	}

	private void readConfig() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader("conf"));
		String line;
		String[] tab;
		while ((line = bufferedReader.readLine()) != null) {
			tab = line.split("=");
			parsedMap.put(tab[0].trim(), tab.length > 1 ? tab[1].trim() : null);
		}
		bufferedReader.close();
	}

	private List<String> getMissingValues() {
		List<String> missing = new LinkedList<>();
		for (String value : keys) {
			if (!parsedMap.containsKey(value)) {
				missing.add(value);
			}
		}
		return missing;
	}

	public String getLibraryLocation() {
		return parsedMap.get(keysMap.get("lib"));
	}

	public String getTempDirLocation() {
		return parsedMap.get(keysMap.get("tmp"));
	}

}