package com.cguillaume.omxcontrol;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Singleton;

@Singleton
public class Playlist {
	
	private List<String> list = new ArrayList<>();
	private Integer current;
	
	public void add(String track) {
		list.add(track);
		System.out.println(list);
	}
	
	public boolean hasNext() {
		if (current == null)
			return list.size() > 0;
		else
			return list.size() > current + 1;
	}
	
	public String next() {
		return list.get(current++);
	}

	public List<String> getList() {
		return list;
	}
}
