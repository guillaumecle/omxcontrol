package com.cguillaume.omxcontrol;

import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import com.cguillaume.omxcontrol.youtube.YoutubeController;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector();
		Spark.get("/", injector.getInstance(MainController.class), new FreeMarkerEngine());
		Spark.get("/ajax/:action/:trackFilePath", injector.getInstance(AjaxController.class));
        Spark.get("/youtube", new YoutubeController());
        Spark.post("/youtube", new YoutubeController());
		Spark.exception(Exception.class, new ExceptionHandler() {

			@Override
			public void handle(Exception exception, Request request, Response response) {
				exception.printStackTrace();
			}

		});
		//omx.startPlaying("/home/pi/music/Our Story.mp3");
	}

}
