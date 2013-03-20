package pack_page;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pack_page.Page_BaseAcces.AccesBaseOffers;
import pack_page.Page_PriceRecommended.BlockOffers;
import pack_page.Page_PublicVacancy.AddedPublicVacancy;
import pack_utils.ExceptFailTest;
import pack_utils.Proper;

public class Page_PriceBasket extends Page
{
	// блок корзина пустая
	@FindBy(xpath="//div[contains(@class, 'HH-Price-CartRightView-Empty')]")
	private WebElement wDivEmptyBasket;
	// блок корзина не пустая
	@FindBy(xpath="//div[contains(@class, 'HH-Price-CartRightView-Nonempty')]")
	private WebElement wDivBasket;
	// Текст с итоговой ценой
	@FindBy(className="HH-Price-CartRightView-Cost")
	private WebElement wStrongTotalPrice;
	// Кнопка перейти к оплате
	@FindBy(xpath="//input[@class='g-button m-button_green m-button_big price-cart__button HH-Price-CartRightView-ProceedToCheckout']")
	private WebElement wInputProceedToCheckout;
	// Первая ссылка убрать
	@FindBy(xpath="//ol[@class='price-cart__items']/li//small")
	private WebElement wLinkFirstRemove;
	// Текст с размером доп скидки
	@FindBy(xpath="//strong[@class='HH-Price-CartRightView-DiscountPercent']")
	private WebElement wTextDiscount;
	
	
	/////////////////////////// заголовки предложений
	String s = "//span[@class='price-cart__item-title']";
	private WebElement wTextTitle[];
	//////////////////////////
	
	// массив ссылок "убрать"
	private WebElement wSmallLinksRemove[];
	// массив цен стоимости предложений в блоке корзина 
	private WebElement wSpanCosts[];
	// массив поясняющих текстов к предложениям
	private WebElement wDivTextForOffer[];
	
	
	// конструктор
 	public Page_PriceBasket(WebDriver driver)
	{
		super(driver);
	}

	// открытие страницы
	public void OpenPage(String sUrl) {}

	// проверка пустой корзины
	public void CheckEmptyBasket() throws ExceptFailTest
	{
		CheckElementPresent(1, "//div[contains(@class, 'HH-Price-CartRightView-Empty')]");
		ScrollToElement(wDivEmptyBasket);
		CheckVisibleElement(wDivEmptyBasket, "Корзина пуста");
	}
	
	// проверка полной корзины
	public void CheckFullBasket() throws ExceptFailTest
	{
		CheckElementPresent(1, "//div[contains(@class, 'HH-Price-CartRightView-Nonempty')]");
		ScrollToElement(wDivBasket);
		CheckVisibleElement(wDivBasket, "В корзине");
	}
	
