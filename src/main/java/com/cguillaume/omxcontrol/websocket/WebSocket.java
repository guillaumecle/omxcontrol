package com.cguillaume.omxcontrol.websocket;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import com.cguillaume.omxcontrol.Injection;
import com.google.inject.Injector;

public class WebSocket extends WebSocketAdapter {
	
	//Injected
	private WebSocketManager webSocketManager;
	
	public WebSocket() {
		Injector injector = Injection.getInjector();
		webSocketManager = injector.getInstance(WebSocketManager.class);
	}
	
	@Override
	public void onWebSocketConnect(Session sess) {
		super.onWebSocketConnect(sess);
		webSocketManager.register(this);
		getSession().setIdleTimeout(Long.MAX_VALUE);
		System.out.println("Client connected");
	}

	@Override
	public void onWebSocketText(String message) {
		super.onWebSocketText(message);
		System.out.println("Received TEXT message: " + message);
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
		webSocketManager.unregister(this);
		System.out.println("Socket Closed: [" + statusCode + "] " + reason);
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		super.onWebSocketError(cause);
		cause.printStackTrace();
	}

	public void send(String text) {
		try {
			getRemote().sendString(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
