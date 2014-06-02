package com.cguillaume.omxcontrol.model;

import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Synthesizer extends Observable {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public abstract void startPlaying(String trackFilePath);
	public abstract void pause();
	public abstract void stop();

	public abstract boolean isAlive();
	public abstract boolean isPlaying();
}
