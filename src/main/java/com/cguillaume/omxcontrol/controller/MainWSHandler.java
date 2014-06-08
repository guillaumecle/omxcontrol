package com.cguillaume.omxcontrol.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.cguillaume.omxcontrol.Config;
import com.cguillaume.omxcontrol.controller.upload.UploadJob;
import com.cguillaume.omxcontrol.controller.upload.UploadQueue;
import com.cguillaume.omxcontrol.model.Player;
import com.cguillaume.omxcontrol.model.Playlist;
import com.cguillaume.omxcontrol.websocket.Handler;
import com.cguillaume.omxcontrol.websocket.WebSocketHandler;

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
		uploadQueue.addJob(uploadJob);
	}

	@Override
	public void handleBinary(byte[] payload, int offset, int len) {
		File file = new File(config.getTempDirLocation() + File.separator + System.currentTimeMillis() + System.nanoTime());
		try {
			file.createNewFile();
			OutputStream outputStream = new FileOutputStream(file);
			outputStream.write(payload, offset, len);
			outputStream.close();
			uploadQueue.addFile((long) len, file);
		} catch (IOException e) {
			logger.error("Error writing temp file", e);
		}
	}

}
