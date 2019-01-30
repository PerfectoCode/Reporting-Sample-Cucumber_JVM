package io.perfecto.reporting;

import java.io.FileNotFoundException;
import java.io.IOException;

import io.perfecto.utils.ApplicationProperties;
import io.perfecto.utils.Logger;
import io.perfecto.utils.Logger.STATUS;

public class ReportiumConstant {
	
	private static ReportiumConstant reportiumConst;

	private ReportiumConstant() throws FileNotFoundException, IOException {
	}
	
	public static ReportiumConstant getReportiumConstant() throws FileNotFoundException, IOException {
		if(reportiumConst==null) {
			reportiumConst = new ReportiumConstant();
		}
		
		return reportiumConst;
	}
	
	public String getProjectName() {
		return readProperty("ProjectName.ENV.VariableName", "Default Project");
	}
	
	public String getProjectVersion() {
		return readProperty("ProjectVersion.ENV.VariableName","1");
	}
	
	public String getJobName() {
		return readProperty("JobName.ENV.VariableName", "Default Job");
	}
	
	public String getJobVersion() {
		return readProperty("JobVersion.ENV.VariableName","1");
	}
	
	private String readProperty(String appPropertyItem,String defaultValue) {
		String property = null;
		try {
			String envPropertyName = ApplicationProperties.getApplicationProperties().readProperties(appPropertyItem);
			if(!envPropertyName.isEmpty()) {
				property = System.getenv(envPropertyName);
			}
			
		}catch(Exception e) {
			Logger.log(STATUS.FAIL, e.getMessage());
		}
		
		return property == null? defaultValue:property;
	}
	
//	public String getConstant(String constantName) {
//		return this.reportiumProp.getProperty(constantName, "No " + );
//	}

}
