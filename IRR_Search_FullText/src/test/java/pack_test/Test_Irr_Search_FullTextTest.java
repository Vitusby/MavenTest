package pack_test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import pack_page.Page_IrrMain;
import pack_page.Page_Search;
import pack_utils.ExceptFailTest;
import pack_utils.Proper;

public class Test_Irr_Search_FullTextTest extends Test_Construct
{
	Page_IrrMain pageIrrMain;
	Page_Search pageSearch;
	ArrayList<String> listFirstSuggest;
	ArrayList<String> listSecondSuggest;
	ArrayList<String> listLinksSuggest;
	
	/*
	@Parameters({ "sUrl"})
	@Test
	public void Test_FindBmw(String sUrl) throws ExceptFailTest
	{
		try
		{
			pageIrrMain = PageFactory.initElements(GetWebDriver(), Page_IrrMain.class);
			print("\r\nПолнотекстовый поиск по слову BMW".toUpperCase());
			pageIrrMain.OpenPage(sUrl);
			pageIrrMain.CloseWindowRegion();
			pageSearch = pageIrrMain.SendTextToFieldSearch(Proper.GetProperty("car_makeBMW"));
			pageSearch.GetAdverts();
			pageSearch.CheckAdvertByOneWords(Proper.GetProperty("sFindBmw"));
			print("Тест успешно завершен. Во всех объявлениях найдено одно из искомых слов");
		}
		finally
		{
			CaptureScreenshot("BmwFullText");
		}
	}
	
	@Parameters({ "sUrl"})
	@Test
	public void Test_FindBummer(String sUrl) throws ExceptFailTest
	{
		String sFirstUrl;
		try
		{
			pageIrrMain = PageFactory.initElements(GetWebDriver(), Page_IrrMain.class);
			print("\r\nПолнотекстовый поиск по слову Бумер".toUpperCase());
			pageIrrMain.OpenPage(sUrl);
			pageIrrMain.CloseWindowRegion();
			pageSearch = pageIrrMain.SendTextToFieldSearch(Proper.GetProperty("car_makeBummer"));
			pageSearch.GetAdverts();
			pageSearch.CheckAdvertByOneWords(Proper.GetProperty("sFindBmw"));
			print("Успешно. Во всех объявлениях найдено одно из искомых слов");
			print("\r\nПроверяем совпадения результатов поиска по слову синониму для слова " + Proper.GetProperty("car_makeBummer"));
			sFirstUrl = pageSearch.SaveLinkFirstAdvert();
			pageIrrMain.OpenPage(sUrl);
			pageIrrMain.CloseWindowRegion();
			pageSearch = pageIrrMain.SendTextToFieldSearch(Proper.GetProperty("car_makeBMWRussian"));
			pageSearch.GetAdverts();
			pageSearch.LikeLinkAdvert(sFirstUrl);
			print("Тест успешно завершен. При поиске по словам синонима \"" + Proper.GetProperty("car_makeBummer") + "\" и \"" +
					Proper.GetProperty("car_makeBMWRussian") + "\" найдено одно и тоже объявление.");
		}
		finally
		{
			CaptureScreenshot("BummerFullText");
		}
	}
	
	*/
	
	@Test (groups = { "AutoTest_3" })
	@Parameters({ "sUrl", "sImageEnable", "sParam1"})
	public void Test_RentFlat(String sUrl, String sImageEnable, String sParam1) throws ExceptFailTest
	{
		try
		{
			pageIrrMain = PageFactory.initElements(GetWebDriver(Integer.parseInt(sImageEnable)), Page_IrrMain.class);
			print("\r\nПолнотекстовый поиск по двум словам".toUpperCase());
			pageIrrMain.OpenPage(sUrl);
			pageIrrMain.CloseWindowRegion();
			pageSearch = pageIrrMain.SendTextToFieldSearch(sParam1);
			pageSearch.GetAdverts();
			pageSearch.CheckAdvertByTwoWords(sParam1);
			print("Тест успешно завершен. Во всех объявлениях найдено оба из искомых слов");
		}
		finally
		{
			CaptureScreenshot("RentFlat");
			driver.quit();
		}
	}
	
	
	
