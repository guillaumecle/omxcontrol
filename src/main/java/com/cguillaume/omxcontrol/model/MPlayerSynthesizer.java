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
		clavier.printf("q");
		alive.set(false);
	}

}
