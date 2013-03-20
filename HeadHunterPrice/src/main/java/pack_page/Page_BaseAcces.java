package pack_page;

import java.util.ArrayList;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pack_utils.ExceptFailTest;
import pack_utils.Proper;

public class Page_BaseAcces extends Page
{
	Page_PriceBasket pagePriceBasket = PageFactory.initElements(this.driver, Page_PriceBasket.class); // страница hh.ru/price блок корзина
	
	// Ссылка Доступ к базе резюме
	@FindBy(xpath="//a[@href='#dbaccess' and contains(text(),'Доступ к базе резюме')]")
	private WebElement LinkAccess;
	// Блок с чекбоксами
	@FindBy(xpath="//div[@class='b-price2-section-body']")
	private WebElement DivPriceShop;
	// Div  с радиобатонами с помощью его проверяем полную загрузку радиобатонов
	@FindBy(xpath="//div[@class='b-price2-dbaccess-list']")
	private WebElement DivRadioButtonWait;
	// Кнопка "В корзину"
	@FindBy(xpath="//input[@class='HH-Price-ResumeAccessChooser-AddToCart']")
	private WebElement ButtonToBasket;
	
	// Массив элементов имен чекбоксов регионов
	private WebElement wCheckBoxNamesRegions[];
	// Массив элементов чекбоксов регионов
	private WebElement wCheckBoxRegions[];
	// Массив элементов имен чекбоксов профессий
	private WebElement wCheckBoxNamesProf[];
	// Массив элементов чекбоксов профессий
	private WebElement wCheckBoxProf[];
	// Массив элементов стоимостей к радиобатону
	private WebElement wRadioButtonCostPrice[];
	// Массив элементов радиобатонов цен
	private WebElement wRadioButtonCost[];
	// Массив элементов радиобатонов дней прордуктов
	private WebElement wRadioButtonCostDay[];
	int nNumberRegion, nNumberProf, nNumberCost; // номера выбранных региона, профессии, стоимости (из отображаемых)

	//внутр. класс храним данные о добавленных предложениях
	class AccesBaseOffers
	{
		WebElement wNamesRegions; // навзание региона
		WebElement wNamesProf; // выбранная профессия
		WebElement wCostPrice; // стоимость предложения
		WebElement wCostDay; // количество дней
		
		AccesBaseOffers(WebElement wNamesRegions, WebElement wNamesProf, WebElement wCostPrice, WebElement wCostDay)
		{
			this.wNamesRegions = wNamesRegions;
			this.wNamesProf = wNamesProf;
			this.wCostPrice = wCostPrice;
			this.wCostDay = wCostDay;
		}
		
		AccesBaseOffers(){}
	
		WebElement GetWNamesRegions() {return wNamesRegions;}
		WebElement GetWNamesProf() {return wNamesProf;}
		WebElement GetWCostPrice() {return wCostPrice;}
		WebElement GetWCostDay() {return wCostDay;}
		
		void SetWNamesRegions(WebElement wEl) {wNamesRegions = wEl;}
		void SetWNamesProf(WebElement wEl) {wNamesProf = wEl;}
		void SetWCostPrice(WebElement wEl) {wCostPrice = wEl;}
		void SetWCostDay(WebElement wEl) {wCostDay = wEl;}
	}
	
	// список объектов с предложениями
	ArrayList<AccesBaseOffers> bList = new ArrayList<AccesBaseOffers>();
	
	// объект предложения
	AccesBaseOffers oTemp = new AccesBaseOffers();
	
	// конструктор
	public Page_BaseAcces(WebDriver driver)
	{
		super(driver);
	}

	public void OpenPage(String sUrl){}

