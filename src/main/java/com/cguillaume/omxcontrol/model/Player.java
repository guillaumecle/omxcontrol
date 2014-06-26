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

	private void goToNext() {
		if (isAtTheEnd()) {
			current.set(0);
		} else {
			incrementCurrent();
		}
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
		if (o instanceof Synthesizer && arg != null && arg instanceof WebSocketActionWrapper) {
			WebSocketActionWrapper wsac = (WebSocketActionWrapper) arg;
			if (wsac.getAction().equals("aliveEnded")) {
				if (playlist.isNotEmpty()) {
					goToNext();
					startCurrent();
				}
			}
		}
	}

}
