package DDFpack.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import DDFpack.util.MyXLSReader;

public class BaseTest {
	
	public WebDriver driver = null;
	public ExtentReports eReport;
    public	ExtentTest eTest;
	public MyXLSReader xlsx ;
	public Properties prop=null;
	
	
	public void initialize() {
		
		
		if(prop==null) {
		 prop = new Properties();
		
		File propFile = new File("src//test//resources//projectconfig.properties");
		
		try {
		
		FileInputStream fis = new FileInputStream(propFile);
		
		prop.load(fis);
		
		   }catch(Exception e) {
		
		     e.printStackTrace();
		
	       }
	
	
	   }
	
	}
	
	public void openBrowser(String browserType) {
		
	
		
		eTest.log(LogStatus.INFO, "Browser Got Opened"+browserType);
		
		if(browserType.equalsIgnoreCase("Chrome")) {
			
			System.setProperty("webdriver.chrome.driver", prop.getProperty("chromeDriverPath"));
			
			 driver = new ChromeDriver();
			
			
		}else if(browserType.equalsIgnoreCase("Firefox")) {
			
			
			System.setProperty("webdriver.gecko.driver", prop.getProperty("firefoxDriverPath"));
			
			 driver = new FirefoxDriver();
			
		}else if(browserType.equalsIgnoreCase("ie")) {
			
			System.setProperty("webdriver.ie.driver", prop.getProperty("ieDriverPath"));
			
			 driver = new InternetExplorerDriver();
			
		}
		
		driver.manage().window().maximize(); 
		
		eTest.log(LogStatus.INFO, "Browser got maximized");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	
	public void navigate(String urlKey) {
		
		String URL= prop.getProperty(urlKey);
		driver.get(URL);
		
		eTest.log(LogStatus.INFO, "Application url got opened");
		
	}
	
	public boolean doLogin(String username, String password) {
		
		//click on the login link
		click("LoginLink_className"); 
		type("EmailTexBox_id",username);
		click("NextButton_xpath");
		type("PasswordTexBox_id",password);
		click("SignInButton_id");
		if (IsElementPresent("CrmOption_cssselector")) {
			
			return true;
		
		}else {
			
			return false;
		}
		
		
	}

	public void click(String LocatorKey) {
		
	   WebElement element= getElement(LocatorKey);
	
	    element.click();
	    
	    eTest.log(LogStatus.INFO, LocatorKey+"clicked");
		
	}
	
	public void type(String LocatorKey, String text) {
		
	     WebElement element = getElement(LocatorKey);
	     element.sendKeys(text);
	     
	     eTest.log(LogStatus.INFO, text+"got typed into the"+LocatorKey);
	}
	
	public boolean IsElementPresent(String LocatorKey) {
		
		WebElement element =getElement(LocatorKey);
		
		boolean elementDisplayedStatus =element.isDisplayed();	
		
		return elementDisplayedStatus;
		
	}
	
	public WebElement getElement(String LocatorKey) {
		
		WebElement element = null;
		
		String LocatorValue = prop.getProperty(LocatorKey);
		
		try {
		
		if(LocatorKey.endsWith("_id")) {
		
		 element = driver.findElement(By.id(LocatorValue));
		
		  }else if(LocatorKey.endsWith("_name")) {
			
			  element = driver.findElement(By.name(LocatorValue));
			  
		}else if(LocatorKey.endsWith("_className")) {
			
			 element = driver.findElement(By.className(LocatorValue));
			
		}else if (LocatorKey.endsWith("_linktext")) {
			
			 element = driver.findElement(By.linkText(LocatorValue));
			 
		}else if(LocatorKey.endsWith("_cssselector")) {
			
			 element = driver.findElement(By.cssSelector(LocatorValue));
			 
		}else if(LocatorKey.endsWith("_xpath")) {
			
			 element = driver.findElement(By.xpath(LocatorValue));
			
		}
		
		}catch(Throwable t){
			
			reportFail(LocatorKey+"holding the"+LocatorValue+"is not findable");
			
		}
		return element;
		
	}
	
	public void reportPass(String message) {
		
		eTest.log(LogStatus.PASS, message);
	}
	
	public void reportFail(String message) {
		
		
		eTest.log(LogStatus.FAIL,message);
		takeScreenshot();
		Assert.fail(message);
	}
	
	public void takeScreenshot() {
		
		Date d=new Date();
		String screenshotFile=d.toString().replace(":", "_").replace(" ", "_")+".png";
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File("screenshots//"+screenshotFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//put screenshot file in reports
		eTest.log(LogStatus.INFO,"Screenshot-> "+ eTest.addScreenCapture(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
	}
	
}
