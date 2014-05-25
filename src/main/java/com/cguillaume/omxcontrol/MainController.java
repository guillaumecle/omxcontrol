package com.cguillaume.omxcontrol;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class MainController implements TemplateViewRoute {

	private static final FilenameFilter ausioFileFilter = (dir, name) -> name.endsWith(".mp3");

	@Override
	public ModelAndView handle(Request request, Response response) {
		File folder = new File("music");
		List<File> list = new ArrayList<>();
		Collections.addAll(list, folder.listFiles(ausioFileFilter));
		Map<String, Object> model = new TreeMap<>();
		model.put("list", list);
		return new ModelAndView(model, "main.ftl");
	}

}
