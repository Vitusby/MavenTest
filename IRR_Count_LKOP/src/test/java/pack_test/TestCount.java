package pack_test;
import pack_page.Page_LoginStargate;
import pack_page.Page_Stargate;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

public class TestCount extends TestConstruct
{
	Page_LoginStargate pageLoginStargate = PageFactory.initElements(GetWebDriver(), Page_LoginStargate.class);
	Page_Stargate pageStargate;
	
	@BeforeTest
	public void befTest() throws Exception
	{
		System.out.println("Start @BeforeTest");
		System.out.println("End @BeforeTest");
	}
	
	@AfterTest
	public void aftTest() throws Exception
	{
		System.out.println("Start @AfterTest");
		//driver.quit();
		System.out.println("End @AfterTest");
	}
	
	@Test(invocationCount = 1)
	public void TestStart() throws Exception
	{
		
		
		System.out.println("Start @Test");
		
		pageLoginStargate.OpenPage();
		pageLoginStargate.CheckElements();
		pageLoginStargate.TypeLoginPassword();
		pageStargate = pageLoginStargate.EnterStargate();
		pageStargate.OpenFormCreatePremium();
		
		System.out.println("End @Test");
	}

	//
	
}
