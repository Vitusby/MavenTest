package pack_page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pack_utils.ExceptFailTest;
import pack_utils.Proper;

public class Page_Main extends Page
{
	// ссылка "Вход в личный кабинет"
	@FindBy(xpath = "//a[@href='/login' and contains(text(),'Вход в личный кабинет')]")      
	private WebElement LinkLogin;
	// Поле ввода логина/email
	@FindBy(id = "loginform-input-username")      
	private WebElement TextFileldLogin;
	// Поле ввода пароля
	@FindBy(id="loginform-input-password")      
	private WebElement TextFileldPassword;
	// Кнопка войти
	@FindBy(xpath = "//input[@class='login-submit']")      
	private WebElement ButtonEnter;
	
	// Конструктор
	public Page_Main(WebDriver driver)
	{
		super(driver);	
	}

	// Метод открытия страницы
	public void OpenPage(String sUrl)
	{
		print("Открываем браузер");
		driver.navigate().to(sUrl);
		print("Открываем страницу " + sUrl);
		driver.manage().window().maximize();
	}
	
	// Метод Авторизации
	public Page_AfterLogin Authorization(String sLogin, String sPassword) throws ExceptFailTest
	{
		print("Авторизация".toUpperCase());
		print("Открываем форму авторизации");
		print("Проверям наличие ссылки \"Вход в личный кабинет\"");
		CheckElementPresent(1,"//a[@href='/login' and contains(text(),'Вход в личный кабинет')]");
		print("Ссылка найдена");
		print("Нажимаем ссылку \"Вход в личный кабинет\"");
		ClickElement(LinkLogin);
		
		print("Проверяем наличие и доступность для ввода поля \"Электронная почта\"");
		CheckElementPresent(2, "loginform-input-username");
		CheckEnableElement(TextFileldLogin, 1);
		print("Поле найдено и доступно для ввода");
		print("Вводим логин - " + sLogin);
		SendText(TextFileldLogin, sLogin);
		print("Проверяем введенные данные в поле \"Электронная почта\"");
		CheckTextForElement(TextFileldLogin, sLogin);
		print("Данные в поле \"Электронная почта\" совпадают с темы что были введены");
		
		print("Проверяем наличие и доступность для ввода поля \"Пароль\"");
		CheckElementPresent(2, "loginform-input-password");
		CheckEnableElement(TextFileldPassword, 1);
		print("Поле найдено и доступно для ввода");
		print("Вводим пароль " + sPassword);
		SendText(TextFileldPassword, sPassword);
		print("Проверяем введенные данные в поле \"Пароль\"");
		CheckTextForElement(TextFileldPassword, sPassword);
		print("Данные в поле \"Пароль\" совпадают с темы что были введены");
		
		print("Проверям наличие и кликабельность кнопки \"Войти\"");
		CheckElementPresent(1,"//input[@class='login-submit']");
		CheckEnableElement(ButtonEnter, 1);
		print("Кнопка найдена. Нажимаем ее.");
		ClickElement(ButtonEnter);
		
		return PageFactory.initElements(driver, Page_AfterLogin.class);
	}
	


}
