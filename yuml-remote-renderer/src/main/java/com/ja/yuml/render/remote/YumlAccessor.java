package com.ja.yuml.render.remote;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface YumlAccessor {

	void init(Map<String, String> config);

	/**
	 * 
	 * @param url
	 * @param parameters
	 * @return the name of the generated image
	 */
	String post(String url, Map<String, String> parameters) throws IOException;

	InputStream getData(String imgUrl) throws IOException;
}
