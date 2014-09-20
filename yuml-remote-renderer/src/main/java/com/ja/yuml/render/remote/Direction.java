package com.ja.yuml.render.remote;

public enum Direction {

	topDown("TB"), leftToRight("LR"), rightToLeft("RL");

	private String value;

	private Direction(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
