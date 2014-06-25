package com.cguillaume.omxcontrol.model;

public class Mp3Metadata {

	public String filePath;
	public String album;
	public String artist;
	public String title;
	public String track;
	public String coverURI;

	public Mp3Metadata(String filePath, String album, String artist, String title, String track, String coverURI) {
		this.filePath = filePath;
		this.album = album;
		this.artist = artist;
		this.title = title;
		this.track = track;
		this.coverURI = coverURI;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getAlbum() {
		return album;
	}

	public String getArtist() {
		return artist;
	}

	public String getTitle() {
		return title;
	}

	public String getTrack() {
		return track;
	}

	public String getCoverURI() {
		return coverURI;
	}

}
