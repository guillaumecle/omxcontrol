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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class Omx {

	private static final Logger logger = LoggerFactory.getLogger(Omx.class);
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
		if (createProcess(command)) {
			playing = true;
			alive = true;
			webSocketManager.sendToAll(new WebSocketActionWrapper("updateCurrent", playlist.getCurrent()));
		}
	}

	private boolean createProcess(String[] command) {
		try {
			omxPlayer = Runtime.getRuntime().exec(command);
			StdReader std = new StdReader(omxPlayer);
			std.addListener(new StreamListener() {

				@Override
				public void onNewLine(String line) {
					logger.info("StdOut : " + line);
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
					logger.warn("ErrOut : " + line);
				}

				@Override
				public void onClose() {
				}
			});
			err.start();
			clavier = new PrintWriter(omxPlayer.getOutputStream(), true);
			return true;
		} catch (IOException e) {
			if (e.getMessage().startsWith("Cannot run program \"omxplayer\"")) {
				logger.error("Your system is not able to run omxplayer");
			} else {
				logger.error("Error creating omxplayer process", e);
			}
			return false;
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
