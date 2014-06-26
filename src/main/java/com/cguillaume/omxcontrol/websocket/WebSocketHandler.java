package com.cguillaume.omxcontrol.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WebSocketHandler {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public void handleDefault(String action, Object message) {
		logger.warn("No correct handler define for this action (" + action + ")");
	}

	public void handleBinary(byte[] payload, int offset, int len) {
		logger.warn("No handler define for binary upload");
	}
}
