package com.cguillaume.omxcontrol;

import spark.Spark;

public class Main {

	public static void main(String[] args) {
		Omx omx = new Omx();
		Spark.get(new MainController());
		Spark.get(new AjaxController(omx));
		omx.startPlaying("/home/pi/music/Our Story.mp3");
    }

}
