package demo;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pack_page.*;
import pack_test.Test_Construct;
import pack_utils.ExceptFailTest;

public class Test_Add_To_FavoritesTest extends Test_Construct {
	Page_IrrMain pageIrrMain;
	Page_Search pageSearch;
	Page_Auto pageAuto;
	Page_Favorites pageFav;

	@Test(groups = { "Demo_1" })
	@Parameters({ "sUrl", "sImageEnable", "sIndex", "sName", "sValue" })
	public void Test_Add_Fav(String sUrl, String sImageEnable, String sIndex,
			String sName, String value) throws ExceptFailTest {
		try {
			pageIrrMain = PageFactory.initElements(
					GetWebDriver(Integer.parseInt(sImageEnable)),
					Page_IrrMain.class);
			print("\r\nДобавление объявления в избранное".toUpperCase());
			pageIrrMain.OpenPage(sUrl);
			pageIrrMain.CloseWindowRegion();
			
			print("Переходим в раздел Авто");
			pageAuto = pageIrrMain.GoToAuto();
			String adName = pageAuto.addToFavoritesByIndex(sIndex);
			pageAuto.addToFavoritesByName(sName);
			
			print("Переходим в раздел Избранное");
			pageFav = pageAuto.goFavorites();
			
			print("Проверяем, что объявления добавлены");
			pageFav.assertAd(sName);
			pageFav.assertAd(adName);
			
			print("Переходим в раздел Авто");
			pageAuto = pageFav.goAuto();
			
			print("Проверяем, что объявления добавлены");
			pageAuto.checkAdByIndex(sIndex, value);
			pageAuto.checkAd(sName, value);

			print("Тест успешно завершен. Все объявления добавлены в избранное и отображаются корректно");
		} finally {
			CaptureScreenshot("AddToFavorites");
			driver.quit();
		}
	}

}