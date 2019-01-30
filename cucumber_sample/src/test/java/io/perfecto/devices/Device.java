package io.perfecto.devices;

import java.util.HashMap;
import java.util.Map;

public class Device {
	
	private Map<String,String> deviceAttributes;
	
	public boolean isMobileDevice() {
		String platformName = deviceAttributes.getOrDefault("platformName", "").trim();
		return platformName.equalsIgnoreCase("IOS")|| platformName.equalsIgnoreCase("ANDROID");
	}
	
	public boolean isAndroid() {
		String platformName = deviceAttributes.getOrDefault("platformName", "").trim();
		return platformName.equalsIgnoreCase("ANDROID");
	}
	
	public Device(Map<String,String> deviceAttributes) {
		this.deviceAttributes = deviceAttributes;
	}
	
	public Device() {
		this.deviceAttributes = new HashMap<>();
	}
	
	void addAttribute(String attributeName,String attributeValue) {
		deviceAttributes.put(attributeName, attributeValue);
	}
	
	public Map<String,String> getDeviceAttributes(){
		return this.deviceAttributes;
	}
	
	@Override
	public String toString() {
		
		return deviceAttributes.toString();
	}

}
