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
	private String sIdAdvert = "246817968";
	
	@BeforeTest
	public void TestBefore() throws ExceptFailTest
	{
		print("Start @BeforeTest");
		wLog = new WriterLog();
		wLog.SetUpWriterLog("Log_Result.html");
		lthe.GetWritterLog(wLog);
		print("End @BeforeTest");
	}
	
	@AfterTest
	public void TestAfter()
	{
		print("Start @AfterTest");
		print("End @AfterTest");
	}
	
	
	@Test(invocationCount = 1)
	@Parameters({ "sUrl", "sLogin", "sPassword" })
	public void TestStart(String sUrl, String sLogin, String sPassword) throws ExceptFailTest
	{
		print("Start @Test");
		try
		{
			// Создаем объявление
			/*pageLoginStargate.GetWriterLog(wLog);
			pageLoginStargate.OpenPage(sUrl);
			pageLoginStargate.CheckElements();
			pageLoginStargate.TypeLoginPassword(sLogin, sPassword);
			pageStargate = pageLoginStargate.EnterStargate();
			pageStargate.GetWriterLog(wLog);
			pageStargate.OpenFormCreateAdvertAuto();
			pageStargate.InputDataAuto();
			*/
			
			/*pageIrr.GetWriterLog(wLog);
			pageIrr.OpenPage("");
			pageIrr.OpenFormAuthorization();
			pageIrrPrOf = pageIrr.LoginOn("login_1");
			pageIrrPrOf.GetWriterLog(wLog);	
			
			sIdAdvert = pageIrrPrOf.GetIdAdvert();
			print(sIdAdvert);
			pageIrrPrOf.LogOutFromIrr();
			*/
			
			
			/*
			pageLoginStargate.GetWriterLog(wLog);
			pageLoginStargate.OpenPage(sUrl);
			pageLoginStargate.CheckElements();
			pageLoginStargate.TypeLoginPassword(sLogin, sPassword);
			pageStargate = pageLoginStargate.EnterStargate();
			pageStargate.GetWriterLog(wLog);
			
			pageStargate.OpenFindForm();
			pageStargate.FindAdvert(sIdAdvert);
			
			
			pageStargate.ChangeDataForAdvert("region", "email", "actionOfAdvert2", "changeOfRubric2", 3);
			*/
			
			
			pageIrr.GetWriterLog(wLog);
			pageIrr.OpenPage("");
			pageIrr.OpenFormAuthorization();
			pageIrrPrOf = pageIrr.LoginOn("login_1");
			
			pageIrrPrOf.GetWriterLog(wLog);
			//pageIrrPrOf.DeleteAllAdvert(); //Удалям все объявления
			
			pageIrrPrOf.GetStatusAndCategory(); // Значение всех счетчиков
			pageIrrPrOf.CheckAllCountersAfterChangeData("2", "2", "2", "0", "2", "0", "2", "2", "3", "3", "3", "нет", "нет", "нет", "Удаление всех объявлений");
			//pageIrrPrOf.CheckAllCountersAfterChangeData(0, 0, 0, 0, 0, 0, 0, 0, -99999, -99999, -99999, -99999, -99999, -99999, "Удаление всех объявлений");
			
			
			//pageIrrPrOf.LogOutFromIrr();
			
			
		}
		catch(Exception exc)
		{
			print("Что то случилось непредвиденное");
			wLog.WriteString(2, "Что то случилось непредвиденное: "+exc.toString());
			exc.printStackTrace();
			throw new ExceptFailTest(exc.toString());
		}
		finally
		{
			//CaptureScreenshot();
			wLog.CloseFile();
			//driver.quit();
		}
		print("End @Test");
	}


	
}
