package com.cguillaume.omxcontrol.model;

import java.io.IOException;
import java.io.PrintWriter;

import com.cguillaume.omxcontrol.proc.CloseListener;
import com.cguillaume.omxcontrol.proc.ErrReader;
import com.cguillaume.omxcontrol.proc.StdReader;
import com.cguillaume.omxcontrol.websocket.WebSocketActionWrapper;
import com.google.inject.Inject;

public abstract class BaseSynthesizer extends Synthesizer {

	@Inject
	protected Volume volume;

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

	protected SynthesizerProcess process;
	protected PrintWriter clavier;

	protected void createProcess(String command, String trackFile) throws IOException {
		String volumeCommand = "-volume";
		ProcessBuilder processBuilder = new ProcessBuilder(command, volumeCommand, volume.getValue().toString(), trackFile);
		Process playerProcess = processBuilder.start();
		StdReader std = new StdReader(playerProcess);
//		std.addNewLineListener(logger::info);
		process = new SynthesizerProcess();
		std.addCloseListener(process);
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

	@Override
	public void setVolume(Integer i) {
		while (!volume.getValue().equals(i)) {
			if (volume.getValue() > i) {
				decreaseVolume();
			} else {
				increaseVolume();
			}
		}
	}

	public class SynthesizerProcess implements CloseListener{

		private boolean stopped;

		public void stop() {
			stopped = true;
		}

		@Override
		public void onClose(Exception e) {
			if(!stopped) {
				alive.set(false);
				BaseSynthesizer.this.setChanged();
				BaseSynthesizer.this.notifyObservers("aliveEnded");
			}
		}
	}
}
