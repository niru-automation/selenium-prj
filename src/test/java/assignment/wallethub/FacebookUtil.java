package assignment.wallethub;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.relevantcodes.extentreports.LogStatus;

public class FacebookUtil extends GenericUtil {

	public static String tdemailOrPhone = "";
	public static String tdpassword = "";

	/************************************************************************
	 * @author Nirupama
	 * @purpose To fetch facebook test data and store in public variables
	 ************************************************************************/
	public static void loadTestData() {
		try {
			getGenericTestdata(tdFilename);
			tdemailOrPhone = getTestData("emailOrPhone", tdFilename);
			tdpassword = getTestData("password", tdFilename);
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Load Test Data",
					"Unexpected error while fetching test data due to exception " + e);
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To navigate to facebook page
	 ************************************************************************/
	public void navigateToFacebookPage() {
		try {
			driver.get(getTestData("baseUrl", tdFilename));
			waitUntilElementLocatedByXpath("Page_Facebook_Login_EmailOrPhone");
			if (isElementDisplayed("Page_Facebook_Login_EmailOrPhone"))
				test.log(LogStatus.PASS, "Navigate to Facebook page", "Navigation Successful");
			else
				test.log(LogStatus.FAIL, "Navigate to Facebook page",
						"Navigation failed <br />" + test.addScreenCapture(captureScreenshot()));
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Navigate to Facebook page",
					"Failed due to exception " + e + "<br /> " + test.addScreenCapture(captureScreenshot()));
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To login to facebook page
	 ************************************************************************/
	public void login() throws FileNotFoundException, IOException {
		enterData("Page_Facebook_Login_EmailOrPhone", tdemailOrPhone);
		enterData("Page_Facebook_Login_Password", tdpassword);
		click("Page_Facebook_Login_LoginButton");
		waitUntilElementLocatedByXpath("Page_Facebook_Home_CreatePost_EnterText");
		if (isElementDisplayed("Page_Facebook_Home_CreatePost_EnterText"))
			test.log(LogStatus.PASS, "Login", "Logged in successfully");
		else
			test.log(LogStatus.FAIL, "Login", "Failed to login <br /> " + test.addScreenCapture(captureScreenshot()));
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To post status 
	 * 			1. Enter status message 
	 * 			2. Select privacy as Only Me
	 *          3. Post Message 
	 *          4. Verify if message is posted
	 ************************************************************************/
	public void postStatus() throws FileNotFoundException, IOException {
		List<WebElement> posts = driver.findElements(By.xpath(getXpath("Page_Facebook_Home_Feed_Stream")));
		int sizeBefore = posts.size();
		String statusMessage = "Hello World " + getTimestamp();
		enterData("Page_Facebook_Home_CreatePost_EnterText", statusMessage);
		waitUntilElementLocatedByXpath("Page_Facebook_Home_CreatePost_SelectPrivacy");
		String privacyValue = getWebElement("Page_Facebook_Home_CreatePost_SelectPrivacy")
				.getAttribute("data-tooltip-content");
		System.out.println(privacyValue);
		if (privacyValue.equals("Public") || privacyValue.equals("Your friends")
				|| privacyValue.contains("Friends; Except:")) {
			clickJs("Page_Facebook_Home_CreatePost_SelectPrivacy");
			waitUntilElementClickableByXpath("Page_Facebook_Home_CreatePost_More");
			clickJs("Page_Facebook_Home_CreatePost_More");
			waitUntilElementClickableByXpath("Page_Facebook_Home_CreatePost_OnlyMe");
			clickJs("Page_Facebook_Home_CreatePost_OnlyMe");
			for (int j = 0; j <= 60; j++) {
				if (getWebElement("Page_Facebook_Home_CreatePost_SelectedPrivacy").getText().equals("Only me"))
					break;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (getWebElement("Page_Facebook_Home_CreatePost_SelectedPrivacy").getText().equals("Only me")) {
				test.log(LogStatus.PASS, "Select Privacy", "Only me privacy is selcted");
			} else
				test.log(LogStatus.FAIL, "Select Privacy",
						"Only me privacy is not selcted <br /> " + test.addScreenCapture(captureScreenshot()));
		}
		clickJs("Page_Facebook_Home_CreatePost_Post");
		waitUntilElementLocatedByXpath("Page_Facebook_Home_Feed_Stream");
		int sizeAfter = 0;
		for (int i = 0; i <= 60; i++) {
			posts = driver.findElements(By.xpath(getXpath("Page_Facebook_Home_Feed_Stream")));
			sizeAfter = posts.size();
			if (sizeAfter > sizeBefore)
				break;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("sizeAfter: " + sizeAfter);
		System.out.println("sizeBefore: " + sizeBefore);
		if (sizeAfter > sizeBefore) {
			String actualStatusText = posts.get(0).getText();
			System.out.println("actualStatusText: " + actualStatusText);
			if (actualStatusText.contains(statusMessage))
				test.log(LogStatus.PASS, "Post Status", "Status posted and displayed in feed");
			else
				test.log(LogStatus.FAIL, "Post Status",
						"Status posted but not displayed in feed <br /> " + test.addScreenCapture(captureScreenshot()));
		} else
			test.log(LogStatus.FAIL, "Post Status",
					"Status not posted <br /> " + test.addScreenCapture(captureScreenshot()));
	}

	/************************************************************************
	* @author Nirupama
	* @purpose
	* 		To logout
	************************************************************************/
	public void logout() throws FileNotFoundException, IOException {
		clickJs("Page_Facebook_Home_AcoountSettings");
		waitUntilElementLocatedByXpath("Page_Facebook_Home_Logout");
		clickJs("Page_Facebook_Home_Logout");
		waitUntilElementLocatedByXpath("Page_Facebook_Login_EmailOrPhone");
		if (isElementDisplayed("Page_Facebook_Login_EmailOrPhone"))
			test.log(LogStatus.PASS, "Logout", "Logged out Successful");
		else
			test.log(LogStatus.FAIL, "Logout", "Failed <br />" + test.addScreenCapture(captureScreenshot()));
	}

}
