package pack_page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pack_utils.ExceptFailTest;

public class Page_Favorites extends Page {

	@FindBy(xpath = "//span[contains(., 'Избранное')]")
	private WebElement menuLink;

	private String adTemplate = "//table[@class='tableLK']/tbody/tr[@class='secondTD']//div[@class='txt-tb'][contains(., '%1$s')]";

	public Page_Favorites(WebDriver driver) {
		super(driver);
	}

	public void OpenPage(String sUrl) {
	}

	/**
	 * Проверяем, что присутствует объявление по тексту
	 * @param text
	 * @throws ExceptFailTest
	 */
	public void assertAd(String text) throws ExceptFailTest {
		text = text.replaceAll("  ", " ").replaceAll("г\\. в", "г.в");
		if (CheckElement(String.format(adTemplate, text), text)) {
			print("Успешно. Найдено объявление с текстом: " + text);
		} else {
			throw new ExceptFailTest(
					"Объявление с заданным текстом не найдено: " + text);
		}

	}

	/**
	 * Возвращаемся на страницу с объявлениями
	 * @return
	 */
	public Page_Auto goAuto() {
		BrowserBack();
		return PageFactory.initElements(driver, Page_Auto.class);
	}
}
