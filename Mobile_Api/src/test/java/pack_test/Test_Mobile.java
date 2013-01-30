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
	public void Test() throws URISyntaxException, IOException, ExceptFailTest
	{
		// try catch
		cM.CreateProfileRequest_1("ag.regions.devel.ps", "dfdgfdg@mail.ru", "vcvcvvcbcvb");
		
	}
	
	@Test (groups = { "Authorization_1_1" })
	public void Test1() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.Authorization_1_1("ag.regions.devel.ps", "ml@rglab.by", "123456");
	}
	
	@Test (groups = { "GetProfile_1_2" })
	public void Test2() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetProfile_1_2("ag.regions.devel.ps", "Timon2180", "retry2");
	}
	
	@Test (groups = { "EditProfile_1_3" })
	public void Test3() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.EditProfile_1_3("ag.regions.devel.ps", "Timon2180", "retry2", "{zip=111111, city=\u041A\u0430\u0440\u0430\u0433\u0430\u043D\u0434\u0430}");
	}
	
	@Test (groups = { "RestorePassword1_4" })
	//@Parameters({"sBaseHost"})
	public void Test4() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.RestorePassword1_4("ag.regions.devel.ps", "Timon2180@mail.ru");
	}
	
	@Test (groups = { "PostAdvert_2_1" })
	public void Test5() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.PostAdvert_2_1("ag.regions.devel.ps", "Timon2180", "retry2", "{category=cars/passenger/new/, region=russia/moskva-gorod/ ,advert_type=auto_new}", 
				"{phone=343434343434, email=mail@mail.com, contact=Vasia}",
				"{make=BMW, model=116, currency=RUR, price=1200, car-year=2010, bodytype=внедорожник, transmittion=механическая}",
				"1.jpeg");
	}
	
	// пользователя с такими данными не существует 245364632
	// на неправильный id = fgdfgdfgdfg отдает какую ту чушь 
	@Test (groups = { "GetAdvert_2_2" })
	public void Test6() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetAdvert_2_2("design.prontosoft.by", "246824574");
	}
	
