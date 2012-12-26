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
	Page_LoginStargate pageLoginStargate = PageFactory.initElements(GetWebDriver(0), Page_LoginStargate.class);
	Page_Stargate pageStargate;
	Page_IrrMain pageIrr = PageFactory.initElements(GetWebDriver(1), Page_IrrMain.class);
	Page_IrrPrivateOffice pageIrrPrOf;
	private String sIdAdvert;
	
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
			
			
			/*pageIrr.GetWriterLog(wLog);
			pageIrr.OpenPage("");
			pageIrr.OpenFormAuthorization();
			pageIrrPrOf = pageIrr.LoginOn("login_1");
			pageIrrPrOf.GetWriterLog(wLog);	
			
			sIdAdvert = pageIrrPrOf.GetIdAdvert();
			print(sIdAdvert);
			pageIrrPrOf.LogOutFromIrr();
			*/
			
		
////////////////////////////////////////////////////////	Заходим и удаляем все объявления , проверяем что удалено САЙТ		
			pageIrr.GetWriterLog(wLog);
			pageIrr.OpenPage("");
			pageIrr.OpenFormAuthorization();
			pageIrrPrOf = pageIrr.LoginOn("login_1");
			
			pageIrrPrOf.GetWriterLog(wLog);
			pageIrrPrOf.DeleteAllAdvert(); //Удалям все объявления
			
			pageIrrPrOf.GetStatusAndCategory(); // Значение всех счетчиков
			pageIrrPrOf.CheckAllCountersAfterChangeData("0", "0", "0", "0", "0", "0", "0", "0", "нет", "нет", "нет", "нет", "нет", "нет", "Удаление всех объявлений");
			//pageIrrPrOf.CheckAllCountersAfterChangeData(0, 0, 0, 0, 0, 0, 0, 0, -99999, -99999, -99999, -99999, -99999, -99999, "Удаление всех объявлений");
			
			
			//pageIrrPrOf.LogOutFromIrr();
////////////////////////////////////////////////////////////	
			
	
///////////////////////////////////////////////////////// Создаем объявление БО
			
			pageLoginStargate.GetWriterLog(wLog);
			pageLoginStargate.OpenPage(sUrl);
			pageLoginStargate.CheckElements();
			pageLoginStargate.TypeLoginPassword(sLogin, sPassword);
			pageStargate = pageLoginStargate.EnterStargate();
			pageStargate.GetWriterLog(wLog);
			pageStargate.OpenFormCreateAdvertAuto();
			pageStargate.InputDataAuto();


////////////////////////////////////////////////////////			
			
////////////////////////////////////////////////////////////  Копируем ID объявления
			
			sIdAdvert = pageIrrPrOf.GetIdAdvert();
			print(sIdAdvert);
			
////////////////////////////////////////////////////////////			
			
			
			
////////////////////////////////////////////////////////////  Заходим в БО вносим изменения
		
			/*pageLoginStargate.GetWriterLog(wLog);
			pageLoginStargate.OpenPage(sUrl);
			pageLoginStargate.CheckElements();
			pageLoginStargate.TypeLoginPassword(sLogin, sPassword);
			pageStargate = pageLoginStargate.EnterStargate();
			pageStargate.GetWriterLog(wLog);
			*/
			pageStargate.OpenFindForm();
			pageStargate.FindAdvert(sIdAdvert);
			
			
			pageStargate.ChangeDataForAdvert("region", "email", "actionOfAdvert2", "changeOfRubric2", 3);
		
//////////////////////////////////////////////////////////////
			
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
			CaptureScreenshot(driver[0], "screen1");
			CaptureScreenshot(driver[1], "screen2");
			wLog.CloseFile();
			driver[0].quit();
			driver[1].quit();
		}
		print("End @Test");
	}


	
}
