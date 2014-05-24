package com.cguillaume.omxcontrol;

import java.io.*;

public class Main {

	public static void main(String[] args) throws IOException {
		Omx omx = new Omx();
		omx.startPlaying("/home/pi/music/Our Story.mp3");
        while (System.in.read() != -1) {
        	System.out.println(omx.pause());
        }
    }

}
