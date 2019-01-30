package io.perfecto.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PerfectoCloudInformation {
	
	private static PerfectoCloudInformation cloudInfo;
	private Map<INFORMATION, String> cData;
	
	public static enum INFORMATION{

		CLOUD_URL("remote.server"),
		CLOUD_PORT("remote.port"),
		USER_NAME("perfecto.capabilities.user"),
		PASSWORD("perfecto.capabilities.password"),
		SECURITY_TOKEN("perfecto.capabilities.securityToken");
		
		INFORMATION(String info) {
			this.info = info;
		}
		
		private String info;
		
		public String getInfo() {
			return info;
		}
	}
	
	public String getHubUrl() {
		return String.format("%snexperience/perfectomobile/wd/hub", cData.get(INFORMATION.CLOUD_URL));
	}
	
	public String getFastHubUrl() {
		return String.format("%snexperience/perfectomobile/wd/hub/fast", cData.get(INFORMATION.CLOUD_URL));
	}
	
	public String getAvailableDevicesAPIBaseUrl() {
		String availableAPI= String.format(
				"%sservices/handsets?operation=list&inUse=false&status=connected",
				cData.get(INFORMATION.CLOUD_URL));
		
		if(cData.get(INFORMATION.PASSWORD).isEmpty()) {
			if(!cData.get(INFORMATION.SECURITY_TOKEN).isEmpty()) {
				availableAPI = String.format("%s&securityToken=%s", availableAPI,cData.get(INFORMATION.SECURITY_TOKEN));
			}
		}else {
			
			availableAPI = String.format("%s&user=%s", availableAPI,cData.get(INFORMATION.USER_NAME));
			availableAPI = String.format("%s&password=%s", availableAPI,cData.get(INFORMATION.PASSWORD));
		}
		return availableAPI;
	}
	
	public String getDevicesAPIBaseUrl() {
		String listDeviceAPI= String.format(
				"%sservices/handsets?operation=list&status=connected",
				cData.get(INFORMATION.CLOUD_URL), cData.get(INFORMATION.USER_NAME));
		
		if(cData.get(INFORMATION.PASSWORD).isEmpty()) {
			if(!cData.get(INFORMATION.SECURITY_TOKEN).isEmpty()) {
				listDeviceAPI = String.format("%s&securityToken=%s", listDeviceAPI,cData.get(INFORMATION.SECURITY_TOKEN));
			}
		}else {
			listDeviceAPI = String.format("%s&user=%s", listDeviceAPI,cData.get(INFORMATION.USER_NAME));
			listDeviceAPI = String.format("%s&password=%s", listDeviceAPI,cData.get(INFORMATION.PASSWORD));
		}
		return listDeviceAPI;
	}
	
	private PerfectoCloudInformation() {
		cData = new HashMap<>();
		try {
			ApplicationProperties appProperties = ApplicationProperties.getApplicationProperties();
			
			String cloudUrl = appProperties.readProperties(INFORMATION.CLOUD_URL.getInfo());

			cData.put(INFORMATION.CLOUD_URL,cloudUrl.endsWith("/")?cloudUrl:(cloudUrl+"/") );
			cData.put(INFORMATION.CLOUD_PORT, appProperties.readProperties(INFORMATION.CLOUD_PORT.getInfo()));
			cData.put(INFORMATION.USER_NAME, appProperties.readProperties(INFORMATION.USER_NAME.getInfo()));
			cData.put(INFORMATION.PASSWORD, appProperties.readProperties(INFORMATION.PASSWORD.getInfo()));
			cData.put(INFORMATION.SECURITY_TOKEN, appProperties.readProperties(INFORMATION.SECURITY_TOKEN.getInfo()));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static PerfectoCloudInformation getCloudInformation() {
		if(cloudInfo==null) {
			cloudInfo = new PerfectoCloudInformation();
		}
		
		return cloudInfo;
	}
	
	public String getInformation(INFORMATION information) {
		if(cData.containsKey(information)) {
			return cData.get(information);
		}else {
			return "";
		}
	}
	
}
