package com.cguillaume.omxcontrol;

public interface StreamListener {

	void onNewLine(String line);
	void onClose();

}
