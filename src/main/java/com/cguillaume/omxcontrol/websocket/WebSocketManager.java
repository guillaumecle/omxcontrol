package com.cguillaume.omxcontrol.websocket;

import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class WebSocketManager {

	List<WebSocket> webSockets = new ArrayList<>();

	public void register(WebSocket webSocket) {
		webSockets.add(webSocket);
	}

	public void unregister(WebSocket webSocket) {
		webSockets.remove(webSocket);
	}

	public void sendToAll(String text) {
		for (WebSocket webSocket : webSockets) {
			webSocket.send(text);
		}
	}

}
