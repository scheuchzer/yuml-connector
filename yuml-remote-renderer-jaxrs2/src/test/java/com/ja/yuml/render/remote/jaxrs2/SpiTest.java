package com.ja.yuml.render.remote.jaxrs2;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.junit.Test;

import com.ja.yuml.render.remote.YumlAccessor;

public class SpiTest {

	@Test
	public void testLoad() {
		ServiceLoader<YumlAccessor> loader = ServiceLoader
				.load(YumlAccessor.class);
		Iterator<YumlAccessor> it = loader.iterator();
		YumlAccessor actual = it.next();
		assertThat(actual, is(notNullValue()));
		assertThat(actual.getClass().getName(),
				is(JaxRs2YumlAccessor.class.getName()));
	}

}
