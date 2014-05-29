package com.cguillaume.omxcontrol.controller;

import javax.inject.Inject;

import org.eclipse.jetty.websocket.api.Session;

import com.cguillaume.omxcontrol.Omx;
import com.cguillaume.omxcontrol.Playlist;
import com.cguillaume.omxcontrol.websocket.WebSocketHandler;

public class MainWSHandler extends WebSocketHandler{

	@Inject
	private Omx omx;
	@Inject
	private Playlist playlist;

	public void pause(Session session, String message) {
		if (omx.isAlive())
			omx.pause();
		else if (playlist.hasNext())
			omx.startPlaying(playlist.next());
	}
	
	public void add(Session session, String message) {
		playlist.add(message);
	}
}
