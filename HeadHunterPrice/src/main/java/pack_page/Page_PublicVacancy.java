package pack_page;

import java.util.ArrayList;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pack_utils.ExceptFailTest;
import pack_utils.Proper;

public class Page_PublicVacancy extends Page
{
	
	Page_PriceBasket pagePriceBasket = PageFactory.initElements(this.driver, Page_PriceBasket.class); // страница hh.ru/price блок корзина
	
	// массив элементов заголовков блоков
	private WebElement wTitlesOffers[];
	// массив элементов поля ввода количества штук
	private WebElement wTextFieldPCS[];
	// массив элементов кнопок "в корзину"
	private WebElement wButtonBasket[];
	// массив итоговых стоимостей
	private WebElement wTextTotalCostTOne[];
	// Ссылка Публикации вакансий
	@FindBy(xpath="//a[@href='#publications' and contains(text(),'Публикации вакансий')]")
	private WebElement LinkPublications;
	// Блок Div с предложениями 
	@FindBy(xpath="//div[@class='b-price2-items g-clear b-price2-section-publications-hh']")
	private WebElement DivBlockPublications;
	
	private int nCountOffersPublicVacancy; // количество отображаемых всего предложений 
	private int nCountAdded; // количество добавленных в корзину предложений 
	private int nTotalCostOffers[]; // сумма цен всех добавленных предложений
	
	private JavascriptExecutor js;
	
	// внутренний для хранения данных о предложениях
	class PublicVacancy
	{
		WebElement wTitle; // заголовок предложения
		WebElement wCountSell[]; // массив  количества штук
		WebElement wPriceForOne[]; // массив стоимости за штуку
		WebElement wTextFieldCost; // поле ввода значения стоимости 
		WebElement wButtonBasket; // кнопка в корзину
		WebElement wTextTotalCost; // итоговая стоимость предложения (кол штук на стоимость)
		
		PublicVacancy(WebElement wTitle, WebElement wCountSell[], WebElement wPriceForOne[], WebElement wTextFieldCost, WebElement wButtonBasket, WebElement wTextTotalCost)
		{
			this.wTitle = wTitle;
			this.wCountSell = wCountSell; 
			this.wPriceForOne = wPriceForOne;
			this.wTextFieldCost = wTextFieldCost;
			this.wButtonBasket = wButtonBasket;
			this.wTextTotalCost = wTextTotalCost;
		}
		
		PublicVacancy(){}
		
		// getter
		WebElement getwTitle() {return wTitle;}
		WebElement[] getwCountSell() {return wCountSell;}
		WebElement[] getwPriceForOne() {return wPriceForOne;}
		WebElement getwTextFieldCost() {return wTextFieldCost;}
		WebElement getwButtonBasket() {return wButtonBasket;}
		WebElement getwTextTotalCost() {return wTextTotalCost;}
		// setter
		void setwTitle(WebElement wTitle) {this.wTitle = wTitle;}
		void setwCountSell(WebElement[] wCountSell) {this.wCountSell = wCountSell;}
		void setwPriceForOne(WebElement[] wPriceForOne) {this.wPriceForOne = wPriceForOne;}
		void setwTextFieldCost(WebElement wTextFieldCost) {this.wTextFieldCost = wTextFieldCost;}
		void setwButtonBasket(WebElement wButtonBasket) {this.wButtonBasket = wButtonBasket;}
		void setwTextTotalCost(WebElement wTextTotalCost) {this.wTextTotalCost = wTextTotalCost;}		
	}

	// внутренний класс для хранения данных о добавленных предложениях в корзину
	class AddedPublicVacancy
	{
		WebElement wTitle; // заголовок
		WebElement wTextTotalCost; // итоговая стоимость предложения (кол штук на стоимость)
		WebElement wButtonBasket; // кнопка в корзину
		
		AddedPublicVacancy(WebElement wTitle,  WebElement wTextTotalCost, WebElement wButtonBasket)
		{
			this.wTitle = wTitle;
			this.wTextTotalCost = wTextTotalCost;
			this.wButtonBasket = wButtonBasket;
		}
		
		AddedPublicVacancy(){}
		
		// getter
		WebElement getwTitle() {return wTitle;}
		WebElement getwTextTotalCost() {return wTextTotalCost;}
		WebElement getwButtonBasket() {return wButtonBasket;}

		// setter
		void setwTitle(WebElement wTitle) {this.wTitle = wTitle;}
		void setwTextTotalCost(WebElement wTextTotalCost) {this.wTextTotalCost = wTextTotalCost;}
		void setwButtonBasket(WebElement wButtonBasket) {this.wButtonBasket = wButtonBasket;}
	}
	
	// список объектов отображаемых предложений
	ArrayList<PublicVacancy> listPublic = new ArrayList<PublicVacancy>();
	// список объектов добавленных предложений
	ArrayList<AddedPublicVacancy> listAddedPublic = new ArrayList<AddedPublicVacancy>();
	
