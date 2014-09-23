package com.ja.yuml.render.remote;

public enum DiagramType {
	activityDiagram("activity", "(start)", "(end)"), classDiagram("class",
			"^-", "^-.-", "]<-", "->[", "-.->"), useCaseDiagram("usecase",
			"]^[", ")>(", ")<(", "(");

	private String yumlName;
	private String[] uniqueAttributes;

	private DiagramType(String yumlName, String... uniqueAttributes) {
		this.yumlName = yumlName;
		this.uniqueAttributes = uniqueAttributes;
	}

	public static DiagramType detect(String dsl) {
		for (DiagramType dt : values()) {
			for (String attribute : dt.uniqueAttributes) {
				if (dsl.contains(attribute)) {
					return dt;
				}
			}
		}
		return classDiagram;
	}
	
	public String toYumlName() {
		return yumlName;
	}
}
