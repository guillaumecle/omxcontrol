package com.cguillaume.omxcontrol;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class MainController implements TemplateViewRoute {

	private static final FilenameFilter ausioFileFilter = new FilenameFilter() {
		
		@Override
		public boolean accept(File arg0, String arg1) {
			return arg1.endsWith(".mp3");
		}
	};

	@Override
	public ModelAndView handle(Request request, Response response) {
		File folder = new File("music");
		List<File> list = new ArrayList<File>();
		for (File file : folder.listFiles(ausioFileFilter)) {
			list.add(file);
		}
		Map<String, Object> model = new TreeMap<>();
		model.put("list", list);
		return new ModelAndView(model, "main.ftl");
	}

}
