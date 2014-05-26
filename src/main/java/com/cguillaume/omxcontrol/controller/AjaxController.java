package com.cguillaume.omxcontrol.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import spark.Request;
import spark.Response;
import spark.Route;

import com.cguillaume.omxcontrol.Omx;
import com.cguillaume.omxcontrol.Playlist;
import com.google.gson.Gson;
import com.google.inject.Inject;

public class AjaxController implements Route {

	private static final String ACTION_PAUSE = "pause";
	private static final String ACTION_ADD = "add";
	
	@Inject
	private Omx omx;
	@Inject
	private Playlist playlist;
	@Inject
	private Gson gson;

	@Override
	public Object handle(Request request, Response response) {
		switch (request.params(":action")) {
		case ACTION_PAUSE:
			if (omx.isAlive())
				return omx.pause();
			else if (playlist.hasNext())
				omx.startPlaying(playlist.next());
			else
				return false;
			break;
		case ACTION_ADD:
			try {
				if (playlist.add(URLDecoder.decode(request.params(":trackFilePath"), "utf-8"))) {
					return gson.toJson(playlist);
				} else {
					return false;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			break;
		default:
			return request.params(":action");
		}
		return "";
	}

}
