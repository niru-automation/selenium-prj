package assignment.wallethub;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

public class WallethubUtil extends GenericUtil {

	public static String tdheaderClassNameToCheckIfLoggedIn = "";
	public static String tdloginPageTitle = "";
	public static String tdprofileUserName = "";
	public static String tdemailAddress = "";
	public static String tdpassword = "";
	public static String tdprofileCompanyTitle = "";
	public static String ratingIndex = "";
	public static String tdpolicyValue = "";
	public static String tdreviewMessage = "";
	public static String tdreviewConfirmationTitle = "";
	public static String tdreviewConfirmationMessage = "";
	public static String actualReviewConfirmationMessage = "";
	public static String actualReviewInUserProfileReview = "";
	public static int userAlreadyLoggedIn;
	public static String reviewXpath = "";
	public static String page_ProfileCompany_Heading2 = "";
	public static String page_ProfileCompanyReview_PolicyValue = "";
	public static String page_UserProfile_Reviews_ProfileCompanyReview = "";
	public static String page_UserProfile_Reviews_RemoveProfileCompanyReview = "";

	/************************************************************************
	 * @author Nirupama
	 * @purpose To fetch wallethub test data and store in public variables
	 ************************************************************************/
	public static void loadTestData() {
		try {
			getGenericTestdata(tdFilename);
			tdheaderClassNameToCheckIfLoggedIn = getTestData("headerClassNameToCheckIfLoggedIn", tdFilename);
			tdloginPageTitle = getTestData("loginPageTitle", tdFilename);
			tdprofileUserName = getTestData("profileUserName", tdFilename);
			tdemailAddress = getTestData("emailAddress", tdFilename);
			tdpassword = getTestData("password", tdFilename);
			tdprofileCompanyTitle = getTestData("profileCompanyTitle", tdFilename);
			tdpolicyValue = getTestData("policyValue", tdFilename);
			tdreviewMessage = getTestData("reviewMessage", tdFilename) + getTimestamp();
			tdreviewConfirmationTitle = getTestData("reviewConfirmationTitle", tdFilename);
			tdreviewConfirmationMessage = getTestData("reviewConfirmationMessage", tdFilename);
			page_ProfileCompany_Heading2="obj_xpath>.//h2[text()='"+getTestData("profileCompanyTitle", tdFilename)+"']";
			page_ProfileCompanyReview_PolicyValue="obj_xpath>//li[text()='"+getTestData("policyValue", tdFilename)+"']";
			page_UserProfile_Reviews_ProfileCompanyReview="obj_xpath>.//strong[text()='"+getTestData("profileCompanyName", tdFilename)+"']/ancestor::div[@class='profile profile-company-name']/following-sibling::p[2]";
			page_UserProfile_Reviews_RemoveProfileCompanyReview="obj_xpath>.//strong[text()='"+getTestData("profileCompanyName", tdFilename)+"']/ancestor::div[@class='profile profile-company-name']/following-sibling::div[@class='review-actions']/div/a[text()='Remove']";
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Load Test Data",
					"Unexpected error while fetching test data due to below exception <br /> " + e);
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To navigate to Profile Company Page (for e.g http://wallethub.com/profile/test_insurance_company/)
	 *          baser url is fetched from test data file (for e.g. http://wallethub.com/profile/)
	 *          page url is fetched from test data file (for e.g. test_insurance_company/)
	 ************************************************************************/
	public void navigateToProfileCompanyUrl() {
		String pcUrl = "";
		try {	
			pcUrl = getTestData("baseUrl", tdFilename) + getTestData("profileCompanyUrl", tdFilename);
			driver.get(pcUrl);
			waitUntilElementLocatedByXpath(page_ProfileCompany_Heading2);
			if (isElementDisplayed(page_ProfileCompany_Heading2))
				test.log(LogStatus.PASS, "Navigate to Profile Company URL", "Navigation Successful <br />" + pcUrl);
			else
				test.log(LogStatus.FAIL, "Navigate to Profile Company URL",
						"Navigation failed <br />" + pcUrl + "<br />" + test.addScreenCapture(captureScreenshot()));
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Navigate to Profile Company URL",
					pcUrl + "<br /> Failed due to below exception <br /> " + e + "<br /> "
							+ test.addScreenCapture(captureScreenshot()));
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To navigate to User Profile Page (for e.g. http://wallethub.com/profile/username/)	            
	 *          baser url is fetched from test data file (for e.g http://wallethub.com/profile/)
	 *          page url is fetched from test data file (for e.g. username/)
	 ************************************************************************/
	public void navigateToUserProfileUrl() {
		String upUrl = "";
		try {
			upUrl = getTestData("baseUrl", tdFilename) + getTestData("userProfileURL", tdFilename);
			driver.get(upUrl);
			waitUntilElementLocatedByXpath("Page_UserProfile_Reviews");
			if (isElementDisplayed("Page_UserProfile_Reviews")) 
				test.log(LogStatus.PASS, "Navigate to User Profile URL", "Navigation Successful <br />" + upUrl);
			else
				test.log(LogStatus.FAIL, "Navigate to User Profile URL",
							"Navigation failed <br />" + upUrl + "<br />" + test.addScreenCapture(captureScreenshot()));
		} catch (Exception e) {
				test.log(LogStatus.FAIL, "Navigate to User Profile URL",
						upUrl + "<br /> Failed due to below exception <br /> " + e + "<br /> "
								+ test.addScreenCapture(captureScreenshot()));
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To navigate to User Profile Reviews Page (for e.g. http://wallethub.com/profile/username/)	            
	 *          baser url is fetched from test data file (for e.g http://wallethub.com/profile/)
	 *          page url is fetched from test data file (for e.g. username/reviews/)
	 ************************************************************************/
	public void navigateToUserProfileReviewsUrl() {
		String uprUrl = "";
		try {
			uprUrl = getTestData("baseUrl", tdFilename) + getTestData("userProfileReviewsUrl", tdFilename);
			driver.get(uprUrl);
			waitUntilElementLocatedByXpath("Page_UserProfile_Reviews");
			if (isElementDisplayed("Page_UserProfile_Reviews")) {
				test.log(LogStatus.PASS, "Navigate to User Profile Reviews URL", "Navigation Successful <br />" + uprUrl);
				click("Page_UserProfile_Reviews");
			} else
				test.log(LogStatus.FAIL, "Navigate to User Profile Reviews URL",
						"Navigation failed <br />" + uprUrl + "<br />" + test.addScreenCapture(captureScreenshot()));
		} catch (Exception e) {
				test.log(LogStatus.FAIL, "Navigate to User Profile Reviews URL",
						uprUrl + "<br /> Failed due to below exception <br /> " + e + "<br /> "
								+ test.addScreenCapture(captureScreenshot()));
		}
	}

	
	/************************************************************************
	 * @author Nirupama
	 * @purpose To check login status and login if required
	 * 			1. Check if user is logged in
	 * 			2a. If user is logged proceed then proceed with below steps
	 * 				If other user is logged in then logout and login
	 * 				If the expected user is logged in then proceed with step3
	 * 			2b. If user is not logged in then login
	 * 			3. Check and remove if review is already added by the user for the profile company
	 ************************************************************************/
	public void verifyLoginStatusAndLoginIfRequired() throws FileNotFoundException, IOException {
		try {
			String headerClass = getWebElement("Page_ProfileCompany_Header").getAttribute("class");
			if (headerClass.equals(tdheaderClassNameToCheckIfLoggedIn)) {
				String actualUserName = getWebElement("Page_ProfileCompany_User").getText();
				/*String[] temp = actualUserName.split("/");
				actualUserName = temp[temp.length - 1];*/
				System.out.println("actualUserName: "+actualUserName);
				System.out.println("actualUserName: "+tdprofileUserName);
				if (actualUserName.equals(tdprofileUserName)) {
					test.log(LogStatus.INFO, "Login", tdprofileUserName + " already logged In");
				} else {
					Actions actions = new Actions(driver);
					Action userA = actions.moveToElement(getWebElement("Page_ProfileCompany_User")).build();
					userA.perform();
					getWebElement("Page_UserProfile_Logout").submit();
					waitUntilElementLocatedByXpath(page_ProfileCompany_Heading2);
					login();
				}
			} else {
				login();
			}
			checkAndRemoveExistingReview();
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Login as <br />"+tdemailAddress+"/"+tdpassword, 
					"Failed due to below exception <br />" + e + "<br />" 
					+ test.addScreenCapture(captureScreenshot()));
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To login
	 ************************************************************************/
	public void login() throws FileNotFoundException, IOException {
		click("Page_ProfileCompany_Login");
		wait.until(ExpectedConditions.titleContains(tdloginPageTitle));
		waitUntilElementLocatedByXpath("Page_Login_EmailAddress");
		enterData("Page_Login_EmailAddress", tdemailAddress);
		enterData("Page_Login_Password", tdpassword);
		click("Page_Login_Login");
		wait.until(ExpectedConditions.titleContains(tdprofileCompanyTitle));
		waitUntilElementLocatedByXpath(page_ProfileCompany_Heading2);
		if (isElementDisplayed(page_ProfileCompany_Heading2))
			test.log(LogStatus.PASS, "Login as <br />"+tdemailAddress+"/"+tdpassword, "Logged in successfully");
		else
			test.log(LogStatus.FAIL, "Login as <br />"+tdemailAddress+"/"+tdpassword, 
					"Login failed <br />"
					+ test.addScreenCapture(captureScreenshot()));
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To Check and remove if review is already added by the user for the profile company
	 * 			1. Check if review is already added
	 * 			2. If review is added then proceed with below steps
	 * 				Navigate to User profile reviews page
	 * 				Remove the review
	 * 				Check if review is removed
	 ************************************************************************/
	public void checkAndRemoveExistingReview() throws FileNotFoundException, IOException {
		try {
			if (isElementDisplayed("Page_profileCompany_YourReview")) {
				navigateToUserProfileReviewsUrl();
				click("Page_UserProfile_Reviews");
				click(page_UserProfile_Reviews_RemoveProfileCompanyReview);
				Alert alert = driver.switchTo().alert();
				alert.accept();
				//wait = new WebDriverWait(driver, Long.parseLong(tddefaultDynamicWaitTime));
				//wait.until(ExpectedConditions.invisibilityOf(getWebElement(page_UserProfile_Reviews_RemoveProfileCompanyReview)));
				List<WebElement> cEle = driver.findElements(By.xpath(getXpath(page_UserProfile_Reviews_RemoveProfileCompanyReview)));
				for (int c=0; c<=30;c++) {
					cEle = driver.findElements(By.xpath(getXpath(page_UserProfile_Reviews_RemoveProfileCompanyReview)));
					if (cEle.size()==0) {
						break;
					}
					Thread.sleep(1000);
				}
				driver.navigate().refresh();
				waitUntilElementLocatedByXpath("Page_UserProfile_Reviews");
				click("Page_UserProfile_Reviews");
				if (isElementDisplayed(page_UserProfile_Reviews_RemoveProfileCompanyReview)) {
					test.log(LogStatus.FAIL, "Check and remove existing review",
							"Review not removed <br />"	+ test.addScreenCapture(captureScreenshot()));
				} else {
					test.log(LogStatus.PASS, "Check and remove existing review", "Review removed successfully");
					navigateToProfileCompanyUrl();
				}
			} else
				test.log(LogStatus.INFO, "Check and remove existing review",
						"Review doesnot exist hence proceeding to write review");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Check and remove existing review", "Failed due to exception <br />" + e
					+ "<br /> " + test.addScreenCapture(captureScreenshot()));
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To Check if color is changed when mouse over on the rating
	 * 			1. Perform mouse over on the rating star
	 * 			2. Check if color is changed
	 ************************************************************************/
	public void verifyColorChangeWhenHoveredOnRating() throws InterruptedException, FileNotFoundException, IOException {
		String ratingMOver = getTestData("ratingToMouseOver", tdFilename);
		String getLoc = getTestData("Page_ProfileCompany_Rating_gPath", tdFilename);
		String[] getLocs = getLoc.split(">");
		String updatedXpath = getLocs[1] + "[" + ratingMOver	+ "]/*[name()='g']/*[name()='path']";
		WebElement ele = driver.findElement(By.xpath(updatedXpath));
		Point sLocationXY = ele.getLocation();
		((JavascriptExecutor) driver).executeScript("javascript:window.focus();");
		((JavascriptExecutor) driver).executeScript("javascript:window.scrollTo " + sLocationXY);

		Actions actions = new Actions(driver);
		Action mouseOver = actions.moveToElement(ele).build();
		mouseOver.perform();

		List<WebElement> ele1 = driver.findElements(By.xpath(updatedXpath));
		int sizeAfter = ele1.size();
		System.out.println(sizeAfter);

		if (sizeAfter > 1)
			test.log(LogStatus.PASS, "Mouse Hover on Rating "+ratingMOver, "Color Changed Successfully when hovered");
		else
			test.log(LogStatus.FAIL, "Mouse Hover on Rating "+ratingMOver, "Color not changed when hovered <br />"
					+ test.addScreenCapture(captureScreenshot()));
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To Check click on the rating
	 ************************************************************************/
	public void clickRating() throws FileNotFoundException, IOException {
		String rating = "";
		try {
			rating = getTestData("reviewRating", tdFilename);
			String getLoc = getTestData("Page_ProfileCompany_Rating", tdFilename);
			String[] getLocs = getLoc.split(">");
			String updatedXpath = getLocs[1] + "[" + rating + "]";
			driver.findElement(By.xpath(updatedXpath)).click();
			waitUntilElementLocatedByXpath("Page_ProfileCompanyReview_Policy");
			if (isElementDisplayed("Page_ProfileCompanyReview_Policy"))
				test.log(LogStatus.PASS, "Click Rating "+rating+" to write review", 
						"Successfully redirected to review page");
			else
				test.log(LogStatus.FAIL, "Click Rating "+rating+" to write review",
						"Not redirected to review page <br />"+ test.addScreenCapture(captureScreenshot()));
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Click Rating "+rating+" to write review", 
					"Failed due to exception" + e+ "<br />" + test.addScreenCapture(captureScreenshot()));
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To write review
	 * 			1. Check if review text has more than 200 characters
	 * 			2a. If <200 characters then log failure
	 * 			2b. If >=200 then enter required data and submit the review
	 ************************************************************************/
	public void writeReview() throws FileNotFoundException, IOException {
		try {			
			if (tdreviewMessage.length() >= 200) {
				click("Page_ProfileCompanyReview_Policy");
				waitUntilElementLocatedByXpath(page_ProfileCompanyReview_PolicyValue);
				click(page_ProfileCompanyReview_PolicyValue);
				enterData("Page_ProfileCompanyReview_WriteReview", tdreviewMessage);
				click("Page_ProfileCompanyReview_SubmitReview");
				wait.until(ExpectedConditions.titleContains(tdreviewConfirmationTitle));
				waitUntilElementLocatedByXpath("Page_ReviewConfirmation_Header");
				actualReviewConfirmationMessage = getWebElement("Page_ReviewConfirmation_Header").getText();
				if (actualReviewConfirmationMessage.contains(tdreviewConfirmationMessage))
					test.log(LogStatus.PASS, "Write Review", "Review submitted successfully");
				else {
					test.log(LogStatus.FAIL, "Write Review",
							"Review submission failed <br /> ActualConfirmationMessage: "
									+ actualReviewConfirmationMessage + " <br /> ExpectedConfirmationMessage: "
									+ tdreviewConfirmationMessage + "<br /> ScreenshotLink--->"
									+ test.addScreenCapture(captureScreenshot()));
				}
			} else {
				test.log(LogStatus.FAIL, "Write Review", "Review Text should be more than 200 characters");
			}
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Write Review", "Failed due to below exception" + e + "<br />"
					+ test.addScreenCapture(captureScreenshot()));
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To check review added is displayed user profile feed
	 ************************************************************************/
	public void verifyIfReviewIsDisplayedInUserProfileActivity() throws FileNotFoundException, IOException {
		String messageReview = getWebElement("Page_UserProfile_Reviews_ProfileCompanyReviewFirstActivity").getText();
		System.out.println("Actual: " + messageReview);
		System.out.println("Expected: " + tdreviewMessage);
		if (messageReview.toString().equals(tdreviewMessage)) {
			test.log(LogStatus.PASS, "Verification of Review in User Profile Activity",
					"Review displayed successfully");
		} else {
			test.log(LogStatus.FAIL, "Verification of Review in User Profile Activity",
					"Review not displayed  <br />" + test.addScreenCapture(captureScreenshot()));
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To check review added is displayed user profile reviews
	 ************************************************************************/
	public void verifyIfReviewIsDisplayedInUserProfileReviews() throws FileNotFoundException, IOException {
		waitUntilElementLocatedByXpath(page_UserProfile_Reviews_ProfileCompanyReview);
		if (isElementDisplayed(page_UserProfile_Reviews_ProfileCompanyReview)) {
			test.log(LogStatus.PASS, "Verification of Review in User Profile page", "Review displayed successfully");
			actualReviewInUserProfileReview = getWebElement(page_UserProfile_Reviews_ProfileCompanyReview).getText();
		} else {
			test.log(LogStatus.FAIL, "Verification of Review in User Profile page",
					"Review not displayed <br />" + test.addScreenCapture(captureScreenshot()));
		}
	}
	
	public void logout() throws FileNotFoundException, IOException {
		clickJs("Page_UserProfile_UserLink");
		getWebElement("Page_UserProfile_Logout").submit();
		/*Actions actions = new Actions(driver);
		Action mouseOver = actions.moveToElement(getWebElement("Page_ProfileCompany_logout")).build();
		mouseOver.perform();*/
		//clickJs("Page_ProfileCompany_logout");
		//getWebElement("Page_ProfileCompany_logout").submit();
		waitUntilElementLocatedByXpath("Page_UserProfile_Login");
		if (isElementDisplayed("Page_UserProfile_Login"))
			test.log(LogStatus.PASS, "Logout", "Logged out successfully");
		else
			test.log(LogStatus.FAIL, "Logout",
					"Failed <br />" + test.addScreenCapture(captureScreenshot()));
	}
}

