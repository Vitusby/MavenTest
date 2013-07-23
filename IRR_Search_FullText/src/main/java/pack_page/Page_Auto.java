package pack_page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pack_utils.ExceptFailTest;

public class Page_Auto extends Page {

	@FindBy(xpath = "//a[@id='additional_menu_fav']")
	private WebElement lnkAddToFav;

	@FindBy(css = "li#blocknote>a")
	private WebElement lnkFavorites;

	// шаблоны локаторов для бъявлений
	private String adLocator = "//table[@class='adListTable']/tbody/tr[@data-position]";
	private String adLocatorIndex = "//table[@class='adListTable']/tbody/tr[@data-position='%1$s']";
	private String adName = "/td[@class='tdTxt']";

	private String adByText = adLocator + adName + "[contains(., '%1$s')]";
	private String adFavor = "/../td[@class='tdPrise']/div[@onclick]";

	public Page_Auto(WebDriver driver) {
		super(driver);
	}

	public void OpenPage(String sUrl) {
	}

	/**
	 * Добавляем объявление в избранное по тексту
	 * @param name
	 * @throws ExceptFailTest
	 */
	public void addToFavoritesByName(String name) throws ExceptFailTest {
		print("Добавляем в избранное объявление с текстом: " + name);
		try {
			addToFav(String.format(adByText, name));
		} catch (ExceptFailTest exc) {
			print("Объявление с заданным текстом не найдено: " + name);
			throw new ExceptFailTest(
					"Объявление с заданным текстом не найдено: " + name);
		}
	}

	/**
	 * Добавляем объявление в избранное по индексу
	 * @param index
	 * @return
	 * @throws ExceptFailTest
	 */
	public String addToFavoritesByIndex(String index) throws ExceptFailTest {
		print("Добавляем в избранное объявление с индексом: " + index);
		String locator = String.format(adLocatorIndex, index) + adName;
		try {
			addToFav(locator);
		} catch (ExceptFailTest exc) {
			print("Объявление с заданным индексом не найдено: " + index);
			throw new ExceptFailTest(
					"Объявление с заданным индексом не найдено: " + index);
		}
		return GetText(locator);
	}

	/**
	 * Проверяем статус объявления по тексту
	 * @param text
	 * @param value
	 * @throws ExceptFailTest
	 */
	public void checkAd(String text, String value) throws ExceptFailTest {
		WebElement wElement = GetElement(String.format(adByText, text)
				+ adFavor);
		checkValue(value, wElement);
	}

	
	/**
	 * Проверяем статус объявления по индексу
	 * @param index
	 * @param value
	 * @throws ExceptFailTest
	 */
	public void checkAdByIndex(String index, String value)
			throws ExceptFailTest {
		WebElement wElement = GetElement(String.format(adLocatorIndex + adName,
				index) + adFavor);
		checkValue(value, wElement);
	}

	/**
	 * Проверяем статус объявления
	 * @param value Статус
	 * @param wElement
	 * @throws ExceptFailTest
	 */
	private void checkValue(String value, WebElement wElement)
			throws ExceptFailTest {
		ClickElementViaJS(wElement);
		String actual = lnkAddToFav.getText();
		if (actual.equals(value)) {
			print("Объявление отображается корректно");
		} else {
			throw new ExceptFailTest("Объявление не было добавлено, статус: "
					+ actual);
		}
	}

	/**
	 * Открываем меню и добавляем в избранное 
	 * @param locator
	 * @throws ExceptFailTest
	 */
	private void addToFav(String locator) throws ExceptFailTest {
		WebElement wElement = GetElement(locator);
		scrollTo(wElement.getLocation().getY());

		wElement = GetElement(locator + adFavor);
		ClickElementViaJS(wElement);
		ClickElementViaJS(lnkAddToFav);
	}

	/**
	 * Переходим в Избранное
	 * @return
	 */
	public Page_Favorites goFavorites() {
		lnkFavorites.click();
		return PageFactory.initElements(driver, Page_Favorites.class);
	}
}
