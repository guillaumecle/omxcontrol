package com.cguillaume.omxcontrol.controller;

import javax.inject.Inject;

import com.cguillaume.omxcontrol.websocket.Handler;
import org.eclipse.jetty.websocket.api.Session;

import com.cguillaume.omxcontrol.Omx;
import com.cguillaume.omxcontrol.Playlist;
import com.cguillaume.omxcontrol.websocket.WebSocketHandler;

public class MainWSHandler extends WebSocketHandler{

	@Inject
	private Omx omx;
	@Inject
	private Playlist playlist;

	@Handler
	public void pause() {
		if (omx.isAlive())
			omx.pause();
		else if (playlist.hasNext())
			omx.startPlaying(playlist.next());
	}

	@Handler
	public void add(String path) {
		playlist.add(path);
	}
}