	@Test (groups = { "AutoTest_4" })
	@Parameters({ "sUrl", "sImageEnable", "sParam1", "sParam2"})
	public void Test_SuggestCompare(String sUrl, String sImageEnable, String sParam1, String sParam2) throws ExceptFailTest
	{
		try
		{
			pageIrrMain = PageFactory.initElements(GetWebDriver(Integer.parseInt(sImageEnable)), Page_IrrMain.class);
			print("\r\nСравнение саджестов для марки и ее синонима(неправильной раскладки)".toUpperCase());
			pageIrrMain.OpenPage(sUrl);
			pageIrrMain.CloseWindowRegion();
			listFirstSuggest = pageIrrMain.GetListSuggest(sParam1);
			listSecondSuggest = pageIrrMain.GetListSuggest(sParam2);
			pageIrrMain.CompareSuggests(listFirstSuggest, listSecondSuggest);	
			print("Тест успешно завершен.");
		}
		finally
		{
			CaptureScreenshot("SuggestCompare");
			driver.quit();
		}
	}
	
	
	@Test (groups = { "AutoTest_5" })
	@Parameters({ "sUrl", "sImageEnable", "sParam1"})
	public void Test_SuggestBMW(String sUrl, String sImageEnable, String sParam1) throws ExceptFailTest
	{
		try
		{
			pageIrrMain = PageFactory.initElements(GetWebDriver(Integer.parseInt(sImageEnable)), Page_IrrMain.class);
			print("\r\nПоиск по саджестам".toUpperCase());
			pageIrrMain.OpenPage(sUrl);
			pageIrrMain.CloseWindowRegion();
			pageIrrMain.GetListSuggest(sParam1);
			pageSearch = pageIrrMain.GoToSuggest();
			pageSearch.GetAdverts();
			pageSearch.GetMakeInFilterAndCompareWithFindWord(sParam1);
			pageSearch.CheckAdvertByMake(sParam1);
			print("Тест успешно завершен.");
		}
		finally
		{
			CaptureScreenshot("SuggestMake");
			driver.quit();
		}
	}
	
	
	
	@Test (groups = { "AutoTest_6" })
	@Parameters({ "sUrl", "sImageEnable", "sParam1"})
	public void Test_SuggestBlockInterest(String sUrl, String sImageEnable, String sParam1) throws ExceptFailTest, UnsupportedEncodingException
	{
		try
		{
			pageIrrMain = PageFactory.initElements(GetWebDriver(Integer.parseInt(sImageEnable)), Page_IrrMain.class);
			print("\r\nПроверка блока \"Возможно Вам также будет интересно\"".toUpperCase());
			pageIrrMain.OpenPage(sUrl);
			pageIrrMain.CloseWindowRegion();
			listFirstSuggest = pageIrrMain.GetListSuggest(sParam1); // получили названия саджестов
			listLinksSuggest = pageIrrMain.GetLinksSuggest(sUrl); // получили их ссылки
			pageSearch = pageIrrMain.SendTextToFieldSearch(sParam1);
			pageSearch.GetLinksBlockIntresting(); // получили название и ссылки саджестов в блоке вам это интересно
			pageSearch.CompareSuggestInMainWithSuggestInBlock(listFirstSuggest, listLinksSuggest); //сравнили саджесты и их ссылки(главная и в блоке)
			print("Тест успешно завершен.");
		}
		finally
		{
			CaptureScreenshot("SuggestInterest");
			driver.quit();
		}
	}
	
	
	
	@Test (groups = { "AutoTest_7" })
	@Parameters({ "sUrl", "sImageEnable", "sParam1"})
	public void Test_SuggestAbsentBlockInterest(String sUrl, String sImageEnable, String sParam1) throws ExceptFailTest, UnsupportedEncodingException
	{
		try
		{
			pageIrrMain = PageFactory.initElements(GetWebDriver(Integer.parseInt(sImageEnable)), Page_IrrMain.class);
			print("\r\nПроверка отсутствия блока \"Возможно Вам также будет интересно\"".toUpperCase());
			pageIrrMain.OpenPage(sUrl);
			pageIrrMain.CloseWindowRegion();
			pageIrrMain.GetListSuggest(sParam1); // получили саджесты
			pageSearch = pageIrrMain.GoToSuggest(); // переходим по первому саджесту(должен быть bmw x6)
			pageSearch.CheckPresentBlockInterest(1); // проверяем, что блока, Вам так же будет интересно, нет
			print("Тест успешно завершен.");
		}
		finally
		{
			CaptureScreenshot("SuggestAbsentInterest");
			driver.quit();
		}
	}
	
	
	
