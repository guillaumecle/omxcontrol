package com.cguillaume.omxcontrol.youtube;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.Scanner;

public class YoutubeDownloader extends Thread {

	private URL youtubeUrl;
	private URL audioUrl;
	private DownloadStatus status;
	private int contentLength;
	private int size;

	public YoutubeDownloader(String url) {
		try {
			this.youtubeUrl = new URL(url);
		} catch (MalformedURLException e) {
			fail(e);
			return;
		}
		this.status = DownloadStatus.WAITING;
	}

	private void fail(Exception e) {
		this.status = DownloadStatus.FAILED;
		e.printStackTrace();
	}

	@Override
	public void run() {
		this.status = DownloadStatus.DOWNLOADING;
		try {
			this.audioUrl = getAudioUrl();
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
	}

	public void download() throws IOException {
		URLConnection connection = audioUrl.openConnection();
		contentLength = connection.getContentLength();
		connection.connect();
		Random random = new Random();
		OutputStream writer = new FileOutputStream("/music/" + random.nextInt(Integer.MAX_VALUE) + ".mp3");
		InputStream stream = connection.getInputStream();
		int read;
		byte[] bytes = new byte[1024];
		size = 0;
		while ((read = stream.read(bytes)) != -1) {
			writer.write(bytes, 0, read);
			size += read;
		}
		writer.close();
		stream.close();
	}

	private URL getAudioUrl() throws IOException {
		Process proc = Runtime.getRuntime().exec("youtube-dl -f 140 -g " + youtubeUrl);
		InputStream stream = proc.getInputStream();
		try (Scanner s = new Scanner(stream).useDelimiter("\\A")) {
			return new URL(s.hasNext() ? s.next() : "");
		}
	}

	public int progress() {
		return 100*size/contentLength;
	}

	public DownloadStatus status(){
		return status;
	}
}
