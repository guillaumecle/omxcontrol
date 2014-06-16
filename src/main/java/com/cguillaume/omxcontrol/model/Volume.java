package com.cguillaume.omxcontrol.model;

import java.util.Observable;

import javax.inject.Singleton;

import com.cguillaume.omxcontrol.websocket.WebSocketActionWrapper;

@Singleton
public class Volume extends Observable {

	private Integer value = 100;

	public void increased() {
		value += 3;
		setChanged();
		notifyObservers(new WebSocketActionWrapper("volumeUpdated", value));
	}

	public void decreased() {
		value -= 3;
		setChanged();
		notifyObservers(new WebSocketActionWrapper("volumeUpdated", value));
	}

	public Integer getValue() {
		return value;
	}

}
