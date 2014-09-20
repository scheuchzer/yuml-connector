package com.ja.yuml.render.remote.jaxrs2;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import lombok.extern.slf4j.Slf4j;

import com.ja.yuml.render.remote.YumlAccessor;

@Slf4j
public class JaxRs2YumlAccessor implements YumlAccessor {
	private ClientBuilder clientBuilder;
	private Client client;

	@SuppressWarnings("rawtypes")
	@Override
	public void init(Map<String, String> config) {
		clientBuilder = ClientBuilder.newBuilder();
		// TODO: proxy config and others if needed
		clientBuilder.register(new MessageBodyReader() {

			@Override
			public boolean isReadable(Class type, Type genericType,
					Annotation[] annotations, MediaType mediaType) {
				return true;
			}

			@Override
			public Object readFrom(Class type, Type genericType,
					Annotation[] annotations, MediaType mediaType,
					MultivaluedMap httpHeaders, InputStream entityStream)
					throws IOException, WebApplicationException {
				return entityStream;
			}
		});
		client = clientBuilder.build();
	}

	@Override
	public String post(String url, Map<String, String> parameters)
			throws IOException {
		WebTarget webTarget = client.target(url);
		StringBuilder body = new StringBuilder();
		for (String key : parameters.keySet()) {
			log.info("adding parameter={}", key);
			body.append(key).append('=')
					.append(URLEncoder.encode(parameters.get(key), "UTF-8"))
					.append('\n');
		}
		String imgName = webTarget.request(MediaType.TEXT_PLAIN).post(
				Entity.entity(body.toString(),
						MediaType.APPLICATION_FORM_URLENCODED), String.class);

		return imgName;
	}

	@Override
	public InputStream getData(String imgUrl) throws IOException {
		WebTarget webTarget = client.target(imgUrl);
		return webTarget.request().get(InputStream.class);
	}

}
