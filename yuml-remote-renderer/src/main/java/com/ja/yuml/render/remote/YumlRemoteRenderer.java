package com.ja.yuml.render.remote;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.StringTokenizer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.lang3.StringUtils;

@Data
@ExtensionMethod({ StringUtils.class })
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class YumlRemoteRenderer {

	private String baseUrl = "http://yuml.me";

	public String createUrl(String model, Style style, Direction direction) {
		String dsl = prepareDsl(model);
		return String.format("%s/diagram/%s;dir:%s/class/%s", baseUrl,
				style.toString(), direction.getValue(), dsl);

	}

	private String prepareDsl(String model) {
		StringTokenizer st = new StringTokenizer(model.trim(), "\n");
		StringBuilder buffer = new StringBuilder();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.startsWith("#")) {
				continue; // skip over comments
			}
			buffer.append(token.trim());
			if (st.hasMoreTokens()) {
				buffer.append(",");
			}
		}
		buffer.append(".svg");
		return buffer.toString();
	}

	public YumlImage render(String model, Style style, Direction direction) {
		try {
			String url = String.format("%s/diagram/%s;dir:%s/class/", baseUrl,
					style.toString(), direction.getValue());
			HttpClient client = new HttpClient();
			String proxyHost = System.getProperty("http.proxyHost");
			String proxyPort = System.getProperty("http.proxyPort");
			if (StringUtils.isNotBlank(proxyHost)
					&& StringUtils.isNotBlank(proxyPort)) {
				client.getHostConfiguration().setProxy(proxyHost,
						Integer.parseInt(proxyPort));
			}

			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("dls_text", prepareDsl(model));
			Map<String, String> config = new HashMap<>();
			YumlAccessor connector = getConnector(config);
			log.info("posting to url={}", url);
			String imgName = connector.post(url, parameters);
			log.info("created img name={}", imgName);
			String imgUrl = String.format("%s/%s", baseUrl, imgName);
			log.debug("imgUrl={}", imgUrl);
			InputStream imgData = connector.getData(imgUrl);
			return new YumlImage(imgUrl, imgData);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private YumlAccessor getConnector(Map<String, String> config) {
		ServiceLoader<YumlAccessor> loader = ServiceLoader
				.load(YumlAccessor.class);
		List<YumlAccessor> impls = new ArrayList<YumlAccessor>();
		Iterator<YumlAccessor> it = loader.iterator();
		while (it.hasNext()) {
			impls.add(it.next());
		}
		YumlAccessor connector = null;
		if (impls.isEmpty()) {
			log.error("No implementation for YumlConnector found. Please add an implementation e.g. by adding the yuml-remote-renderer-httpclient.jar");
		} else if (impls.size() > 1) {
			log.warn(
					"Multiple implementations for YumlConnector found. Will use the first one found. {}",
					impls);
		} else {
			connector = impls.get(0);
			log.info("Using YumlConnector={}", connector.getClass().getName());
			connector.init(config);
		}
		return connector;

	}

}
