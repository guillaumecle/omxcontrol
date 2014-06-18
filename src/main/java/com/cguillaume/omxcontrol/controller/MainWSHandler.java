package com.cguillaume.omxcontrol.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.cguillaume.omxcontrol.Config;
import com.cguillaume.omxcontrol.job.UploadJob;
import com.cguillaume.omxcontrol.controller.upload.UploadQueue;
import com.cguillaume.omxcontrol.job.Jobs;
import com.cguillaume.omxcontrol.model.Player;
import com.cguillaume.omxcontrol.model.Playlist;
import com.cguillaume.omxcontrol.model.Synthesizer;
import com.cguillaume.omxcontrol.websocket.Handler;
import com.cguillaume.omxcontrol.websocket.WebSocketHandler;
import com.cguillaume.omxcontrol.youtube.YoutubeDownloader;

@Singleton
public class MainWSHandler extends WebSocketHandler {

	@Inject
	private Player player;
	@Inject
	private Playlist playlist;
	@Inject
	private UploadQueue uploadQueue;
	@Inject
	private Config config;
	@Inject
	private YoutubeDownloader youtubeDownloader;
	@Inject
	private Synthesizer synthesizer;
	@Inject
	private Jobs jobs;

	@Handler
	public void pause() {
		player.play();
	}

	@Handler
	public void add(String path) {
		playlist.add(path);
	}

	@Handler
	public void uploadFile(UploadJob uploadJob) {
		jobs.add(uploadJob);
		uploadQueue.addJob(uploadJob);
	}

	@Handler
	public void dlFromYoutube(YoutubeJob youtubeJob) {
		jobs.add(youtubeJob);
		youtubeDownloader.init(youtubeJob);
		new Thread(youtubeDownloader).start();
//		youtubeDownloader.run();
	}

	@Handler
	public void changeVolume(Boolean up) {
		if (up) {
			synthesizer.increaseVolume();
		} else {
			synthesizer.decreaseVolume();
		}
	}

	@Override
	public void handleBinary(byte[] payload, int offset, int len) {
		File file = new File(config.getTempDirLocation() + File.separator + System.currentTimeMillis() + System.nanoTime());
		try {
			file.createNewFile();
			OutputStream outputStream = new FileOutputStream(file);
			outputStream.write(payload, offset, len);
			outputStream.close();
			uploadQueue.addFile(file);
		} catch (IOException e) {
			logger.error("Error writing temp file", e);
		}
	}

}
