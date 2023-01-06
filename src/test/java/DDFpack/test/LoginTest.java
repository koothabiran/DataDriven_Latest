package DDFpack.test;

import java.util.HashMap;


import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import com.relevantcodes.extentreports.LogStatus;

import DDFpack.base.BaseTest;
import DDFpack.util.DataUtil;
import DDFpack.util.ExtentManager;
import DDFpack.util.MyXLSReader;

public class LoginTest extends BaseTest {
	
	
	
	@BeforeClass
	public void init() {
		
		initialize();
		
	}
	
	@DataProvider
	public Object[][] getData() {
		
		Object[][] obj=null;
		
		//get the data from the excell file
		
		try {
		
		 xlsx = new MyXLSReader(prop.getProperty("xlsxFilePath"));
		
		 obj =DataUtil.getTestData(xlsx, "LoginTest", "Data");
		
	}catch(Exception e) {
		
		e.printStackTrace();
		
	  }

		return obj;
	}
	
	
	@Test(dataProvider="getData")
	public void doLoginTest(HashMap<String,String> map) {
		
		 eReport =ExtentManager.getInstance();
		
		 eTest = eReport.startTest("LoginTest");
		
		eTest.log(LogStatus.INFO, "Login test is started");
		
		if(!DataUtil.isRunnable(xlsx, "LoginTest", "Testcases")||map.get("Runmode").equals("N")) {
			
			eTest.log(LogStatus.INFO, "Skipping the test if the runmode is set to N");
			throw new SkipException("Skipping the test if the runmode is set to N");
			 
		}
		
		openBrowser(map.get("Browser"));
		
		navigate("appURL");
		
		
		boolean actualResult = doLogin(map.get("Username"),map.get("Password"));
		
	    String ExpectedRes=	map.get("ExpectedResult");
	    
	    boolean ExpectedResult = false;
	    
	    if(ExpectedRes.equalsIgnoreCase("Sucess")) {
	    	
	    	ExpectedResult= true;
	    	
	    }else if(ExpectedRes.equalsIgnoreCase("Failure")) {
	    	
	    	ExpectedResult= false;
	    }
	    
	    if(actualResult==ExpectedResult) {
	    	
	    	reportPass("LoginTest got passed");
	    	
	    }else {
	    	
	    	reportFail("LoginTest got failed");
	    }
		
	}
	
	@AfterMethod
	public void testClosure() {
		
		if(eReport!=null) {
		eReport.endTest(eTest);
		
		eReport.flush();
		
		}
		if(driver!=null) {
			
			driver.quit();
		}
		
	}

	
}
