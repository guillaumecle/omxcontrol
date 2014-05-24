package com.cguillaume.omxcontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StreamReader extends Thread {

	private InputStream in;
	private List<StreamListener> listeners = new ArrayList<StreamListener>();

	public StreamReader(InputStream in) {
		this.in = in;
	}

	public StreamReader addListener(StreamListener streamListener) {
		listeners.add(streamListener);
		return this;
	}

	@Override
	public void run() {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				for (StreamListener listener : listeners) {
					listener.onNewLine(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (StreamListener listener : listeners) {
			listener.onClose();
		}
	}

}