package com.cguillaume.omxcontrol;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import spark.Request;
import spark.Response;
import spark.Route;

public class MainController extends Route {

	private static final FilenameFilter ausioFileFilter = new FilenameFilter() {
		
		@Override
		public boolean accept(File arg0, String arg1) {
			return arg1.endsWith(".mp3");
		}
	};

	protected MainController() {
		super("/");
	}

	@Override
	public Object handle(Request request, Response response) {
		File folder = new File("music");
		List<File> list = new ArrayList<File>();
		for (File file : folder.listFiles(ausioFileFilter)) {
			list.add(file);
		}
		return list;
	}

}
