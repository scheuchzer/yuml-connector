package com.ja.yuml.render.remote;

public enum Direction {

	topDown("TB"), leftToRight("LR"), rightToLeft("RL");

	private String yuml;

	private Direction(String yuml) {
		this.yuml = yuml;
	}
	
	public String toYuml() {
		return yuml;
	}
}