	// Метод проверки выбран ли блок Рекомендуемое
	public void CheckElementPageBaseAcces() throws ExceptFailTest
	{
		print("Проверка наличия элементов страницы \"Покупка услуг\" блока Доступ к базе резюме".toUpperCase());
		print("Проверяем наличие ссылки \"Доступ к базе резюме\"");
		CheckElementPresent(1, "//a[@href='#dbaccess' and contains(text(),'Доступ к базе резюме')]");
		print("Ссылка \""+ LinkAccess.getText() + "\" найдена");
		print("Нажимаем ее");
		ClickElement(LinkAccess);
		print("Проверяем что ссылка \"Доступ к базе резюме\" выбрана (color = black)");
		CheckCssElement(LinkAccess, "color", Proper.GetProperty("sColorLinkSelected"));
		print("Ссылка \""+ LinkAccess.getText() + "\" выбрана");
		print("Приверяем наличие заголовка блока с чекбоксами предложений");
		CheckElementPresent(1, "//div[@class='b-price2-section-body']");
		CheckEnableElement(DivPriceShop, 1);
		CheckVisibleElement(DivPriceShop, "Блок чекбоксов");
		print("Блок с чекбоксами предложений блока \"Доступ к базе резюме\" отображается");
	}
	// Получение списка отображаемых чекбоксов Регионов 
	public void GetRegionsCheckBox() throws ExceptFailTest
	{
		print("\r\nПолучаем список чекбоксов Регионов");
		wCheckBoxNamesRegions = this.GetAllWebElements(Proper.GetProperty("sHpathCheckBoxNamesRegions")); // название регионов рядом счекбоксами
		wCheckBoxRegions = this.GetAllWebElements(Proper.GetProperty("sHpathCheckBoxRegions")); // сами чекбоксы
		if((wCheckBoxNamesRegions.length != wCheckBoxRegions.length) | (wCheckBoxNamesRegions.length == 0) | (wCheckBoxRegions.length == 0))
		{
			
			print("Количество чебоксов регионов не сопало с количеством названий данных чекбоксов или их количество равно нулю");
			throw new ExceptFailTest("Количество чебоксов регионов не сопало с количеством названий данных чекбоксов или их количество равно нулю");
		}
		print("Чекбоксы регионов получены");
	}
	
	// Получение списка отображаемых активных чекбоксов Профессий
	public void GetProfCheckBox() throws ExceptFailTest
	{
		ArrayList<WebElement[]> wList;
		
		// получаем все чекбоксы и текст рядом сними
		print("\r\nПолучаем список чекбоксов профессий");
		wCheckBoxNamesProf = GetAllWebElements(Proper.GetProperty("sHpathCheckBoxNamesProf"));
		wCheckBoxProf = GetAllWebElements(Proper.GetProperty("sHpathCheckBoxProf"));
		if((wCheckBoxNamesProf.length != wCheckBoxProf.length) | (wCheckBoxNamesProf.length == 0) | (wCheckBoxProf.length == 0))
		{
			
			print("Количество чекбоксов профессий не сопало с количеством названий данных чекбоксов или их количество равно нулю");
			throw new ExceptFailTest("Количество чебоксов профессий не сопало с количеством названий данных чекбоксов или их количество равно нулю");
		}
		print("Все чекбоксы профессий получены");
		
		// теперь получаем только активные
		print("Получаем активные чекбоксы профессий и их названия");
		wList = GetActiveCheckBoxAndTheirNames(wCheckBoxProf, wCheckBoxNamesProf);
		wCheckBoxProf = wList.get(0); // массив активных чекбоксов
		wCheckBoxNamesProf = wList.get(1); // массив  названий к ним
		
		print("Активные чекбоксы профессий получены");
	}
	
