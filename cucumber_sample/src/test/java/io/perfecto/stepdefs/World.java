package io.perfecto.stepdefs;

import org.openqa.selenium.remote.RemoteWebDriver;

import cucumber.runtime.java.picocontainer.PicoFactory;
import io.perfecto.reporting.Reports;

// Class to hold webdriver and reportium client
public class World extends PicoFactory{

	private RemoteWebDriver webDriver;
	private Reports report;
	private int stepNumber = 1;
	
	public void incrementStep() {
		++stepNumber;
	}
	
	public String getCurrentStepName() {
		return String.format("Scenario Step Number - %d", stepNumber);
	}
	
	// Get Webdriver
	public RemoteWebDriver getWebDriver() {
		return webDriver;
	}
	
	// Set Webdriver
	public void setWebDriver(RemoteWebDriver webDriver) {
		this.webDriver = webDriver;
	}
	
	// Get Reportium client
	public Reports getReport() {
		return report;
	}
	
	// Set Reportium client
	public void setReport(Reports report) {
		this.report = report;
	}
}
