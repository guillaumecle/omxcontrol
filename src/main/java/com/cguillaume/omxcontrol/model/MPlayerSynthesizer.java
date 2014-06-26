package com.cguillaume.omxcontrol.model;

import java.io.IOException;

import javax.inject.Singleton;

@Singleton
public class MPlayerSynthesizer extends BaseSynthesizer {

	private static final String commandName = "mplayer";

	@Override
	public void startPlaying(String trackFilePath) {
		if (alive.get()) {
			stop();
		}
		try {
			createProcess(commandName, trackFilePath);
			alive.set(true);
			playing.set(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		if (!alive.get())
			throw new IllegalStateException();
		clavier.printf(" ");
		playing.set(!playing.get());
	}

	public void stop() {
		process.stop();
		clavier.printf("q");
		playing.set(false);
		alive.set(false);
	}

	@Override
	public void increaseVolume() {
		if (volume.getValue() < 100) {
			if (alive.get()) {
				clavier.printf("0");
			}
			volume.increased();
		}
	}

	@Override
	public void decreaseVolume() {
		if (volume.getValue() > 1) {
			if (alive.get()) {
				clavier.printf("9");
			}
			volume.decreased();
		}
	}

}