	// Получение списка отображаемых радиобатонов
	public void GetPriceRadioButton() throws ExceptFailTest
	{	
		print("\r\nПолучаем список радиобатонов стоимостей доступных для оплаты");
		wRadioButtonCostPrice = GetAllWebElements(Proper.GetProperty("sXPathRadioButtonCostPrice"));
		wRadioButtonCost = GetAllWebElements(Proper.GetProperty("sXpathRadioButtonCost"));
		wRadioButtonCostDay = GetAllWebElements(Proper.GetProperty("sXpathRadioButtonCostDay"));
		if( (wRadioButtonCostPrice.length != wRadioButtonCost.length) | (wRadioButtonCostPrice.length == 0) | (wRadioButtonCost.length == 0))
		{
			print(wRadioButtonCostPrice.length);
			print(wRadioButtonCost.length);
			print("Количество цен для радиобатонов не сопало с количеством данных радиобатонов или их количество равно нулю");
			throw new ExceptFailTest("Количество цен для радиобатонов не сопало с количеством данных радиобатонов или их количество равно нулю");
		}
		print("Радиобатоны цен получены");
	}
	// Выбор региона
	public void ChooseRegion() throws ExceptFailTest
	{
		print("\r\nВыбираем рандомный регион из списка отображаемых");
		nNumberRegion = GetRandomNumber(wCheckBoxNamesRegions.length);
		this.ScrollToElement(wCheckBoxNamesRegions[nNumberRegion]);
		ClickElement(wCheckBoxNamesRegions[nNumberRegion]);
		CheckIsSelectedElement(wCheckBoxRegions[nNumberRegion], true, "чекбокс");
		print("Выбран регион " + wCheckBoxNamesRegions[nNumberRegion].getText());
		oTemp.SetWNamesRegions(wCheckBoxNamesRegions[nNumberRegion]); // добавили в объект предложения данные о регионе
	}
	// Выбор профессии
	public void ChooseProf() throws ExceptFailTest
	{
		print("\r\nВыбираем рандомную профессию из списка отображаемых(активные чекбоксы)");
		nNumberProf = GetRandomNumber(wCheckBoxNamesProf.length);
		ScrollToElement(wCheckBoxNamesProf[nNumberProf]);
		ClickElement(wCheckBoxNamesProf[nNumberProf]);
		CheckIsSelectedElement(wCheckBoxProf[nNumberProf], true, "чекбокс");
		print("Выбран профессия " + wCheckBoxNamesProf[nNumberProf].getText());
		ChecksAtributeElement(DivRadioButtonWait, "class", "b-price2-dbaccess-list"); // ожидаем загрузки блока с радиобатонами цен (изменение значения атрибута)
		oTemp.SetWNamesProf(wCheckBoxNamesProf[nNumberProf]); // добавили в объект предложения данные о профессии
	}
	// Выбор Стоимости
	public void ChooseCost() throws ExceptFailTest
	{
		print("\r\nВыбираем рандомную стоимость из списка отображаемых");
		nNumberCost =  GetRandomNumber(wRadioButtonCostPrice.length);
		ClickElement(wRadioButtonCostPrice[nNumberCost]);
		CheckIsSelectedElement(wRadioButtonCost[nNumberCost], true, "радиобатон");
		print("Выбран продукт стоимостью " + wRadioButtonCostPrice[nNumberCost].getText() + " на "
				+ wRadioButtonCostDay[nNumberCost].getText());
		
		oTemp.SetWCostPrice(wRadioButtonCostPrice[nNumberCost]); // добавили в объект предложения данные о стоимости
		oTemp.SetWCostDay(wRadioButtonCostDay[nNumberCost]); // добавили в объект предложения данные о количестве дней
	}
	// Добавление в корзину
	public void AddOffersToBasket() throws ExceptFailTest
	{
		
		print("\r\nПроверяем наличие блока \"Корзина\" - c пустой корзиной");
		pagePriceBasket.CheckEmptyBasket();
		print("Корзина пустая - корректно");
		
		print("Добавляем выбранное предложение в корзину");
		print("Проверяем отображение и доступность кнопки \"В корзину\"");
		CheckElementPresent(1, "//input[@class='HH-Price-ResumeAccessChooser-AddToCart']");
		CheckEnableElement(ButtonToBasket, 1);
		print("Кнопка отображается и доступна");
		print("Нажимаем ее");
		ClickElement(ButtonToBasket);
		Sleep(2000);
		print("Проверяем что кнопка \"В корзину\" более не доступна");
		CheckEnableElement(ButtonToBasket, 2);
		print("Кнопка не доступна. Корректно");
		
		print("Проверяем наличие блока \"Корзина\" - с добавлеными заказами");
		pagePriceBasket.CheckFullBasket();
		print("В корзину добавлены заказы. Корректно\r\n");
		
		bList.add(oTemp); // добавили в лист наше предложение
		
	}
	// Сравнение того что добавлялось с тем что отображается в корзине
	public void LikeAddedOffersWithOffersInBasket() throws ExceptFailTest
	{
		print("\r\nПолучаем добавленные в корзину предложения, итоговую сумму. Сравниваем полученные данные");
		pagePriceBasket.GetOffersFromBasket(1, Proper.GetProperty("bFlagCompany"));
		pagePriceBasket.GetTextForAddedOffers(1);
		pagePriceBasket.CheckOffersPriceAccesBase(bList);
		
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
		
	//Получение массива активных чекбоксов и их названий
	private ArrayList<WebElement[]>  GetActiveCheckBoxAndTheirNames(WebElement wEl[], WebElement wEl2[]) throws ExceptFailTest
	{
		ArrayList<WebElement> wListCh = new ArrayList<WebElement>(); // здесь сами чекбоксы активные
		ArrayList<WebElement> wListAc = new ArrayList<WebElement>(); // здесь названия или текст рядом с ними
		ArrayList<WebElement[]> wMasList = new ArrayList<WebElement[]>(); // 
		
		WebElement wTempCh[], wTempAc[];
		int nCountActive = 0;
		
		for(int i=0; i<wEl.length; i++)
		{
			if(wEl[i].isEnabled())
			{
				nCountActive++;
				wListCh.add(wEl[i]);
				wListAc.add(wEl2[i]);
			}
		}
		if(nCountActive > 0)
		{
			wTempCh = new WebElement[nCountActive];
			wTempAc = new WebElement[nCountActive];
		}
		else
		{
			print("Нет ни одного активного чекбокса");
			throw new ExceptFailTest("Нет ни одного активного чекбокса");
		}
		
		for(int i=0; i<wListCh.size(); i++)
		{
			wTempCh[i] = wListCh.get(i);
			wTempAc[i] = wListAc.get(i);
			
		}
		
		wMasList.add(wTempCh);
		wMasList.add(wTempAc);
		
		return wMasList;
	}
	
	
}
