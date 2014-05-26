package com.cguillaume.omxcontrol.proc;

public interface StreamListener {

	void onNewLine(String line);
	void onClose();

}
