package com.cguillaume.omxcontrol;

import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import com.cguillaume.omxcontrol.controller.MainController;
import com.cguillaume.omxcontrol.controller.MainWSHandler;
import com.cguillaume.omxcontrol.model.MPlayerSynthesizer;
import com.cguillaume.omxcontrol.model.Synthesizer;
import com.cguillaume.omxcontrol.websocket.WebSocketHandler;
import com.cguillaume.omxcontrol.websocket.WebSocketServer;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(WebSocketHandler.class).to(MainWSHandler.class);
				bind(Synthesizer.class).to(MPlayerSynthesizer.class);
			}
		});
		Injection.init(injector);
		Spark.exception(Exception.class, (exception, request, response) -> exception.printStackTrace());
		Spark.staticFileLocation("public");
		Spark.get("/", injector.getInstance(MainController.class), new FreeMarkerEngine());
		WebSocketServer.start();
	}

}