// НЕ ПРОВЕРИЛ С ЭТОГО	
	
	// картинку редактируем orig
	@Test (groups = { "EditAdvert_2_3" })
	public void Test7() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.EditAdvert_2_3("design.prontosoft.by","Timon2180", "retry2", "246824574" , "1.jpeg");
	}
	
	
	@Test (groups = { "DeleteAdvert_2_4" })
	public void Test8() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.DeleteAdvert_2_4("design.prontosoft.by","Timon2180", "retry2", "246824574");
	}
	
	
	// можно добавить свое собственное объявление
	@Test (groups = { "AddAdvertToFavourite_2_5" })
	public void Test9() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.AddAdvertToFavourite_2_5("design.prontosoft.by","Timon2180", "retry2", "246824574");
	}
	
	
	@Test (groups = { "DeleteAdvertFromFavourite_2_6" })
	public void Test10() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.DeleteAdvertFromFavourite_2_6("design.prontosoft.by","Timon2180", "retry2", "246824574");
	}
	
	
	// Проверить для ИП и ОП
	@Test (groups = { "GetPaidProductsToStepToAdd_2_7" })
	public void Test11() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetPaidProductsToStepToAdd_2_7("design.prontosoft.by", "246824574");
	}
	
	
	// Проверить для ИП и ОП
	@Test (groups = { "GetPaidProductsFromLK_2_8" })
	public void Test12() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetPaidProductsFromLK_2_8("design.prontosoft.by", "246824574");
	}
	
	
	// Проверить для ИП и ОП
	@Test (groups = { "GetFreeProductsForAdvert_2_9" })
	public void Test13() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetFreeProductsForAdvert_2_9("design.prontosoft.by", "246824574");
	}
	
	// Проверить для ИП и ОП (есть пакет нету)
	// возвращает не только массив error
	@Test (groups = { "ActivationAdvert_2_10" })
	public void Test14() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.ActivationAdvert_2_10("vo.ru.master.g.devel.ps","Timon2181", "retry2", "245363995" , "gfdgdf");
	}
	
		
	@Test (groups = { "DeactivateAdvert_2_11" })
	public void Test15() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.DeactivateAdvert_2_11("design.prontosoft.by","ml@rglab.by", "123456", "245364635");
	}
	
	
	@Test (groups = { "Prolongadvert_2_12" })
	public void Test16() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.Prolongadvert_2_12("vo.ru.master.g.devel.ps","Timon2180", "retry2", "245364635", "");
	}
	
	@Test (groups = { "PushUpAdvert_2_13" })
	public void Test17() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.PushUpAdvert_2_13("vo.ru.master.g.devel.ps","Timon2180", "retry2", "245364635", "");
	}
	
	
	@Test (groups = { "HighLightAdvert_2_14" })
	public void Test18() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.HighLightAdvert_2_14("vo.ru.master.g.devel.ps","Timon2180", "retry2", "245364635", "");
	}
	
	
	@Test (groups = { "SetPremiumForAdvert_2_15" })
	public void Test19() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.SetPremiumForAdvert_2_15("vo.ru.master.g.devel.ps","Timon2180", "retry2", "245364635", "true", "7");
	}

	@Test (groups = { "VoteForAdvertHigh_2_16" })
	public void Test20() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.VoteForAdvertHigh_2_16("design.prontosoft.by","Timon2181", "retry2", "246824575");
	}
	
	
	@Test (groups = { "VoteForAdvertLower_2_17" })
	public void Test21() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.VoteForAdvertLower_2_17("design.prontosoft.by","Timon2181", "retry2", "246824575");
	}
	
	
	@Test (groups = { "GetListForCategory_2_18" })
	public void Test22() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetListForCategory_2_18("design.prontosoft.by", "{category=cars/passenger/new/, region=russia/moskva-gorod/, currency=RUR, offset=0, limit=20, sort_by=date_sort:desc, include_privates=true, include_companies=true}");
	}
	
	
	@Test (groups = { "GetListSearchCategory_2_19" })
	public void Test23() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetListSearchCategory_2_19("design.prontosoft.by", "{category=cars/passenger/new/, region=russia/moskva-gorod/, offset=0, limit=20, sort_by=date_sort:desc}", "make=AC/model=ACE");
	}
	
	
	
	// Ошибка запроса к базе
	@Test (groups = { "GetListFavourite_2_20" })
	public void Test24() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetListFavourite_2_20("design.prontosoft.by","Timon2181", "retry2", "{offset=0, limit=20}");
	}
	
	
	@Test (groups = { "GetListOwnAdvert_2_21" })
	public void Test25() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetListOwnAdvert_2_21("design.prontosoft.by","Timon2181", "retry2", "{offset=0, limit=20}");
	}
	
	
	@Test (groups = { "GetListUserAdvert_2_22" })
	public void Test26() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetListUserAdvert_2_22("design.prontosoft.by", "{user_id=10644682, offset=0, limit=20}");
	}
	
	
	@Test (groups = { "GetRubricator_3_1" })
	public void Test27() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetRubricator_3_1("design.prontosoft.by", "cars/");
	}
	
	
	@Test (groups = { "GetCastomfieldsForAddAdvert_3_2" })
	public void Test28() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetCastomfieldsForAddAdvert_3_2("design.prontosoft.by", "{category=cars/passenger/new/, region=russia/moskva-gorod/, advert_type=auto_new}");
	}
	
	
	@Test (groups = { "GetCastomfieldsForEditAdvert_3_3" })
	public void Test29() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetCastomfieldsForEditAdvert_3_3("design.prontosoft.by", "{category=cars/passenger/new/, advert_type=auto_new}");
	}
	
	
	@Test (groups = { "GetCastomfieldsForSearchAdvert_3_4" })
	public void Test30() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetCastomfieldsForSearchAdvert_3_4("design.prontosoft.by", "{category=cars/passenger/new/, region=russia/moskva-gorod/}");
	}
	

	@Test (groups = { "GetRegions_4_1" })
	public void Test31() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetRegions_4_1("design.prontosoft.by");
	}
	
	// Какие отдает ??????
	@Test (groups = { "GetPopularCities_4_2" })
	public void Test32() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetPopularCities_4_2("design.prontosoft.by", "russia/moskva-gorod/");
	}
	
	
	@Test (groups = { "GetCitiesSuggest_4_3" })
	public void Test33() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetCitiesSuggest_4_3("design.prontosoft.by", "{region=russia/, search_string=Москва}");
	}
	
	
	@Test (groups = { "GetStreetsSuggest_4_4" })
	public void Test34() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetStreetsSuggest_4_4("design.prontosoft.by", "{region=russia/moskva-gorod/, search_string=байкальская}");
	}
	
	
	// Не работает
	@Test (groups = { "GetHousesSuggest_4_5" })
	public void Test35() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetHousesSuggest_4_5("design.prontosoft.by", "{region=russia/moskva-gorod/, street=бакальская, search_string=22}");
	}
	
	
	@Test (groups = { "GetdistrictSuggest_4_6" })
	public void Test36() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetdistrictSuggest_4_6("design.prontosoft.by", "{region=russia/moskva-gorod/, street=бакальская, house=23, search_string=гольяно}");
	}
	
	
	@Test (groups = { "GetDirectionSuggest_4_7" })
	public void Test37() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetDirectionSuggest_4_7("design.prontosoft.by", " {region=russia/moskovskaya-obl/, search_string=курс}");
	}
	
	
	// Не работает
	@Test (groups = { "GetHighwaySuggest_4_8" })
	public void Test38() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetHighwaySuggest_4_8("design.prontosoft.by", "{region=russia/moskovskaya-obl/, search_string=варшавское}");
	}
	
	//Ничего не нашли
	@Test (groups = { "GetMetroSuggest_4_9" })
	public void Test39() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetMetroSuggest_4_9("design.prontosoft.by", "{region=russia/moskovskaya-obl/, search_string=кутузова}");
	}
	
	
	// евро не переведено кодировка,  currencies не массив  в ответе
	@Test (groups = { "GetCurrencies_5_1" })
	public void Test40() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetCurrencies_5_1("design.prontosoft.by");
	} 
	
	
	// должен отдавать value отдает currencies
	@Test (groups = { "GetDictinary_6_1" })
	public void Test41() throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		cM.GetDictinary_6_1("design.prontosoft.by", "Car makers commercial");
	} 
	
	
}