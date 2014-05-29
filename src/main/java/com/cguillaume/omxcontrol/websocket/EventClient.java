package com.cguillaume.omxcontrol.websocket;

import java.net.URI;
import java.util.concurrent.Future;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class EventClient {
	public static void main(String[] args) {
		URI uri = URI.create("ws://localhost:8080/events/");

		WebSocketClient client = new WebSocketClient();
		try {
			try {
				client.start();
				WebSocket socket = new WebSocket();
				Future<Session> fut = client.connect(socket, uri);
				Session session = fut.get();
				session.getRemote().sendString("Hello");
				session.close();
			} finally {
				client.stop();
			}
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}
}
