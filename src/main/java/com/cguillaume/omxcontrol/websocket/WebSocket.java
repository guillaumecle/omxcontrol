package com.cguillaume.omxcontrol.websocket;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cguillaume.omxcontrol.Injection;
import com.google.gson.Gson;
import com.google.inject.Injector;

public class WebSocket extends WebSocketAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(WebSocket.class);
	
	//Injected
	private WebSocketManager webSocketManager;
	//Injected
	private WebSocketHandler webSocketHandler;
	//Injected
	private Gson gson;
	
	public WebSocket() {
		Injector injector = Injection.getInjector();
		webSocketManager = injector.getInstance(WebSocketManager.class);
		webSocketHandler = injector.getInstance(WebSocketHandler.class);
		gson = injector.getInstance(Gson.class);
	}
	
	@Override
	public void onWebSocketConnect(Session sess) {
		super.onWebSocketConnect(sess);
		sess.setIdleTimeout(Long.MAX_VALUE);
		webSocketManager.register(this);
		logger.info("Client connected");
	}

	@Override
	public void onWebSocketText(String message) {
		super.onWebSocketText(message);
		WebSocketActionWrapper actionWrapper = gson.fromJson(message, WebSocketActionWrapper.class);
		try {
			Method method = webSocketHandler.getClass().getMethod(actionWrapper.getAction(), Session.class, String.class);
			try {
				method.invoke(webSocketHandler, getSession(), actionWrapper.getMessage());
			} catch (IllegalAccessException | InvocationTargetException e) {
				logger.error("Error invoking handler", e);
			}
		} catch (NoSuchMethodException e) {
			webSocketHandler.handleDefault(actionWrapper.getAction(), actionWrapper.getMessage());
		}
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
		webSocketManager.unregister(this);
		logger.info("Socket Closed: [" + statusCode + "] " + reason);
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		super.onWebSocketError(cause);
		logger.error("Error on webscoket connection", cause);
	}

	public void send(WebSocketActionWrapper actionWraper) {
		try {
			getRemote().sendString(gson.toJson(actionWraper));
		} catch (IOException e) {
			logger.error("Error sending websocket message", e);
		}
	}
}
