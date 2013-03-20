package pack_page;

import java.util.ArrayList;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pack_utils.ExceptFailTest;
import pack_utils.Proper;

public class Page_PriceRecommended extends Page
{
	Page_PriceBasket pagePriceBasket = PageFactory.initElements(this.driver, Page_PriceBasket.class); // страница hh.ru/price блок корзина
	
	// Ссылка Рекомендуемое
	@FindBy(xpath="//a[@href='#recommended' and contains(text(),'Рекомендуемое')]")
	private WebElement LinkRecommended;
	// Заголовок блока рекоммендуемое
	@FindBy(className="form-box-flagged__flag-title")
	private WebElement TextTitleFlagGift;
	
	// сумма цен всех спецпредложений 
	private int nCostSpecialOffers[];
	// количество блоков спецпредложений
	private int nCountSpecialOffers;
	
	private JavascriptExecutor js;
	// список объектов блоков с предложениями
	ArrayList<BlockOffers> bList = new ArrayList<BlockOffers>();
	
	//внутр. класс храним данные о блока спецпредложений
	class BlockOffers
	{
		WebElement wTitleOffers;
		WebElement wCostOffers;
		WebElement wButtonToBasket;
		
		BlockOffers(WebElement wTitleOffers, WebElement wCostOffers, WebElement wButtonToBasket)
		{
			this.wTitleOffers = wTitleOffers;
			this.wCostOffers = wCostOffers;
			this.wButtonToBasket = wButtonToBasket;
		}
		
		WebElement GetWTitle() {return wTitleOffers;}
		WebElement GetWCost() {return wCostOffers;}
		WebElement GetWButtonToBasket() {return wButtonToBasket;}
	}
	
	// Конструктор
	public Page_PriceRecommended(WebDriver driver)
	{
		super(driver);
	}

	// Открытие страницы
	public void OpenPage(String sUrl){}
	
	// Метод проверки выбран ли блок Рекомендуемое
	public void CheckElementPagePriceRecommended() throws ExceptFailTest
	{
		print("\r\nПроверка наличия элементов страницы \"Покупка услуг\" блока Рекомендуемое".toUpperCase());
		print("Проверяем наличие ссылки \"Рекомендуемое\"");
		CheckElementPresent(1, "//a[@href='#recommended' and contains(text(),'Рекомендуемое')]");
		print("Ссылка \""+ LinkRecommended.getText() + "\" найдена");
		print("Нажимаем ее");
		ClickElement(LinkRecommended);
		print("Проверяем что ссылка \"Рекомендуемое\" выбрана (color = black)");
		CheckCssElement(LinkRecommended, "color", Proper.GetProperty("sColorLinkSelected"));
		print("Ссылка \""+ LinkRecommended.getText() + "\" выбрана");
		print("Приверяем наличие заголовка блока \"Подарки при первой покупке\"");
		CheckElementPresent(4, "form-box-flagged__flag-title");
		ScrollToElement(TextTitleFlagGift);
		CheckEnableElement(TextTitleFlagGift, 1);
		CheckVisibleElement(TextTitleFlagGift, "Подарки при первой покупке");
		print("Заголовок \""+ TextTitleFlagGift.getText() + "\" найден");	
	}

	// Получение блоков спецпредложений, их заголовков, цены и кнопок "в корзину"
	public void GetBlocksSpecialOffers() throws ExceptFailTest
	{
		WebElement wDivSpecialOffers[];// массив элементов с названием  спецпредложений
		WebElement wSpanSpecialOffersPrice[];// массив элементов с ценами спецпредложений
		WebElement wButtonSpecialOffersPrice[];// массив элементов с кнопками в корзину для спецпредложений
		
		js = (JavascriptExecutor) driver;
		print("\r\nПроверяем количество блоков со спецпредложениями");
		long lDivSpecial = (Long) js.executeScript("return document.getElementsByClassName('price-spoffers__special-offer-title').length;");
		nCountSpecialOffers = (int)lDivSpecial;
		if(nCountSpecialOffers == 0)
		{
			print("Количество блоков с предложениями равно нулю. Тест провален");
			throw new ExceptFailTest("Количество блоков со спецпредложениями равно нулю.");
		}
		print("Количество блоков = " + nCountSpecialOffers);
		
		print("Получаем название предложения для каждого блока спецпредложений");
		wDivSpecialOffers = new WebElement[nCountSpecialOffers];
		wDivSpecialOffers = GetAllWebElements(Proper.GetProperty("sHpathSpecialOffers"));
		if(wDivSpecialOffers.length != nCountSpecialOffers)
		{	
			print("Количество заголовков для спецпредложений не совпало с количество блоков спецпредложений");
			throw new ExceptFailTest("Количество заголовков для спецпредложений не совпало с количество блоков спецпредложений");
		}
		
		print("Получаем стоимость предложения для каждого блока спецпредложений");
		wSpanSpecialOffersPrice = new WebElement[nCountSpecialOffers];
		wSpanSpecialOffersPrice = GetAllWebElements(Proper.GetProperty("sHpathSpecialOffersPrice"));
		if(wSpanSpecialOffersPrice.length != nCountSpecialOffers)
		{	
			print("Количество записей цен для спецпредложений не совпало с количество блоков спецпредложений");
			throw new ExceptFailTest("Количество записей цен для спецпредложений не совпало с количество блоков спецпредложений");
		}
		
		print("Получаем кнопоки \"В корзину\" для блоков спецпредложений");
		wButtonSpecialOffersPrice = new WebElement[nCountSpecialOffers];
		wButtonSpecialOffersPrice = GetAllWebElements(Proper.GetProperty("sHpathButtonBuySpecial"));
		if(wButtonSpecialOffersPrice.length != nCountSpecialOffers)
		{	
			print("Количество кнопок \"В корзину\" для спецпредложений не совпало с количество блоков спецпредложений");
			throw new ExceptFailTest("Количество кнопок \"В корзину\" для спецпредложений не совпало с количество блоков спецпредложений");
		}
		
		// создаем объект каждого спеца и добавляем его в список
		for(int i=0; i<nCountSpecialOffers; i++)
			bList.add(new BlockOffers(wDivSpecialOffers[i], wSpanSpecialOffersPrice[i], wButtonSpecialOffersPrice[i]));
	}
	
