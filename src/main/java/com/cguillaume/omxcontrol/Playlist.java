package com.cguillaume.omxcontrol;

import java.util.ArrayList;
import java.util.List;

import com.cguillaume.omxcontrol.websocket.WebSocketActionWrapper;
import com.cguillaume.omxcontrol.websocket.WebSocketManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Playlist {

	@Inject
	private transient Omx omx;
	@Inject
	private transient WebSocketManager webSocketManager;

	private boolean playOnAdd = true;
	private List<String> list = new ArrayList<>();
	private int current = -1;
	
	public void add(String track) {
		list.add(track);
		if (playOnAdd && !omx.isAlive())
			omx.startPlaying(next());
		webSocketManager.sendToAll(new WebSocketActionWrapper("playlistUpdated", this));
	}
	
	public boolean hasNext() {
		return list.size() > current + 1;
	}
	
	public String next() {
		return list.get(++current);
	}

	public List<String> getList() {
		return list;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
		omx.startPlaying(list.get(current));
	}
}
