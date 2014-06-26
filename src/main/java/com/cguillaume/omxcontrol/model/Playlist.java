package com.cguillaume.omxcontrol.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.cguillaume.omxcontrol.websocket.WebSocketActionWrapper;
import com.google.inject.Singleton;

@Singleton
public class Playlist extends Observable {

	private final List<Mp3Metadata> list = new ArrayList<>();

	public void add(Mp3Metadata track) {
		list.add(track);
		setChanged();
		notifyObservers(new WebSocketActionWrapper("playlistUpdated", list));
	}

	public boolean isNotEmpty() {
		return !list.isEmpty();
	}

	public Integer size() {
		return list.size();
	}

	public Mp3Metadata get(Integer integer) {
		return list.get(integer);
	}

	public List<Mp3Metadata> getList() {
		return list;
	}

}
