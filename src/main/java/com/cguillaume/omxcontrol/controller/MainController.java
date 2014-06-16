package com.cguillaume.omxcontrol.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

import com.cguillaume.omxcontrol.model.*;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import com.cguillaume.omxcontrol.Config;
import com.cguillaume.omxcontrol.Util;
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
		Map<String, Object> model = new HashMap<>();
		File folder = new File(config.getLibraryLocation());
		List<Mp3Metadata> lib = new ArrayList<>();
		for (File file : folder.listFiles(audioFileFilter)) {
			lib.add(getPrettyText(file));
		}
		model.put("lib", lib);

		model.put("volume", volume.getValue());
		model.put("playing", Boolean.toString(synthesizer.isPlaying()));
		model.put("list", playlist.getList());
		model.put("current", player.getCurrent());
		model.put("freeSpace", util.getFreeSpace());
		return new ModelAndView(model, "main.ftl");
	}

	private Mp3Metadata getPrettyText(File file) {
		MP3File mp3File = null;
		String albun = null;
		String artist = null;
		String title = null;
		String track = null;
		try {
			mp3File = new MP3File(file);
			albun = getAlbum(mp3File);
			artist = getArtist(mp3File);
			title = getTitle(mp3File);
			track = getTrackNumber(mp3File);
		} catch (IOException | TagException e) {
			e.printStackTrace();
		}
		return new Mp3Metadata(file.getAbsolutePath(), albun, artist, title, track);
	}

	private String getAlbum(MP3File mp3File) {
		if (mp3File.getID3v1Tag() != null) {
			return mp3File.getID3v1Tag().getAlbumTitle();
		} else if (mp3File.getID3v2Tag() != null) {
			return mp3File.getID3v2Tag().getAlbumTitle();
		} else {
			return null;
		}
	}

	private String getArtist(MP3File mp3File) {
		if (mp3File.getID3v1Tag() != null) {
			return mp3File.getID3v1Tag().getArtist();
		} else if (mp3File.getID3v2Tag() != null) {
			return mp3File.getID3v2Tag().getLeadArtist();
		} else {
			return null;
		}
	}

	private String getTitle(MP3File mp3File) {
		if (mp3File.getID3v1Tag() != null) {
			return mp3File.getID3v1Tag().getSongTitle();
		} else if (mp3File.getID3v2Tag() != null) {
			return mp3File.getID3v2Tag().getSongTitle();
		} else {
			return null;
		}
	}

	private String getTrackNumber(MP3File mp3File) {
		if (mp3File.getID3v1Tag() != null) {
			return mp3File.getID3v1Tag().getTrackNumberOnAlbum();
		} else if (mp3File.getID3v2Tag() != null) {
			return mp3File.getID3v2Tag().getTrackNumberOnAlbum();
		} else {
			return null;
		}
	}

}
