package com.cguillaume.omxcontrol.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

import com.cguillaume.omxcontrol.model.Volume;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import com.cguillaume.omxcontrol.Config;
import com.cguillaume.omxcontrol.Util;
import com.cguillaume.omxcontrol.model.Player;
import com.cguillaume.omxcontrol.model.Playlist;
import com.cguillaume.omxcontrol.model.Synthesizer;
import com.google.inject.Inject;

public class MainController implements TemplateViewRoute {

	private static final List<String> allowedExtensions = Arrays.asList("mp3", "m4a");

	private static final FilenameFilter audioFileFilter = (dir, name) -> {
		String[] split = name.split("\\.");
		String extension = split[split.length - 1];
		return allowedExtensions.contains(extension);
	};

	@Inject
	private Playlist playlist;
	@Inject
	private Player player;
	@Inject
	private Synthesizer synthesizer;
	@Inject
	private Config config;
	@Inject
	private Util util;
	@Inject
	private Volume volume;

	@Override
	public ModelAndView handle(Request request, Response response) {
		Map<String, Object> model = new TreeMap<>();
		File folder = new File(config.getLibraryLocation());
		List<File> lib = new ArrayList<>();
		Collections.addAll(lib, folder.listFiles(audioFileFilter));
		model.put("lib", lib);

		model.put("volume", volume.getValue());
		model.put("playing", Boolean.toString(synthesizer.isPlaying()));
		model.put("list", playlist.getList());
		model.put("current", player.getCurrent());
		model.put("freeSpace", util.getFreeSpace());
		return new ModelAndView(model, "main.ftl");
	}

}
