package com.cguillaume.omxcontrol.proc;


public class StdReader extends StreamReader {

	public StdReader(Process process) {
		super(process.getInputStream());
	}

}