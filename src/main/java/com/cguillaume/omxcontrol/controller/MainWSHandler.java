package com.cguillaume.omxcontrol.controller;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.cguillaume.omxcontrol.model.Player;
import com.cguillaume.omxcontrol.model.Playlist;
import com.cguillaume.omxcontrol.websocket.Handler;
import com.cguillaume.omxcontrol.websocket.WebSocketHandler;

@Singleton
public class MainWSHandler extends WebSocketHandler{

	@Inject
	private Player player;
	@Inject
	private Playlist playlist;

	@Handler
	public void pause() {
		player.play();
	}

	@Handler
	public void add(String path) {
		playlist.add(path);
	}

}
