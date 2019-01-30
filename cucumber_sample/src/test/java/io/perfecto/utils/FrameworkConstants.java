package io.perfecto.utils;

import java.io.FileNotFoundException;
import java.io.IOException;

public class FrameworkConstants {
	
	private String scenarioFolderPath;
	private static FrameworkConstants frameworkConstants;
	
	private FrameworkConstants() {
	}
	
	public static FrameworkConstants getFrameworkConstants() {
		if(frameworkConstants == null) {
			frameworkConstants = new FrameworkConstants();
		}
		
		return frameworkConstants;
	}
	
	public String getScenarioPath() throws FileNotFoundException, IOException {
		
		if(scenarioFolderPath==null) {
			scenarioFolderPath = ApplicationProperties.getApplicationProperties().readProperties("scenario.file.loc");
		}
		
		return scenarioFolderPath.endsWith("/")?scenarioFolderPath:scenarioFolderPath+"/";
	}
	
	

}
