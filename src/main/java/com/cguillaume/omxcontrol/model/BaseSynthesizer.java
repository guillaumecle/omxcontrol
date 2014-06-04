package com.cguillaume.omxcontrol.model;

import java.io.IOException;
import java.io.PrintWriter;

import com.cguillaume.omxcontrol.proc.ErrReader;
import com.cguillaume.omxcontrol.proc.StdReader;
import com.cguillaume.omxcontrol.websocket.WebSocketActionWrapper;

public abstract class BaseSynthesizer extends Synthesizer {

	protected final VeryPrivate<Boolean> alive = new VeryPrivate<Boolean>(false) {
		@Override
		protected void onUpdate() {
			BaseSynthesizer.this.setChanged();
			BaseSynthesizer.this.notifyObservers(new WebSocketActionWrapper("aliveChanged", value));
		}
	};
	protected final VeryPrivate<Boolean> playing = new VeryPrivate<Boolean>(false) {
		@Override
		protected void onUpdate() {
			BaseSynthesizer.this.setChanged();
			BaseSynthesizer.this.notifyObservers(new WebSocketActionWrapper("playingChanged", value));
		}
	};

	protected Process playerProcess;
	protected PrintWriter clavier;

	protected void createProcess(String command, String trackFile) throws IOException {
		ProcessBuilder processBuilder = new ProcessBuilder(command, trackFile);
		playerProcess = processBuilder.start();
		StdReader std = new StdReader(playerProcess);
//		std.addNewLineListener(logger::info);
		std.addCloseListenerListener(e -> alive.set(false));
		std.start();
		ErrReader err = new ErrReader(playerProcess);
		err.addNewLineListener(logger::warn);
		err.start();
		clavier = new PrintWriter(playerProcess.getOutputStream(), true);
	}

	@Override
	public boolean isAlive() {
		return alive.get();
	}

	@Override
	public boolean isPlaying() {
		return playing.get();
	}

}