	// Проверка Количества блоков спецпредложений, их заголовков, цены и кнопок "в корзину"
	public void CheckBlocksSpesialOffers() throws ExceptFailTest
	{
		BlockOffers oTemp;
		
		print("\r\nПроверяем заголовок, цену, кнопку \"В корзину\" блоков спецпредложений на доступность и видимость");
		for(int i=0; i<bList.size(); i++)
		{
			print("Блок номер " + (i+1));
			oTemp = bList.get(i);
			CheckVisibleElement(oTemp.GetWTitle(), "Заголовок");
			print("Заголовок блока: " + oTemp.GetWTitle().getText());
			CheckVisibleElement(oTemp.GetWCost(), "Стоимость");
			print("Стоимость продукта для данного блока: " + oTemp.GetWCost().getText());
			CheckEnableElement(oTemp.GetWButtonToBasket(),1);
			CheckVisibleElement(oTemp.GetWButtonToBasket(), "Кнопка в корзину");
			print("Кнопка \"В корзину\" для спецпредложения видима и доступна\r\n");
		}
	}
	
	// Добавление спецов в корзину
	public void AddToBasketSpecialOffers() throws ExceptFailTest
	{
		BlockOffers oTempOffers;
		nCostSpecialOffers = new int[nCountSpecialOffers]; // здесь храним значения стоимостей спецов
		
		print("\r\nПроверяем наличие блока \"Корзина\" - c пустой корзиной");
		pagePriceBasket.CheckEmptyBasket();
		print("Корзина пустая - корректно");
		
		print("Добавляем в корзину спецпредложения");
		for(int i=0; i<bList.size(); i++)
		{
			oTempOffers = bList.get(i);
			print("Добавляем в корзину " + (i+1) +" спецпредложение \"" + oTempOffers.GetWTitle().getText() + "\"");
			nCostSpecialOffers[i] = GetIntFromTextInWebElement(oTempOffers.GetWCost());// тянем запись о цене из не удаляем все не цифры и добавляем в массив в котором хранятся цены
			ClickElement(oTempOffers.GetWButtonToBasket());
			print("Проверяем доступность кнопки в \"В корзину\" для добавленного в корзину спецпредложения");
			Sleep(2000);
			ChecksAtributeElement(oTempOffers.GetWButtonToBasket(), "disabled", "true");
			print("Кнопка \"В корзину\" для спецпредложения \"" + oTempOffers.GetWTitle().getText() + "\" недоступна. Корректно.");
		}
		
		print("Проверяем наличие блока \"Корзина\" - с добавлеными заказами");
		pagePriceBasket.CheckFullBasket();
		print("В корзину добавлены заказы. Корректно\r\n");		
	}

	// Сравнение то что добавляли с тем что добавилось
	public void LikeAddedOffersWithOffersInBasket() throws ExceptFailTest
	{
		print("\r\nПолучаем добавленные в корзину предложения, итоговую сумму");
		pagePriceBasket.GetOffersFromBasket(nCountSpecialOffers, Proper.GetProperty("bFlagCompany"));
		print("Сравниваем стоимость каждого предложения из блока \"Корзина\" со стоимостью из блока \"Рекомедуемое\" ");
		pagePriceBasket.CheckOffersPrice(bList, nCostSpecialOffers, "Рекомендуемое");
	}
	
	// Удаление из корзины предложений и проверка что она пустая
	public void DeleteOffersFromBasket() throws ExceptFailTest
	{
		print("\r\nУдаляем предложения из корзины");
		pagePriceBasket.DeleteOffers();
		print("Проверяем что корзина пуста");
		pagePriceBasket.CheckEmptyBasket();
		print("Корзина пустая - корректно");
	}
	
	
	
}
