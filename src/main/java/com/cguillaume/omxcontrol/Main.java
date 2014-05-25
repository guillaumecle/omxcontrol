package com.cguillaume.omxcontrol;

import com.cguillaume.omxcontrol.youtube.YoutubeController;
import spark.Spark;

public class Main {

	public static void main(String[] args) {
		Omx omx = new Omx();
		Spark.get(new MainController());
		Spark.get(new AjaxController(omx));
        Spark.get(new YoutubeController());
        Spark.post(new YoutubeController());
        omx.startPlaying("/home/pi/music/Our Story.mp3");
    }

}
