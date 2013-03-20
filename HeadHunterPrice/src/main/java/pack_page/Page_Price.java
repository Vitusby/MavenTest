package pack_page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pack_utils.ExceptFailTest;

public class Page_Price extends Page
{
	// Заголовок Покупка услуг
	@FindBy(xpath="//h1[@class='b-title' and contains(text(),'Покупка услуг')]")
	private WebElement TextTitle;
	// Ссылка Рекомендуемое
	@FindBy(xpath="//a[@href='#recommended' and contains(text(),'Рекомендуемое')]")
	private WebElement LinkRecommended;
	// Ссылка Доступ к базе резюме
	@FindBy(xpath="//a[@href='#dbaccess' and contains(text(),'Доступ к базе резюме')]")
	private WebElement LinkAccess;
	// Ссылка Публикации вакансий
	@FindBy(xpath="//a[@href='#publications' and contains(text(),'Публикации вакансий')]")
	private WebElement LinkPublications;
	// Ссылка Публикации вакансий
	@FindBy(xpath="//a[@href='#additional' and contains(text(),'Дополнительные услуги')]")
	private WebElement LinkAdditional;
	// Ссылка Реклама на сайте
	@FindBy(xpath="//a[@href='#advertisement' and contains(text(),'Реклама на сайте')]")
	private WebElement LinkAdvertisement;
	// Ссылка CV Market
	@FindBy(xpath="//a[@href='#cvmarket' and contains(text(),'CV Market')]")
	private WebElement LinkCVMarket;
	
	
	// Конструктор
	public Page_Price(WebDriver driver)
	{
		super(driver);
	}

	// Метод открытия страницы
	public void OpenPage(String sUrl){}
	
	// Метод проверки наличия элементов на странице Price
	public void CheckElementToPagePrice() throws ExceptFailTest
	{
		print("Проверка наличия элементов страницы \"Покупка услуг\"".toUpperCase());
		print("Проверяем наличие заголовка \"Покупка услуг\"");
		CheckElementPresent(1, "//h1[@class='b-title' and contains(text(),'Покупка услуг')]");
		print("Заголовок \""+ TextTitle.getText() + "\" найден");
		
		print("Проверяем наличие ссылки \"Рекомендуемое\"");
		CheckElementPresent(1, "//a[@href='#recommended' and contains(text(),'Рекомендуемое')]");
		print("Ссылка \""+ LinkRecommended.getText() + "\" найдена");
		
		print("Проверяем наличие ссылки \"Доступ к базе резюме\"");
		CheckElementPresent(1, "//a[@href='#dbaccess' and contains(text(),'Доступ к базе резюме')]");
		print("Ссылка \""+ LinkAccess.getText() + "\" найдена");
		
		print("Проверяем наличие ссылки \"Публикации вакансий\"");
		CheckElementPresent(1, "//a[@href='#publications' and contains(text(),'Публикации вакансий')]");
		print("Ссылка \""+ LinkPublications.getText() + "\" найдена");
		
		print("Проверяем наличие ссылки \"Дополнительные услуги\"");
		CheckElementPresent(1, "//a[@href='#additional' and contains(text(),'Дополнительные услуги')]");
		print("Ссылка \""+ LinkAdditional.getText() + "\" найдена");
		
		print("Проверяем наличие ссылки \"CV Market\"");
		CheckElementPresent(1, "//a[@href='#advertisement' and contains(text(),'Реклама на сайте')]");
		print("Ссылка \""+ LinkAdvertisement.getText() + "\" найдена");
		
		print("Проверяем наличие ссылки \"Реклама на сайте\"");
		CheckElementPresent(1, "//a[@href='#cvmarket' and contains(text(),'CV Market')]");
		print("Ссылка \""+ LinkCVMarket.getText() + "\" найдена");
		
	}

}
