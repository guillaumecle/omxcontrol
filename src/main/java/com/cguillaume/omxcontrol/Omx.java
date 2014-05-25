package com.cguillaume.omxcontrol;

import java.io.IOException;
import java.io.PrintWriter;

public class Omx {

	public static final String commandName = "omxplayer";

	private Process omxPlayer;
	private StdReader std;
	private ErrReader err;
	private PrintWriter clavier;
	private boolean playing;
	private boolean alive;

	public void startPlaying(String trackFilePath) {
		String[] command = new String[] {
				commandName,
				trackFilePath
		};
		createProces(command);
		playing = true;
		alive = true;
	}

	private void createProces(String[] command) {
		if (alive)
			stop();
		try {
			omxPlayer = Runtime.getRuntime().exec(command);
			std = new StdReader(omxPlayer);
			std.addListener(new StreamListener() {

				@Override
				public void onNewLine(String line) {
					System.out.println("LOG : " + line);
				}

				@Override
				public void onClose() {
					alive = false;
				}
			});
			std.start();
			err = new ErrReader(omxPlayer);
			err.addListener(new StreamListener() {

				@Override
				public void onNewLine(String line) {
					System.err.println("ERROR : " + line);
				}

				@Override
				public void onClose() {}
			});
			err.start();
			clavier = new PrintWriter(omxPlayer.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean pause() {
		if (!alive)
			throw new IllegalStateException();
		clavier.printf(" ");
		playing = !playing;
		return playing;
	}

	public boolean stop() {
		omxPlayer.destroy();
		return alive = false;
	}

	public boolean isPlaying() {
		return playing;
	}

	public boolean isAlive() {
		return alive;
	}
}
