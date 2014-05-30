package com.cguillaume.omxcontrol.websocket;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Singleton;

@Singleton
public class WebSocketManager {

	private List<WebSocket> webSockets = new ArrayList<>();

	public void register(WebSocket webSocket) {
		webSockets.add(webSocket);
	}

	public void unregister(WebSocket webSocket) {
		webSockets.remove(webSocket);
	}

	public void sendToAll(WebSocketActionWrapper actionWraper) {
		for (WebSocket webSocket : webSockets) {
			webSocket.send(actionWraper);
		}
	}

}
