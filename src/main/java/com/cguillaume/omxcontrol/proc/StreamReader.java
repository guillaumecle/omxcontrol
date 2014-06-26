package com.cguillaume.omxcontrol.proc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StreamReader extends Thread {

	private InputStream in;
	private List<NewLineListener> newLineListeners = new ArrayList<>();
	private List<CloseListener> closeListeners = new ArrayList<>();

	public StreamReader(InputStream in) {
		this.in = in;
	}

	public StreamReader addNewLineListener(NewLineListener newLineListener) {
		newLineListeners.add(newLineListener);
		return this;
	}

	public StreamReader addCloseListener(CloseListener closeListener) {
		closeListeners.add(closeListener);
		return this;
	}

	@Override
	public void run() {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		String line;
		IOException e = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				for (NewLineListener newLineListener : newLineListeners) {
					newLineListener.onNewLine(line);
				}
			}
		} catch (IOException ioe) {
			e = ioe;
		}
		for (CloseListener closeListener : closeListeners) {
			closeListener.onClose(e);
		}
	}

}