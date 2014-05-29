package com.cguillaume.omxcontrol.websocket;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import com.cguillaume.omxcontrol.Omx;
import com.google.inject.Inject;

public class EventSocket extends WebSocketAdapter {

	@Inject 
	private Omx omx;
	
	@Override
	public void onWebSocketConnect(Session sess) {
		super.onWebSocketConnect(sess);
		getSession().setIdleTimeout(Long.MAX_VALUE);
		System.out.println(this);
		System.out.println(omx);
		System.out.println("Client connected");
	}

	@Override
	public void onWebSocketText(String message) {
		super.onWebSocketText(message);
		System.out.println(omx);
		System.out.println("Received TEXT message: " + message);
		try {
			getRemote().sendString("Bonjour");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
		System.out.println("Socket Closed: [" + statusCode + "] " + reason);
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		super.onWebSocketError(cause);
		cause.printStackTrace(System.err);
	}
}
