package com.cguillaume.omxcontrol.youtube;

import spark.Request;
import spark.Response;
import spark.Route;

public class YoutubeController extends Route {
	public YoutubeController() {
		super("/youtube");
	}

	@Override
	public Object handle(Request request, Response response) {
		String url = request.queryParams("url");
		if (url == null) {
			return form();
		}
		new YoutubeDownloader(url).start();
		response.redirect("/youtube");
		return "";
	}

	public String form() {
		return "<form method=POST><input type=text name=url placeholder=URL />"
				+ "<input type=submit /></form>";
	}
}
