package com.cguillaume.omxcontrol;

import spark.Request;
import spark.Response;
import spark.Route;

public class AjaxController extends Route {

	private static final String ACTION_PAUSE = "pause";
	private Omx omx;

	protected AjaxController(Omx omx) {					
		super("/ajax/:action");
		this.omx = omx;
	}

	@Override
	public Object handle(Request request, Response response) {
		switch (request.params(":action")) {
		case ACTION_PAUSE:
			omx.pause();
			break;
		default:
			return request.params(":action");
		}
		return "";
	}

}
