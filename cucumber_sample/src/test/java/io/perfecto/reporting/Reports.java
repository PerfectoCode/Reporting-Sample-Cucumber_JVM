package io.perfecto.reporting;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResult;
import com.perfecto.reportium.test.result.TestResultFactory;

import io.perfecto.utils.Logger;
import io.perfecto.utils.Logger.STATUS;

public class Reports {

	private PerfectoExecutionContext execContext;
	private ReportiumClient reportiumClient;

	public Reports(SearchContext driver) {

		if (driver != null) {

			try {
				ReportiumConstant reportConstants = ReportiumConstant.getReportiumConstant();

				Project project = new Project(reportConstants.getProjectName(), reportConstants.getProjectVersion());

				Job job = new Job(reportConstants.getJobName(), Integer.parseInt(reportConstants.getJobVersion()));

				this.execContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder().withProject(project)
						.withJob(job).withWebDriver((WebDriver) driver).build();

				this.reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(execContext);

			} catch (Exception e) {
				Logger.log(STATUS.FAIL, e.getMessage());
			}
		}
	}

	/*
	 * startTest method starts the test in Perfecto reporting.
	 */
	public void startTest(String testName) {
		
		Logger.log(STATUS.INFO, ">>>>>> Perfecto Digital Zoom test case started");
		if (this.reportiumClient != null) {

			@SuppressWarnings("rawtypes")
			TestContext testContext = new TestContext.Builder().build();
			this.reportiumClient.testStart(testName, testContext);
		}
	}

	/*
	 * endTest method ends the test in Perfecto reporting.
	 */
	public void endTest(boolean isPass, Throwable... errorMessage) {

		Logger.log(STATUS.INFO, ">>>>>> Perfecto Digital Zoom test case ended");
		
		if (this.reportiumClient != null) {
			if (isPass) {
				this.reportiumClient.testStop(TestResultFactory.createSuccess());
			} else {
				TestResult testResult = TestResultFactory.createFailure("No Failure Reason provided - Framework limitation");
				this.reportiumClient.testStop(testResult);
			}
		}

	}

	/*
	 * startStep method starts test step in Perfecto reporting.
	 */
	public void startStep(String stepDescription) {
		Logger.log(STATUS.INFO, ">>>>>> Perfecto Digital Zoom test step started");
		if (this.reportiumClient != null) {
			this.reportiumClient.stepStart(stepDescription);
		}
	}

	/*
	 * endStep method ends test step in Perfecto reporting.
	 */
	public void endStep() {
		Logger.log(STATUS.INFO, ">>>>>> Perfecto Digital Zoom test step ended");
		if (this.reportiumClient != null) {
			this.reportiumClient.stepEnd();
		}
	}

}
