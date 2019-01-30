package io.perfecto.stepdefs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.AfterStep;
import cucumber.api.java.Before;
import cucumber.api.java.BeforeStep;
import io.perfecto.devices.DesiredCapability;
import io.perfecto.devices.Device;
import io.perfecto.reporting.ReportiumConstant;
import io.perfecto.reporting.Reports;
import io.perfecto.utils.Logger;
import io.perfecto.utils.Logger.STATUS;
import io.perfecto.utils.PerfectoCloudInformation;

public class CucumberHooks {
	
	private World world;
	
	public CucumberHooks(World world) {
		this.world = world;
	}
	
	@Before
	public void beforeScenario(Scenario scenario) throws FileNotFoundException, IOException {
		
		// Get Scenario name
		String scenarioName  = scenario.getName();
		
		Logger.log(STATUS.INFO, "================ Scenario Started - " + scenarioName);
		
		ReportiumConstant reportConstants = ReportiumConstant.getReportiumConstant();

		Map<String, String> deviceParams = new HashMap<>();
		deviceParams.put("platformName", "Windows");
		deviceParams.put("platformVersion", "10");
		deviceParams.put("browserName", "Chrome");
		deviceParams.put("browserVersion", "latest-1");
		deviceParams.put("location", "US East");
		deviceParams.put("scriptName", scenarioName);
		deviceParams.put("report.jobName", reportConstants.getJobName());
		deviceParams.put("report.jobNumber", reportConstants.getJobVersion());
		
		Device deviceInformation = new Device(deviceParams);
		
		DesiredCapabilities desiredCapabilities = DesiredCapability
				.getDesiredCapabilities(deviceInformation);
		
		PerfectoCloudInformation cloudInformation = PerfectoCloudInformation.getCloudInformation();
		
		RemoteWebDriver driver = null;
		try {
			
			// Instantiate webdriver
			if (deviceInformation.isMobileDevice()) {
				driver = new RemoteWebDriver(new URL(cloudInformation.getHubUrl()),
						desiredCapabilities);
			} else {
				driver = new RemoteWebDriver(new URL(cloudInformation.getFastHubUrl()),
						desiredCapabilities);
			}
			
			//Set webdriver to world object
			world.setWebDriver(driver);
			
		} catch (MalformedURLException e) {
			Logger.log(STATUS.FAIL, e.getLocalizedMessage());
		} catch (WebDriverException e) {
			Logger.log(STATUS.FAIL, e.getLocalizedMessage());
		}finally {
			// Instantiate and set report to world object
			world.setReport(new Reports(world.getWebDriver()));
			
			// Start Test in Perfecto reportium
			world.getReport().startTest(scenarioName);
		}
	}
	
	@After
	public void afterScenario(Scenario scenario) {
		
		// Get Scenario name
		String scenarioName  = scenario.getName();
		
		// Get web driver from world object
		RemoteWebDriver driver = world.getWebDriver();
		
		if(driver!=null) {
			
			// Check whether test is passed or failed
			if(scenario.isFailed()) {
				world.getReport().endTest(false);
			}else {
				world.getReport().endTest(true);
			}
			
			driver.quit();
		}

		Logger.log(STATUS.INFO, "================ Scenario Ended - " + scenarioName);
	}
	
	@BeforeStep
	public void beforeStep(Scenario scenario) {
		world.getReport().startStep(world.getCurrentStepName());
	}
	
	@AfterStep
	public void afterStep(Scenario scenario) {
		world.getReport().endStep();
	}
}
