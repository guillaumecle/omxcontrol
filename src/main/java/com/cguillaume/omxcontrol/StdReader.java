package com.cguillaume.omxcontrol;

public class StdReader extends StreamReader {

	public StdReader(Process process) {
		super(process.getInputStream());
	}

}