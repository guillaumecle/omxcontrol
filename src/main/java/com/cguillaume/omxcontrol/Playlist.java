package com.cguillaume.omxcontrol;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Playlist {

	@Inject
	private transient Omx omx;

	private boolean playOnAdd = true;
	private List<String> list = new ArrayList<>();
	private int current = -1;
	
	public boolean add(String track) {
		boolean b = list.add(track);
		if (playOnAdd && !omx.isAlive())
			omx.startPlaying(next());
		return b;
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
