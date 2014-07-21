package com.cguillaume.omxcontrol.model;

import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.cguillaume.omxcontrol.websocket.WebSocketActionWrapper;

@Singleton
public class Player extends Observable implements Observer {

	@Inject
	private Synthesizer synthesizer;
	@Inject
	private Playlist playlist;

	private boolean playOnAdd = true;
	private boolean repatePlaylist = false;
	
	private VeryPrivate<Integer> current = new VeryPrivate<Integer>(-1) {
		@Override
		protected void onUpdate() {
			Player.this.setChanged();
			Player.this.notifyObservers(new WebSocketActionWrapper("updateCurrent", value));
		}
	};

	public void play() {
		if (synthesizer.isAlive()) {
			synthesizer.pause();
		} else {
			if (playlist.isNotEmpty()) {
				goToNext();
				startCurrent();
			}
		}
	}

	private boolean goToNext() {
		if (isAtTheEnd()) {
			if (!repatePlaylist) {
				return false;
			}
			current.set(0);
		} else {
			incrementCurrent();
		}
		return true;
	}

	private boolean isAtTheEnd() {
		return current.get() + 1 == playlist.size();
	}

	private void incrementCurrent() {
		current.set(current.get() + 1);
	}

	private void startCurrent() {
		synthesizer.startPlaying(playlist.get(current.get()).filePath);
	}

	public Integer getCurrent() {
		return current.get();
	}

	public void startAt(Integer i) {
		current.set(i);
		startCurrent();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Synthesizer && arg != null && arg instanceof String) {
			String string = (String) arg;
			if (string.equals("aliveEnded")) {
				handlePlayEnd();
			}
		}
	}

	private void handlePlayEnd() {
		if (playlist.isNotEmpty()) {
			if (goToNext()) {
				startCurrent();
			}
		}
	}

}