	// получение данных о предложениях в блоке корзина (цена и итоговая стоимость)  (для всех вкладок)
	public void GetOffersFromBasket(int nCountAddedOffers, String bFlagCompany) throws ExceptFailTest
	{
		// заголовки
		wTextTitle = GetAllWebElements("//span[@class='price-cart__item-title']");
		wTextTitle = GetAllWebElements("//span[@class='price-cart__item-title']");
		if(wTextTitle.length != nCountAddedOffers)
		{	
			print("Количество заголовков блоков предложений в блоке \"Корзина\" - "+ wTextTitle.length +" не равны значению, добавленных предложений - " + nCountAddedOffers);
			throw new ExceptFailTest("Количество заголовков блоков в блоке \"Корзина\" - "+ wTextTitle.length +" не равны значению, добавленных предложений - " + nCountAddedOffers);
		}
		print("Количество заголовков блоков предложений в блоке \"Корзина\" - "+ wTextTitle.length +" равны значению, добавленных предложений - " + nCountAddedOffers);
		
		// стоимость
		wSpanCosts = GetAllWebElements(Proper.GetProperty("sHpathAddedOffersBasket"));
		if(wSpanCosts.length != nCountAddedOffers)
		{	
			print("Количество записей с ценой, в блоке \"Корзина\" - " + wSpanCosts.length + ", не совпало с количеством добавленных предложений - " + nCountAddedOffers);
			throw new ExceptFailTest("Количество записей с ценой, в блоке \"Корзина\" - " + wSpanCosts.length + ", не совпало с количеством добавленных предложений - " + nCountAddedOffers);
		}
		print("Количество записей с ценой, в блоке \"Корзина\" - " + wSpanCosts.length + ", совпало с количеством добавленных предложений - " + nCountAddedOffers);
		
		// ссылки убрать
		wSmallLinksRemove = GetAllWebElements(Proper.GetProperty("sHpathLinkRemoveOffersBasket"));
		if(wSmallLinksRemove.length != nCountAddedOffers)
		{	
			print("Количество ссылок \"Убрать \" в блоке \"Корзина\" - "+ wSmallLinksRemove.length +" не равны значению, добавленных предложений - " + nCountAddedOffers);
			throw new ExceptFailTest("Количество ссылок \"Убрать \" в блоке \"Корзина\" - "+ wSmallLinksRemove.length +" не равны значению, добавленных предложений - " + nCountAddedOffers);
		}
		print("Количество ссылок \"Убрать \" в блоке \"Корзина\" - "+ wSmallLinksRemove.length +" равны значению, добавленных предложений - " + nCountAddedOffers);
		
		// итоговая стоимость
		CheckElementPresent(4, "HH-Price-CartRightView-Cost");
		print("Итоговая стоимость в блоке \"Корзина\" получена - " + wStrongTotalPrice.getText());
		
		//  кнопка перейти к оплате (только для компании)
		if(bFlagCompany.equals("true"))
		{
			CheckElementPresent(1, "//input[@class='g-button m-button_green m-button_big price-cart__button HH-Price-CartRightView-ProceedToCheckout']");
			CheckVisibleElement(wInputProceedToCheckout, "Перейти к оплате");
			print("Кнопка \"Перейти к оплате\" отображается и доступна");
		}
		
	}
	
	// получение поясняющего текста к предложению  в корзине(для предложений доступ к базе резюме)
	public void GetTextForAddedOffers(int nCountAddedOffers) throws ExceptFailTest
	{
		wDivTextForOffer = new WebElement[nCountAddedOffers];
		wDivTextForOffer = GetAllWebElements(Proper.GetProperty("sHpathTextForOffers"));
		if(wDivTextForOffer.length != nCountAddedOffers)
		{	
			print("Количество записей поясняющего текста, в блоке \"Корзина\",  не совпало с количеством добавленных предложений");
			throw new ExceptFailTest("Количество записей поясняющего текста, в блоке \"Корзина\",  не совпало с количеством добавленных предложений");
		}
		print("Записи поясняющего текста добавленных в блок \"Корзина\" предложений получены");
	}
	
