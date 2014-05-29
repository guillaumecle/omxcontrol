package com.cguillaume.omxcontrol;

import com.cguillaume.omxcontrol.websocket.WebSocketServer;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import com.cguillaume.omxcontrol.controller.AjaxController;
import com.cguillaume.omxcontrol.controller.MainController;
import com.cguillaume.omxcontrol.youtube.YoutubeController;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector();
		Injection.init(injector);
		Spark.exception(Exception.class, (exception, request, response) -> exception.printStackTrace());
		Spark.staticFileLocation("public");
		Spark.get("/", injector.getInstance(MainController.class), new FreeMarkerEngine());
		Spark.get("/ajax/:action/:trackFilePath", injector.getInstance(AjaxController.class));
		Spark.get("/ajax/:action", injector.getInstance(AjaxController.class));
		Spark.get("/youtube", injector.getInstance(YoutubeController.class));
		WebSocketServer.start();
	}

}
