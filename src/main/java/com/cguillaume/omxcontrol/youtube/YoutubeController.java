package com.cguillaume.omxcontrol.youtube;

import com.google.inject.Singleton;

import spark.Request;
import spark.Response;
import spark.Route;

@Singleton
public class YoutubeController implements Route {

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
