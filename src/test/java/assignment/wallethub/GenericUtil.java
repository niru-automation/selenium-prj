package assignment.wallethub;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class GenericUtil {

	public static ExtentReports report;
	public static ExtentTest test;

	public static WebDriver driver;
	public static WebDriverWait wait;
	public static String tddefaultDynamicWaitTime = "";
	public static String tdbrowser = "ff";
	public static String tdFilename = "";

	/************************************************************************
	 * @author Nirupama
	 * @purpose To get genreic test data like browser/default wait time/... from
	 *          properties file(Test data file) Test data file is located at
	 *          <Project directory>/testdata
	 * @param filepath
	 *            Complete filepath of the properties file
	 * @param key
	 *            Key for which value has to fetched
	 * @return Return value of key
	 ************************************************************************/
	public static void getGenericTestdata(String filename) throws FileNotFoundException, IOException {
		tdbrowser = getTestData("browser", tdFilename);
		tddefaultDynamicWaitTime = getTestData("defaultDynamicWaitTime", tdFilename);
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To get value of a key from properties file(Test data file) Test data
	 *          file is located at <Project directory>/testdata
	 * @param filepath
	 *            Complete filepath of the properties file
	 * @param key
	 *            Key for which value has to fetched
	 * @return Return value of key
	 ************************************************************************/
	public static String getTestData(String key, String filename) throws FileNotFoundException, IOException {
		String sFileSeparator = System.getProperty("file.separator");
		String path = System.getProperty("user.dir");
		path = path + sFileSeparator + "testdata" + sFileSeparator + filename;
		return getValueFromPropertiesFile(path, key);
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To get value of a key from properties file
	 * @param filepath
	 *            Complete filepath of the properties file
	 * @param key
	 *            Key for which value has to fetched *
	 * @return Return value of key
	 ************************************************************************/
	public static String getValueFromPropertiesFile(String filePath, String key)
			throws FileNotFoundException, IOException {
		Properties props = new Properties();
		props.load(new FileInputStream(filePath));
		return props.getProperty(key);
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To get current time stamp in yyyyMMddHHmmss format
	 * @return Return current timestamp in yyyyMMddHHmmss format
	 ************************************************************************/
	public static String getTimestamp() {
		String ts = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		return ts;
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose Capture screenshot of the desktop Screenshot is store at location
	 *          <Project directory>/reports/wallethub/screenshots filename is
	 *          screenshot+<current time stamp in yyyyMMddHHmmss format
	 * @return Return complete screenshot path
	 ************************************************************************/
	public static String captureScreenshot() {
		String filePath = System.getProperty("user.dir") + "\\reports\\screenshots\\screenshot" + getTimestamp()
				+ ".png";
		try {
			Robot robot = new Robot();

			// Capture the screen shot of the area of the screen defined by the rectangle
			BufferedImage bi = robot.createScreenCapture(new Rectangle(1690, 1060));
			ImageIO.write(bi, "png", new File(filePath));

		} catch (AWTException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To get WebElement object using locator and locator value If locator
	 *          is xpath then WebElement object is derived using By.xpath If locator
	 *          is class then WebElement object is derived using By.class If locator
	 *          is id then WebElement object is derived using By.id
	 * @param element
	 *            Element with combination of locator and value separated by >
	 *            should be passed as parameter for.e.g
	 *            xpath>.//div[@class='review-rating']
	 * @return return derived WebElement object
	 ************************************************************************/
	public static WebElement getWebElement(String element) throws FileNotFoundException, IOException {
		String ele = element;
		if (!element.startsWith("obj_"))
			ele = getTestData(element, tdFilename);
		String[] eles = ele.split(">");
		String locatorType = eles[0].replace("obj_", "");
		String locatorValue = eles[1];
		if (locatorType.equalsIgnoreCase("xpath"))
			return driver.findElement(By.xpath(locatorValue));
		else if (locatorType.equalsIgnoreCase("class"))
			return driver.findElement(By.className(locatorValue));
		else if (locatorType.equalsIgnoreCase("id"))
			return driver.findElement(By.id(locatorValue));
		else {
			test.log(LogStatus.FAIL, "Get WebElement object for the " + element,
					"Please provide correct locator and locator value");
			return null;
		}
	}
	
	public static WebElement getWebElementWithoutGetTestData(String element) throws FileNotFoundException, IOException {
		String[] eles = element.split(">");
		String locatorType = eles[0];
		String locatorValue = eles[1];
		if (locatorType.equalsIgnoreCase("xpath"))
			return driver.findElement(By.xpath(locatorValue));
		else if (locatorType.equalsIgnoreCase("class"))
			return driver.findElement(By.className(locatorValue));
		else if (locatorType.equalsIgnoreCase("id"))
			return driver.findElement(By.id(locatorValue));
		else
			return null;
	}

	/************************************************************************
	 * @author Nirupama
	 * @param element
	 *            Element with combination of locator and value separated by >
	 *            should be passed as parameter for.e.g
	 *            xpath>.//div[@class='review-rating']
	 * @return returns locator value for e.g if paramrter is
	 *         xpath>.//div[@class='review-rating'] Method returns
	 *         .//div[@class='review-rating']
	 ************************************************************************/
	public static String getXpath(String element) {
		String ele = element;
		try {
			if (!element.startsWith("obj_"))
				ele = getTestData(element, tdFilename);
			String[] eles = ele.split(">");
			return eles[1];
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Get xpath of element " + element + " from test data file",
					"Failed due to below exception <br /> " + e);
			return "";
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @param element
	 *            Element with combination of locator and value separated by >
	 *            should be passed as parameter for.e.g id>review-rating
	 * @return returns locator value for e.g if paramrter is id>review-rating Method
	 *         returns review-rating
	 ************************************************************************/
	public static String getId(String element) {
		String ele = element;
		try {			
			if (!element.startsWith("obj_"))
				ele = getTestData(element, tdFilename);
			String[] eles = ele.split(">");
			return eles[1];
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Get xpath of element " + element + " from test data file",
					"Failed due to below exception <br /> " + e);
			return "";
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose Click on the element If unexpected error occurred while clicking on
	 *          the element log failure in test report
	 * @param element
	 *            Element with combination of locator and value separated by >
	 *            should be passed as parameter for.e.g
	 *            xpath>.//div[@class='review-rating']
	 ************************************************************************/
	public static void click(String element) {
		try {
			getWebElement(element).click();
			test.log(LogStatus.PASS, "Click Element ", getXpath(element)+" clicked successfully");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Click Element ",
					getXpath(element)+" Click failed due to below exception <br /> " + e + " <br />"
							+ test.addScreenCapture(captureScreenshot()));
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose Click on the element using Javascript executor If unexpected error
	 *          occurred while clicking on the element log failure in test report
	 * @param element
	 *            Element with combination of locator and value separated by >
	 *            should be passed as parameter for.e.g
	 *            xpath>.//div[@class='review-rating']
	 ************************************************************************/
	public static void clickJs(String element) {
		try {
			WebElement ele = getWebElement(element);
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			Point sLocationXY = ele.getLocation();
			((JavascriptExecutor) driver).executeScript("javascript:window.focus();");
			((JavascriptExecutor) driver).executeScript("javascript:window.scrollTo " + sLocationXY);
			executor.executeScript("arguments[0].click();", getWebElement(element));
			test.log(LogStatus.PASS, "Click Element ", getXpath(element)+" clicked successfully");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Click Element ",
					getXpath(element)+" Click failed due to below exception <br /> " + e + " <br />"
							+ test.addScreenCapture(captureScreenshot()));
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose Enter data in element/field If unexpected error occurred while
	 *          performing the sendkeys action log failure in test report
	 * @param element
	 *            Element with combination of locator and value separated by >
	 *            should be passed as parameter for.e.g
	 *            xpath>.//div[@class='review-rating']
	 * @param value
	 *            Value that needs to be set in the element/field
	 ************************************************************************/
	public static void enterData(String element, String value) {
		try {
			getWebElement(element).sendKeys(value);
			test.log(LogStatus.PASS, "Enter data ", value + " Data entered successfully in field <br />"+getXpath(element));
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Enter data ", value + " Data not entered in field <br />"+getXpath(element)
					+ " <br /> " + e + "<br /> " + test.addScreenCapture(captureScreenshot()));
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To check id element is displayed
	 * @param element
	 *            Element with combination of locator and value separated by >
	 *            should be passed as parameter for.e.g
	 *            xpath>.//div[@class='review-rating']
	 * @return return true if element is displayed else return false
	 ************************************************************************/
	public static boolean isElementDisplayed(String element) throws FileNotFoundException, IOException {
		try {
			return getWebElement(element).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isElementDisplayed(WebElement element) throws FileNotFoundException, IOException {
		try {
			return element.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To wait until visibility of element is located Default time is set
	 *          in variable tddefaultDynamicWaitTime (tddefaultDynamicWaitTime is
	 *          fetched in getGenericTestdata or you can directly set value before
	 *          calling this method)
	 * @param element
	 *            Element with combination of id and value separated by > should be
	 *            passed as parameter for.e.g id>review-rating
	 ***********************************************************************/
	public void waitUntilElementLocatedById(String element) {
		wait = new WebDriverWait(driver, Long.parseLong(tddefaultDynamicWaitTime));
		String id = null;
		try {
			id = getId(element);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
			if (!getWebElement(element).isDisplayed())
				test.log(LogStatus.FAIL, "Wait for element to be located",
						getId(element)+"<br /> Element not displayed <br />" + test.addScreenCapture(captureScreenshot()));
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Wait for element to be located",
					getId(element)+"<br /> Element not displayed due to below exception <br /> " + e + "<br />"
							+ test.addScreenCapture(captureScreenshot()));
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To wait until visibility of element is located Default time is set
	 *          in variable tddefaultDynamicWaitTime (tddefaultDynamicWaitTime is
	 *          fetched in getGenericTestdata or you can directly set value before
	 *          calling this method)
	 * @param element
	 *            Element with combination of xpath and value separated by > should
	 *            be passed as parameter for.e.g
	 *            xpath>.//div[@class='review-rating']
	 ***********************************************************************/
	public void waitUntilElementLocatedByXpath(String element) {
		wait = new WebDriverWait(driver, Long.parseLong(tddefaultDynamicWaitTime));
		String xpath = null;
		try {
			xpath = getXpath(element);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			if (!getWebElement(element).isDisplayed())
				test.log(LogStatus.FAIL, "Wait for element to be located",
						 getXpath(element)+" <br /> Element not displayed <br />" + test.addScreenCapture(captureScreenshot()));
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Wait for element to be located",
					 getXpath(element)+"<br /> Element not displayed due to below exception <br /> " + e + "<br />"
							+ test.addScreenCapture(captureScreenshot()));
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To wait until element is clickable Default time is set in variable
	 *          tddefaultDynamicWaitTime (tddefaultDynamicWaitTime is fetched in
	 *          getGenericTestdata or you can directly set value before calling this
	 *          method)
	 * @param element
	 *            Element with combination of xpath and value separated by > should
	 *            be passed as parameter for.e.g
	 *            xpath>.//div[@class='review-rating']
	 ***********************************************************************/
	public void waitUntilElementClickableByXpath(String element) {
		wait = new WebDriverWait(driver, Long.parseLong(tddefaultDynamicWaitTime));
		String xpath = null;
		try {
			xpath = getXpath(element);
			wait.until(ExpectedConditions.elementToBeClickable((By.xpath(getXpath(element)))));
			if (!getWebElement(element).isDisplayed())
				test.log(LogStatus.FAIL, "Wait for element to be clickable",
						getXpath(element)+"<br /> Element not displayed <br />" + test.addScreenCapture(captureScreenshot()));
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Wait for element to be clickable",
					getXpath(element)+"<br /> Element not displayed due to below exception <br /> " + e + "<br />"
							+ test.addScreenCapture(captureScreenshot()));
		}
	}

	/************************************************************************
	 * @author Nirupama
	 * @purpose To launch browser
	 * @param browserName
	 *            Supports Firefox, Chrome and IE Accepts value
	 *            ff/firefox/chrome/ie/internet explorer
	 ************************************************************************/
	public static void launchBrowser(String browserName) {
		try {
			if (browserName.equalsIgnoreCase("FF") || browserName.equalsIgnoreCase("Firefox")) {
				System.setProperty("webdriver.gecko.driver",
						System.getProperty("user.dir") + "\\libs\\geckodriver.exe");
				FirefoxOptions opts = new FirefoxOptions();
				opts.setProfile(new FirefoxProfile());
				opts.addPreference("dom.webnotifications.enabled", false);
				opts.addArguments("-private");
				driver = new FirefoxDriver(opts);
			} else if (browserName.equalsIgnoreCase("chrome")) {
				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + "\\libs\\chromedriver.exe");
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--incognito");
				options.addArguments("--disable-notifications");
				DesiredCapabilities capabilities = DesiredCapabilities.chrome();
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
				capabilities.setCapability("chrome.switches", Arrays.asList("--incognito"));
				driver = new ChromeDriver(capabilities);
			} else if (browserName.equalsIgnoreCase("IE") || browserName.equalsIgnoreCase("Internet Explorer")) {
				System.setProperty("webdriver.ie.driver",
						System.getProperty("user.dir") + "\\libs\\IEDriverServer.exe");
				driver = new InternetExplorerDriver();
			} else {
				test.log(LogStatus.FAIL, "Launch Browser",
						"Browser not launched as the test data provided is not FF/Firefox/Chrome  <br /> browser value provided is "
								+ browserName);
			}
			if (driver == null)
				test.log(LogStatus.FAIL, "Launch Browser",
						"Browser not launched as driver is null <br />" + test.addScreenCapture(captureScreenshot()));
			else {
				driver.manage().window().maximize();
				test.log(LogStatus.PASS, "Launch Browser", browserName + " launched successfully");
			}
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Launch Browser", "Browser not launched due to below exception <br /> " + e
					+ "<br /> " + test.addScreenCapture(captureScreenshot()));
		}
	}
}
