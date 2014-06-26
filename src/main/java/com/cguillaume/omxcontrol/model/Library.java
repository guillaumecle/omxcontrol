package com.cguillaume.omxcontrol.model;

import com.cguillaume.omxcontrol.Config;
import com.cguillaume.omxcontrol.websocket.WebSocketActionWrapper;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.FrameBodyAPIC;
import org.farng.mp3.id3.ID3v2_3Frame;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

@Singleton
public class Library extends Observable {

	private static final List<String> allowedExtensions = Arrays.asList("mp3", "m4a");

	private static final FilenameFilter audioFileFilter = (dir, name) -> {
		String[] split = name.split("\\.");
		String extension = split[split.length - 1];
		return allowedExtensions.contains(extension);
	};

	private final List<Mp3Metadata> list = new ArrayList<>();

	@Inject
	public Library(Config config) {
		File folder = new File(config.getLibraryLocation());
		for (File file : folder.listFiles(audioFileFilter)) {
			list.add(getTrack(file));
		}
	}

	public Mp3Metadata get(Integer i) {
		return list.get(i);
	}

	public List<Mp3Metadata> getList() {
		return list;
	}

	public void add(File file) {
		add(getTrack(file));
	}

	public void add(Mp3Metadata track) {
		list.add(track);
		setChanged();
		notifyObservers(new WebSocketActionWrapper("libraryUpdated", getList()));
	}

	private Mp3Metadata getTrack(File file) {
		MP3File mp3File;
		String album = null;
		String artist = null;
		String title = null;
		String track = null;
		String coverBase64 = null;
		try {
			mp3File = new MP3File(file);
			album = getAlbum(mp3File);
			artist = getArtist(mp3File);
			title = getTitle(mp3File);
			track = getTrackNumber(mp3File);
			coverBase64 = getCover(mp3File);
		} catch (IOException | TagException e) {
			e.printStackTrace();
		}
		if (title == null) {
			title = file.getName();
		}
		return new Mp3Metadata(file.getAbsolutePath(), album, artist, title, track, coverBase64);
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

	private String getCover(MP3File mp3File) {
		if (mp3File.getID3v2Tag() != null) {
			Iterator apics = mp3File.getID3v2Tag().getFrameOfType("APIC");
			if (apics.hasNext()) {
				ID3v2_3Frame frame = (ID3v2_3Frame) apics.next();
				FrameBodyAPIC apic = (FrameBodyAPIC) frame.getBody();
				byte[] data = (byte[]) apic.getObject("Picture Data");
				String mime = (String) apic.getObject("MIME Type");
				Base64.Encoder encode = Base64.getEncoder();
				String res = encode.encodeToString(data);
				return "data:" + mime + ";base64," + res;
			}
			return null;
		} else {
			return null;
		}
	}

}
