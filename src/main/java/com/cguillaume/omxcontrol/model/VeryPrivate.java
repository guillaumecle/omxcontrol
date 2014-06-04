package com.cguillaume.omxcontrol.model;

public abstract class VeryPrivate<V> {

	protected V value;

	public VeryPrivate(V init) {
		value = init;
	}

	protected abstract void onUpdate();

	public void set(V value) {
		this.value = value;
		onUpdate();
	}

	public V get() {
		return value;
	}

}
