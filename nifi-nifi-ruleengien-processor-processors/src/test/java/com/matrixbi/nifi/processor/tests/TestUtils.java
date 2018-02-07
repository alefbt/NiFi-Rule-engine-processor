package com.matrixbi.nifi.processor.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestUtils {


	static InputStream getFileIS(String path) throws IOException {
		File initialFile = new File(path);
		return new FileInputStream(initialFile);
	}
	
	public String getResourcePath(String relativePath) {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(relativePath).getFile());
		return file.getAbsolutePath();
	}
}
