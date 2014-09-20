package com.ja.yuml.render.remote.jaxrs2;

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

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class Jaxrs2ClientYumlAccessorTest {

	private final int port = 40000;
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(
			port));

	@Test
	public void testPost() throws Exception {
		stubFor(post(urlEqualTo("/test")).withRequestBody(
				equalTo(String.format("dsl_text=%s.svg\n",
						URLEncoder.encode("[foo]", "UTF-8")))).willReturn(
				aResponse().withBody("123abc.svg")));
		JaxRs2YumlAccessor client = new JaxRs2YumlAccessor();
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
		JaxRs2YumlAccessor client = new JaxRs2YumlAccessor();
		client.init(new HashMap<String, String>());
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("dsl_text", "[foo].svg");
		InputStream actualStream = client.getData(String.format(
				"http://localhost:%s/123abc.svg", port));
		String actualData = IOUtils.toString(actualStream);
		assertThat(actualData, is("<svg/>"));
	}

	@Test
	public void testPostAndGetRealBackend() throws Exception {
		JaxRs2YumlAccessor client = new JaxRs2YumlAccessor();
		client.init(new HashMap<String, String>());
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("dsl_text", "[foo].svg");
		String actualName = client.post("http://yuml.me/diagram/scruffy/class",
				parameters);
		assertThat(actualName, endsWith(".svg"));

		InputStream actualStream = client.getData(String.format(
				"http://yuml.me/%s", actualName));
		String actualData = IOUtils.toString(actualStream);
		assertThat(actualData, containsString("<svg "));
		assertThat(actualData, containsString("</svg>"));
	}
}
