package com.cguillaume.omxcontrol;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Singleton;

@Singleton
public class Playlist {
	
	private List<String> list = new ArrayList<>();
	private int current = -1;
	
	public boolean add(String track) {
		return list.add(track);
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
}
