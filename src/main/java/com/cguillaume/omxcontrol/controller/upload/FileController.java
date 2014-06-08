package com.cguillaume.omxcontrol.controller.upload;

import java.io.IOException;

import javax.servlet.ServletException;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class FileController implements Route{

	@Override
	public Object handle(Request request, Response response) {
		QueryParamsMap map = request.queryMap();
		System.out.println(map.toMap());
		System.out.println(request.attributes());
		System.out.println(request.attribute("name"));
		System.out.println(request.headers());
		System.out.println(request.attributes());
		try {
			System.out.println(request.raw().getParts());
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}
		return "ok";
	}
}
