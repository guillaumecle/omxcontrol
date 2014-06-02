package com.cguillaume.omxcontrol.model;

import com.cguillaume.omxcontrol.websocket.WebSocketActionWrapper;

import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;

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
			Player.this.notifyObservers(new WebSocketActionWrapper("updateCurrent", current.get()));
		}
	};

	public void play() {
		if (synthesizer.isAlive()) {
			synthesizer.pause();
		} else {
			if (playlist.isNotEmpty()) {
				if (!current.get().equals(playlist.size())) {
					current.set(current.get() + 1);
				} else {
					current.set(0);
				}
				startCurrent();
			}
		}
	}

	public void addTrack(String track) {
		playlist.add(track);
	}

	private void startCurrent() {
		synthesizer.startPlaying(playlist.get(current.get()));
	}

	public Integer getCurrent() {
		return current.get();
	}

	@Override
	public void update(Observable o, Object arg) {

	}

}
