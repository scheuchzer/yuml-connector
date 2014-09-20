package com.ja.yuml.render.remote.httpclient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.ja.yuml.render.remote.YumlAccessor;

@Slf4j
@ExtensionMethod(StringUtils.class)
public class HttpClientYumlAccessor implements YumlAccessor {

	private HttpClient client;

	@Override
	public String post(String url, Map<String, String> parameters)
			throws IOException {
		PostMethod postMethod = new PostMethod(url);
		for (String key : parameters.keySet()) {
			log.info("adding parameter={}", key);
			postMethod.addParameter(key, parameters.get(key));
		}
		try {
			client.executeMethod(postMethod);
			String imgName = postMethod.getResponseBodyAsString();
			return imgName;
		} finally {
			postMethod.releaseConnection();
		}
	}

	@Override
	public InputStream getData(String imgUrl) throws IOException {
		GetMethod getMethod = new GetMethod(imgUrl);
		client.executeMethod(getMethod);
		try {

			return new ByteArrayInputStream(IOUtils.toByteArray(getMethod
					.getResponseBodyAsStream()));
		} finally {
			getMethod.releaseConnection();
		}
	}

	@Override
	public void init(Map<String, String> config) {
		HttpClient client = new HttpClient();
		String proxyHost = config.get("http.proxyHost");
		String proxyPort = config.get("http.proxyPort");
		if (proxyHost.isNotBlank() && proxyPort.isNotBlank()) {
			client.getHostConfiguration().setProxy(proxyHost,
					Integer.parseInt(proxyPort));
		}
		this.client = client;

	}

}