	// Конструктор
	public Page_PublicVacancy(WebDriver driver)
	{
		super(driver);
	}

	// Открытие страницы
	public void OpenPage(String sUrl){}
	
	// Метод проверки выбран ли блок Публикация вакансий
	public void CheckElementPagePublicVacancy() throws ExceptFailTest
	{
		print("Проверка наличия элементов страницы \"Покупка услуг\" блока Доступ к базе резюме".toUpperCase());
		print("Проверяем наличие ссылки \"Доступ к базе резюме\"");
		CheckElementPresent(1, "//a[@href='#publications' and contains(text(),'Публикации вакансий')]");
		print("Ссылка \""+ LinkPublications.getText() + "\" найдена");
		print("Нажимаем ее");
		ClickElement(LinkPublications);
		print("Проверяем что ссылка \"Доступ к базе резюме\" выбрана (color = black)");
		CheckCssElement(LinkPublications, "color", Proper.GetProperty("sColorLinkSelected"));
		print("Ссылка \""+ LinkPublications.getText() + "\" выбрана");
		print("Приверяем наличие блока Публикации ваканси");
		CheckElementPresent(1, "//div[@class='b-price2-items g-clear b-price2-section-publications-hh']");
		CheckEnableElement(DivBlockPublications, 1);
		CheckVisibleElement(DivBlockPublications, "Блок Публикация вакансий");
		print("Блок предложений для блока \"Публикация вакансий\" отображается");
	}

	// Метод получения данных о предложениях Публикация вакансий и создании объяектов на один блок один объект
	@SuppressWarnings("unchecked")
	public void GetBlocksPublicVacancy() throws ExceptFailTest
	{
		print("Получаем данные о предложениях");
		WebElement w1[], w2[];
		PublicVacancy objTemp; // объект данных о предложении
		ArrayList<WebElement> list = new ArrayList<WebElement>(); // массив ссылок количества штук 
		ArrayList<WebElement> list2 = new ArrayList<WebElement>(); // массив текста стоимостей за штуку
		
		wTitlesOffers = this.GetAllWebElements(Proper.GetProperty("sXpathTitlesPublicVacancy")); // получаем заголовки
		wTextFieldPCS = this.GetAllWebElements(Proper.GetProperty("sXpathTextFieldPCSPublicVacancy")); // получаем поля ввода кол. штук
		wButtonBasket = this.GetAllWebElements(Proper.GetProperty("sXpathButtonBasketPublicVacancy")); // получаем кнопки в корзину
		wTextTotalCostTOne = this.GetAllWebElements(Proper.GetProperty("sXpatnTextTotalCostOnePublicVacancy")); // итоговые стоимости предложений

		Sleep(1000);
		js = (JavascriptExecutor) driver;
		// количество блоков с выбором стоимостей(те блоки которые видны у них есть ссылки стоимостей за количество)
		long lCountPublic = (Long) js.executeScript("return document.getElementsByClassName('b-price2-item-discount-table').length");
		if(lCountPublic == 0) // если не получили с первого раза то перепроверяем
			lCountPublic = (Long) js.executeScript("return document.getElementsByClassName('b-price2-item-discount-table').length");
		if(lCountPublic == 0)
		{
			print("Не удалось поллучить блоки с вакансиями");
			throw new ExceptFailTest("Не удалось получить блоки с публикациями вакансий");
		}
		nCountOffersPublicVacancy = (int)lCountPublic;
		print("Количество отображаемых блоков предложений \"Публикации вакансий\" - " + lCountPublic); // количество блоков с выбором стоимостей
		
		//проверяем что других элементов в блоках не меньше чем самих блоков
		if((wTitlesOffers.length < lCountPublic) || (wTextFieldPCS.length < lCountPublic) || (wButtonBasket.length < lCountPublic) || (wTextTotalCostTOne.length < lCountPublic))
		{
			print("В отображаемых блоках не хватает основных элементов (заголовка, поля ввода количества штук, кнопки или итоговой стоимости)");
			throw new ExceptFailTest("В отображаемых блоках не хватает основных элементов (заголовка, поля ввода количества штук, кнопки или итоговой стоимости)");
		}
		
		
		// получаем данные по одному блоку и заносим в лист объектов предложений
		for(int i=0; i<lCountPublic; i++)
		{
			// лист элементов выбора количества штук
			list =  (ArrayList<WebElement>) js.executeScript("return document.getElementsByClassName('b-price2-item-discount-table')["  + i + "].getElementsByTagName('span');");
			// лист элементов стоимостей за штуку 
			list2 = (ArrayList<WebElement>) js.executeScript("return document.getElementsByClassName('b-price2-item-discount-table')[" + i + "].getElementsByTagName('td');");
			w1 = list.toArray(new WebElement[list.size()]);
			w2 = list2.toArray(new WebElement[list2.size()]);
			// создаем объект данных предложения
			objTemp = new PublicVacancy(wTitlesOffers[i], w1, w2, wTextFieldPCS[i], wButtonBasket[i], wTextTotalCostTOne[i]);
			// добавлеяем его в список
			listPublic.add(objTemp);
		}
		print("Данные получены");	
	}
	
	
	public void AddToBasketPublicVacancy() throws ExceptFailTest
	{
		PublicVacancy objTempPV; // данных о предложениях
		AddedPublicVacancy objTempAPV; // данные о добавленных предложениях
		
		WebElement wTitleTemp, wTotalCostTemp, wButtonTemp; // сюда заголовок, итоговую стоимость, кнопку 
		
		print("Проверяем наличие блока \"Корзина\" - c пустой корзиной");
		pagePriceBasket.CheckEmptyBasket();
		print("Корзина пустая - корректно");
		
		print("\r\nДобавляем рандомное число предложений в корзину (не больше общего количества доступных)");
		nCountAdded = GetRandomNumber(nCountOffersPublicVacancy - 1) + 1; // получили это число
		print("Добавляем ".toUpperCase() + nCountAdded + " предложений\r\n".toUpperCase());
		
		nTotalCostOffers = new int[nCountAdded]; // сюда складываем все стоимости итоговых предложений
		
		for(int i=0; i<nCountAdded; i++)
		{
			// Получаем i объект предложения 
			objTempPV = listPublic.get(i); 
			// получаем заголовок предложения
			wTitleTemp = objTempPV.getwTitle(); // заголовок
			print("Добавляем предожение - " + wTitleTemp.getText());
			// выбираем количество штук для предложения
			ChoosePCSClickOrEnter(GetRandomNumber(2), objTempPV); // выбираем количество штук
			// получаем итоговую стоимость
			wTotalCostTemp = objTempPV.getwTextTotalCost(); // итоговая стоимость
			nTotalCostOffers[i] = GetIntFromTextInWebElement(wTotalCostTemp); // складываем итоговую стоимость каждого предложения в массив 
			print("Итоговая стоимость - " + wTotalCostTemp.getText());
			// получаем кнопку В корзину
			wButtonTemp = objTempPV.getwButtonBasket(); // кнопка в корзину
			print("Проверяем доступность кнопки \"В корзину\"");
			CheckEnableElement(wButtonTemp, 1); 
			print("Кнопка \"В корзину\" доступна");
			print("Нажимаем ее\r\n");
			ClickElement(wButtonTemp);
			Sleep(2000);
			// получаем объект добавленного в корзину предложения
			objTempAPV = new AddedPublicVacancy(wTitleTemp, wTotalCostTemp, wButtonTemp);
			// добавляем объект в список 
			listAddedPublic.add(objTempAPV);
		}
		
		print("Проверяем наличие блока \"Корзина\" - с добавлеными заказами");
		pagePriceBasket.CheckFullBasket();
		print("В корзину добавлены заказы. Корректно\r\n");
	}
	
