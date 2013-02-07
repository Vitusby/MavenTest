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
	// Автотесты
/////////////////////////////////////////////////////////////////////////////////////////////////////////// 	
	/*@Test (groups = { "AutoTest" })
	@Parameters({"sBaseHost"})
	public void AutoTest_CreateProfile(String sBaseHost) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
			cM.CreateProfileReqeust(sBaseHost); // Создание профиля
	}
	
	@Test (groups = { "AutoTest" })
	@Parameters({"sBaseHost"})
	public void AutoTest_Authorization(String sBaseHost) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
			cM.Authorization(sBaseHost); // Авторизация	
	}
	
	@Test (groups = { "AutoTest" })
	@Parameters({"sBaseHost"})
	public void AutoTest_GetAndEditProfile(String sBaseHost) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
			cM.GetAndEditProfile(sBaseHost); // Получение и редактирование профиля
	}
	
	@Test (groups = { "AutoTest" })
	@Parameters({"sBaseHost"})
	public void AutoTest_RestorePassworde(String sBaseHost) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
			cM.RestorePassword(sBaseHost); // Восстановление пароля
	}
	*/
	@Test (groups = { "AutoTest" })
	@Parameters({"sBaseHost"})
	public void AutoTest_AddGetEditAdvertOP(String sBaseHost) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
			cM.AddGetEditAdvertOP(sBaseHost); // Подача/получение/редактирование/
	}
	/*
	@Test (groups = { "AutoTest" })
	@Parameters({"sBaseHost"})
	public void AutoTest_AddGetListDeleteOP(String sBaseHost) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
			cM.AddGetListDeleteOP(sBaseHost); 
	}
	*/
	
	
	/*@Test (groups = { "AutoTest" })
	@Parameters({"sBaseHost"})
	public void AutoTest_AddFavGetListFavDeleteFavOP(String sBaseHost) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
			cM.AddFavGetListFavDeleteFavOP(sBaseHost); // Подача/получение листинга/редактирование/
	}
	*/
	
	
	
	
	
	// Параметризированные тесты
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	@Test (groups = { "CreateProfileRequest_1" })
	@Parameters({"sBaseHost", "sLogin", "sPassword"})
	public void Test(String sBaseHost, String sLogin, String sPassword) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.CreateProfileRequest_1(sBaseHost, sLogin, sPassword);
	}

	@Test (groups = { "Authorization_1_1" })
	@Parameters({"sBaseHost", "sLogin", "sPassword"})
	public void Test1(String sBaseHost, String sLogin, String sPassword) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
			cM.Authorization_1_1(sBaseHost, sLogin, sPassword);
	}

	@Test (groups = { "GetProfile_1_2" })
	@Parameters({"sBaseHost", "sLogin", "sPassword", "bAuthFlag"})
	public void Test2(String sBaseHost, String sLogin, String sPassword, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetProfile_1_2(sBaseHost, sLogin, sPassword, bAuthFlag);
	}
	
	@Test (groups = { "EditProfile_1_3" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sParam", "bAuthFlag"})
	public void Test3(String sBaseHost, String sLogin, String sPassword, String sParam, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.EditProfile_1_3(sBaseHost, sLogin, sPassword, sParam, bAuthFlag);
	}
	
	@Test (groups = { "RestorePassword1_4" })
	@Parameters({"sBaseHost", "sLogin"})
	public void Test4(String sBaseHost, String sLogin) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.RestorePassword1_4(sBaseHost, sLogin);
	}
	
	@Test (groups = { "PostAdvert_2_1" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sParam",  "sParam1",  "sParam2", "sVideoUrl", "bAuthFlag"})
	public void Test5(String sBaseHost, String sLogin, String sPassword, String sParam, String sParam1, String sParam2, String sVideoUrl, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.PostAdvert_2_1(sBaseHost, sLogin, sPassword, sParam, sParam1, sParam2, sVideoUrl, "2.jpg", bAuthFlag);
	}
	
	@Test (groups = { "GetAdvert_2_2" })
	@Parameters({"sBaseHost", "sIdAdvert"})
	public void Test6(String sBaseHost, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetAdvert_2_2(sBaseHost, sIdAdvert);
	}
	
	@Test (groups = { "EditAdvert_2_3" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert", "sParam1",   "sParam2", "sVideoUrl",  "bAuthFlag"})
	public void Test7(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, String sParam1, String sParam2, String sVideoUrl ,boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.EditAdvert_2_3(sBaseHost, sLogin, sPassword, sIdAdvert, sParam1, sParam2, "1.jpeg", sVideoUrl,  bAuthFlag);
	}
		
	@Test (groups = { "DeleteAdvert_2_4" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert", "bAuthFlag"})
	public void Test8(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.DeleteAdvert_2_4(sBaseHost, sLogin, sPassword, sIdAdvert, bAuthFlag);
	}
	
	@Test (groups = { "AddAdvertToFavourite_2_5" })
	@Parameters({"sBaseHost", "sLogin", "sPassword", "sIdAdvert", "bAuthFlag"})
	public void Test9(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.AddAdvertToFavourite_2_5(sBaseHost, sLogin, sPassword, sIdAdvert, bAuthFlag);
	}
		
	@Test (groups = { "DeleteAdvertFromFavourite_2_6" })
	@Parameters({"sBaseHost", "sLogin", "sPassword", "sIdAdvert", "bAuthFlag"})
	public void Test10(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.DeleteAdvertFromFavourite_2_6(sBaseHost, sLogin, sPassword, sIdAdvert, bAuthFlag);
	}
	
	@Test (groups = { "GetPaidProductsToStepToAdd_2_7" })
	@Parameters({"sBaseHost","sIdAdvert"})
	public void Test11(String sBaseHost, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetPaidProductsToStepToAdd_2_7(sBaseHost, sIdAdvert);
	}
	
	@Test (groups = { "GetPaidProductsFromLK_2_8" })
	@Parameters({"sBaseHost","sIdAdvert"})
	public void Test12(String sBaseHost, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetPaidProductsFromLK_2_8(sBaseHost, sIdAdvert);
	}
	
	@Test (groups = { "GetFreeProductsForAdvert_2_9" })
	@Parameters({"sBaseHost","sIdAdvert"})
	public void Test13(String sBaseHost, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetFreeProductsForAdvert_2_9(sBaseHost, sIdAdvert);
	}
	
	@Test (groups = { "ActivationAdvert_2_10" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert", "bApp_token", "bAuthFlag"})
	public void Test14(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, boolean bApp_token, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.ActivationAdvert_2_10(sBaseHost, sLogin, sPassword, sIdAdvert, bApp_token, bAuthFlag);
	}
	
	@Test (groups = { "DeactivateAdvert_2_11" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert", "bAuthFlag"})
	public void Test15(String sBaseHost, String sLogin, String sPassword, String sIdAdvert,  boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.DeactivateAdvert_2_11(sBaseHost, sLogin, sPassword, sIdAdvert, bAuthFlag);
	}
	
	@Test (groups = { "Prolongadvert_2_12" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert", "bApp_token", "bAuthFlag"})
	public void Test16(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, boolean bApp_token, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.Prolongadvert_2_12(sBaseHost, sLogin, sPassword, sIdAdvert, bApp_token, bAuthFlag);
	}
	
	@Test (groups = { "PushUpAdvert_2_13" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert", "bApp_token", "bAuthFlag"})
	public void Test17(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, boolean bApp_token, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.PushUpAdvert_2_13(sBaseHost, sLogin, sPassword, sIdAdvert, bApp_token, bAuthFlag);
	}
	
	@Test (groups = { "HighLightAdvert_2_14" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert", "bApp_token", "bAuthFlag"})
	public void Test18(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, boolean bApp_token, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.HighLightAdvert_2_14(sBaseHost, sLogin, sPassword, sIdAdvert, bApp_token, bAuthFlag);
	}
	
	@Test (groups = { "SetPremiumForAdvert_2_15" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert", "bApp_token" , "sNumberDays", "bAuthFlag"})
	public void Test19(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, boolean bApp_token, String sNumberDays, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.SetPremiumForAdvert_2_15(sBaseHost, sLogin, sPassword, sIdAdvert, bApp_token, sNumberDays, bAuthFlag);
	}

	@Test (groups = { "VoteForAdvertHigh_2_16" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert", "bAuthFlag"})
	public void Test20(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.VoteForAdvertHigh_2_16(sBaseHost, sLogin, sPassword, sIdAdvert, bAuthFlag);
	}
	
	@Test (groups = { "VoteForAdvertLower_2_17" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sIdAdvert", "bAuthFlag"})
	public void Test21(String sBaseHost, String sLogin, String sPassword, String sIdAdvert, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.VoteForAdvertLower_2_17(sBaseHost, sLogin, sPassword, sIdAdvert, bAuthFlag);
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
	
	@Test (groups = { "GetListFavourite_2_20" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sParam", "bAuthFlag"})
	public void Test24(String sBaseHost, String sLogin, String sPassword, String sParam, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetListFavourite_2_20(sBaseHost, sLogin, sPassword, sParam, bAuthFlag);
	
	}
	
	@Test (groups = { "GetListOwnAdvert_2_21" })
	@Parameters({"sBaseHost", "sLogin", "sPassword" , "sParam", "bAuthFlag"})
	public void Test25(String sBaseHost, String sLogin, String sPassword, String sParam, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetListOwnAdvert_2_21(sBaseHost, sLogin, sPassword, sParam, bAuthFlag);
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
	
	@Test (groups = { "GetPopularCities_4_2" })
	@Parameters({"sBaseHost", "sRegion"})
	public void Test32(String sBaseHost, String sRegion) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetPopularCities_4_2(sBaseHost, sRegion);
	}
	
	@Test (groups = { "GetCitiesWithDomen_4_2_1" })
	@Parameters({"sBaseHost"})
	public void Test32_1(String sBaseHost) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetCitiesWithDomen_4_2_1(sBaseHost);
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
	
	@Test (groups = { "GetMicroDistrictSuggest_4_8" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test36_1(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetMicroDistrictSuggest_4_8(sBaseHost, sParam);
	}
	
	@Test (groups = { "GetAOSuggest_4_9" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test36_2(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetAOSuggest_4_9(sBaseHost, sParam);
	}
	
	@Test (groups = { "GetDirectionSuggest_4_10" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test37(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetDirectionSuggest_4_10(sBaseHost, sParam);
	}
	
	@Test (groups = { "GetHighwaySuggest_4_11" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test38(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetHighwaySuggest_4_11(sBaseHost, sParam);
	}
	
	@Test (groups = { "GetMetroSuggest_4_12" })
	@Parameters({"sBaseHost", "sParam"})
	public void Test39(String sBaseHost, String sParam) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetMetroSuggest_4_12(sBaseHost, sParam);
	}
	
	@Test (groups = { "GetCurrencies_5_1" })
	@Parameters({"sBaseHost"})
	public void Test40(String sBaseHost) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetCurrencies_5_1(sBaseHost);
	} 
	
	@Test (groups = { "GetDictinary_6_1" })
	@Parameters({"sBaseHost", "sNameDictinary"})
	public void Test41(String sBaseHost, String sNameDictinary) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetDictinary_6_1(sBaseHost, sNameDictinary);
		//"Car makers commercial"
	} 
	
	
}