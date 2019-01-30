package io.perfecto.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ApplicationProperties {
	
	private Properties appProperties;
	private static ApplicationProperties applicationProperties;
	
	private static String APP_PROPERTY_FILE_NAME = "src/test/resources/properties/application.properties";
	
	private ApplicationProperties() throws FileNotFoundException, IOException{
		appProperties = new Properties();
		appProperties.load(new FileInputStream(new File(APP_PROPERTY_FILE_NAME)));
	}
	
	public static ApplicationProperties getApplicationProperties() throws FileNotFoundException, IOException {
		if(applicationProperties == null) {
			applicationProperties = new ApplicationProperties();
		}
		
		return applicationProperties;
	}
	
	public String readProperties(String propertyName) {
		return appProperties.getProperty(propertyName, "");
	}

}
