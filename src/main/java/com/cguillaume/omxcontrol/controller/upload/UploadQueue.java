package com.cguillaume.omxcontrol.controller.upload;

import java.io.File;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.cguillaume.omxcontrol.Config;
import com.cguillaume.omxcontrol.websocket.WebSocketActionWrapper;

@Singleton
public class UploadQueue extends Observable {

	@Inject
	private Config config;

	private Map<Long, File> files = new TreeMap<>();
	private Map<Long, String> names = new TreeMap<>();

	public void addFile(Long size, File file) {
		String name = names.get(size);
		if (name != null) {
			renameFile(file, name);
		} else {
			files.put(size, file);
		}
	}

	public void addName(Long size, String name) {
		File file = files.get(size);
		if (file != null) {
			renameFile(file, name);
		} else {
			names.put(size, name);
		}
	}

	private void renameFile(File file, String name) {
		String finalName = config.getLibraryLocation() + File.separator + name;
		file.renameTo(new File(finalName));
		setChanged();
		notifyObservers(new WebSocketActionWrapper("upload", new JsFile(finalName, file.length())));
	}

}