	@Test (groups = { "AutoTest_8" })
	@Parameters({ "sUrl", "sImageEnable", "sParam1", "sParam2"})
	public void Test_FindBMWX5(String sUrl, String sImageEnable, String sParam1, String sParam2) throws ExceptFailTest, UnsupportedEncodingException
	{
		try
		{
			pageIrrMain = PageFactory.initElements(GetWebDriver(Integer.parseInt(sImageEnable)), Page_IrrMain.class);
			print("\r\nПоиск по марке и модели и проверка наличия только категории \"" + sParam2 + "\" на странице с результатами поиска".toUpperCase());
			pageIrrMain.OpenPage(sUrl);
			pageIrrMain.CloseWindowRegion();
			pageSearch = pageIrrMain.SendTextToFieldSearch(sParam1); // перешли по полнотексту 
			pageSearch.GetLinksMainCategoryInSeachPage();
			pageSearch.CheckNamesAndCountMainCategoriesInSearchPage(sParam2, 1);
			print("Тест успешно завершен.");
		}
		finally
		{
			CaptureScreenshot("BMWX5FullText");
			driver.quit();
		}
	}
	
	
	@Test (groups = { "AutoTest_9" })
	@Parameters({ "sUrl", "sImageEnable", "sParam1", "sParam2", "sParam3"})
	public void Test_FindStopWord(String sUrl, String sImageEnable, String sParam1, String sParam2, String sParam3) throws ExceptFailTest, UnsupportedEncodingException
	{
		try
		{
			pageIrrMain = PageFactory.initElements(GetWebDriver(Integer.parseInt(sImageEnable)), Page_IrrMain.class);
			print("\r\nПоиск по марке и модели со стоп словами и проверка категорий на странице с результатами поиска".toUpperCase());
			
			print("Поиск по марке без стоп слова".toUpperCase());
			pageIrrMain.OpenPage(sUrl);
			pageIrrMain.CloseWindowRegion();
			pageSearch = pageIrrMain.SendTextToFieldSearch(sParam1); // перешли по полнотексту 
			pageSearch.GetLinksMainCategoryInSeachPage();
			pageSearch.CheckNamesAndCountMainCategoriesInSearchPage(sParam3, 2);
			
			print("\r\nПоиск по марке со стоп словом".toUpperCase());
			pageIrrMain.OpenPage(sUrl);
			pageIrrMain.CloseWindowRegion();
			pageSearch = pageIrrMain.SendTextToFieldSearch(sParam2); // перешли по полнотексту 
			pageSearch.GetLinksMainCategoryInSeachPage();
			pageSearch.CheckNamesAndCountMainCategoriesInSearchPage(sParam3, 1);
			print("Тест успешно завершен.");
		}
		finally
		{
			CaptureScreenshot("StopWord");
			driver.quit();
		}
	}
	
	
	
	@Test (groups = { "AutoTest_10" })
	@Parameters({ "sUrl", "sImageEnable", "sParam1", "sParam2", "sParam3"})
	public void Test_FindSinonim(String sUrl, String sImageEnable, String sParam1, String sParam2, String sParam3) throws ExceptFailTest, UnsupportedEncodingException
	{
		try
		{
			pageIrrMain = PageFactory.initElements(GetWebDriver(Integer.parseInt(sImageEnable)), Page_IrrMain.class);
			print("\r\nПоиск по марке и модели и синониму на русском марке и модели".toUpperCase());
			
			print("Поиск по марке и модели: ".toUpperCase() + sParam1);
			pageIrrMain.OpenPage(sUrl);
			pageIrrMain.CloseWindowRegion();
			pageSearch = pageIrrMain.SendTextToFieldSearch(sParam1); // перешли по полнотексту 
			pageSearch.GetLinksMainCategoryInSeachPage();
			pageSearch.CheckNamesAndCountMainCategoriesInSearchPage(sParam3, 1);
			print("Получаем список найденныйх объявлений по поиску - " + sParam1);
			listFirstSuggest = pageSearch.GetAdverts(); // получили список ссылок на найденные объявления
			
			print("\r\nПоиск по синониму марки и модели: ".toUpperCase() + sParam2);
			pageIrrMain.OpenPage(sUrl);
			pageIrrMain.CloseWindowRegion();
			pageSearch = pageIrrMain.SendTextToFieldSearch(sParam2); // перешли по полнотексту 
			pageSearch.GetLinksMainCategoryInSeachPage();
			pageSearch.CheckNamesAndCountMainCategoriesInSearchPage(sParam3, 1);
			print("Получаем список найденныйх объявлений по поиску - " + sParam2);
			listSecondSuggest = pageSearch.GetAdverts(); // получили список ссылок на найденные объявления
			
			print("Сравниваем результаты поиска");
			pageSearch.CompareListResult(listFirstSuggest, listSecondSuggest);
			print("Тест успешно завершен.");
		}
		finally
		{
			CaptureScreenshot("FindSinonims");
			driver.quit();
		}
	}
}
