package com.cguillaume.omxcontrol.websocket;

import com.google.gson.Gson;

public class WebSocketActionWrapper {

	private String action;
	
	private String message;

	public WebSocketActionWrapper(String action, Object message) {
		this.action = action;
		this.message = new Gson().toJson(message);
	}

	public String getAction() {
		return action;
	}

	public String getMessage() {
		return message;
	}

}