	// сравниваем цены предложений с ценами в блоке корзина, сравнение итоговой суммы (для 1 вкладки Спецпередложения)
	public void CheckOffersPrice(ArrayList<BlockOffers> bList, int nCostSpecialOffers[], String sNameFrame) throws ExceptFailTest
	{
		BlockOffers oTemp; // объект предложения (для 1, 3, 4 вкладки)
		WebElement wTitle, wCost;
		int nCostRecommended, nCostBasket; 
		for(int i=0; i<bList.size(); i++) // получаем каждый объект предложения который мы добавляли в корзину
		{
			oTemp = bList.get(i); // получили один объект
			wTitle = oTemp.GetWTitle();
			wCost =  oTemp.GetWCost();
			
			print("Проверяем совпадение стоимости " + (i+1) + " предложения блока \"Спецпредложения\" под названием - \"" + wTitle.getText().toUpperCase() +
					"\" со стоимостью " + (i+1) + " записи в блоке \"Корзина\" под названием - \"" + wTextTitle[i].getText().toUpperCase() + "\"");
			nCostRecommended = GetIntFromTextInWebElement(wCost); // получили стоимость для предложения
			nCostBasket = GetIntFromTextInWebElement(wSpanCosts[i]); // получили стоимость этого предложения но в корзине
			if(nCostRecommended == nCostBasket) // сравнили стоимость из блока с предложениями со стоимостью в корзине
				print("Cтоимость " + (i+1) + " предложения блока \"" + sNameFrame + "\" равное " + nCostRecommended +
						" совпало со стоимостью " + (i+1) + " записи равное " + nCostBasket + " в блоке \"Корзина\"");	
			else
			{
				print("Cтоимость " + (i+1) + " предложения блока \"" + sNameFrame + "\" равное " + nCostRecommended +
						" не совпало со стоимостью " + (i+1) + " записи равное " + nCostBasket + " в блоке \"Корзина\"");	
				throw new ExceptFailTest("Стоимости не совпали");
			}
		}
		
		// сравниваем итоговую сумму всех добавленных предложений в корзину с итоговой суммой отображаемой в корзине
		print("Проверяем совпадение суммы стоимостей добавленных предложений из блока "+ sNameFrame +" с итоговой суммой в блоке \"Корзина\"");
		nCostRecommended = GetTotalSumm(nCostSpecialOffers); // получили сумму стоимости всех добавленных предложений 
		nCostBasket = GetIntFromTextInWebElement(wStrongTotalPrice); // получили итоговую стоиммость из корзины
		if(nCostRecommended == nCostBasket)
		{
			print("Сумма стоимостей предложений блока \"" + sNameFrame + "\" равное " + nCostRecommended +
					" совпало с итоговой стоимостью равной " + nCostBasket + ", записей предложений в блоке \"Корзина\"");	
		}
		else
		{
			print("Сумма стоимостей предложений блока \"" + sNameFrame + "\" равное " + nCostRecommended +
					" не совпало с итоговой стоимостью равной " + nCostBasket + ", записей предложений в блоке \"Корзина\"");	
			throw new ExceptFailTest("Стоимости не совпали");
		}
		
		
	}
	
