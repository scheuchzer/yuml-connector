package com.ja.yuml.render.remote;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

@Slf4j
public class YumlRemoteRendererTest {

	@Test
	public void testRender() {
		YumlRemoteRenderer renderer = new YumlRemoteRenderer();
		String model = "[foo]^-[bar]\n" + "[foo]^-[tar]";

		YumlImage actual = renderer.render(model, Style.scruffy,
				Direction.leftToRight);

		assertThat(actual.getImgUrl().toString(), startsWith("http://yuml.me/"));
		log.info("{}", actual);

	}

	@Test
	public void testCreateDirectUrl() throws MalformedURLException {
		YumlRemoteRenderer renderer = new YumlRemoteRenderer();
		String model = "[foo]^-[bar]\n" + "[foo]^-[tar]";

		String actual = renderer.createUrl(model, Style.scruffy,
				Direction.leftToRight);
		assertThat(
				actual,
				is("http://yuml.me/diagram/scruffy;dir:LR/class/[foo]^-[bar],[foo]^-[tar].svg"));
	}
}
