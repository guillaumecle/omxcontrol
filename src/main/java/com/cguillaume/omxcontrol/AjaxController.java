package com.cguillaume.omxcontrol;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import spark.Request;
import spark.Response;
import spark.Route;

import com.google.inject.Inject;

public class AjaxController implements Route {

	private static final String ACTION_PAUSE = "pause";
	private static final String ACTION_ADD = "add";
	
	@Inject
	private Omx omx;
	@Inject
	private Playlist playlist;

//	@Inject
//	public AjaxController(Omx omx) {
//		this.omx = omx;
//	}
	
	@Override
	public Object handle(Request request, Response response) {
		switch (request.params(":action")) {
		case ACTION_PAUSE:
			if (omx.isAlive())
				omx.pause();
			else
				omx.startPlaying(playlist.next());
			break;
		case ACTION_ADD:
			try {
				playlist.add(URLDecoder.decode(request.params(":trackFilePath"), "utf-8"));
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