	// сравнение цены, итоговой суммы , деталей  добавленного предложения с тем что отображается в корзине (для 2 вкладки Доступ к базе резюме)
	public void CheckOffersPriceAccesBase(ArrayList<AccesBaseOffers> bList) throws ExceptFailTest
	{
		AccesBaseOffers oTemp; // объект предложений (для 2 вкладки)
		int nCost, nCostBasket; 
		String sRegion, sDay, sProf, sText;
		for(int i=0; i<bList.size(); i++) // получаем каждый объект предложения который мы добавляли в корзину (в нашем случае я добавлял только один)
		{
			oTemp = bList.get(i);
			// 1 - Сравниваем стоимость
			print("Проверяем совпадение стоимости добавленного предложения блока \"Доступ к базе резюме\" со стоимостью  записи в блоке \"Корзина\"");
			nCost =  GetIntFromTextInWebElement(oTemp.GetWCostPrice()); // получили стоимость для предложения
			nCostBasket = GetIntFromTextInWebElement(wSpanCosts[i]);  // получили стоимость этого предложения но в корзине
			if(nCost == nCostBasket) // сравнили стоимость из блока с предложениями со стоимостью в корзине
				print("Cтоимость добавленного предложения блока \"Доступ к базе резюме\" равное " + nCost +
						" совпало со стоимостью записи равное " + nCostBasket + " в блоке \"Корзина\"");	
			else
			{
				print("Cтоимость добавленного предложения блока \"Доступ к базе резюме\" равное " + nCost +
						" не совпало со стоимостью записи равное " + nCostBasket + " в блоке \"Корзина\"");	
				throw new ExceptFailTest("Стоимости не совпали");
			}
			// 2 - Сравниваем регионы
			print("Ищем в пояняющем тексте, для предложения отображаемого в корзине, текст с регионом который был выбран");
			print("Ищем регион - " + oTemp.GetWNamesRegions().getText());
			print("В поясняющем тексте - " + wDivTextForOffer[i].getText());
			sRegion = oTemp.GetWNamesRegions().getText().toLowerCase();
			sText =  wDivTextForOffer[i].getText().toLowerCase();
			if(sText.indexOf(sRegion) != -1)
				print("Текст с названием региона содержится в пояcняющем тексте для предложения в корзине. Коректно.");
			else
			{
				throw new ExceptFailTest("Текст с названием региона не содержится в пояcняющем тексте для предложения в корзине.");
			}
			// 3 - Сравниваем количество дней
			print("Ищем в пояняющем тексте, для предложения отображаемого в корзине, текст с количеством дней для предложения которое было выбрано");
			print("Ищем количество дней - " + oTemp.GetWCostDay().getText());
			print("В поясняющем тексте - " + wDivTextForOffer[i].getText());
			sDay = oTemp.GetWCostDay().getText().toLowerCase();
			if(sText.indexOf(sDay) != -1)
				print("Текст с количество дней для предложения, содержится в пояcняющем тексте для предложения в корзине. Коректно.");
			else
			{
				throw new ExceptFailTest("Текст с количество дней для предложения не содержится в пояcняющем тексте для предложения в корзине.");
			}
			// 4 - Сравнение профессии
			print("Ищем в пояняющем тексте, для предложения отображаемого в корзине, текст названия профессии для предложения которое было выбрано");
			print("Ищем название профессии - " + oTemp.GetWNamesProf().getText());
			print("В поясняющем тексте - " + wDivTextForOffer[i].getText());
			sProf =  oTemp.GetWNamesProf().getText().toLowerCase();
			if(sText.indexOf(sProf) != -1)
				print("Текст название профессии для предложения, содержится в пояcняющем тексте для предложения в корзине. Коректно.");
			else
			{
				throw new ExceptFailTest("Текст название профессии для предложения, не содержится в пояcняющем тексте для предложения в корзине.");
			}
			// 5 - Сравнение итоговой стоимости 
			print("Проверяем совпадение суммы стоимостей добавленных предложений из блока \"Доступ к базе резюме\" с итоговой суммой в блоке \"Корзина\"");
			nCost = GetIntFromTextInWebElement(oTemp.GetWCostPrice()); 
			nCostBasket = GetIntFromTextInWebElement(wStrongTotalPrice); // получили итоговую стоиммость из корзины
			if(nCost == nCostBasket)
			{
				print("Сумма стоимостей предложений блока \"Доступ к базе резюме\" равное " + nCost +
						" совпало с итоговой стоимостью равной " + nCostBasket + ", записей предложений в блоке \"Корзина\"");	
			}
			else
			{
				print("Сумма стоимостей предложений блока \"Доступ к базе резюме\" равное " + nCost +
						" не совпало с итоговой стоимостью равной " + nCostBasket + ", записей предложений в блоке \"Корзина\"");	
				throw new ExceptFailTest("Стоимости не совпали");
			}
		}
	}
	
	
	// сравнение цены, итоговой суммы, добавленных предложений с тем что отображается в корзине (для 3 вкладки Публикации вакансий)
	public void CheckOffersPricePublicVacancy(ArrayList<AddedPublicVacancy> bList, int nTotalCostOffers[]) throws ExceptFailTest
	{
		AddedPublicVacancy oTemp; // объект предложений добавленных в корзину (для 3 вкладки)
		int nCost, nCostBasket; // стоимость предложения, стоимость его же в корзине
		WebElement wTitle, wCost; // заголовок, cтоимость
		for(int i=0; i<bList.size(); i++) // получаем каждый объект предложения который мы добавляли в корзину
		{
			oTemp = bList.get(i);
			wTitle = oTemp.getwTitle(); // заголовок
			wCost = oTemp.getwTextTotalCost(); // стоимость
			// 1 - Сравниваем стоимость
			print("Проверяем совпадение стоимости "+ (i+1) +" добавленного предложения блока \"Публикация вакансий\" под названием - \"" +
					wTitle.getText().toUpperCase() + "\" со стоимостью записи в блоке \"Корзина\" с заголовком  - \"" + wTextTitle[i].getText().toUpperCase() +"\"");
			nCost =  GetIntFromTextInWebElement(wCost); // получили стоимость для предложения
			nCostBasket = GetIntFromTextInWebElement(wSpanCosts[i]);  // получили стоимость этого предложения но в корзине
			if(nCost == nCostBasket) // сравнили стоимость из блока с предложениями со стоимостью в корзине
				print("Cтоимость добавленного предложения блока \"Публикация вакансий\" равное " + nCost +
						" совпало со стоимостью записи равное " + nCostBasket + " в блоке \"Корзина\"");	
			else
			{
				print("Cтоимость добавленного предложения блока \"Публикация вакансий\" равное " + nCost +
						" не совпало со стоимостью записи равное " + nCostBasket + " в блоке \"Корзина\"");	
				throw new ExceptFailTest("Стоимости не совпали");
			}
			print("");
		}
		
		// сравниваем итоговую сумму всех добавленных предложений в корзину с итоговой суммой отображаемой в корзине
		print("Проверяем совпадение суммы стоимостей добавленных предложений из блока \"Публикация вакансий\" с итоговой суммой в блоке \"Корзина\"");
		nCost = GetTotalSumm(nTotalCostOffers); // получили сумму стоимости всех добавленных предложений 
		nCostBasket = GetIntFromTextInWebElement(wStrongTotalPrice); // получили итоговую стоиммость из корзины
		if(nCost == nCostBasket)
		{
			print("Сумма стоимостей предложений блока \"Публикация вакансий\" равная " + nCost +
					" совпало с итоговой стоимостью равной " + nCostBasket + ", записей предложений в блоке \"Корзина\"");	
		}
		else
		{
			print("Сумма стоимостей предложений блока \"Публикация вакансий\" равная " + nCost +
					" не совпало с итоговой стоимостью равной " + nCostBasket + ", записей предложений в блоке \"Корзина\"");
			print("Возможно у предложений есть дополнительная скидка");
			GetDiscount(nCost, nCostBasket);
		}
	}
	
	

