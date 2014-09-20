package com.ja.yuml.render.remote;

import java.io.InputStream;

import lombok.Data;

@Data
public class YumlImage {

	private final String imgUrl;

	private final InputStream data;

}
