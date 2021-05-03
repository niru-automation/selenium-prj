package assignment.wallethub;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

public class Assignment2 extends WallethubUtil {

	@BeforeClass
	public void beforeTest() {
		tdFilename = "wallethub.properties";
		String reportFilename = "WHWriteReview" + getTimestamp() + ".html";
		report = new ExtentReports(System.getProperty("user.dir") + "\\reports\\wallethub\\" + reportFilename);
		test = report.startTest("WriteReview");
		loadTestData();
	}

	@Test
	public void tc1_writeReview() {

		try {
			launchBrowser(tdbrowser);
			assertTrue(driver != null);

			navigateToProfileCompanyUrl();
			assertTrue(isElementDisplayed(getWebElement(page_ProfileCompany_Heading2)));

			verifyLoginStatusAndLoginIfRequired();
			assertTrue(isElementDisplayed(getWebElement(page_ProfileCompany_Heading2)));

			verifyColorChangeWhenHoveredOnRating();

			clickRating();
			assertTrue(isElementDisplayed("Page_ProfileCompanyReview_Policy"));

			writeReview();
			assertTrue(actualReviewConfirmationMessage.contains(tdreviewConfirmationMessage));

			navigateToUserProfileUrl();
			assertTrue(isElementDisplayed("Page_UserProfile_Reviews"));

			verifyIfReviewIsDisplayedInUserProfileActivity();

			navigateToUserProfileReviewsUrl();
			assertTrue(isElementDisplayed("Page_UserProfile_Reviews"));

			verifyIfReviewIsDisplayedInUserProfileReviews();
			assertTrue(actualReviewInUserProfileReview.equals(tdreviewMessage));
			
			navigateToUserProfileUrl();
			logout();
			assertTrue(isElementDisplayed("Page_UserProfile_Login"));		

		} catch (AssertionError ae) {
			test.log(LogStatus.FAIL, "Script terminated due to Assertion Failure",
					ae + "<br /> " + test.addScreenCapture(captureScreenshot()));
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Script terminated due to unexpected",
					e + "<br /> " + test.addScreenCapture(captureScreenshot()));
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
