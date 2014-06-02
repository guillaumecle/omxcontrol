package com.cguillaume.omxcontrol.websocket;

import javax.inject.Inject;

import com.cguillaume.omxcontrol.model.Player;
import com.cguillaume.omxcontrol.model.Playlist;
import com.cguillaume.omxcontrol.model.Synthesizer;
import com.google.inject.Singleton;

@Singleton
public class WebSocketManager {

	private Synthesizer synthesizer;
	private Playlist playlist;
	private Player player;

	@Inject
	public WebSocketManager(Synthesizer synthesizer, Playlist playlist, Player player) {
		this.synthesizer = synthesizer;
		this.playlist = playlist;
		this.player = player;
		synthesizer.addObserver(player);
	}
//	private List<WebSocket> webSockets = new ArrayList<>();

	public void register(WebSocket webSocket) {
		synthesizer.addObserver(webSocket);
		playlist.addObserver(webSocket);
		player.addObserver(webSocket);
//		webSockets.add(webSocket);
	}

	public void unregister(WebSocket webSocket) {
		synthesizer.deleteObserver(webSocket);
		playlist.addObserver(webSocket);
		player.addObserver(webSocket);
//		webSockets.remove(webSocket);
	}
//
//	public void sendToAll(WebSocketActionWrapper actionWraper) {
//		for (WebSocket webSocket : webSockets) {
//			webSocket.send(actionWraper);
//		}
//	}

}
