package com.cguillaume.omxcontrol.websocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

import com.cguillaume.omxcontrol.controller.upload.UploadQueue;
import com.cguillaume.omxcontrol.model.Library;
import com.cguillaume.omxcontrol.model.Player;
import com.cguillaume.omxcontrol.model.Playlist;
import com.cguillaume.omxcontrol.model.Synthesizer;
import com.cguillaume.omxcontrol.model.Volume;
import com.google.inject.Singleton;

@Singleton
public class WebSocketManager {

//	private List<WebSocket> webSockets = new ArrayList<>();
	private List<Observable> observables = new ArrayList<>();

	@Inject
	public WebSocketManager(Synthesizer synthesizer, Playlist playlist, Player player,
							UploadQueue uploadQueue, Volume volume, Library library) {
		observables.add(synthesizer);
		observables.add(playlist);
		observables.add(player);
		observables.add(uploadQueue);
		observables.add(volume);
		observables.add(library);
		synthesizer.addObserver(player);
	}

	public void register(WebSocket webSocket) {
		for (Observable observable : observables) {
			observable.addObserver(webSocket);
		}
//		webSockets.add(webSocket);
	}

	public void unregister(WebSocket webSocket) {
		for (Observable observable : observables) {
			observable.deleteObserver(webSocket);
		}
//		webSockets.remove(webSocket);
	}

	public void addObservable(Observable observable) {
		observables.add(observable);
	}

	public void deleteObservable(Observable observable) {
		observables.remove(observable);
	}
//
//	public void sendToAll(WebSocketActionWrapper actionWraper) {
//		for (WebSocket webSocket : webSockets) {
//			webSocket.send(actionWraper);
//		}
//	}

}
