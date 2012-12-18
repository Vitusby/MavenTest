package pack_test;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import pack_page.Page_IrrMain;
import pack_page.Page_IrrPrivateOffice;
import pack_page.Page_LoginStargate;
import pack_page.Page_Stargate;
import pack_utils.ExceptFailTest;
import pack_utils.WriterLog;


public class Test_128 extends TestConstruct
{
	Page_LoginStargate pageLoginStargate = PageFactory.initElements(GetWebDriver(), Page_LoginStargate.class);
	Page_Stargate pageStargate;
	Page_IrrMain pageIrr = PageFactory.initElements(GetWebDriver(), Page_IrrMain.class);
	Page_IrrPrivateOffice pageIrrPrOf;
	
	@BeforeTest
	public void TestBefore() throws ExceptFailTest
	{
		System.out.println("Start @BeforeTest");
		wLog = new WriterLog();
		wLog.SetUpWriterLog("Log_Result.html");
		lthe.GetWritterLog(wLog);
		System.out.println("End @BeforeTest");
	}
	
	@AfterTest
	public void TestAfter()
	{
		System.out.println("Start @AfterTest");
		System.out.println("End @AfterTest");
	}
	
	
	@Test(invocationCount = 1)
	@Parameters({ "sUrl", "sLogin", "sPassword" })
	public void TestStart(String sUrl, String sLogin, String sPassword) throws ExceptFailTest
	{
		System.out.println("Start @Test");
		try
		{
			
			//Удалям все объявления перед тестом
			pageIrr.GetWriterLog(wLog);
			pageIrr.OpenPage("");
			pageIrr.OpenFormAuthorization();
			pageIrrPrOf = pageIrr.LoginOn("login_1");
			pageIrrPrOf.GetWriterLog(wLog);	
			
			pageIrrPrOf.GetStatusAndCategory();
			
			/*pageIrrPrOf.GetCurrentStatus();
			pageIrrPrOf.DeleteAllAdvert();
			pageIrrPrOf.CheckCountAndVisibleAdvert();
			pageIrrPrOf.CheckOldAndNewStatus(2);
			pageIrrPrOf.LogOutFromIrr();
			
			// Содаем объявление
			pageLoginStargate.GetWriterLog(wLog);
			pageLoginStargate.OpenPage(sUrl);
			pageLoginStargate.CheckElements();
			pageLoginStargate.TypeLoginPassword(sLogin, sPassword);
			pageStargate = pageLoginStargate.EnterStargate();
			pageStargate.GetWriterLog(wLog);
			pageStargate.OpenFormCreateAdvertAuto();
			pageStargate.InputDataAuto();
			*/
		}
		catch(Exception exc)
		{
			System.out.println("Что то случилось непредвиденное");
			wLog.WriteString(2, "Что то случилось непредвиденное: "+exc.toString());
			throw new ExceptFailTest(exc.toString());
		}
		finally
		{
			//CaptureScreenshot();
			//wLog.CloseFile();
			//driver.quit();
		}
		System.out.println("End @Test");
	}
	
}
