package com.wavefront.labs.convert;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public interface Converter {

	void init(Properties properties);

	void parse(Object data) throws IOException;

	List<Object> convert();

}
