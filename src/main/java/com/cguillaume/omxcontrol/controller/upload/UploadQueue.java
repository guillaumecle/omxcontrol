package com.cguillaume.omxcontrol.controller.upload;

import java.io.File;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.cguillaume.omxcontrol.Config;
import com.cguillaume.omxcontrol.job.UploadJob;
import com.cguillaume.omxcontrol.job.UploadStatus;
import com.cguillaume.omxcontrol.model.Library;
import com.cguillaume.omxcontrol.websocket.WebSocketActionWrapper;

@Singleton
public class UploadQueue extends Observable {

	@Inject
	private Config config;
	@Inject
	private Library library;

	private Map<Long, File> files = new TreeMap<>();
	private Map<Long, UploadJob> jobs = new TreeMap<>();

	public void addFile(File file) {
		UploadJob uploadJob = jobs.remove(file.length());
		if (uploadJob != null) {
			renameFile(file, uploadJob);
			files.remove(file.length());
		} else {
			files.put(file.length(), file);
		}
	}

	public void addJob(UploadJob uploadJob) {
		uploadJob.status = UploadStatus.WAITING;
		setChanged();
		notifyObservers(new WebSocketActionWrapper("uploadCompleted", uploadJob));
		File file = files.remove(uploadJob.jsFile.size);
		if (file != null) {
			renameFile(file, uploadJob);
			jobs.remove(uploadJob.jsFile.size);
		} else {
			jobs.put(uploadJob.jsFile.size, uploadJob);
		}
	}

	private void renameFile(File file, UploadJob uploadJob) {
		String finalName = config.getLibraryLocation() + File.separator + uploadJob.jsFile.name;
		File newFile = new File(finalName);
		file.renameTo(newFile);
		uploadJob.progress = 100;
		uploadJob.jsFile.name = finalName;
		uploadJob.status = UploadStatus.SUCCESS;
		setChanged();
		notifyObservers(new WebSocketActionWrapper("uploadCompleted", uploadJob));
		library.add(newFile);
	}

}
