package com.cguillaume.omxcontrol.websocket;

import javax.inject.Inject;

import com.cguillaume.omxcontrol.controller.upload.UploadQueue;
import com.cguillaume.omxcontrol.model.Player;
import com.cguillaume.omxcontrol.model.Playlist;
import com.cguillaume.omxcontrol.model.Synthesizer;
import com.google.inject.Singleton;

@Singleton
public class WebSocketManager {

	private Synthesizer synthesizer;
	private Playlist playlist;
	private Player player;
	private UploadQueue uploadQueue;

	@Inject
	public WebSocketManager(Synthesizer synthesizer, Playlist playlist, Player player, UploadQueue uploadQueue) {
		this.synthesizer = synthesizer;
		this.playlist = playlist;
		this.player = player;
		this.uploadQueue = uploadQueue;
		synthesizer.addObserver(player);
	}
//	private List<WebSocket> webSockets = new ArrayList<>();

	public void register(WebSocket webSocket) {
		synthesizer.addObserver(webSocket);
		playlist.addObserver(webSocket);
		player.addObserver(webSocket);
		uploadQueue.addObserver(webSocket);
//		webSockets.add(webSocket);
	}

	public void unregister(WebSocket webSocket) {
		synthesizer.deleteObserver(webSocket);
		playlist.deleteObserver(webSocket);
		player.deleteObserver(webSocket);
		uploadQueue.deleteObserver(webSocket);
//		webSockets.remove(webSocket);
	}
//
//	public void sendToAll(WebSocketActionWrapper actionWraper) {
//		for (WebSocket webSocket : webSockets) {
//			webSocket.send(actionWraper);
//		}
//	}

}
