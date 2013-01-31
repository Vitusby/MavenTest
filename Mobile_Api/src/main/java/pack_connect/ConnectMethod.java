package pack_connect;
//http://www.jarvana.com/jarvana/search?search_type=project&project=org.json

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.utils.URIBuilder;
import com.google.appengine.repackaged.org.json.*;

import pack_utils.ExceptFailTest;


public class ConnectMethod extends Connect_Request_Abstract
{
	private URIBuilder builder;; 
	private URI uri;
	private JSONObject jsonObject;
	
	
	// Создание профиля
	public void CreateProfileRequest_1(String sHost, String sEmail, String sPassword) throws URISyntaxException, IOException, ExceptFailTest
	{
		print("1.	Создание профиля");
		print("Параметры для запроса");
		print("email = "+ sEmail);
		print("password = "+ sPassword);
		builder = new URIBuilder();
		
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account")
    		.setParameter("email", sEmail)
    		.setParameter("password", sPassword);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequest(uri);
    	print("Парсим ответ....");
    	
    	// Проверка что получили
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + "Профиль пользователя создан");
    	else
    	{
    		print("Не удалось создать профилль пользователя\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}
	}	
	// Авторизация
	public String Authorization_1_1(String sHost, String sUsername, String sPassword) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		print("1.1.	Авторизация");
		print("Параметры для запроса");
		print("email = "+ sUsername);
		print("password = "+ sPassword);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/login")
    		.setParameter("username", sUsername)
    		.setParameter("password", sPassword);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequest(uri);
    	print("Парсим ответ....");
    	
    	
    	jsonObject = ParseResponse(sResponse);
    	String sTempResponse = jsonObject.toString();
    	print(sTempResponse);
    	
    	if(sTempResponse.equals("{\"error\":{\"description\":\"Не указан логин или пароль\",\"code\":1}}"))
    	{
    		print("Не указан логин или пароль");
    		print("Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}
    	if(sTempResponse.equals("{\"error\":{\"description\":\"Пользователя с такими данными не существует\",\"code\":3}}"))
    	{
    		print("Пользователя с такими данными не существует");
    		print("Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}
    	String sAuth_token = (String) jsonObject.get("auth_token");
    	if(sAuth_token != null)
    	{
    	         print("Auth_token = "+ sAuth_token);
    	         print("Ответ сервера:\r\n"+ jsonObject.toString());
    	         return sAuth_token;
    	}
    	else 
    	{
    		print("Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}
		
	}
	// Получение профиля
	public String GetProfile_1_2(String sHost,String sUsername, String sPassword) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		String  sAuth_token= "";
		sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		
		print("1.2.	Получение профиля");
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account")
    		.setParameter("auth_token", sAuth_token);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	String sTempResponse = jsonObject.toString();
    	
    	print("Ответ сервера:\r\n"+ sTempResponse);
    	
    	if(jsonObject.isNull("error"))
    		return sAuth_token;
    	else
    	{
    		print("Тест провален");
    		throw new ExceptFailTest("Тест провален");
    	}
    	
	}
	// Редактирование профиля
	public void EditProfile_1_3(String sHost,String sUsername, String sPassword, String sUser_info) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		sAuth_token = GetProfile_1_2(sHost, sUsername, sPassword);
		print("1.3.	Редактирование профиля");
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("user_info = "+ sUser_info);
		String sQuery = CreateArrayRequest("user_info", sUser_info);
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account")
    		.setQuery(sQuery)
    		.setParameter("auth_token", sAuth_token);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpPutRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	String sTempResponse = jsonObject.toString();
    	
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:\r\n"+ sTempResponse);
    	else
    	{
    		print("Тест провален");
    		print("Ответ сервера:\r\n"+ sTempResponse);
    		throw new ExceptFailTest("Тест провален");
    	}
		
	}
	// Восстановление пароля
	public void RestorePassword1_4(String sHost, String sEmail) throws URISyntaxException, IOException, ExceptFailTest
	{
		print("1.4.	Восстановление пароля");
		print("Параметры для запроса");
		print("email = "+ sEmail);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/restore")
    		.setParameter("email", sEmail);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " На указанный email отправлено письмо восстановления с инструкцией по восстановлению пароля");
    	else
    	{
    		print("Не удалось восстановить пароль\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}
    	
	}
	
	// Подача объявления
	public void PostAdvert_2_1(String sHost, String sUsername, String sPassword, String sCatRegAdv,  String sAdvertisement, String sCustom_fields, String sPathImage) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		
		print("");
		print("2.1.	Подача объявления");
		print("Параметры для запроса");
		print("sCatRegAdv = "+ sCatRegAdv);
		print("sAdvertisement = "+ sAdvertisement);
		print("sCustom_fields = "+ sCustom_fields);
		
		String sRequest = CreateSimpleRequest(sCatRegAdv);
		String sRequest1 = CreateArrayRequest("advertisement" ,sAdvertisement);
		String sRequest2 = CreateDoubleArrayRequest("advertisement", "custom_fields", sCustom_fields);
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert")
    		.setQuery(sRequest+sRequest1+sRequest2)
    		.setParameter("auth_token", sAuth_token);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequestImage(uri, sPathImage);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " Объявление создано");
    		
    	else
    	{
    		print("Не удалось создать объявление\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}
    	
	}
	// Получение объявления
	public String GetAdvert_2_2(String sHost, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		print("2.2.	Получение объявления");
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/"+ sIdAdvert);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	
    	if(jsonObject.isNull("error"))
    	{	
    		print("Ответ сервера:" + jsonObject.toString() + " Объявление получено");
    		
    		print("Ищем ссылку на изображение в объявлении");
    		JSONObject jTemp = jsonObject.getJSONObject("advertisement");
    		String s = (String) jTemp.get("images").toString();
    		if(s.equals("[]"))
    			return "false";
    		else
    		{
    			JSONArray ar = (JSONArray) jTemp.get("images");
    			jTemp = (JSONObject) ar.get(0);
    			print(jTemp.get("orig").toString());
    			return jTemp.get("orig").toString();
    		}
    	}
    	else
    	{
    		print("Объявление не получено\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}
	}
	// Редактирование объявления
	public void EditAdvert_2_3(String sHost, String sUsername, String sPassword, String sIdAdvert, String sCustom_fields, String sPathImageNew) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		String  sAuth_token= "";
		sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		String sUrlImage = GetAdvert_2_2(sHost, sIdAdvert);
		print("");
		print("2.3.	Редактирование объявления");
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("ADVERTISEMENT_ID = "+ sIdAdvert);
		if(sUrlImage.equals("false"))
		{
			print("В объявлении нет изображений ");
		}
		else
		{
			print("UrlImage = "+ sUrlImage);
		}
		
		String sRequest = CreateDoubleArrayRequest("advertisement", "custom_fields", sCustom_fields);
		
		builder = new URIBuilder();
		
		if(!sUrlImage.equals("false"))
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/"+ sIdAdvert)
    		.setQuery(sRequest + "&deleted_images[0]=" + sUrlImage).setParameter("auth_token", sAuth_token);

    	else
    		builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/"+ sIdAdvert)
    		.setQuery(sRequest).setParameter("auth_token", sAuth_token);
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPutRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " Объявление отредактировано");
    		
    	else
    	{
    		print("Не удалось отредактировать объявление\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}
		
	}
	// Удаление объявления
	public void DeleteAdvert_2_4(String sHost, String sUsername, String sPassword, String sIdAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		print("2.4.	Удаление объявления");
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert)
    			.setParameter("auth_token", sAuth_token);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpDeleteRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " Объявление удалено проверьте ЛК");
    	else
    	{
    		print("Не удалось удалить объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Добавление объявления в избранное
	public void AddAdvertToFavourite_2_5(String sHost, String sUsername, String sPassword, String sIdAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		
		String  sAuth_token= "";
		sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		
		print("2.5.	Добавление объявления в «Избранное»");
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert +"/favorite")
    			.setParameter("auth_token", sAuth_token);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " Объявление добавлено в избранное, проверьте вкладку изюранные для данного пользователя");
    	else
    	{
    		print("Не удалось добавить объявлени \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}
    	
	}
	// Удаления объявления из избранного
	public void DeleteAdvertFromFavourite_2_6(String sHost, String sUsername, String sPassword, String sIdAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		print("2.4.	Удаление объявления из «Избранное»");
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("ADVERTISEMENT_ID = "+ sIdAdvert);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert +"/favorite")
    			.setParameter("auth_token", sAuth_token);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpDeleteRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " Объявление удалено из избранного, проверьте вкладку");
    	else
    	{
    		print("Не удалось удалить объявление из избранного \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Получение списка платных продуктов для объявления доступных на этапе подачи объявления
	public void GetPaidProductsToStepToAdd_2_7(String sHost, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest
	{
		print("2.7.	Получение списка платных продуктов для объявления доступных на этапе подачи объявления");
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/products");
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " Список получен");
    	else
    	{
    		print("Не удалось получить список продуктов \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Получение списка платных продуктов для объявления доступных в личном кабинете пользователя
	public void GetPaidProductsFromLK_2_8(String sHost, String sIdAdvert) throws ExceptFailTest, URISyntaxException, IOException
	{
		print("2.8.	Получение списка платных продуктов для объявления доступных в личном кабинете пользователя");
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/products/pers_acc");
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " Список получен");
    	else
    	{
    		print("Не удалось получить список продуктов \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Получение списка бесплатных действий над объявлением
	public void GetFreeProductsForAdvert_2_9(String sHost, String sIdAdvert) throws ExceptFailTest, URISyntaxException, IOException
	{
		print("2.9.	Получение списка бесплатных действий над объявлением");
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/actions");
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " Список получен");
    	else
    	{
    		print("Не удалось получить список продуктов \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Активация объявлений
	public void ActivationAdvert_2_10(String sHost, String sUsername, String sPassword, String sIdAdvert, String sApp_token) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		
		print("2.10.	Активация объявлений");
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("ADVERTISEMENT_ID = "+ sIdAdvert);
		print("sApp_token = "+ sApp_token);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/activate")
    		.setParameter("auth_token", sAuth_token)
    		.setParameter("app_token", sApp_token);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpPostRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " Объявление активировано");
    	else
    	{
    		print("Не удалось активировать объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
		
	}
	// Деактивация объявлений
	public void DeactivateAdvert_2_11(String sHost, String sUsername, String sPassword, String sIdAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		
		print("2.11.	Деактивация объявления");
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("ADVERTISEMENT_ID = "+ sIdAdvert);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/deactivate")
    		.setParameter("auth_token", sAuth_token);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	

    	String sResponse = HttpPostRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " Объявление деактивировано");
    	else
    	{
    		print("Не удалось деактивировать объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
    	
	}
	// Продление объявления
	public void Prolongadvert_2_12(String sHost, String sUsername, String sPassword, String sIdAdvert, String sApp_token) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		
		print("2.12.	Продление объявления");
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("ADVERTISEMENT_ID = "+ sIdAdvert);
		print("sApp_token = "+ sApp_token);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/prolong")
    		.setParameter("auth_token", sAuth_token)
    		.setParameter("app_token", sApp_token);;
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	

    	String sResponse = HttpPostRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " Объявление продлено");
    	else
    	{
    		print("Не удалось продлить объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Поднятие объявления
	public void PushUpAdvert_2_13(String sHost, String sUsername, String sPassword, String sIdAdvert, String sApp_token) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		
		print("2.13.	Поднятие объявления в списке");
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("ADVERTISEMENT_ID = "+ sIdAdvert);
		print("sApp_token = "+ sApp_token);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/pushup")
    		.setParameter("auth_token", sAuth_token)
    		.setParameter("app_token", sApp_token);;
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	

    	String sResponse = HttpPostRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " Объявление поднято");
    	else
    	{
    		print("Не удалось поднять объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Выделение объявления 
	public void HighLightAdvert_2_14(String sHost, String sUsername, String sPassword, String sIdAdvert, String sApp_token) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		
		print("2.14. Выделения объявления в списке");
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("ADVERTISEMENT_ID = "+ sIdAdvert);
		print("sApp_token = "+ sApp_token);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/highlight")
    		.setParameter("auth_token", sAuth_token)
    		.setParameter("app_token", sApp_token);;
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpPostRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " Объявление выделено");
    	else
    	{
    		print("Не удалось выделить объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Назначение «Премиум» объявлению
	public void SetPremiumForAdvert_2_15(String sHost, String sUsername, String sPassword, String sIdAdvert, String sApp_token, String sNumberDays) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		
		print("2.15. Назначение «Премиум» объявлению");
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("ADVERTISEMENT_ID = "+ sIdAdvert);
		print("sApp_token = "+ sApp_token);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/premium")
    		.setParameter("auth_token", sAuth_token)
    		.setParameter("app_token", sApp_token)
    		.setParameter("number", sNumberDays);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpPostRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " Объявлению назначен премиум");
    	else
    	{
    		print("Не удалось назначить премиум объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Проголосовать за объявление (повысить рейтинг объявления)
	public void VoteForAdvertHigh_2_16(String sHost, String sUsername, String sPassword, String sIdAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		
		print("2.16.	Проголосовать за объявление (повысить рейтинг объявления)");
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("ADVERTISEMENT_ID = "+ sIdAdvert);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/vote")
    		.setParameter("auth_token", sAuth_token);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpPostRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " Объявлению +1 голос");
    	else
    	{
    		print("Не удалось добавить голос объявлению \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Проголосовать за объявление (снизить рейтинг объявления)
	public void VoteForAdvertLower_2_17(String sHost, String sUsername, String sPassword, String sIdAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		
		print("2.17.	Проголосовать за объявление (снизить рейтинг объявления)");
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("ADVERTISEMENT_ID = "+ sIdAdvert);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/vote")
    		.setParameter("auth_token", sAuth_token);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpDeleteRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:" + jsonObject.toString() + " Объявлению -1 голос");
    	else
    	{
    		print("Не удалось отнять голос у объявления \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Получение листинга объявлений категории
	public void GetListForCategory_2_18(String sHost, String sDataForListing) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		print("2.18.	Получение листинга объявлений категории");
		print("Параметры для запроса");
		print("DataForListing = "+ sDataForListing);
		
		String sQuery = CreateSimpleRequest(sDataForListing);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/category")
    		.setQuery(sQuery);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера: Листинг объявлений получен");
    		print("");
    		JSONArray ar = jsonObject.getJSONArray("advertisements");
    		for(int i=0; i<ar.length(); i++)
    		{
    			print("");
    			print(ar.get(i));
    		}
    	}
    	else
    	{
    		print("Не удалось получить листинг категории \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Фильтрация/поиск объявлений по критериям 
	public void GetListSearchCategory_2_19(String sHost, String sDataForListing, String sDataForSearch) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		print("2.19.	Фильтрация/поиск объявлений по критериям");
		print("Параметры для запроса");
		print("DataForListing = "+ sDataForListing);
		
		String sQuery = CreateSimpleRequest(sDataForListing);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/search")
    		.setQuery(sQuery);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
   
    	String ss =	"/&filters=/search/"+sDataForSearch;
    	String s1 = uri.toString()+ss;
    	uri = new URI(s1);
    	
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера" + jsonObject.toString() + "Фильтр-листинг получен");
    		print("");
    		JSONArray ar = jsonObject.getJSONArray("advertisements");
    		for(int i=0; i<ar.length(); i++)
    		{	
    			print("");
    			print(ar.get(i));
    		}
    	}
    	else
    	{
    		print("Не удалось получить фильтр-листинг по критериям \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	//2.20.	Получение листинга объявлений, добавленных в «Избранное»
	public void GetListFavourite_2_20(String sHost, String sUsername, String sPassword, String sDataForSearchFavourite) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		
		
		print("2.20.	Получение листинга объявлений, добавленных в «Избранное»");
		print("Параметры для запроса");
		print("DataForSearchFavourite = "+ sDataForSearchFavourite);
		
		
		String sQuery = CreateSimpleRequest(sDataForSearchFavourite);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/favorites")
    		.setQuery(sQuery)
    		.setParameter("auth_token", sAuth_token);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "листинг объявлений, добавленных в «Избранное» получен");
    		JSONArray ar = jsonObject.getJSONArray("advertisements");
    		for(int i=0; i<ar.length(); i++)
    			print(ar.get(i));
    	}
    	else
    	{
    		print("Не удалось получить листинг объявлений, добавленных в «Избранное» \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	//2.21.	Получение листинга «своих» объявлений
	public void GetListOwnAdvert_2_21(String sHost, String sUsername, String sPassword, String sDataForSearchOwnAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		
		
		print("2.21.	Получение листинга «своих» объявлений");
		print("Параметры для запроса");
		print("DataForSearchOwnAdvert = "+ sDataForSearchOwnAdvert);
		
		
		String sQuery = CreateSimpleRequest(sDataForSearchOwnAdvert);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/personal")
    		.setQuery(sQuery)
    		.setParameter("auth_token", sAuth_token);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "листинг объявлений «своих» объявлений получен");
    		print("");
    		JSONArray ar = jsonObject.getJSONArray("advertisements");
    		for(int i=0; i<ar.length(); i++)
    		{	
    			print("");
    			print(ar.get(i));
    		}
    	}
    	else
    	{
    		print("Не удалось получить листинг «своих» объявлений \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	//2.22.	Получение листинга объявлений пользователя
	public void GetListUserAdvert_2_22(String sHost, String sDataForSearchUserAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("2.22.	Получение листинга объявлений пользователя");
		print("Параметры для запроса");
		print("DataForSearchUserAdvert = "+ sDataForSearchUserAdvert);
		
		
		String sQuery = CreateSimpleRequest(sDataForSearchUserAdvert);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/user")
    		.setQuery(sQuery);
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "листинг объявлений пользователя получен");
    		print("");
    		JSONArray ar = jsonObject.getJSONArray("advertisements");
    		for(int i=0; i<ar.length(); i++)
    		{
    			print("");
    			print(ar.get(i));
    		}
    	}
    	else
    	{
    		print("Не удалось получить листинг объявлений пользователя \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	
	//3.1.	Получение рубрикатора сайта
	public void GetRubricator_3_1(String sHost, String sCategory) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("3.1.	Получение рубрикатора сайта");
		print("Параметры для запроса");
		print("category = "+ sCategory);
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/categories")
    		.setParameter("category", sCategory);
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "рубрикатора сайта получен");
    		print("");
    		JSONArray ar = jsonObject.getJSONArray("categories");
    		for(int i=0; i<ar.length(); i++)
    			print(ar.get(i));
    	}
    	else
    	{
    		print("Не удалось получить рубрикатора сайта \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	//3.2.	Получение списка полей рубрики для подачи объявления
	public void GetCastomfieldsForAddAdvert_3_2(String sHost, String sDataCustomfieldsAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("3.2.	Получение списка полей рубрики для подачи объявления");
		print("Параметры для запроса");
		print("DataCustomfieldsAdvert = "+ sDataCustomfieldsAdvert);
		String sQuery = CreateSimpleRequest(sDataCustomfieldsAdvert);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/categories/fields/post")
    		.setQuery(sQuery);
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "список полей рубрики для подачи объявления получен");
    		print("");
    		jsonObject = jsonObject.getJSONObject("group_custom_fields");
    		print(jsonObject.keys());
    	}
    	else
    	{
    		print("Не удалось получить список полей рубрики для подачи объявления \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	//3.3.	Получение списка полей рубрики для редактирования объявления
	public void GetCastomfieldsForEditAdvert_3_3(String sHost, String sDataCustomfieldsEditAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("3.3.	Получение списка полей рубрики для редактирования объявления");
		print("Параметры для запроса");
		print("DataCustomfieldsEditAdvert = "+ sDataCustomfieldsEditAdvert);
		String sQuery = CreateSimpleRequest(sDataCustomfieldsEditAdvert);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/categories/fields/edit")
    		.setQuery(sQuery);
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "список полей рубрики для редактирования объявления получен");
    		print("");
    		jsonObject = jsonObject.getJSONObject("group_custom_fields");
    		print(jsonObject.names());
    	}
    	else
    	{
    		print("Не удалось получить список полей рубрики для редактирования объявления получен \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	//3.4.	Получение списка полей рубрики для фильтрации объявлений
	public void GetCastomfieldsForSearchAdvert_3_4(String sHost, String sDataCustomfieldsEditAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("3.4.	Получение списка полей рубрики для фильтрации объявлений");
		print("Параметры для запроса");
		print("DataCustomfieldsEditAdvert = "+ sDataCustomfieldsEditAdvert);
		String sQuery = CreateSimpleRequest(sDataCustomfieldsEditAdvert);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/categories/fields/search")
    		.setQuery(sQuery);
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "список полей рубрики для фильтрации объявлений получен");
    		JSONArray ar = jsonObject.getJSONArray("default");
    		for(int i=0; i<ar.length(); i++)
    			print(ar.get(i));
    	}
    	else
    	{
    		print("Не удалось получить список полей рубрики для фильтрации объявлений \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	//4.1.	Получение списка субъектов РФ
	public void GetRegions_4_1(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("4.1.	Получение списка субъектов РФ");
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/regions");
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "список субъектов РФ получен");
    		JSONArray ar = jsonObject.getJSONArray("regions");
    		for(int i=0; i<ar.length(); i++)
    			print(ar.get(i));
    	}
    	else
    	{
    		print("Не удалось получить список субъектов РФ \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	//4.2.	Получение списка популярных городов
	public void GetPopularCities_4_2(String sHost, String sRegion) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("4.2.	Получение списка популярных городов");
		print("Параметры для запроса");
		print("region = "+ sRegion);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/regions/cities")
    	.setParameter("region", sRegion);
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "список популярных городов получен");
    		JSONArray ar = jsonObject.getJSONArray("regions");
    		for(int i=0; i<ar.length(); i++)
    			print(ar.get(i));
    	}
    	else
    	{
    		print("Не удалось получить популярных городов \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	// 4.3.	Поиск городов и населенных пунктов по названию (саджест)
	public void GetCitiesSuggest_4_3(String sHost, String sDataCitiesSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("4.3.	Поиск городов и населенных пунктов по названию (саджест)");
		print("Параметры для запроса");
		print("DataCitiesSuggest = "+ sDataCitiesSuggest);
	
		String sQuery = CreateSimpleRequest(sDataCitiesSuggest);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/regions/search")
    		.setQuery(sQuery);
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "\r\n список городов и населенных пунктов по названию (саджест) получен");
    		print("");
    		JSONArray ar = jsonObject.getJSONArray("regions");
    		for(int i=0; i<ar.length(); i++)
    		{
    			print(ar.get(i));
    		}
    	}
    	else
    	{
    		print("Не удалось получить городов и населенных пунктов по названию (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	//4.4.	Получение списка улиц (саджест)	
	public void GetStreetsSuggest_4_4(String sHost, String sDataStreetsSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("4.4.	Получение списка улиц (саджест)");
		print("Параметры для запроса");
		print("DataStreetsSuggest = "+ sDataStreetsSuggest);
	
		String sQuery = CreateSimpleRequest(sDataStreetsSuggest);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/regions/streets")
    		.setQuery(sQuery);
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "список улиц (саджест) получен");
    		JSONArray ar = jsonObject.getJSONArray("streets");
    		for(int i=0; i<ar.length(); i++)
    			print(ar.get(i));
    	}
    	else
    	{
    		print("Не удалось получить список улиц (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	//4.5.	Получение списка домов улицы (саджест)
	public void GetHousesSuggest_4_5(String sHost, String sDataHousesSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("4.5.	Получение списка домов улицы (саджест)".toUpperCase());
		print("Параметры для запроса");
		print("DataHousesSuggest = "+ sDataHousesSuggest);
	
		String sQuery = CreateSimpleRequest(sDataHousesSuggest);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/regions/houses")
    		.setQuery(sQuery);
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "\r\n список домов улицы (саджест) получен");
    		print("");
    		JSONArray ar = jsonObject.getJSONArray("houses");
    		for(int i=0; i<ar.length(); i++)
    			print(ar.get(i));
    	}
    	else
    	{
    		print("Не удалось получить список домов улицы (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	//4.6.	Получение списка районов/микрорайонов (саджест)
	public void GetdistrictSuggest_4_6(String sHost, String sDataDistrictSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("4.6.	Получение списка районов/микрорайонов (саджест)");
		print("Параметры для запроса");
		print("DataDistrictSuggest = "+ sDataDistrictSuggest);
	
		String sQuery = CreateSimpleRequest(sDataDistrictSuggest);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/regions/districts")
    		.setQuery(sQuery);
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "список районов/микрорайонов (саджест) получен");
    		JSONArray ar = jsonObject.getJSONArray("districts");
    		for(int i=0; i<ar.length(); i++)
    			print(ar.get(i));
    	}
    	else
    	{
    		print("Не удалось получить список районов/микрорайонов (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	//4.7.	Получение списка направлений (саджест)
	public void GetDirectionSuggest_4_7(String sHost, String sDataDirectionSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("4.7.	Получение списка направлений (саджест)");
		print("Параметры для запроса");
		print("DataDirectionSuggest = "+ sDataDirectionSuggest);
	
		String sQuery = CreateSimpleRequest(sDataDirectionSuggest);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/regions/directions")
    		.setQuery(sQuery);
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "список направлений (саджест) получен");
    		JSONArray ar = jsonObject.getJSONArray("directions");
    		for(int i=0; i<ar.length(); i++)
    			print(ar.get(i));
    	}
    	else
    	{
    		print("Не удалось получить список направлений (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	//4.8.	Получение списка шоссе (саджест)
	public void GetHighwaySuggest_4_8(String sHost, String sDataHighwaySuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("4.8.	Получение списка шоссе (саджест)");
		print("Параметры для запроса");
		print("DataHighwaySuggest = "+ sDataHighwaySuggest);
	
		String sQuery = CreateSimpleRequest(sDataHighwaySuggest);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/regions/highway")
    		.setQuery(sQuery);
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "список шоссе (саджест) получен");
    		JSONArray ar = jsonObject.getJSONArray("highway");
    		for(int i=0; i<ar.length(); i++)
    			print(ar.get(i));
    	}
    	else
    	{
    		print("Не удалось получить список шоссе (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	//4.9.	Получение списка станций метро (саджест)
	public void GetMetroSuggest_4_9(String sHost, String sDataMetroSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("4.9.	Получение списка станций метро (саджест)");
		print("Параметры для запроса");
		print("DataMetroSuggest = "+ sDataMetroSuggest);
	
		String sQuery = CreateSimpleRequest(sDataMetroSuggest);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/regions/metro")
    		.setQuery(sQuery);
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "список станций метро (саджест) получен");
    		JSONArray ar = jsonObject.getJSONArray("metro");
    		for(int i=0; i<ar.length(); i++)
    			print(ar.get(i));
    	}
    	else
    	{
    		print("Не удалось получить список станций метро (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	
	//5.1.	Получение списка валют
	public void GetCurrencies_5_1(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("5.1.	Получение списка валют");
	
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/currencies");
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "список валют получен");
    		/*JSONArray ar = jsonObject.getJSONArray("currencies");
    		for(int i=0; i<ar.length(); i++)
    			print(ar.get(i));*/
    	}
    	else
    	{
    		print("Не удалось получить список валют \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	
	//6.1.	Получение значений словаря
	public void GetDictinary_6_1(String sHost, String sNameDict) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("6.1.	Получение значений словаря");
		print("Параметры для запроса");
		print("NameDictinary = "+ sNameDict);
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/dictionary/" + sNameDict);
    	
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "значения словаря получены");
    		JSONArray ar = jsonObject.getJSONArray("currencies");
    		for(int i=0; i<ar.length(); i++)
    			print(ar.get(i));
    	}
    	else
    	{
    		print("Не удалось получить значения словаря \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	private JSONObject ParseResponse(String sResponse) throws ExceptFailTest
	   {
		   JSONObject tempJsonObject = null;
		   try
	    	{
			   tempJsonObject = new JSONObject(sResponse);
	    	}
	    	catch(JSONException exc)
	    	{
	    		print("Не удалось распарсить ответ");
	    		print("Ответ на запрос:");
	    		print(sResponse);
	    		exc.printStackTrace();
	    		throw new ExceptFailTest("Не удалось распарсить ответ");
	    	}
		   	return tempJsonObject;
	   }
	
	
}

