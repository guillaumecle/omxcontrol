package com.cguillaume.omxcontrol.youtube;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Observable;
import java.util.Scanner;

import com.cguillaume.omxcontrol.Config;
import com.cguillaume.omxcontrol.controller.YoutubeJob;
import com.cguillaume.omxcontrol.model.VeryPrivate;
import com.cguillaume.omxcontrol.websocket.WebSocketActionWrapper;
import com.cguillaume.omxcontrol.websocket.WebSocketManager;
import com.google.inject.Inject;

public class YoutubeDownloader extends Observable implements Runnable {

	@Inject
	private Config config;
	private WebSocketManager webSocketManager;

	private YoutubeJob job;
	private URL youtubeUrl;
	private URL audioUrl;
	private String fileName;
	private DownloadStatus status;
	private int contentLength;
	private VeryPrivate<Integer> size = new VeryPrivate<Integer>(0) {

		private int lastValue = 0;

		@Override
		protected void onUpdate() {
			job.progress = 100 * value / contentLength;
			if(job.progress != lastValue) {
				lastValue = job.progress;
				YoutubeDownloader.this.setChanged();
				YoutubeDownloader.this.notifyObservers(new WebSocketActionWrapper("downloadProgress", job));
			}
		}
	};

	@Inject
	public YoutubeDownloader(WebSocketManager webSocketManager) {
		this.webSocketManager = webSocketManager;
		if (webSocketManager != null) {
			webSocketManager.addObservable(this);
		}
	}

	private void fail(Exception e) {
		this.status = DownloadStatus.FAILED;
		e.printStackTrace();
		forgotMe();
	}

	public void init(YoutubeJob youtubeJob) {
		init(youtubeJob.url);
		bindJob(youtubeJob);
	}

	public YoutubeDownloader init(String url) {
		try {
			this.youtubeUrl = new URL(url);
		} catch (MalformedURLException e) {
			fail(e);
			return this;
		}
		this.status = DownloadStatus.WAITING;
		return this;
	}

	private void bindJob(YoutubeJob job) {
		this.job = job;
	}

	@Override
	public void run() {
		try {
			retrieveAudioUrlAndTitle();
		} catch (IOException e) {
			fail(e);
			return;
		}
		try {
			download();
		} catch (IOException e) {
			fail(e);
			return;
		}
		status = DownloadStatus.SUCCESS;
		forgotMe();
	}

	public void download() throws IOException {
		status = DownloadStatus.DOWNLOADING;
		URLConnection connection = audioUrl.openConnection();
		contentLength = connection.getContentLength();
		connection.connect();
		OutputStream writer = new FileOutputStream(fileName);
		InputStream stream = connection.getInputStream();
		int read;
		byte[] bytes = new byte[1024];
		while ((read = stream.read(bytes)) != -1) {
			writer.write(bytes, 0, read);
			size.set(size.get() + read);
		}
		writer.close();
		stream.close();
	}

	private void retrieveAudioUrlAndTitle() throws IOException {
		status = DownloadStatus.WAITING_FOR_METADATA;
		Process proc = Runtime.getRuntime().exec("youtube-dl --get-filename -o \"%(title)s.%(ext)s\" -f 140 -g " + youtubeUrl);
		try (Scanner s = new Scanner(proc.getInputStream()).useDelimiter("\\n")) {
			audioUrl = new URL(s.hasNext() ? s.next() : "");
			fileName = config.getLibraryLocation() + File.separator + (s.hasNext() ? s.next() : "");
		}
	}

	public DownloadStatus status(){
		return status;
	}

	private void forgotMe() {
		if (webSocketManager != null) {
			webSocketManager.deleteObservable(this);
		}
	}
}
