package com.ja.yuml.render.remote.httpclient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
@Slf4j
public class HttpClientYumlAccessorTest {

	private final int port = 40000;
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(
			port));

	@Test
	public void testPost() throws Exception {
		stubFor(post(urlEqualTo("/test")).withRequestBody(
				equalTo(String.format("dsl_text=%s.svg",
						URLEncoder.encode("[foo]", "UTF-8")))).willReturn(
				aResponse().withBody("123abc.svg")));
		HttpClientYumlAccessor client = new HttpClientYumlAccessor();
		client.init(new HashMap<String, String>());
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("dsl_text", "[foo].svg");
		String actualName = client.post(
				String.format("http://localhost:%s/test", port), parameters);
		assertThat(actualName, is("123abc.svg"));
	}

	@Test
	public void testGetData() throws Exception {
		stubFor(get(urlEqualTo("/123abc.svg")).willReturn(
				aResponse().withHeader("Content-Type", "application/svg")
						.withBody("<svg/>")));
		HttpClientYumlAccessor client = new HttpClientYumlAccessor();
		client.init(new HashMap<String, String>());
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("dsl_text", "[foo].svg");
		InputStream actual = client.getData(String.format(
				"http://localhost:%s/123abc.svg", port));
		String actualData = IOUtils.toString(actual);
		assertThat(actualData, is("<svg/>"));
	}

	@Test
	public void testPostAndGetRealBackendClass() throws Exception {
		HttpClientYumlAccessor client = new HttpClientYumlAccessor();
		client.init(new HashMap<String, String>());
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("dsl_text", "[foo].svg");
		String actualName = client.post("http://yuml.me/diagram/scruffy/class",
				parameters);
		log.info("actualName={}", actualName);
		assertThat(actualName, endsWith(".svg"));

		InputStream actual = client.getData(String.format("http://yuml.me/%s",
				actualName));
		String actualData = IOUtils.toString(actual);
		assertThat(actualData, containsString("<svg "));
		assertThat(actualData, containsString("</svg>"));
	}
	
	@Test
	public void testPostAndGetRealBackendActivity() throws Exception {
		HttpClientYumlAccessor client = new HttpClientYumlAccessor();
		client.init(new HashMap<String, String>());
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("dsl_text", "(start)->(Boil Kettle)->(end).svg");
		String actualName = client.post("http://yuml.me/diagram/scruffy/activity",
				parameters);
		log.info("actualName={}", actualName);
		assertThat(actualName, endsWith(".svg"));

		InputStream actual = client.getData(String.format("http://yuml.me/%s",
				actualName));
		String actualData = IOUtils.toString(actual);
		assertThat(actualData, containsString("<svg "));
		assertThat(actualData, containsString("</svg>"));
	}
	
	@Test
	public void testPostAndGetRealBackendUseCase() throws Exception {
		HttpClientYumlAccessor client = new HttpClientYumlAccessor();
		client.init(new HashMap<String, String>());
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("dsl_text", "[Customer]-(Login).svg");
		String actualName = client.post("http://yuml.me/diagram/scruffy/usecase",
				parameters);
		log.info("actualName={}", actualName);
		assertThat(actualName, endsWith(".svg"));

		InputStream actual = client.getData(String.format("http://yuml.me/%s",
				actualName));
		String actualData = IOUtils.toString(actual);
		assertThat(actualData, containsString("<svg "));
		assertThat(actualData, containsString("</svg>"));
	}
}
