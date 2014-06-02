package com.cguillaume.omxcontrol.websocket;

import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cguillaume.omxcontrol.Injection;
import com.google.gson.Gson;
import com.google.inject.Injector;

public class WebSocket extends WebSocketAdapter implements Observer {
	
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
		Method[] methods = webSocketHandler.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().equals(actionWrapper.getAction()) && method.isAnnotationPresent(Handler.class)) {
				try {
					Class<?>[] types = method.getParameterTypes();
					if (types.length > 2) {
						break;
					}
					Object[] params = new Object[types.length];
					for (int i = 0; i < types.length; i++) {
						if(types[i].equals(Session.class)) {
							params[i] = getSession();
						} else {
							params[i] = gson.fromJson(actionWrapper.getMessage(), types[i]);
						}
					}
					method.invoke(webSocketHandler, params);
				} catch (Exception e) {
					logger.error("Error invoking handler", e);
				}
				return;
			}
		}
		webSocketHandler.handleDefault(actionWrapper.getAction(), actionWrapper.getMessage());
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
		} catch (Exception e) {
			logger.error("Error sending websocket message", e);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg != null && arg instanceof WebSocketActionWrapper) {
			send((WebSocketActionWrapper) arg);
		}
	}
}
