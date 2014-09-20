package com.ja.yuml.render.remote;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DummyYumlAccessor implements YumlAccessor {

	@Override
	public void init(Map<String, String> config) {

	}

	@Override
	public String post(String url, Map<String, String> parameters)
			throws IOException {
		log.info("url={}", url);
		return "12345.svg";
	}

	@Override
	public InputStream getData(String imgUrl) throws IOException {
		return new ByteArrayInputStream("<svg/>".getBytes());
	}

}
