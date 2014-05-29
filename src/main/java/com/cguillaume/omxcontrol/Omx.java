package com.cguillaume.omxcontrol;

import java.io.IOException;
import java.io.PrintWriter;

import com.cguillaume.omxcontrol.proc.ErrReader;
import com.cguillaume.omxcontrol.proc.StdReader;
import com.cguillaume.omxcontrol.proc.StreamListener;
import com.cguillaume.omxcontrol.websocket.WebSocketActionWrapper;
import com.cguillaume.omxcontrol.websocket.WebSocketManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Omx {

	public static final String commandName = "omxplayer";

	@Inject
	private transient Playlist playlist;
	@Inject
	private transient WebSocketManager webSocketManager;

	private transient Process omxPlayer;
	private transient PrintWriter clavier;

	private boolean playing;
	private boolean alive;

	public void startPlaying(String trackFilePath) {
		if (alive)
			stop();
		String[] command = new String[] {
				commandName,
				trackFilePath
		};
		createProcess(command);
		playing = true;
		alive = true;
		webSocketManager.sendToAll(new WebSocketActionWrapper("playlistUpdated", playlist));
	}

	private void createProcess(String[] command) {
		try {
			omxPlayer = Runtime.getRuntime().exec(command);
			StdReader std = new StdReader(omxPlayer);
			std.addListener(new StreamListener() {

				@Override
				public void onNewLine(String line) {
					System.out.println("LOG : " + line);
				}

				@Override
				public void onClose() {
					alive = false;
					if (playlist.hasNext())
						startPlaying(playlist.next());
				}
			});
			std.start();
			ErrReader err = new ErrReader(omxPlayer);
			err.addListener(new StreamListener() {

				@Override
				public void onNewLine(String line) {
					System.err.println("ERROR : " + line);
				}

				@Override
				public void onClose() {
				}
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
