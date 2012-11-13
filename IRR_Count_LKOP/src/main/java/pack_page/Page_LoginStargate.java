package pack_page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pack_utils.Proper;

public class Page_LoginStargate extends Page
{
	String sUrlStargate="";
	
	
	@FindBy(id="ext-comp-1003")             
	private WebElement inputLogin;
	
	@FindBy(id="ext-comp-1004")
	private WebElement inputPassword;
	
	@FindBy(id="ext-gen31")
	private WebElement buttonEnter;
	
	@FindBy(id="ext-comp-1009")
	private WebElement tableButton;
	
	
	public Page_LoginStargate(WebDriver driver)
	{
		super(driver);	
	}

	@Override
	public void OpenPage()
	{
		sUrlStargate = Proper.GetProperty("urlStargate");
		driver.get(sUrlStargate);
		driver.manage().window().maximize();
		System.out.println("Открываем страницу "+sUrlStargate);
	}

	public void CheckElements() throws Exception 
	{
		CheckElementPresent(2,"ext-comp-1003");
		CheckElementPresent(2,"ext-comp-1004");
		CheckElementPresent(2,"ext-gen31");
	}
	
	public void TypeLoginPassword()
	{
		inputLogin.clear();
		inputLogin.sendKeys(Proper.GetProperty("login"));
		System.out.println("Вводим логин");
		inputPassword.clear();
		inputPassword.sendKeys(Proper.GetProperty("password"));
		System.out.println("Вводим пароль");
	}
	
	public Page_Stargate EnterStargate() throws Exception
	{
		CheckAtributeElement("class", "x-btn-wrap x-btn ", tableButton);
		buttonEnter.click();
		System.out.println("Нажимаем Войти");
		return PageFactory.initElements(driver, Page_Stargate.class);
	}
	
}
