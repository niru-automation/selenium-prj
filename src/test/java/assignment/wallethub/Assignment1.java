package assignment.wallethub;

import static org.testng.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

public class Assignment1 extends FacebookUtil{

	@BeforeClass
	public void beforeTest() {
		tdFilename = "facebook.properties";
		String reportFilename = "facebook"+getTimestamp()+".html";
		report = new ExtentReports(System.getProperty("user.dir") + "\\reports\\facebook\\"+reportFilename);
		test = report.startTest("WriteReview");
		loadTestData();
	}

	@Test
	public void task() throws FileNotFoundException, IOException {
		
		try {
			launchBrowser(tdbrowser);	
		
			navigateToFacebookPage();
			
			login();
			
			postStatus();
			
			logout();
		
		}catch(AssertionError ae) {
			test.log(LogStatus.FAIL, "Script terminated due to Assertion Failure", ae +"<br /> "+test.addScreenCapture(captureScreenshot()));
		}catch(Exception e) {
			test.log(LogStatus.FAIL, "Script terminated due to unexpected", e+"<br /> "+test.addScreenCapture(captureScreenshot()));
		}
	}

	@AfterClass
	public void afterTest() {
		report.endTest(test);
		report.flush();
		if (driver!=null)
			driver.close();
	}
}

