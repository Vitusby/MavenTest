package pack_page;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pack_utils.ExceptFailTest;

public class Page_IrrMain extends Page {

	@FindBy(xpath = "//div[@class='popupRegions']")
	private WebElement wWindowSelectOfRegion;

	@FindBy(xpath = "//div[@class='button-style btn-a']/a")
	private WebElement wOkButtonWindowSelectOfRegion;
	
	// поле поиска
	@FindBy(xpath = "//input[@id='id_keywords']")
	private WebElement wInputFieldSearch;

	// кнопка найти когда введен текст
	@FindBy(xpath = "//div[contains(@class,'wrSearch')]//a")
	private WebElement wButtonSearch;
	
	// список саджестов
	private WebElement wLinkSuggestText[];
	String sXpathSuggest = "//ul[@class='ui-autocomplete ui-menu ui-widget ui-widget-content ui-corner-all']/li[@class='ui-menu-item']/a";
	//String sXpathSuggest = "//ul[@class='ui-autocomplete ui-menu ui-widget ui-widget-content ui-corner-all']/li[@class='ui-menu-item']/a[@class='ui-corner-all']";
	
	String sF; // слово по которому ищем здесь храним
	
	private JavascriptExecutor js;

	
	public Page_IrrMain(WebDriver driver)
	{
		super(driver);
	}

	public void OpenPage(String sUrl)
	{
		driver.navigate().to(sUrl);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		System.out.println("Открываем страницу " + sUrl);
	}

	public void CloseWindowRegion() throws ExceptFailTest
	{

		CheckElementPresent(1, "//div[@class='popupRegions']");
		if (wWindowSelectOfRegion.isDisplayed()) {
			print("Закрываем окно выбора регионов");
			CheckElementPresent(1, "//div[@class='button-style btn-a']/a");
			wOkButtonWindowSelectOfRegion.click();
		}
	}
	
	public Page_Search SendTextToFieldSearch(String sFindWord) throws ExceptFailTest
	{
		
		CheckElementPresent(1, "//input[@id='id_keywords']");
		print("\r\nВводим в поле поиска текст - " + sFindWord);
		SendText(wInputFieldSearch, sFindWord);
		Sleep(500);
		CheckElementPresent(1, "//div[contains(@class,'wrSearch')]//a");
		print("Нажимаем найти");
		ClickElement(wButtonSearch);
		return PageFactory.initElements(driver, Page_Search.class);
	}
	
	// получение саджестов
	public ArrayList<String> GetListSuggest(String sFindWord) throws ExceptFailTest
	{
		ArrayList<String> listTemp = new ArrayList<String>();
		sF = sFindWord; 
		js = (JavascriptExecutor) driver;
		driver.get(driver.getCurrentUrl());
		CheckElementPresent(1, "//input[@id='id_keywords']");
		print("Вводим в поле поиска текст - " + sFindWord);
		SendText(wInputFieldSearch, sFindWord);
		Sleep(1000);
		
		try
		{
			wLinkSuggestText = this.GetAllWebElements(sXpathSuggest);
		}
		catch(ExceptFailTest exc)
		{
			print("Нет ни одного саджеста для слова - " + sFindWord);
			throw new ExceptFailTest("Нет ни одного саджеста для слова - " + sFindWord);
		}
		print("\r\nСписок названий саджестов получен");
		CaptureScreenshot("SuggestMain");
		print(wLinkSuggestText.length);
		
		/*ArrayList<String> lTemp = new ArrayList<String>();
		ArrayList<String> list = (ArrayList<String>)js.executeScript(" var dd = new Array(); $('li[role$=\"menuitem\"]').each(function(i) { dd[i] = $(this).data('item.autocomplete').title});return dd;");
		print(list.size());
		for(int i=0; i<list.size(); i++)
			print(list.get(i).replaceAll("<span class=\"snippet\">", "").replaceAll("</span>", ""));
		
		
		print("");
		*/
		
		for(int i=0; i<wLinkSuggestText.length; i++)
		{
			listTemp.add(wLinkSuggestText[i].getText().toLowerCase());
			print(wLinkSuggestText[i].getText());
		}
		return listTemp;
	}
	
	// получение лист ссылок в саджестах
	@SuppressWarnings({ "unchecked" })
	public ArrayList<String> GetLinksSuggest(String sUrl) throws UnsupportedEncodingException
	{
		js = (JavascriptExecutor) driver;
		ArrayList<String> lTemp = new ArrayList<String>();
		ArrayList<String> list = (ArrayList<String>)js.executeScript(" var dd = new Array(); $('li[role$=\"menuitem\"]').each(function(i) { dd[i] = $(this).data('item.autocomplete').url });return dd;");
		
		for(int i=0; i<list.size(); i++)
		{
			lTemp.add(sUrl.toLowerCase() + URLDecoder.decode(list.get(i), "utf-8").toLowerCase());
		}
		
		print("\r\nСписок ссылок саджестов получен");
		for(int i=0; i<lTemp.size(); i++)
		{
			print(lTemp.get(i));
		}
		return lTemp;
		
	}
	
	//сравнение двух лист
	public void CompareSuggests(ArrayList<String> listOne, ArrayList<String> listSecond) throws ExceptFailTest
	{
		if(listOne.containsAll(listSecond))
			print("Списки саджестов одинаковы");
		else
		{
			print("Списки саджестов не одинаковы");
			throw new ExceptFailTest("Списки саджестов не одинаковы");
		}
	}
	
	// переход по саджесту
	public Page_Search GoToSuggest() throws ExceptFailTest
	{
		if(wLinkSuggestText.length == 0)
		{
			print("Нет ни одного саджеста для слова - " + sF);
			throw new ExceptFailTest("Нет ни одного саджеста для слова - " + sF);
		}
		print("\r\nПереходим по саджесту - " + wLinkSuggestText[0].getText());
		ClickElement(wLinkSuggestText[0]);
		return PageFactory.initElements(driver, Page_Search.class);	
	}
	
	
}
