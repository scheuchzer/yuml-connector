package com.ja.yuml.render.remote;

public enum Style {

	scruffy,
	plain,
	boring("nofunky"),
	nofunky;
	
	private String yuml;
	
	private Style() {
		
	}
	
	private Style(String yuml) {
		this.yuml = yuml;
	}
	
	public String toYuml() {
		return yuml == null ? toString() : yuml;
	}
	
	
}
