package io.perfeco.cucumber;

import cucumber.api.cli.Main;

public class CukeRunner {
	
	public static void main(String[] args) throws Throwable {
        final String[] cucumberArgs = {
                "-g",
                "io.perfecto.stepdefs",
                "classpath:features"
        };

        Main.main(cucumberArgs);
    }
}