	// удаление предложений
	public void DeleteOffers() throws ExceptFailTest
	{
		for(int i=0; i<wSmallLinksRemove.length; i++)
		{
			print("Удаляем " +(i+1)+ " предложение");
			CheckElementPresent(1, "//ol[@class='price-cart__items']/li//small");
			wLinkFirstRemove.click();
			Sleep(2000);
		}
	}
	//[@class='price-cart__item ']
	// сумма массива
	private int GetTotalSumm(int nMas[])
	{
		int nSumm = 0;
		for(int i: nMas)
		{
			nSumm += i; 
		}
		return nSumm;
	}
	
	// получение значения доп скидки и сравнение с отображаемой в корзине
	private void GetDiscount(int nCost, int nCostBasket) throws ExceptFailTest
	{
		int nDiscount = 0, nTemp;
		print("Проверяем отображение доп. скидки в блоке корзина");
		CheckElementPresent(1, "//strong[@class='HH-Price-CartRightView-DiscountPercent']");
		print("В блоке Корзина отображается дополнительная скидка");
		nDiscount = GetIntFromTextInWebElement(wTextDiscount);
		print("Ее значение " + nDiscount + "%");
		print("Высчитываем скидку по отношению общей суммы стоимости всех добавленных предложений к итоговой сумме в корзине");
		nTemp = Math.round((100-((nCostBasket*100)/nCost)));
		print("Сравниваем полученный размер скидки с указанной скидкой в корзине");
		if(nDiscount == nTemp)
		{
			print("Скидка полученная рассчетом - " + nTemp + "% равна доп. скидке указанной в корзине - " + nDiscount + "%");
			print("C учетом доп. скидки суммы корректны");
		}
		else
		{
			print("Скидка полученная рассчетом - " + nTemp + "% не равна доп. скидке указанной в корзине - " + nDiscount + "%");
			throw new ExceptFailTest("Стоимости не совпали");
		}
	
	}

	
}
