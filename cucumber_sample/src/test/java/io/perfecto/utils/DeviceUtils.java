package io.perfecto.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.FluentWait;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.google.common.base.Function;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.perfecto.devices.Device;
import io.perfecto.utils.Logger.STATUS;

public class DeviceUtils {
	
	private static final Map<String, String> capabilityMap;

	static {
		Map<String, String> deviceSelectionMap = new HashMap<String, String>();
		deviceSelectionMap.put("deviceName", "deviceId");
		deviceSelectionMap.put("platformName", "os");
		deviceSelectionMap.put("platformVersion", "osVersion");
		deviceSelectionMap.put("manufacturer", "manufacturer");
		deviceSelectionMap.put("model", "model");
		deviceSelectionMap.put("location", "location");
		deviceSelectionMap.put("description", "description");
		deviceSelectionMap.put("network", "operator");
		deviceSelectionMap.put("resolution", "resolution");
		capabilityMap = Collections.unmodifiableMap(deviceSelectionMap);

	}
	
	private static boolean isDeviceCombinationValid(String url) {
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		try {
			HttpResponse response = httpClient.execute(request);
			
			if(response.getStatusLine().getStatusCode()==200) {
				BufferedReader rd = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));

					StringBuffer result = new StringBuffer();
					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
					DocumentBuilder builder;  
					try {  
					    builder = factory.newDocumentBuilder();  
					    Document document = builder.parse(new InputSource(new StringReader(result.toString())));   
					    return document.getElementsByTagName("handset").getLength()>0;
					} catch (Exception e) {  
					    return false;
					} 
					
			}else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isDeviceAvailable(Device deviceInformation) throws TimeoutException{
		
		if(deviceInformation==null) return true;
		
		if(!deviceInformation.isMobileDevice()) return true;
		
		PerfectoCloudInformation perfectoConstants = PerfectoCloudInformation.getCloudInformation();
		
		String optionalParameters = getOptionalParameters(deviceInformation);
		
		if(!isDeviceCombinationValid(perfectoConstants.getDevicesAPIBaseUrl() + optionalParameters)) {
			return false;
		}
		
		String availableDevicesUrl = String.format("%s%s",
				perfectoConstants.getAvailableDevicesAPIBaseUrl(),
				optionalParameters);
		
		FluentWait<String> deviceWait = new FluentWait<String>(availableDevicesUrl);
		
		deviceWait.withTimeout(Duration.ofMinutes(1));
		deviceWait.pollingEvery(Duration.ofSeconds(10));
		
		return deviceWait.until(new Function<String,Boolean>(){

			@Override
			public Boolean apply(String apiUrl) {
				
				HttpClient httpClient = HttpClientBuilder.create().build();
				HttpGet request = new HttpGet(apiUrl);
				try {
					HttpResponse response = httpClient.execute(request);
					
					if(response.getStatusLine().getStatusCode()==200) {
						BufferedReader rd = new BufferedReader(
								new InputStreamReader(response.getEntity().getContent()));

							StringBuffer result = new StringBuffer();
							String line = "";
							while ((line = rd.readLine()) != null) {
								result.append(line);
							}
							
							DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
							DocumentBuilder builder;  
							try {  
							    builder = factory.newDocumentBuilder();  
							    Document document = builder.parse(new InputSource(new StringReader(result.toString())));   
							    return document.getElementsByTagName("handset").getLength()>0;
							} catch (Exception e) {  
							    return false;
							} 
							
					}else {
						return false;
					}
				} catch (Exception e) {
					Logger.log(STATUS.INFO,e.getLocalizedMessage());
					return false;
				}
			}
			
		});
	}
	
	private static String getOptionalParameters(Device deviceInformation) {
		
		StringBuilder url = new StringBuilder();
		
		Map<String,String> deviceProperties = deviceInformation.getDeviceAttributes();
		
		Set<String> keySet = deviceProperties.keySet();
		
		Iterator<String> keyIter = keySet.iterator();
		
		String currentKey;
		
		while(keyIter.hasNext()) {
			currentKey = keyIter.next();
			url = url.append("&");
			url = url.append(capabilityMap.getOrDefault(currentKey, currentKey));
			url = url.append("=");
			url = url.append(deviceProperties.get(currentKey));
		}
		
		return url.toString();
		
	}
	
	public static void setLocation(AppiumDriver<MobileElement> driver, String address) {
		Map<String, Object> params = new HashMap<>();
		params.put("address", address);
		driver.executeScript("mobile:location:set", params);
	}
	
}
