package com.wavefront.labs.convert.converter.grapher;

import com.wavefront.labs.convert.Converter;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public abstract class AbstractGrapherConverter implements Converter {
	protected Properties properties;

	public AbstractGrapherConverter() {
		this.properties = new Properties();
	}

	@Override
	public void init(Properties properties) {
		this.properties = properties;
	}

	@Override
	public abstract void parse(Object data) throws IOException;

	@Override
	public abstract List convert();
}
