package com.cguillaume.omxcontrol.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.cguillaume.omxcontrol.Config;
import com.cguillaume.omxcontrol.Playlist;
import com.google.inject.Inject;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class MainController implements TemplateViewRoute {

	private static final FilenameFilter audioFileFilter = (dir, name) -> name.endsWith(".mp3");

	@Inject
	private Playlist playlist;
	@Inject
	private Config config;

	@Override
	public ModelAndView handle(Request request, Response response) {
		Map<String, Object> model = new TreeMap<>();
		File folder = new File(config.getLibraryLocation());
		List<File> lib = new ArrayList<>();
		Collections.addAll(lib, folder.listFiles(audioFileFilter));
		model.put("lib", lib);
		List<String> list = playlist.getList();
		model.put("list", list);
		model.put("current", playlist.getCurrent());
		return new ModelAndView(model, "main.ftl");
	}

}
