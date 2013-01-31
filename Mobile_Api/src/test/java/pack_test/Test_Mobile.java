package pack_test;

import java.io.IOException;
import java.net.URISyntaxException;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.appengine.repackaged.org.json.JSONException;

import pack_connect.ConnectMethod;
import pack_utils.ExceptFailTest;

public class Test_Mobile
{
	ConnectMethod cM = new ConnectMethod();
	
	@Test (groups = { "CreateProfileRequest_1" })
	@Parameters({"sBaseHost", "sLogin", "sPassword"})
	public void Test(String sBaseHost, String sLogin, String sPassword) throws URISyntaxException, IOException, ExceptFailTest
	{
		// try catch
		cM.CreateProfileRequest_1(sBaseHost, sLogin, sPassword);
		
	}
	
	@Test (groups = { "Authorization_1_1" })
	@Parameters({"sBaseHost", "sLogin", "sPassword"})
	public void Test1(String sBaseHost, String sLogin, String sPassword) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.Authorization_1_1(sBaseHost, sLogin, sPassword);
	}
	
	@Test (groups = { "GetProfile_1_2" })
	@Parameters({"sBaseHost", "sLogin", "sPassword"})
	public void Test2(String sBaseHost, String sLogin, String sPassword) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetProfile_1_2(sBaseHost, sLogin, sPassword);
	}
	
	@Test (groups = { "EditProfile_1_3" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sParam"})
	public void Test3(String sBaseHost, String sLogin, String sPassword, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.EditProfile_1_3(sBaseHost, sLogin, sPassword, sParam);
	}
	
	@Test (groups = { "RestorePassword1_4" })
	@Parameters({"sBaseHost", "sLogin"})
	public void Test4(String sBaseHost, String sLogin) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.RestorePassword1_4(sBaseHost, sLogin);
	}
	
	@Test (groups = { "PostAdvert_2_1" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sParam",  "sParam1",  "sParam2"})
	public void Test5(String sBaseHost, String sLogin, String sPassword, String sParam, String sParam1, String sParam2) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		/*cM.PostAdvert_2_1("ag.regions.devel.ps", "Timon2180", "retry2", "{category=cars/passenger/new/, region=russia/moskva-gorod/ ,advert_type=auto_new}", 
				"{phone=343434343434, email=mail@mail.com, contact=Vasia}",
				"{make=BMW, model=116, currency=RUR, price=1200, car-year=2010, bodytype=внедорожник, transmittion=механическая}",
				"1.jpeg");*/
		
		cM.PostAdvert_2_1(sBaseHost, sLogin, sPassword, sParam, sParam1, sParam2, "1.jpeg");
	}
	
	// пользователя с такими данными не существует 245364632
	// на неправильный id = fgdfgdfgdfg отдает какую ту чушь 
	@Test (groups = { "GetAdvert_2_2" })
	@Parameters({"sBaseHost", "sIdAdvert"})
	public void Test6(String sBaseHost, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetAdvert_2_2(sBaseHost, sIdAdvert);
	}
	
	
	// картинку редактируем orig
	@Test (groups = { "EditAdvert_2_3" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert",  "sParam2"})
	public void Test7(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, String sParam2) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.EditAdvert_2_3(sBaseHost, sLogin, sPassword, sIdAdvert, sParam2 , "1.jpeg");
	}
	
	
	@Test (groups = { "DeleteAdvert_2_4" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert"})
	public void Test8(String sBaseHost, String sLogin, String sPassword, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.DeleteAdvert_2_4(sBaseHost, sLogin, sPassword, sIdAdvert);
	}
	
	
	// можно добавить свое собственное объявление
	@Test (groups = { "AddAdvertToFavourite_2_5" })
	@Parameters({"sBaseHost", "sLogin", "sPassword", "sIdAdvert", "sAuthFlag"})
	public void Test9(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, boolean sAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.AddAdvertToFavourite_2_5(sBaseHost, sLogin, sPassword, sIdAdvert, sAuthFlag);
	}
	
	
	@Test (groups = { "DeleteAdvertFromFavourite_2_6" })
	@Parameters({"sBaseHost", "sLogin", "sPassword", "sIdAdvert"})
	public void Test10(String sBaseHost, String sLogin, String sPassword, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.DeleteAdvertFromFavourite_2_6(sBaseHost, sLogin, sPassword, sIdAdvert);
	}
	
	
	// Проверить для ИП и ОП
	@Test (groups = { "GetPaidProductsToStepToAdd_2_7" })
	@Parameters({"sBaseHost","sIdAdvert"})
	public void Test11(String sBaseHost, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetPaidProductsToStepToAdd_2_7(sBaseHost, sIdAdvert);
	}
	
	
	// Проверить для ИП и ОП
	@Test (groups = { "GetPaidProductsFromLK_2_8" })
	@Parameters({"sBaseHost","sIdAdvert"})
	public void Test12(String sBaseHost, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetPaidProductsFromLK_2_8(sBaseHost, sIdAdvert);
	}
	
	
	// Проверить для ИП и ОП
	@Test (groups = { "GetFreeProductsForAdvert_2_9" })
	@Parameters({"sBaseHost","sIdAdvert"})
	public void Test13(String sBaseHost, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetFreeProductsForAdvert_2_9(sBaseHost, sIdAdvert);
	}
	
	// Проверить для ИП и ОП (есть пакет нету)
	// возвращает не только массив error
	@Test (groups = { "ActivationAdvert_2_10" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert", "sApp_token"})
	public void Test14(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, String sApp_token) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.ActivationAdvert_2_10(sBaseHost, sLogin, sPassword, sIdAdvert, sApp_token);
	}
	
		
	@Test (groups = { "DeactivateAdvert_2_11" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert"})
	public void Test15(String sBaseHost, String sLogin, String sPassword, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.DeactivateAdvert_2_11(sBaseHost, sLogin, sPassword, sIdAdvert);
	}
	
	
	@Test (groups = { "Prolongadvert_2_12" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert", "sApp_token"})
	public void Test16(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, String sApp_token) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.Prolongadvert_2_12(sBaseHost, sLogin, sPassword, sIdAdvert, sApp_token);
	}
	
	@Test (groups = { "PushUpAdvert_2_13" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert", "sApp_token"})
	public void Test17(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, String sApp_token) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.PushUpAdvert_2_13(sBaseHost, sLogin, sPassword, sIdAdvert, sApp_token);
	}
	
	
	@Test (groups = { "HighLightAdvert_2_14" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert", "sApp_token"})
	public void Test18(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, String sApp_token) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.HighLightAdvert_2_14(sBaseHost, sLogin, sPassword, sIdAdvert, sApp_token);
	}
	
	
	@Test (groups = { "SetPremiumForAdvert_2_15" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert", "sApp_token" , "sNumberDays"})
	public void Test19(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, String sApp_token, String sNumberDays) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.SetPremiumForAdvert_2_15(sBaseHost, sLogin, sPassword, sIdAdvert, sApp_token, sNumberDays);
	}

	@Test (groups = { "VoteForAdvertHigh_2_16" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert"})
	public void Test20(String sBaseHost, String sLogin, String sPassword, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.VoteForAdvertHigh_2_16(sBaseHost, sLogin, sPassword, sIdAdvert);
	}
	
	
	@Test (groups = { "VoteForAdvertLower_2_17" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert"})
	public void Test21(String sBaseHost, String sLogin, String sPassword, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.VoteForAdvertLower_2_17(sBaseHost, sLogin, sPassword, sIdAdvert);
	}
	
	
	@Test (groups = { "GetListForCategory_2_18" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test22(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetListForCategory_2_18(sBaseHost, sParam);
	}
	
	
	@Test (groups = { "GetListSearchCategory_2_19" })
	@Parameters({"sBaseHost", "sParam", "sParam1"})
	public void Test23(String sBaseHost, String sParam, String sParam1 ) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetListSearchCategory_2_19(sBaseHost, sParam, sParam1);
	}
	
	
	
	// Ошибка запроса к базе
	@Test (groups = { "GetListFavourite_2_20" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sParam"})
	public void Test24(String sBaseHost, String sLogin, String sPassword, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetListFavourite_2_20(sBaseHost, sLogin, sPassword, sParam);
	
	}
	
	
	@Test (groups = { "GetListOwnAdvert_2_21" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sParam"})
	public void Test25(String sBaseHost, String sLogin, String sPassword, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetListOwnAdvert_2_21(sBaseHost, sLogin, sPassword, sParam);
	}
	
	
	@Test (groups = { "GetListUserAdvert_2_22" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test26(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetListUserAdvert_2_22(sBaseHost, sParam);
	}
	
	
	@Test (groups = { "GetRubricator_3_1" })
	@Parameters({"sBaseHost", "sCategory"})
	public void Test27(String sBaseHost, String sCategory) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetRubricator_3_1(sBaseHost, sCategory);
	}
	
	
	@Test (groups = { "GetCastomfieldsForAddAdvert_3_2" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test28(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetCastomfieldsForAddAdvert_3_2(sBaseHost, sParam);
	}
	
	
	@Test (groups = { "GetCastomfieldsForEditAdvert_3_3" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test29(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetCastomfieldsForEditAdvert_3_3(sBaseHost, sParam);
	}
	
	
	@Test (groups = { "GetCastomfieldsForSearchAdvert_3_4" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test30(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetCastomfieldsForSearchAdvert_3_4(sBaseHost, sParam);
	}
	

	@Test (groups = { "GetRegions_4_1" })
	@Parameters({"sBaseHost"})
	public void Test31(String sBaseHost) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetRegions_4_1(sBaseHost);
	}
	
	// Какие отдает ??????
	@Test (groups = { "GetPopularCities_4_2" })
	@Parameters({"sBaseHost", "sRegion"})
	public void Test32(String sBaseHost, String sRegion) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetPopularCities_4_2(sBaseHost, sRegion);
	}
	
	
	@Test (groups = { "GetCitiesSuggest_4_3" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test33(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetCitiesSuggest_4_3(sBaseHost, sParam);
	}
	
	
	@Test (groups = { "GetStreetsSuggest_4_4" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test34(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetStreetsSuggest_4_4(sBaseHost, sParam);
	}
	
	
	@Test (groups = { "GetHousesSuggest_4_5" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test35(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetHousesSuggest_4_5(sBaseHost, sParam);
	}
	
	
	@Test (groups = { "GetDistrictSuggest_4_6" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test36(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetDistrictSuggest_4_6(sBaseHost, sParam);
	}
	
	
	@Test (groups = { "GetDirectionSuggest_4_7" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test37(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetDirectionSuggest_4_7(sBaseHost, sParam);
	}
	
	
	// Не работает
	@Test (groups = { "GetHighwaySuggest_4_8" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test38(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetHighwaySuggest_4_8(sBaseHost, sParam);
	}
	
	//Ничего не нашли
	@Test (groups = { "GetMetroSuggest_4_9" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test39(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetMetroSuggest_4_9(sBaseHost, sParam);
		//{region=russia/moskovskaya-obl/, search_string=кутузова}
	}
	
	
	// евро не переведено кодировка,  currencies не массив  в ответе
	@Test (groups = { "GetCurrencies_5_1" })
	@Parameters({"sBaseHost"})
	public void Test40(String sBaseHost) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetCurrencies_5_1(sBaseHost);
	} 
	
	
	// должен отдавать value отдает currencies
	@Test (groups = { "GetDictinary_6_1" })
	@Parameters({"sBaseHost", "sNameDictinary"})
	public void Test41(String sBaseHost, String sNameDictinary) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetDictinary_6_1(sBaseHost, sNameDictinary);
		//"Car makers commercial"
	} 
	
	
}