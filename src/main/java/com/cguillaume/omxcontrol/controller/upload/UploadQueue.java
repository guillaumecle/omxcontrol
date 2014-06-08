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
	private Map<Long, UploadJob> jobs = new TreeMap<>();

	public void addFile(Long size, File file) {
		UploadJob uploadJob = jobs.get(size);
		if (uploadJob != null) {
			renameFile(file, uploadJob);
		} else {
			files.put(size, file);
		}
	}

	public void addJob(UploadJob uploadJob) {
		File file = files.get(uploadJob.jsFile.size);
		if (file != null) {
			renameFile(file, uploadJob);
		} else {
			jobs.put(uploadJob.jsFile.size, uploadJob);
		}
	}

	private void renameFile(File file, UploadJob uploadJob) {
		String finalName = config.getLibraryLocation() + File.separator + uploadJob.jsFile.name;
		file.renameTo(new File(finalName));
		setChanged();
		uploadJob.jsFile.name = finalName;
		notifyObservers(new WebSocketActionWrapper("uploadCompleted", uploadJob));
	}

}
