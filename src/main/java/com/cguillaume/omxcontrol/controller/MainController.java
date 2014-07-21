package com.cguillaume.omxcontrol.controller;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import com.cguillaume.omxcontrol.Config;
import com.cguillaume.omxcontrol.Util;
import com.cguillaume.omxcontrol.model.Library;
import com.cguillaume.omxcontrol.model.Player;
import com.cguillaume.omxcontrol.model.Playlist;
import com.cguillaume.omxcontrol.model.Synthesizer;
import com.cguillaume.omxcontrol.model.Volume;
import com.google.inject.Inject;

public class MainController implements TemplateViewRoute {

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
	@Inject
	private Library library;

	@Override
	public ModelAndView handle(Request request, Response response) {
		Map<String, Object> model = new HashMap<>();
		model.put("lib", library.getList());
		model.put("volume", volume.getValue());
		model.put("playing", Boolean.toString(synthesizer.isPlaying()));
		model.put("alive", Boolean.toString(synthesizer.isAlive()));
		model.put("list", playlist.getList());
		model.put("current", player.getCurrent());
		model.put("freeSpace", util.getFreeSpace());
		return new ModelAndView(model, "main.ftl");
	}

}
