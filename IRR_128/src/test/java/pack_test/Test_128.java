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
import pack_utils.ArrayListDataStep;
import pack_utils.ExceptFailTest;
import pack_utils.Proper;
import pack_utils.WriterLog;


public class Test_128 extends TestConstruct
{
	Page_LoginStargate pageLoginStargate = PageFactory.initElements(GetWebDriver(0), Page_LoginStargate.class);
	Page_Stargate pageStargate;
	Page_IrrMain pageIrr = PageFactory.initElements(GetWebDriver(1), Page_IrrMain.class); //test_128_1
	Page_IrrPrivateOffice pageIrrPrOf;
	Page_IrrMain pageIrr2 = PageFactory.initElements(GetWebDriver(2), Page_IrrMain.class); //test_128_2
	Page_IrrPrivateOffice pageIrrPrOf2;
	private String sIdAdvert = "247859253";
	private ArrayListDataStep list;
	
	
	@BeforeTest
	public void TestBefore() throws ExceptFailTest
	{
		print("Start @BeforeTest");
		wLog = new WriterLog();
		wLog.SetUpWriterLog("Log_Result.html");
		lthe.GetWritterLog(wLog);
		list = new ArrayListDataStep();
		list.CheckListExists();
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
////////////////////////////////////////////////////////	Заходим и удаляем все объявления , проверяем что удалено САЙТ //test_128_1
			print("Пользователь test_128_1");
			wLog.WriteString(1, "Пользователь test_128_1");
			pageIrr.GetWriterLog(wLog);
			pageIrr.OpenPage("");
			pageIrr.OpenFormAuthorization();
			pageIrrPrOf = pageIrr.LoginOn("login_1");
			
			pageIrrPrOf.GetWriterLog(wLog);
			//pageIrrPrOf.DeleteAllAdvert(); //Удалям все объявления
			
			//pageIrrPrOf.GetStatusAndCategory(); // Значение всех счетчиков
			//pageIrrPrOf.CheckAllCountersAfterChangeData("0", "0", "0", "0", "0", "0", "0", "0", "нет", "нет", "нет", "нет", "нет", "нет", "Удаление всех объявлений для test_128_1");
	
////////////////////////////////////////////////////////////	Заходим и удаляем все объявления , проверяем что удалено САЙТ //test_128_2
			
			
			print("Пользователь test_128_2");
			wLog.WriteString(1, "Пользователь test_128_2");
			pageIrr2.GetWriterLog(wLog);
			pageIrr2.OpenPage("");
			pageIrr2.OpenFormAuthorization();
			pageIrrPrOf2 = pageIrr2.LoginOn("login_2");
			
			pageIrrPrOf2.GetWriterLog(wLog);
			//pageIrrPrOf2.DeleteAllAdvert(); //Удалям все объявления
			
			//pageIrrPrOf2.GetStatusAndCategory(); // Значение всех счетчиков
			//pageIrrPrOf2.CheckAllCountersAfterChangeData("0", "0", "0", "0", "0", "0", "0", "0", "нет", "нет", "нет", "нет", "нет", "нет", "Удаление всех объявлений для test_128_2");
			
			
	
///////////////////////////////////////////////////////// Создаем объявление БО
			
			pageLoginStargate.GetWriterLog(wLog);
			pageLoginStargate.OpenPage(sUrl);
			pageLoginStargate.CheckElements();
			pageLoginStargate.TypeLoginPassword(sLogin, sPassword);
			pageStargate = pageLoginStargate.EnterStargate();
			pageStargate.GetWriterLog(wLog);
			//pageStargate.OpenFormCreateAdvertAuto();
			//pageStargate.InputDataAuto();


////////////////////////////////////////////////////////			
			
////////////////////////////////////////////////////////////  Копируем ID объявления
			
			//sIdAdvert = pageIrrPrOf.GetIdAdvert();
			sIdAdvert = "247863217";
			print(sIdAdvert);
			
////////////////////////////////////////////////////////////			
			
////////////////////////////////////////////////////////////  Заходим в БО вносим изменения  Шаг 1
			
			
			for(int i=40; i<list.GetSizeList(); i++)
			{
				print("ШАГ " + i);
				wLog.WriteString(0, "ШАГ " + i);
				
				String sMas[] = list.GetList(i);
				//for(String s : sMas)
					//print(s);
			
				// 	emai_old - 0 	        // email_new - 1
				// 	changeOfRubric_old - 2  // changeOfRubric_new - 3
				//  region_old - 4          // region_new - 5	
				//  actionOfAdvert_old - 6	// actionOfAdvert_new - 7
				
				//  user_128_1 (шаг 1-18)
				//  sMyAdvert - 8 
				//  sAllStatus - 9 			// sActiveS - 10         	// sNotActiveS - 11
				//  sAllList - 12			// sNotActiveL - 13			// sActiveL - 14
				//	sAllCategory - 15		
				//  sMainAuto - 16			// sEasyCars - 17			// sUsedCars - 18
				//  sMainRealt - 19			// sRealtSell - 20			// sRealtSecond - 21
				//  nOperation - 22
				
				// 	user_128_2 (шаг 19-)
				//  sMyAdvert - 23 
				//  sAllStatus - 24			// sActiveS - 25         	// sNotActiveS - 26
				//  sAllList - 27			// sNotActiveL - 28			// sActiveL - 29
				//	sAllCategory - 30		
				//  sMainAuto - 31			// sEasyCars - 32			// sUsedCars - 33
				//  sMainRealt - 34			// sRealtSell - 35			// sRealtSecond - 36
		
			
				pageStargate.OpenFindForm();
				pageStargate.FindAdvert(sIdAdvert);
				
				
				pageStargate.ChangeDataForAdvert(sMas[1], sMas[3], sMas[5], sMas[7], sMas[22],
						"изменение владельца объявления с " + Proper.GetProperty(sMas[0]).toUpperCase() + " на " + Proper.GetProperty(sMas[1]).toUpperCase() + "\r\n" +
						"изменение региона объявления с " + Proper.GetProperty(sMas[2]).toUpperCase() + " на " + Proper.GetProperty(sMas[3]).toUpperCase() + "\r\n" +
						"изменение рубрики объявления с " + Proper.GetProperty(sMas[4]).toUpperCase() + " на " + Proper.GetProperty(sMas[5]).toUpperCase() + "\r\n" +
						"изменение статуса активности объявления с " + Proper.GetProperty(sMas[6]).toUpperCase() + " на " + Proper.GetProperty(sMas[7]).toUpperCase());
				
				wLog.WriteString(0, "</br>");
				print("ПРОВЕРКА ЗНАЧЕНИЯ СЧЕТЧИКОВ ДЛЯ USER_128_1");
				wLog.WriteString(3, "ПРОВЕРКА ЗНАЧЕНИЯ СЧЕТЧИКОВ ДЛЯ USER_128_1");
				pageIrrPrOf.ReloadPage(true);
				pageIrrPrOf.GetStatusAndCategory(); // Значение всех счетчиков
				pageIrrPrOf.CheckAllCountersAfterChangeData(sMas[8], sMas[9], sMas[10], sMas[11], sMas[12], sMas[13], sMas[14], sMas[15], sMas[16], sMas[17], sMas[18], sMas[19], sMas[20], sMas[21], 
						"изменение владельца объявления с " + Proper.GetProperty(sMas[0]).toUpperCase() + " на " + Proper.GetProperty(sMas[1]).toUpperCase() + "\r\n" +
						"изменение региона объявления с " + Proper.GetProperty(sMas[2]).toUpperCase() + " на " + Proper.GetProperty(sMas[3]).toUpperCase() + "\r\n" +
						"изменение рубрики объявления с " + Proper.GetProperty(sMas[4]).toUpperCase() + " на " + Proper.GetProperty(sMas[5]).toUpperCase() + "\r\n" +
						"изменение статуса активности объявления с " + Proper.GetProperty(sMas[6]).toUpperCase() + " на " + Proper.GetProperty(sMas[7]).toUpperCase());
				
				if(i>17)
				{
					wLog.WriteString(0, "</br>");
					print("ПРОВЕРКА ЗНАЧЕНИЯ СЧЕТЧИКОВ ДЛЯ USER_128_2");
					wLog.WriteString(3, "ПРОВЕРКА ЗНАЧЕНИЯ СЧЕТЧИКОВ ДЛЯ USER_128_2");
					pageIrrPrOf2.ReloadPage(false);
					pageIrrPrOf2.GetStatusAndCategory(); // Значение всех счетчиков
					pageIrrPrOf2.CheckAllCountersAfterChangeData(sMas[23], sMas[24], sMas[25], sMas[26], sMas[27], sMas[28], sMas[29], sMas[30], sMas[31], sMas[32], sMas[33], sMas[34], sMas[35], sMas[36], 
							"изменение владельца объявления с " + Proper.GetProperty(sMas[0]).toUpperCase() + " на " + Proper.GetProperty(sMas[1]).toUpperCase() + "\r\n" +
							"изменение региона объявления с " + Proper.GetProperty(sMas[2]).toUpperCase() + " на " + Proper.GetProperty(sMas[3]).toUpperCase() + "\r\n" +
							"изменение рубрики объявления с " + Proper.GetProperty(sMas[4]).toUpperCase() + " на " + Proper.GetProperty(sMas[5]).toUpperCase() + "\r\n" +
							"изменение статуса активности объявления с " + Proper.GetProperty(sMas[6]).toUpperCase() + " на " + Proper.GetProperty(sMas[7]).toUpperCase());
				}
				wLog.WriteString(0, "</br>");
			}

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
			CaptureScreenshot(driver[2], "screen3");
			wLog.CloseFile();
			driver[0].quit();
			driver[1].quit();
			driver[2].quit();
		}
		print("End @Test");
	
		
	}


	
}
