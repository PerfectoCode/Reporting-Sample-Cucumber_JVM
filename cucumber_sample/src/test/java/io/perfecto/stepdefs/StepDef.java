package io.perfecto.stepdefs;

import org.junit.Assert;
import org.openqa.selenium.remote.RemoteWebDriver;

import cucumber.api.java.en.Given;

public class StepDef{

	// World Object instance to inject driver and reportium objects
	private World world;
	
	public StepDef(World world) {
		this.world = world;
	}
	
	@Given("^I have (\\d+) cukes in my belly$")
	public void i_have_cukes_in_my_belly(int arg1) throws Throwable {
		
		// Getting web driver instance from world object
		RemoteWebDriver driver = this.world.getWebDriver();
		
		driver.get("http://perfecto.io");
		
		Assert.assertEquals("Wrong title", driver.getTitle());
	}


}
