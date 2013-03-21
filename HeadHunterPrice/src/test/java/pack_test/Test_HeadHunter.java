package pack_test;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.AfterTest;
import pack_page.Page_AfterLogin;
import pack_page.Page_BaseAcces;
import pack_page.Page_Main;
import pack_page.Page_Price;
import pack_page.Page_PriceRecommended;
import pack_page.Page_PublicVacancy;
import pack_utils.ExceptFailTest;
import pack_utils.Proper;

@SuppressWarnings("unused")
public class Test_HeadHunter extends Test_Construct 
{
	Page_Main pageMain = PageFactory.initElements(GetWebDriver(), Page_Main.class); // главная hh.ru
	Page_AfterLogin pageAfterLogin; // после авторизации hh.ru/employer/vacancies?firstPageAfterLogon
	Page_Price pagePrice; //  hh.ru/price
	Page_PriceRecommended pagePriceRecommended; // hh.ru/price вкладка Рекомендуем
	Page_BaseAcces pageBaseAcces; //  hh.ru/price Доступ к базе
	Page_PublicVacancy pagePublicVacancy; //  hh.ru/price  Публикация вакансий
	
	/*@Test()
	@Parameters({ "sLogin", "sPassword" })
	public void Test_Authorization(String sLogin, String sPassword) throws ExceptFailTest
	{
		print("TEST_1");
		pageMain.OpenPage(Proper.GetProperty("sUrlMainPage"));
		pageAfterLogin = pageMain.Authorization(sLogin, sPassword);
		pageAfterLogin.CheckAutorization(Proper.GetProperty("bFlagCompany"));
	}
	
	@Test(dependsOnMethods = { "Test_Authorization" })
	public void Test_AfterLogin() throws ExceptFailTest
	{
		print("\r\nTEST_2");
		pagePrice = pageAfterLogin.GoToPagePrice(Proper.GetProperty("bFlagCompany"));
	}
	
	@Test(dependsOnMethods = { "Test_Authorization", "Test_AfterLogin"})
	public void Test_Price() throws ExceptFailTest
	{
		print("\r\nTEST_3");
		pagePrice.CheckElementToPagePrice();
	}
	*/
	
	
	
	
	
	//@Test(dependsOnMethods = { "Test_Authorization", "Test_AfterLogin", "Test_Price"})
	@Test
	public void Test_PriceRecommended() throws ExceptFailTest // тест блока рекоммендуемое
	{
		driver.navigate().to("http://hh.ru/price"); // пришлось добавить так как не работает страницы price для авторизованного пользователя
		print("\r\nTEST_4");
		pagePriceRecommended = PageFactory.initElements(GetWebDriver(), Page_PriceRecommended.class);
		pagePriceRecommended.CheckElementPagePriceRecommended();
		pagePriceRecommended.GetBlocksSpecialOffers();
		pagePriceRecommended.CheckBlocksSpesialOffers();
		pagePriceRecommended.AddToBasketSpecialOffers();
		pagePriceRecommended.LikeAddedOffersWithOffersInBasket();
		pagePriceRecommended.DeleteOffersFromBasket();
	}
	
	
	@AfterMethod
	public void TestAfter()
	{
		print("После каждого метода");
	}
	
	//@Test(dependsOnMethods = { "Test_Authorization", "Test_AfterLogin", "Test_Price"})
	@Test
	public void Test_BaseAcces() throws ExceptFailTest
	{
		driver.navigate().to("http://hh.ru/price");
		print("\r\nTEST_5");
		pageBaseAcces = PageFactory.initElements(GetWebDriver(), Page_BaseAcces.class); 
		pageBaseAcces.CheckElementPageBaseAcces();
		pageBaseAcces.GetRegionsCheckBox();
		pageBaseAcces.ChooseRegion();
		pageBaseAcces.GetProfCheckBox();
		pageBaseAcces.ChooseProf();
		pageBaseAcces.GetPriceRadioButton();
		pageBaseAcces.ChooseCost();
		pageBaseAcces.AddOffersToBasket();
		pageBaseAcces.LikeAddedOffersWithOffersInBasket();
		pageBaseAcces.DeleteOffersFromBasket();
	}
	
	//@Test(dependsOnMethods = { "Test_Authorization", "Test_AfterLogin", "Test_Price"})
	@Test()
	public void Test_PublicVacancy() throws ExceptFailTest
	{
		driver.navigate().to("http://hh.ru/price");
		print("\r\nTEST_6");
		pagePublicVacancy = PageFactory.initElements(GetWebDriver(), Page_PublicVacancy.class);
		pagePublicVacancy.CheckElementPagePublicVacancy();
		pagePublicVacancy.GetBlocksPublicVacancy();
		pagePublicVacancy.AddToBasketPublicVacancy();
		pagePublicVacancy.LikeAddedOffersWithOffersInBasket();
		pagePublicVacancy.DeleteOffersFromBasket();
	}
	
	
	@AfterTest()
	public void AfterTest()
	{
		driver.quit();
	}
	
}
