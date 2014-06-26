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
				if (!current.get().equals(playlist.size())) {
					incrementCurrent();
				} else {
					current.set(0);
				}
				startCurrent();
			}
		}
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

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Synthesizer && arg != null && arg instanceof WebSocketActionWrapper) {
			WebSocketActionWrapper wsac = (WebSocketActionWrapper) arg;
			if (wsac.getAction().equals("aliveChanged") && wsac.getMessage().equals("false")) {
				if (playlist.size() > current.get() + 1) {
					incrementCurrent();
					startCurrent();
				}
			}
		}
	}

}