	// Сравнение того что добавлялось с тем что отображается в корзине
	public void LikeAddedOffersWithOffersInBasket() throws ExceptFailTest
	{
	
			print("Получаем добавленные в корзину предложения, итоговую сумму. Сравниваем данные");
			pagePriceBasket.GetOffersFromBasket(nCountAdded, Proper.GetProperty("bFlagCompany"));
			pagePriceBasket.CheckOffersPricePublicVacancy(listAddedPublic, nTotalCostOffers);
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
	
	// метод выбора как добавить количество штук ввести в поле или кликнуть на ссылку количества
	private void ChoosePCSClickOrEnter(int nFlag, PublicVacancy obj)
	{
		WebElement wTemp, wTemp1[], wTemp2[];
		int nSelect; // выбранная номер ссылки количества штук
		int nEnteredPCS = 0; // количество введенных штук
		if(nFlag == 0) // выбираем кликом
		{
			wTemp1 = obj.getwCountSell(); //  массив количества штук 
			wTemp2 = obj.getwPriceForOne(); // массив стоимостей за штуку
			print("Выбираем количество вакансий кликом на отображаемые варианты значений");
			print("Отображается:");
			for(int i=0; i<wTemp1.length; i++)
			{
				print("от " + wTemp1[i].getText() + " по " + wTemp2[i].getText());
			}
			nSelect = GetRandomNumber(wTemp1.length);
			print("ВЫБИРАЕМ " + wTemp1[nSelect].getText() + " по " + wTemp2[nSelect].getText());
			ClickElement(wTemp1[nSelect]);
		}
		else // вводим сами
		{
			wTemp = obj.getwTextFieldCost(); // поде ввода количества штук
			print("Выбираем количество вакансий вводом своего значения количества штук");
			while(nEnteredPCS == 0)
				nEnteredPCS =  GetRandomNumber(1000); // можем ввести до 100 штук вакансий
			print("ВВОДИМ " + nEnteredPCS + " штук(и)");
			SendText(wTemp, ((Integer)nEnteredPCS).toString());
		}
	}
	
	
	
}
