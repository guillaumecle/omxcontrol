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
import com.cguillaume.omxcontrol.job.YoutubeJob;
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
	private VeryPrivate<DownloadStatus> status = new VeryPrivate<DownloadStatus>(DownloadStatus.UNINITIALIZED) {
		@Override
		protected void onUpdate() {
			job.status = value;
			YoutubeDownloader.this.setChanged();
			YoutubeDownloader.this.notifyObservers(new WebSocketActionWrapper("downloadProgress", job));
		}
	};
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
	private VeryPrivate<String> name = new VeryPrivate<String>(null) {
		@Override
		protected void onUpdate() {
			job.name = value;
			YoutubeDownloader.this.setChanged();
			YoutubeDownloader.this.notifyObservers(new WebSocketActionWrapper("downloadProgress", job));
		}
	};

	@Inject
	public YoutubeDownloader(WebSocketManager webSocketManager) {
		this.webSocketManager = webSocketManager;
		webSocketManager.addObservable(this);
	}

	private void fail(Exception e) {
		status.set(DownloadStatus.FAILED);
		e.printStackTrace();
		forgotMe();
	}

	public void init(YoutubeJob youtubeJob) {
		bindJob(youtubeJob);
		try {
			this.youtubeUrl = new URL(youtubeJob.url);
		} catch (MalformedURLException e) {
			fail(e);
		}
		status.set(DownloadStatus.WAITING);
	}

	private void bindJob(YoutubeJob job) {
		this.job = job;
		this.name.set(job.name);
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
		status.set(DownloadStatus.SUCCESS);
		forgotMe();
	}

	public void download() throws IOException {
		status.set(DownloadStatus.DOWNLOADING);
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
		status.set(DownloadStatus.WAITING_FOR_METADATA);
		Process proc = Runtime.getRuntime().exec("youtube-dl --get-filename -o \"%(title)s.%(ext)s\" -f 140 -g " + youtubeUrl);
		try (Scanner s = new Scanner(proc.getInputStream()).useDelimiter("\\n")) {
			audioUrl = new URL(s.hasNext() ? s.next() : "");
			String videoName = s.hasNext() ? s.next() : "";
			name.set("Download of " + videoName);
			fileName = config.getLibraryLocation() + File.separator + videoName;
		}
	}

	private void forgotMe() {
		if (webSocketManager != null) {
			webSocketManager.deleteObservable(this);
		}
	}
}
