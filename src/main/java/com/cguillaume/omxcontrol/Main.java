package com.cguillaume.omxcontrol;

import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import com.cguillaume.omxcontrol.youtube.YoutubeController;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector();
		Spark.exception(Exception.class, (exception, request, response) -> exception.printStackTrace());
		Spark.staticFileLocation("public");
		Spark.get("/", injector.getInstance(MainController.class), new FreeMarkerEngine());
		Spark.get("/ajax/:action/:trackFilePath", injector.getInstance(AjaxController.class));
        Spark.get("/youtube", injector.getInstance(YoutubeController.class));
        Spark.post("/youtube", injector.getInstance(YoutubeController.class));
	}

}
