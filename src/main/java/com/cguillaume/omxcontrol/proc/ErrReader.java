package com.cguillaume.omxcontrol.proc;

public class ErrReader extends StreamReader {

	public ErrReader(Process process) {
		super(process.getErrorStream());
	}

}
