package pack_page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pack_utils.ExceptFailTest;
import pack_utils.Proper;

public class Page_AfterLogin extends Page
{
	// Текст логина для компании
	@FindBy(xpath="//li[@class='usermenu-item m-item_username HH-ApplicantSettingsFio-TopItem']")
	private WebElement TextItemFio;
	// Текст id клиента
	@FindBy(xpath="//li[@class='usermenu-item m-clientid']")
	private WebElement TextClientId;
	// Ссылка "Купить услуги"
	@FindBy(xpath="//a[@href='/price' and contains(text(),'Купить услуги')]")
	private WebElement LinkPrice;
	
	@FindBy(xpath="//li[@class='usermenu-item']/strong[contains(text(),'Письмо  Рассылка')]")
	private WebElement TextFio;
	
	// Конструктор
	public Page_AfterLogin(WebDriver driver) 
	{
		super(driver);
	}
	// Метод открытия страницы
	public void OpenPage(String sUrl){}
	
	// Метод проверки авторизации
	public void CheckAutorization(String sFlagCompany) throws ExceptFailTest
	{
		print("Проверка авторизации пользователя".toUpperCase());
		print("Проверяем отображение логина на странице");
		if(sFlagCompany.equals("true")) // компания
		{
			CheckElementPresent(1, "//li[@class='usermenu-item m-item_username HH-ApplicantSettingsFio-TopItem']");
			print("Логин найден");
			print("Проверяем совпадение отображаемого логина с ожидаемым");
			CheckTextForElement(TextItemFio, Proper.GetProperty("sRussianLogin"));
			print("Логин \"" + TextItemFio.getText() + "\" совпал с ожидаемым \"" + Proper.GetProperty("sRussianLogin") + "\"");
		
			print("Проверяем отображение id клиента на странице");
			CheckElementPresent(1, "//li[@class='usermenu-item m-clientid']");
			print("Id клиента найден");
			print("Проверяем совпадение отображаемого id клиента с ожидаемым");
			CheckTextForElement(TextClientId, Proper.GetProperty("sIdClient"));
			print("Id клиента \"" + TextClientId.getText() + "\" совпал с ожидаемым \"" + Proper.GetProperty("sIdClient") + "\"");
		}
		else // обычный пользователь
		{
			CheckElementPresent(1, "//li[@class='usermenu-item']/strong[contains(text(),'Письмо  Рассылка')]");
			print("Логин найден");
			print("Проверяем совпадение отображаемого логина с ожидаемым");
			print(TextFio.getText());
			CheckTextForElement(TextFio, Proper.GetProperty("sRussianLogin"));
			print("Логин \"" + TextFio.getText() + "\" совпал с ожидаемым \"" + Proper.GetProperty("sRussianLogin") + "\"");
		}
			
	}
	
	// Метод перехода на страницу Price
	public Page_Price GoToPagePrice(String sFlagCompany) throws ExceptFailTest
	{
		print("Открытие страницы \"Покупка услуг\"".toUpperCase());
		if(sFlagCompany.equals("true")) // компания
		{
			print("Проверям наличие ссылки \"Купить услуги\"");
			CheckElementPresent(1, "//a[@href='/price' and contains(text(),'Купить услуги')]");
			print("Ссылка найдена");
			print("Нажимаем ссылку \"Купить услуги\"");
			ClickElement(LinkPrice);
		}
		else // обычный пользователь
		{
			print("Открываее страницу http://hh.ru/price");
			driver.get(Proper.GetProperty("sUrlMainPage") + "/price");
		}
		print("Страница " + driver.getCurrentUrl() + " открыта");	
		return PageFactory.initElements(driver, Page_Price.class);
	}

}
