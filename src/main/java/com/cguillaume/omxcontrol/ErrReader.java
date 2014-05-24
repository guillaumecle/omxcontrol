package com.cguillaume.omxcontrol;

public class ErrReader extends StreamReader {

	public ErrReader(Process process) {
		super(process.getErrorStream());
	}

}
