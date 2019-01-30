package io.perfecto.reporting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import io.perfecto.utils.ApplicationProperties;

public class FailureReasonsMap {
	
	private static FailureReasonsMap failureReasonsMap;
	private JSONObject failureMap = new JSONObject();
	
	private FailureReasonsMap() {
		try(BufferedReader bis = new BufferedReader(new FileReader(new File(ApplicationProperties.getApplicationProperties().readProperties("FailureMap.Path"))))){
			StringBuilder json = new StringBuilder();
			String line;
			while((line=bis.readLine())!=null) {
				json.append(line);
			}
			
			failureMap = new JSONObject(json.toString());

		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		}
	}
	
	private String getErrorMap(String message) throws JSONException{
		Matcher matcher;
		Pattern failurePattern;
		Set<String> failurePatterns = this.failureMap.keySet();
		for(String pattern:failurePatterns) {
			failurePattern = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE);
			matcher = failurePattern.matcher(message);
			if(matcher.find()) {
				return pattern;
			}
		}
		
		throw new JSONException("Failure not present");
	}
	
	public static String getFailureID(String errorMessage) {

		if(failureReasonsMap==null) {
			failureReasonsMap = new FailureReasonsMap();
		}
		
		try {
			
			JSONObject failureReason = failureReasonsMap.failureMap.getJSONObject(failureReasonsMap.getErrorMap(errorMessage));
			if(failureReason.has("isEnabled")) {
				
				if(failureReason.getBoolean("isEnabled")) {
					return failureReason.getString("failureID");
				}else {
					return "";
				}
				
			}else {
				return failureReason.getString("failureID");
			}
		}catch(JSONException e) {
			return "";
		}
	}

}
