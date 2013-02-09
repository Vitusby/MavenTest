package pack_connect;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;

import org.apache.http.client.utils.URIBuilder;
import com.google.appengine.repackaged.org.json.*;

import pack_utils.ExceptFailTest;
import pack_utils.HM;
import pack_utils.Proper;
import pack_utils.RamdomData;

public class ConnectMethod extends Connect_Request_Abstract
{
	private URIBuilder builder;; 
	private URI uri;
	private JSONObject jsonObject;
	String mas_Advertisment[] = {"phone", "phone_add", "contact", "phone2", "phone_add2", "alternative_contact", "web",
			"price", "currency", "title", "text"};
	String mas_Auto2[] = {"make", "model", "mileage", "engine-power", "condition", "car-year", "transmittion",
			"modification", "bodytype", "electromirror", "cruiscontrol", "color"};
	String mas_Realt2[] = {"etage", "rooms", "private", "meters-total", "mapStreet", "mapHouseNr", "etage-all",
			"walltype", "house-series", "kitchen", "internet", "telephone", "state"};
	String mas_TIY2[] = {"used-or-new", "vacuumclean_wash", "offertype", "model"};
	
	class InnerDataHM // вннутренний класс храним здесь значения для объявлений после того как они созданы для проверки
	{
		String sID;
		HM<String, String> hObj_Adv_Inn;
		HM<String, String> hObj_Cust_Inn;
			
		InnerDataHM(HM<String, String> hObj_Adv, HM<String, String> hObj_Cust, String sC)
		{
			hObj_Adv_Inn = hObj_Adv;
			hObj_Cust_Inn = hObj_Cust;
			sID = sC;
		}
		HM<String, String> GetAdvertismentData() {return hObj_Adv_Inn;} 
		HM<String, String> GetCustomfieldData() {return hObj_Cust_Inn;} 
		String GetID() {return sID;}	
	}

// автотесты	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	// Создание профиля АвтоТест
	public void CreateProfileReqeust(String sHost) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		print("------------------------------------------------------------------------------------------------------------");
		print("Создание профиля - Тест".toUpperCase());
		print("\r\nСоздание профиля".toUpperCase());
		print("Параметры для запроса");
		print("Генерируем Еmail и пароль");
		String sEmail = RamdomData.GetRamdomString(7)+"@yopmail.com";
		String sPassword = RamdomData.GetRamdomString(7);
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
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nПрофиль пользователя создан\r\n");
    	else
    	{
    		print("Не удалось создать профилль пользователя\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10)+"\r\n");
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}
    	
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
	}
	// Авторизация АвтоТест
	public void Authorization(String sHost) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		JSONObject jTemp;
		print("------------------------------------------------------------------------------------------------------------");
		print("Авторизация - Тест".toUpperCase());
		print("\r\nАвторизация - Обычный пользователь".toUpperCase());
		print("Параметры для запроса");
		print("email = "+ Proper.GetProperty("login_authOP"));
		print("password = "+ Proper.GetProperty("password"));
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/login")
    		.setParameter("username", Proper.GetProperty("login_authOP"))
    		.setParameter("password", Proper.GetProperty("password"));
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
    	{
	    	String sAuth_token = (String) jsonObject.get("auth_token");
	    	if(sAuth_token != null)
	    	{
	    	         print("Auth_token получен = "+ sAuth_token);
	    	         print("Ответ сервера:\r\n"+ jsonObject.toString(10) + "\r\nПользователь авторизован");
	    	}
	    	else
	    	{
	    		print("Не удалось получить ключ Auth_token");
	    		print("Тест провален".toUpperCase());
	    		throw new ExceptFailTest("Тест провален");
	    	}
    	}
    	else 
    	{
    		print("Ответ сервера:\r\n"+ jsonObject.toString(10) + "\r\n");
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}
    	
    	/////////////////////////////////////////////////////////////////////////////////////////////
    	print("\r\nАвторизация - Интернет партнер".toUpperCase());
		print("Параметры для запроса");
		print("email = "+ Proper.GetProperty("login_authIP"));
		print("password = "+ Proper.GetProperty("password"));
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/login")
    		.setParameter("username", Proper.GetProperty("login_authIP"))
    		.setParameter("password", Proper.GetProperty("password"));
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	sResponse = HttpPostRequest(uri);
    	print("Парсим ответ....");
    	jsonObject = ParseResponse(sResponse);
    	
    	
    	if(jsonObject.isNull("error"))
    	{
	    	String sAuth_token = (String) jsonObject.get("auth_token");
	    	if(sAuth_token != null)
	    	{
	    	         print("Auth_token получен = "+ sAuth_token);  
	    	         print("Ответ сервера:\r\n"+ jsonObject.toString(10) + "\r\nПользователь авторизован");
	    	}
	    	else
	    	{
	    		print("Не удалось получить ключ Auth_token");
	    		print("Тест провален".toUpperCase());
	    		throw new ExceptFailTest("Тест провален");
	    	}
    	}
    	else 
    	{
    		print("Ответ сервера:\r\n"+ jsonObject.toString(10) + "\r\n");
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}
    	///////////////////////////////////////////////////////////////////////////////////////////////
 
		print("\r\nАвторизация - Несуществующий пользователь".toUpperCase());
		print("Параметры для запроса");
		print("email = " + Proper.GetProperty("login_authNotExist"));
		print("password = " + Proper.GetProperty("password"));
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/login")
    		.setParameter("username", Proper.GetProperty("login_authNotExist"))
    		.setParameter("password", Proper.GetProperty("password"));
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	sResponse = HttpPostRequest(uri);
    	print("Парсим ответ....");
    	jsonObject = ParseResponse(sResponse);
    	
    	jTemp = jsonObject.getJSONObject("error");
    	String sResult = jTemp.getString("description");
    	
    	if(sResult.equals("Пользователя с такими данными не существует"))
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nПользователя не существует");
    	else 
    	{
    		print("Ответ сервера:\r\n"+ jsonObject.toString(10) + "\r\n");
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}
    	
    	///////////////////////////////////////////////////////////////////////////////////////////////
    	
    	print("\r\nАвторизация - Забаненный пользователь".toUpperCase());
		print("Параметры для запроса");
		print("email = " + Proper.GetProperty("login_authBan"));
		print("password = " + Proper.GetProperty("password"));
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/login")
    		.setParameter("username", Proper.GetProperty("login_authBan"))
    		.setParameter("password", Proper.GetProperty("password"));
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	sResponse = HttpPostRequest(uri);
    	print("Парсим ответ....");
    	jsonObject = ParseResponse(sResponse);
    	
    	jTemp = jsonObject.getJSONObject("error");
    	sResult = jTemp.getString("description");
    	
    	if(sResult.equals("Пользователь не активный"))
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nПользователя не активен");
    	else 
    	{
    		print("Ответ сервера:\r\n"+ jsonObject.toString(10) + "\r\n");
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}
    	
		///////////////////////////////////////////////////////////////////////////////////////////////
    	
		print("\r\nАвторизация - Неактивный(не подтвердивший регистрацию) пользователь".toUpperCase());
		print("Параметры для запроса");
		print("email = " + Proper.GetProperty("login_authNotActive"));
		print("password = " + Proper.GetProperty("password"));
		builder = new URIBuilder();
		builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/login")
		.setParameter("username", Proper.GetProperty("login_authNotActive"))
		.setParameter("password", Proper.GetProperty("password"));
		uri = builder.build();
		if(uri.toString().indexOf("%25") != -1)
		{
		String sTempUri = uri.toString().replace("%25", "%");
		uri = new URI(sTempUri);			
		}
		print("Отправляем запрос. Uri Запроса: "+uri.toString());
		sResponse = HttpPostRequest(uri);
		print("Парсим ответ....");
		jsonObject = ParseResponse(sResponse);
		
		jTemp = jsonObject.getJSONObject("error");
		sResult = jTemp.getString("description");
		
		if(sResult.equals("Пользователя с такими данными не существует"))
		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nПользователя не существует");
		else 
		{
		print("Ответ сервера:\r\n"+ jsonObject.toString(10) + "\r\n");
		print("Тест провален".toUpperCase());
		throw new ExceptFailTest("Тест провален");
		}
		
		print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
    	
	}
	// Получение/Редактирование профиля Автотест
	public void GetAndEditProfile(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		JSONObject jTemp, jData;
		String jLogin="", jEmail="";
		
		String sLogin = Proper.GetProperty("login_authOP");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		print("------------------------------------------------------------------------------------------------------------");
		print("Авторизация, получение, редактирование профиля - Тест".toUpperCase()+"\r\n");
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
		
		print("\r\nПолучение профиля".toUpperCase());
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
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:\r\n"+ jsonObject.toString(10)+"\r\nПрофиль получен.");
    		print("Проверяем совпадение логина и email");
    		jTemp = jsonObject.getJSONObject("user_info"); 
    		jData = jTemp; // для проверки и сравнения данных
    		jLogin = jTemp.getString("login"); // используем при сравнени после редактирования профиля
    		jEmail = jTemp.getString("email"); // используем при сравнени после редактирования профиля
    		
    		if(jTemp.getString("login").equals(sLogin) && jTemp.getString("email").equals(sLogin))
    		{
    			print("Логин пользователя: "+ sLogin + " для которого запрашивается профиль, совпал с логином: "+ jTemp.getString("login") + " полученным в профиле");
    			print("Email пользователя: "+ sLogin + " для которого запрашивается профиль, совпал с логином: "+ jTemp.getString("email") + " полученным в профиле");
    		}
    		else
    		{
    			print("Тест провален. Логин: " + sLogin +" или Email: " + sLogin + " пользователя для котрого запрашивалсяя профиль," +
    					" не совпали с полученным логином: "+ jTemp.getString("login") + " или Email: " + jTemp.getString("email"));	
    			print("Тест провален".toUpperCase());
    			throw new ExceptFailTest("Тест провален");
    		}
    	}
    	else
    	{
    		print("Ответ сервера:\r\n"+ jsonObject.toString(10)+"");
    		print("Тест провален");
    		throw new ExceptFailTest("Тест провален");
    	}
    	
    	print("\r\nРедактирование профиля".toUpperCase());
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("Генерируем данные");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		HM<String, String> hObj = new HM<String, String>(); 
		String mas[] = {"site", "zip", "building", "phone", "other_email", "fax", "street", "icq", "contact", "dont_subscribe", "city",
				"title", "address", "mobile", "email", "login"};
		
		for(int i=0; i<mas.length; i++)
		{
			hObj.SetValue(mas[i], RamdomData.GetRandomData(Proper.GetProperty(mas[i]), jData.getString(mas[i])));
		}
		
		String sQuery = CreateArrayRequest("user_info", hObj.GetStringFromAllHashMap());
		print("user_info = "+ hObj.GetStringFromAllHashMap());
		
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
    	
    	sResponse = HttpPutRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	
    	if(jsonObject.isNull("error"))
    	{	
    		print("Ответ сервера:\r\n"+ jsonObject.toString(10));
    		print("Проверяем изменения данных для профиля");
    		jTemp = jsonObject.getJSONObject("user_info"); 
    		jData = jTemp; // для проверки и сравнения данных
    		for(int i=0; i<mas.length; i++)
    		{
    			if(mas[i].equals("login") || mas[i].equals("email"))
    			{
	    			// проверяем не изменился ли login
	    			if(mas[i].equals("login"))
	    			{
	    				if(jLogin.equals(jData.getString(mas[i])))
	    					print("Значение login = " + jLogin + 
		    						" не изменилось после редактирования профиля " + mas[i] + " = " + jData.getString(mas[i]));
		    			else
		    			{
		    				print("Значение login = " + jLogin + 
		    						" изменилось после редактирования профиля " + mas[i] + " = " + jData.getString(mas[i]));
		    				print("Тест провален".toUpperCase());
		    				throw new ExceptFailTest("Тест провален");
		    			}
	    			}
	    			// проверяем не изменился ли email
	    			if(mas[i].equals("email"))
	    			{
	    				if(jEmail.equals(jData.getString(mas[i])))
		    				print("Значение email = " + jEmail + 
		    						" не изменилось после редактирования профиля " + mas[i] + " = " + jData.getString(mas[i]));
		    			else
		    			{
		    				print("Значение профиля email = " + jEmail + 
		    						" изменилось после редактирования профиля " + mas[i] + " = " + jData.getString(mas[i]));
		    				print("Тест провален".toUpperCase());
		    				throw new ExceptFailTest("Тест провален");
		    			}
	    			}
    			}
    			else
    			{
					// проверяем изменились ли другие данные
					if(hObj.GetValue(mas[i]).equals(jData.getString(mas[i])))
						print("Значение " + mas[i] +" = " + hObj.GetValue(mas[i]) + " указанное для запроса редактирования профиля," +
								" совпало с полученным значение в профиле после редактирования " + mas[i] + " = " + jData.getString(mas[i]));
					else
					{
						print("Значение " + mas[i] +" = " + hObj.GetValue(mas[i]) + " указанное для запроса редактирования профиля," +
								" не совпало с полученным значение в профиле после редактирования " + mas[i] + " = " + jData.getString(mas[i]));
						print("Тест провален".toUpperCase());
						throw new ExceptFailTest("Тест провален");
					}
    			}
    			
    		}
    	}
    	else
    	{
    		print("Тест провален".toUpperCase());
    		print("Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	} 
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
	}
	// Восстановления пароля Автотест, 
	public void RestorePassword(String sHost) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		print("------------------------------------------------------------------------------------------------------------");
		print("Восстановление пароля - Тест".toUpperCase()+"\r\n");
		print("Восстановление пароля".toUpperCase());
		print("Параметры для запроса");
		print("email = "+ Proper.GetProperty("login_authOP"));
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/restore")
    		.setParameter("email", Proper.GetProperty("login_authOP"));
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
    		print("Ответ сервера:" + jsonObject.toString(10) + "\r\n На указанный email отправлено письмо восстановления с инструкцией по восстановлению пароля");
    	else
    	{
    		print("Не удалось восстановить пароль\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
	}
	
	
	
	// Подача/Получение/Редактирование объявление ОП Автотест
	public void AddGetEditAdvertOP(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, InterruptedException
	{
		String sIdAuto, sIdRealt, sIdTIU, sImageUrlAuto, sImageUrlRealt, sImageUrlTIY; 
		String sLogin = Proper.GetProperty("login_authOP");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		HM<String, String> hObj_Auto;
		HM<String, String> hObj_Auto2;
		HM<String, String> hObj_Realt;
		HM<String, String> hObj_Realt2;
		HM<String, String> hObj_TIY;
		HM<String, String> hObj_TIY2;
		JSONObject jData;
		InnerDataHM objAuto, objRealt, objTIY;
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Подача, получение, редактирование объявления ОП - Тест".toUpperCase()+"\r\n");
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
		
/////////////////////////////////////////////////////////////////////////////////////////////////		
		print("\r\nПодача объявления в рубрику Авто с пробегом".toUpperCase());
		objAuto = PostAdvert(sHost, mas_Advertisment, mas_Auto2, sAuth_token, "category_auto", "image");
		sIdAuto = objAuto.GetID();  // сюда сохраняем значение id
		hObj_Auto = objAuto.GetAdvertismentData(); // сюда сохраняем значение массива адветисемент (контакты, title, web, price и т.д. указанные при подаче )  
		hObj_Auto2 = objAuto.GetCustomfieldData(); // сюда сохраняем значение массива кастомфилдов, указанные при подаче


/////////////////////////////////////////////////////////////////////////////////////////////////    	
    	print("\r\nПодача объявления в рубрику Недвижимость - Вторичный рынок".toUpperCase());
    	objRealt = PostAdvert(sHost, mas_Advertisment, mas_Realt2, sAuth_token, "category_realt", "image2");
    	sIdRealt = objRealt.GetID();
    	hObj_Realt = objRealt.GetAdvertismentData();
    	hObj_Realt2 = objRealt.GetCustomfieldData();
    	
    	
///////////////////////////////////////////////////////////////////////////////////////////////// 
    	print("\r\nПодача объявления в рубрику Электроника и техника - Вторичный рынок".toUpperCase());
    	objTIY = PostAdvert(sHost, mas_Advertisment, mas_TIY2, sAuth_token, "category_electron", "image3");
    	sIdTIU = objTIY.GetID();
    	hObj_TIY = objTIY.GetAdvertismentData();
    	hObj_TIY2 = objTIY.GetCustomfieldData();
    
    	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    	jData = GetAdvert(sHost, sIdAuto, "Авто с пробегом");
    	print("Проверяем корректность указанных данных при подаче объявления");
    	sImageUrlAuto = ValidateDataFromAdvertAfterPost(mas_Advertisment, mas_Auto2, hObj_Auto, hObj_Auto2, jData);
		print("");
    	
		jData = GetAdvert(sHost, sIdRealt, "Вторичный рынок");
    	print("Проверяем корректность указанных данных при подаче объявления");
    	sImageUrlRealt = ValidateDataFromAdvertAfterPost(mas_Advertisment, mas_Realt2, hObj_Realt, hObj_Realt2, jData);
		print("");
		
		jData = GetAdvert(sHost, sIdTIU, "Пылесосы");
    	print("Проверяем корректность указанных данных при подаче объявления");
    	sImageUrlTIY = ValidateDataFromAdvertAfterPost(mas_Advertisment, mas_TIY2, hObj_TIY, hObj_TIY2, jData);
		print("");
		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		print("\r\nРедактирование объявления. Авто с пробегом");
		objAuto = EditAdvert(sHost, mas_Advertisment, mas_Auto2, objAuto, sAuth_token, sImageUrlAuto);
		sIdAuto = objAuto.GetID(); // сюда сохраняем значение id
		hObj_Auto = objAuto.GetAdvertismentData(); // сюда сохраняем значение массива адветисемент (контакты, title, web, price и т.д. указанные при редактировании )  
		hObj_Auto2 = objAuto.GetCustomfieldData(); // сюда сохраняем значение массива кастомфилдов, указанные при редактировании
		ValidateDataFromAdvertAfterEdit(mas_Advertisment, mas_Auto2, hObj_Auto, hObj_Auto2);
		
		print("\r\nРедактирование объявления. Вторичный рынок");
		objRealt = EditAdvert(sHost, mas_Advertisment, mas_Realt2, objRealt, sAuth_token, sImageUrlRealt);
		sIdRealt = objAuto.GetID(); // сюда сохраняем значение id
		hObj_Realt = objRealt.GetAdvertismentData(); // сюда сохраняем значение массива адветисемент (контакты, title, web, price и т.д. указанные при редактировании )  
		hObj_Realt2 = objRealt.GetCustomfieldData(); // сюда сохраняем значение массива кастомфилдов, указанные при редактировании
		ValidateDataFromAdvertAfterEdit(mas_Advertisment, mas_Realt2, hObj_Realt, hObj_Realt2);
		
		print("\r\nРедактирование объявления. Пылесосы");
		objTIY = EditAdvert(sHost, mas_Advertisment, mas_TIY2, objTIY, sAuth_token, sImageUrlTIY);
		sIdTIU = objAuto.GetID(); // сюда сохраняем значение id
		hObj_TIY = objTIY.GetAdvertismentData(); // сюда сохраняем значение массива адветисемент (контакты, title, web, price и т.д. указанные при редактировании )  
		hObj_TIY2 = objTIY.GetCustomfieldData(); // сюда сохраняем значение массива кастомфилдов, указанные при редактировании
		ValidateDataFromAdvertAfterEdit(mas_Advertisment, mas_TIY2, hObj_TIY, hObj_TIY2);
		
///////////////////////////////////////////////////////////////////////////////////////
		Sleep(10000);
		
		print("\r\nУдаляем поданные объявления");
		DeleteAdvert(sHost, sAuth_token, sIdAuto);
		DeleteAdvert(sHost, sAuth_token, sIdRealt);
		DeleteAdvert(sHost, sAuth_token, sIdTIU);
		
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
    	
	}
	// редактирование объявления автотест
	private InnerDataHM EditAdvert(String sHost, String sMas_Adv[], String sMas_Cust[], InnerDataHM obj_old,  String sAuth_token, String sUrlImage) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		
		InnerDataHM obj_after_edit;
		String sId = obj_old.GetID();
		
		print("Параметры для запроса");
		print("sAuth_token = "+ sAuth_token);
		print("sImageUrlAuto = " + sUrlImage);
		print("sVideo = " + Proper.GetProperty("video2"));
		print("Генерируем данные");
		
		String sVideo = "&advertisement[video]="+Proper.GetProperty("video2");
		
		
		//генерим advertisement 
		HM<String, String> hObj_Adv_New = new HM<String, String>(); 
		for(int i=0; i<sMas_Adv.length; i++)
		{
			hObj_Adv_New.SetValue(sMas_Adv[i], RamdomData.GetRandomData(Proper.GetProperty(sMas_Adv[i]), obj_old.GetAdvertismentData().GetValue(sMas_Adv[i])));
		}
		String sRequest1 = CreateArrayRequest("advertisement",  hObj_Adv_New.GetStringFromAllHashMap());

		// генерим advertisement [custom_fields]
		HM<String, String> hObj_Cust_New = new HM<String, String>(); 
		for(int i=0; i<sMas_Cust.length; i++)
		{
			hObj_Cust_New.SetValue(sMas_Cust[i], RamdomData.GetRandomData(Proper.GetProperty(sMas_Cust[i]), obj_old.GetCustomfieldData().GetValue(sMas_Cust[i])));
		}
		hObj_Cust_New.PrintKeyAndValue();
		String sRequest2 = CreateDoubleArrayRequest("advertisement", "custom_fields",  hObj_Cust_New.GetStringFromAllHashMap());
		
		builder = new URIBuilder();
		builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/"+ obj_old.GetID())
			.setQuery(sRequest1 + sRequest2 + sVideo + "&deleted_images[0]=" + sUrlImage).setParameter("auth_token", sAuth_token);
		uri = builder.build();
		if(uri.toString().indexOf("%25") != -1)
		{
			String sTempUri = uri.toString().replace("%25", "%");
			uri = new URI(sTempUri);			
		}
		print("Отправляем запрос. Uri Запроса: " + uri.toString());
		
    	String sResponse = HttpPutRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("\r\nОтвет сервера:\r\n" + jsonObject.toString(10) + "\r\n Объявление отредактировано");
    		obj_after_edit = new InnerDataHM(hObj_Adv_New, hObj_Cust_New, sId); // сохраняем значения данных после редактирования
    		return obj_after_edit;
    	}
    	else
    	{
    		print("Не удалось отредактировать объявление\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// подача объявления автотест ОП
 	private InnerDataHM PostAdvert(String sHost, String sMas_Adv[], String sMas_Cust[], String sAuth_token, String sCategoryData, String sImage) throws JSONException, URISyntaxException, IOException, ExceptFailTest
		{
			
			String sRequest, sRequest1, sRequest2, sRet;
			JSONObject jTemp;
			InnerDataHM obj;
			
			print("Параметры для запроса");
			print("sAuth_token = "+ sAuth_token);
			print("sCatRegAdv = "+ Proper.GetProperty(sCategoryData));
			print("sVideo = " + Proper.GetProperty("video"));
			print("Генерируем данные");
			
			String sVideo = "&advertisement[video]="+Proper.GetProperty("video");
			sRequest = CreateSimpleRequest(Proper.GetProperty(sCategoryData)); //category_auto
			
			//генерим advertisement 
			HM<String, String> hObj_Adv = new HM<String, String>(); //здесь будем хранить {param=value} для advertisement
			for(int i=0; i<sMas_Adv.length; i++)
			{
				hObj_Adv.SetValue(sMas_Adv[i], RamdomData.GetRandomData(Proper.GetProperty(sMas_Adv[i]), ""));
			}
			sRequest1 = CreateArrayRequest("advertisement",  hObj_Adv.GetStringFromAllHashMap());
			
			// генерим advertisement [custom_fields]
			HM<String, String> hObj_Cust = new HM<String, String>();  //здесь будем хранить {param=value} для advertisement [customfields]
			for(int i=0; i<sMas_Cust.length; i++)
			{
					hObj_Cust.SetValue(sMas_Cust[i], RamdomData.GetRandomData(Proper.GetProperty(sMas_Cust[i]), ""));
			}
			hObj_Cust.PrintKeyAndValue();
			sRequest2 = CreateDoubleArrayRequest("advertisement", "custom_fields",  hObj_Cust.GetStringFromAllHashMap());
			
			builder = new URIBuilder();
	    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert")
	    		.setQuery(sRequest+sRequest1+sRequest2+sVideo)
	    		.setParameter("auth_token", sAuth_token);
	    	uri = builder.build();
	    	if(uri.toString().indexOf("%25") != -1)
	    	{
	    		String sTempUri = uri.toString().replace("%25", "%");
	    		uri = new URI(sTempUri);			
	    	}
	    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
	    	String sResponse = HttpPostRequestImage(uri, Proper.GetProperty(sImage));
	    	print("Парсим ответ....");
	    	
	    	jsonObject = ParseResponse(sResponse);
	    	if(jsonObject.isNull("error"))
	    	{
	    		print("\r\nОтвет сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление создано");
	    		jTemp = jsonObject.getJSONObject("advertisement");
	    		sRet =  jTemp.getString("id");
	    		print("ID объявление = " + sRet);
	    		obj = new InnerDataHM(hObj_Adv, hObj_Cust, sRet); // сохраняем значения поданных данных и id созданой объявки 
	    		return obj;
	    	}
	    	else
	    	{
	    		print("Не удалось создать объявление\r\n"+
	    				"Ответ сервера:\r\n"+ jsonObject.toString());
	    		print("Тест провален".toUpperCase());
	    		throw new ExceptFailTest("Тест провален");
	    	}	
		}
 	// валидация  сравнение данных объявления что было, с тем что стало, после редактирования для автотестов
 	private void ValidateDataFromAdvertAfterEdit(String mas_Adv[], String mas_Cust[], HM<String, String> obj_Adv, HM<String, String> obj_Cust) throws JSONException, ExceptFailTest
 	{
 		JSONObject jTemp, jD, jD2, jD3;
		HM<String, String> objHM = new HM<String, String>();
		jTemp = jsonObject.getJSONObject("advertisement");  // используем общий jsonObject указанный для всего класса(при редактировании ответ приходит в него)
		jD = jTemp; // для проверки и сравнения данных
		for(int i=0; i<mas_Adv.length; i++)
		{
			if(mas_Adv[i].equals("price") || mas_Adv[i].equals("currency"))
				continue;
			
			if(mas_Adv[i].equals("phone") || mas_Adv[i].equals("phone_add") ||  mas_Adv[i].equals("contact") || mas_Adv[i].equals("phone2") ||  mas_Adv[i].equals("phone_add2") ||  mas_Adv[i].equals("alternative_contact"))
			{
    			// проверяем не изменился ли phone, phone_add, contact, phone2, phone_add2, alternative_contact
				if(!obj_Adv.GetValue(mas_Adv[i]).equals(jD.getString(mas_Adv[i])))
				{
					print("Значение " + mas_Adv[i] +" = " + jD.getString(mas_Adv[i])  + " указанное для при подаче объявления," +
							" осталось прежним после редактирования данного объявления, не равно значение указанному при редактировании " + mas_Adv[i] + 
							" = " + obj_Adv.GetValue(mas_Adv[i]) + " Корректно." );		
				}
				else
				{
					print("Значение " + mas_Adv[i] +" = " + jD.getString(mas_Adv[i])  + " указанное для при подаче объявления," +
							" не осталось прежним после редактирования данного объявления, равно значение указанному при редактировании " + mas_Adv[i] + 
							" = " + obj_Adv.GetValue(mas_Adv[i]));	
					print("Тест провален".toUpperCase());
		    		throw new ExceptFailTest("Тест провален");
				}
			}
			else
			{
				// проверяем что изменился title, text, web.
				if(obj_Adv.GetValue(mas_Adv[i]).equals(jD.getString(mas_Adv[i])))
				{
					print("Значение " + mas_Adv[i] +" = " + obj_Adv.GetValue(mas_Adv[i]) + " указанное для при редактировании объявления," +
							" изменилось после редактирования данного объявления, равно значение полученному в объявлени после редактирования " + mas_Adv[i] + 
							" = " + jD.getString(mas_Adv[i]) + " Корректно." );		
				}
				else
				{
					print("Значение " + mas_Adv[i] +" = " + obj_Adv.GetValue(mas_Adv[i]) + " указанное для при редактировании объявления," +
							" не изменилось после редактирования данного объявления, не равно значение полученному в объявлени после редактирования " + mas_Adv[i] + 
							" = " + jD.getString(mas_Adv[i]));		
					print("Тест провален".toUpperCase());
		    		throw new ExceptFailTest("Тест провален");
				}
				
			}
			
		}		
		// получаем название и значения кастомфилдов, найденных в объявлении  и  заливаем их в HashMap
		jTemp = jTemp.getJSONObject("group_custom_fields");
		JSONArray ar = jTemp.names(), ar2;
		for(int i=0; i<ar.length(); i++)
		{
			jD = jTemp.getJSONObject(ar.getString(i));
			if(!jD.getString("custom_fields").equals("[]"))
			{
				jD2 = jD.getJSONObject("custom_fields");
				ar2 = jD2.names();
				for(int j=0; j<ar2.length(); j++)
				{
					String key = ar2.getString(j);
					jD3 = jD2.getJSONObject(ar2.getString(j));
					String value = jD3.getString("value");
					objHM.SetValue(key, value);
				}
			}
				
		}
		// Сравниваем значения
		for(int i=0; i<mas_Cust.length; i++)
		{
			
			if(obj_Cust.GetValue(mas_Cust[i]).equals(objHM.GetValue(mas_Cust[i])))
			{
				print("Значение " + mas_Cust[i] +" = " + obj_Cust.GetValue(mas_Cust[i]) + " указанное для при редактировании объявления," +
						" совпало со значение после получения данного объявления " + mas_Cust[i] + " = " + objHM.GetValue(mas_Cust[i]));		
			}
			else
			{
				if( (obj_Cust.GetValue(mas_Cust[i]).equals("0")) && (objHM.GetValue(mas_Cust[i])==null) )
				{
					print("Значение " + mas_Cust[i] +" = " + obj_Cust.GetValue(mas_Cust[i]) + " указанное при редактировании, не найдено в" +
							" объявление так как является булевским и при значении = 0, в объявление удаляется. Корректно.");
				}
				else
				{
					print("Значение " + mas_Cust[i] +" = " + obj_Cust.GetValue(mas_Cust[i]) + " указанное для при редактировании объявления," +
							" не совпало со значение после получения данного объявления " + mas_Cust[i] + " = " + objHM.GetValue(mas_Cust[i]));	
					print("Тест провален".toUpperCase());
		    		throw new ExceptFailTest("Тест провален");
				}
			}
			
		}	
		// Проверяем наличие видео
		
	 	print("\r\nПроверяем наличие видео");
		String sVideo = Proper.GetProperty("video2");
		sVideo = sVideo.replaceAll("watch", "embed/").replaceFirst("\\?v=", "");
		jTemp = jsonObject.getJSONObject("advertisement"); 
		if(jTemp.getString("video").indexOf(sVideo)!=-1)
		{
			print("Объявление содержит ссылку на видео. Корректно");
			print(sVideo);
		}
		else
		{
			if(jTemp.getString("video").equals(""))
			{	
				print("В объявление не было добавлено новое видео, после редактирования. Тест провален".toUpperCase());
				print("video: " + jTemp.getString("video"));
				throw new ExceptFailTest("Тест провален");
			}
			else
			{
				print("В объявление сталось старое видео, после редактирования. Тест провален".toUpperCase());
				print("video: " + jTemp.getString("video"));
				throw new ExceptFailTest("Тест провален");
			}
		}
		
		// Проверяем что картинка удалилась
		print("\r\nПроверяем наличие изображений");
		String s = jTemp.getString("images");
		if(s.equals("[]"))
		{
			print("В объявление было удалено изображение. Корректно".toUpperCase());
			print("images:" + jTemp.getString("images"));
			
		}
		else
		{
			print("В объявление не было удалено изображение.".toUpperCase());
			ar = (JSONArray) jTemp.get("images");
			for(int i=0; i<ar.length(); i++)
			{
				jTemp = (JSONObject) ar.get(i);
				print(jTemp.toString(10));
			}
			throw new ExceptFailTest("Тест провален");
		}	
 	}
 	// валидация данных сравнение что подавалось с тем что было получено после подачи для автотестов
 	private String ValidateDataFromAdvertAfterPost(String mas_Adv[], String mas_Cust[], HM<String, String> obj_Adv, HM<String, String> obj_Cust, JSONObject jObj) throws JSONException, ExceptFailTest
	{
		JSONObject jTemp, jD, jD2, jD3;
		HM<String, String> objHM = new HM<String, String>();
		jTemp = jObj.getJSONObject("advertisement"); 
		jD = jTemp; // для проверки и сравнения данных
		for(int i=0; i<mas_Adv.length; i++)
		{
			if(mas_Adv[i].equals("price") || mas_Adv[i].equals("currency"))
				continue;
			else
			{
				if(obj_Adv.GetValue(mas_Adv[i]).equals(jD.getString(mas_Adv[i])))
				{
					print("Значение " + mas_Adv[i] +" = " + obj_Adv.GetValue(mas_Adv[i]) + " указанное для при подаче объявления," +
							" совпало со значение после получения данного объявления " + mas_Adv[i] + " = " + jD.getString(mas_Adv[i]));		
				}
				else
				{
					print("Значение " + mas_Adv[i] +" = " + obj_Adv.GetValue(mas_Adv[i]) + " указанное для при подаче объявления," +
							" не совпало со значение после получения данного объявления " + mas_Adv[i] + " = " + jD.getString(mas_Adv[i]));	
					print("Тест провален".toUpperCase());
		    		throw new ExceptFailTest("Тест провален");
				}
			}
		}
		// получаем название и значения кастомфилдов, найденных в объявлении  и  заливаем их в HashMap
		jTemp = jTemp.getJSONObject("group_custom_fields");
		JSONArray ar = jTemp.names(), ar2;
		for(int i=0; i<ar.length(); i++)
		{
			jD = jTemp.getJSONObject(ar.getString(i));
			if(!jD.getString("custom_fields").equals("[]"))
			{
				jD2 = jD.getJSONObject("custom_fields");
				ar2 = jD2.names();
				for(int j=0; j<ar2.length(); j++)
				{
					String key = ar2.getString(j);
					jD3 = jD2.getJSONObject(ar2.getString(j));
					String value = jD3.getString("value");
					objHM.SetValue(key, value);
				}
			}
				
		}
		// Сравниваем значения
		for(int i=0; i<mas_Cust.length; i++)
		{
			
			if(obj_Cust.GetValue(mas_Cust[i]).equals(objHM.GetValue(mas_Cust[i])))
			{
				print("Значение " + mas_Cust[i] +" = " + obj_Cust.GetValue(mas_Cust[i]) + " указанное для при подаче объявления," +
						" совпало со значение после получения данного объявления " + mas_Cust[i] + " = " + objHM.GetValue(mas_Cust[i]));		
			}
			else
			{
				if( (obj_Cust.GetValue(mas_Cust[i]).equals("0")) && (objHM.GetValue(mas_Cust[i])==null) )
				{
					print("Значение " + mas_Cust[i] +" = " + obj_Cust.GetValue(mas_Cust[i]) + " указанное при подаче, не найдено в" +
							" объявление так как является булевским и при значении = 0, в объявление не добавляется. Корректно.");
				}
				else
				{
					print("Значение " + mas_Cust[i] +" = " + obj_Cust.GetValue(mas_Cust[i]) + " указанное для при подаче объявления," +
							" не совпало со значение после получения данного объявления " + mas_Cust[i] + " = " + objHM.GetValue(mas_Cust[i]));	
					print("Тест провален".toUpperCase());
		    		throw new ExceptFailTest("Тест провален");
				}
			}
			
		}
		// Проверяем наличие видео
		print("Проверяем наличие видео");
		String sVideo = Proper.GetProperty("video");
		sVideo = sVideo.replaceAll("watch", "embed/").replaceFirst("\\?v=", "");
		jTemp = jObj.getJSONObject("advertisement"); 
		if(jTemp.getString("video").indexOf(sVideo)!=-1)
		{
			print("Объявление содержит ссылку на видео. Корректно");
			print(sVideo);
		}
		else
		{
			print("В объявление не было добавлено видео. Тест провален".toUpperCase());
			print("video: " + jTemp.getString("video"));
    		throw new ExceptFailTest("Тест провален");
		}
		// Проверяем наличие картинок
		print("Проверяем наличие изображений");
		String s = jTemp.getString("images");
		if(s.equals("[]"))
		{
			print("В объявление не было добавлено изображение. Тест провален".toUpperCase());
			print("images:" + jTemp.getString("images"));
			throw new ExceptFailTest("Тест провален");
		}
		else
		{
			print("Объявление содерижит изображения. Корректно");
			ar = (JSONArray) jTemp.get("images");
			for(int i=0; i<ar.length(); i++)
			{
				jTemp = (JSONObject) ar.get(i);
				print(jTemp.toString(10));
			}
			return jTemp.getString("orig");
		}
		
	}
	// получения объявления для автотестов
	private JSONObject GetAdvert(String sHost, String sIdAdvert, String sText) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		print("\r\nПолучение объявления".toUpperCase()+" рубрики " + sText + " ID = " + sIdAdvert);
		print("Параметры запроса");
		print("ID = " + sIdAdvert);
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
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление получено");
    		return jsonObject;
    	}
    	else
    	{
    		print("Объявление не получено\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}
		
	}

	
	public void AddActivateDeactivateProlongPushUpHighLightPremiumIP(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String sLogin = Proper.GetProperty("login_authIP");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		JSONObject jData;
		String sIdAdvert;
		int nCountAdvert, nCountAdvert_after;
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Попытка подачи, редактирования, активации, деактивации, выделения, продления, поднятия, назначения премиум ИП - Тест".toUpperCase()+"\r\n");
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
		
		print("\r\nШаг 1".toUpperCase());
		print("Запоминаем количество объявлений в ЛК ИП до подачи объявления".toUpperCase());
		print("Получаем листинг объявлений в ЛК (Данному пользователю для проведения теста поданно одно объявления с фронта сайта)");
		jData = GetListOwnAdvert(sHost, sAuth_token);
		print("Получаем количество объявлени в листинге ЛК до подачи");
		nCountAdvert = GetCountAdvertListLK(jData);
		print("У пользователя " + sLogin +" в ЛК " + nCountAdvert + " объявление(ий)");
		
		
		print("\r\nШаг 2".toUpperCase());
		print("Попытка подачи объявления в рубрику Авто с пробегом ИП".toUpperCase());
		print("Подаем объявление");
		PostAdvertIP(sHost, mas_Advertisment, mas_Auto2, sAuth_token, "category_auto", "image");
		
		print("\r\nПроверяем количество объявлений в ЛК ИП после попытки подачи");
		print("\r\nПолучаем листинг объявлений в ЛК (Данному пользователю для проведения теста поданно одно объявления с фронта сайта)".toUpperCase());
		jData = GetListOwnAdvert(sHost, sAuth_token);
		print("Получаем количество объявлений в листинге ЛК до подачи");
		nCountAdvert_after = GetCountAdvertListLK(jData);
		if(nCountAdvert == nCountAdvert_after)
			print("У пользователя " + sLogin +" после попытки подачи объявления в ЛК " + nCountAdvert_after + " объявление(ий). Корректно. Осталось столько же как и до попытки подачи");
		
		print("\r\nШаг 3".toUpperCase());
		print("Получаем ID первого объявления в листинге ЛК ИП");
		JSONArray ar = jData.getJSONArray("advertisements");
		jData = (JSONObject) ar.get(0);
		sIdAdvert = jData.getString("id");
		print("ID объявления для которого будут производится попытки манипуляций = " + sIdAdvert);

	
	}	
	// подача ИП для автотестов
	private void PostAdvertIP(String sHost, String sMas_Adv[], String sMas_Cust[], String sAuth_token, String sCategoryData, String sImage) throws JSONException, URISyntaxException, IOException, ExceptFailTest
	{
		
		String sRequest, sRequest1, sRequest2;
		
		print("Параметры для запроса");
		print("sAuth_token = "+ sAuth_token);
		print("sCatRegAdv = "+ Proper.GetProperty(sCategoryData));
		print("sVideo = " + Proper.GetProperty("video"));
		print("Генерируем данные");
		
		String sVideo = "&advertisement[video]="+Proper.GetProperty("video");
		sRequest = CreateSimpleRequest(Proper.GetProperty(sCategoryData)); //category_auto
		
		//генерим advertisement 
		HM<String, String> hObj_Adv = new HM<String, String>(); //здесь будем хранить {param=value} для advertisement
		for(int i=0; i<sMas_Adv.length; i++)
		{
			hObj_Adv.SetValue(sMas_Adv[i], RamdomData.GetRandomData(Proper.GetProperty(sMas_Adv[i]), ""));
		}
		sRequest1 = CreateArrayRequest("advertisement",  hObj_Adv.GetStringFromAllHashMap());
		
		// генерим advertisement [custom_fields]
		HM<String, String> hObj_Cust = new HM<String, String>();  //здесь будем хранить {param=value} для advertisement [customfields]
		for(int i=0; i<sMas_Cust.length; i++)
		{
				hObj_Cust.SetValue(sMas_Cust[i], RamdomData.GetRandomData(Proper.GetProperty(sMas_Cust[i]), ""));
		}
		hObj_Cust.PrintKeyAndValue();
		sRequest2 = CreateDoubleArrayRequest("advertisement", "custom_fields",  hObj_Cust.GetStringFromAllHashMap());
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert")
    		.setQuery(sRequest+sRequest1+sRequest2+sVideo)
    		.setParameter("auth_token", sAuth_token);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequestImage(uri, Proper.GetProperty(sImage));
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("\r\nОтвет сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление создано. Но ИП партнер не имеет право подавать объявления.");
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");	
    	}
    	else
    	{
    		print("Не удалось создать объявление. Корректно. ИП не имеет право создавать объявления\r\n"+
    				"Ответ сервера:\r\n" + jsonObject.toString() + "\r\n");
    	}	
	}
	// подсчет объявлений в листинге ЛК для автотеста
	private int GetCountAdvertListLK(JSONObject jObj) throws JSONException, ExceptFailTest
	{
		if(jObj.getString("advertisements").equals("[]"))
		{
			print("Листинг объявлений получен, но в листинге нету ни одного объявления");
			print("Тест провален. Для работы данного теста необходимо подать объявление в рубрику Авто с пробегом. Пользователю api2@yopmail.com ".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
		}
		
		JSONArray ar = jObj.getJSONArray("advertisements");
		
		return ar.length(); //возвращаем количество объявления в листинге 
	}	
	
	
	// Подача/Получение листинга ЛК/Удаление
	public void AddGetListDeleteOP(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String sIdAdvert; 
		String sLogin = Proper.GetProperty("login_authOP");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		JSONObject jData;
		
		InnerDataHM objRealt;
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Подача,получение листинга ЛК, удаление объявления ОП - Тест".toUpperCase()+"\r\n");
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
		
		print("\r\nПодача объявления в рубрику Недвижимость - Вторичный рынок".toUpperCase());
    	objRealt = PostAdvert(sHost, mas_Advertisment, mas_Realt2, sAuth_token, "category_realt", "image2");
    	sIdAdvert = objRealt.GetID();
    	
    	print("\r\nПолучаем листинг объявлений в ЛК");
    	jData = GetListOwnAdvert(sHost, sAuth_token);
    	
    	print("\r\nИщем поданное объявление в листинге ЛК");
    	print("ID искомого объявления = " + sIdAdvert);
    	FindAdvertFromListAfterPost(jData, sIdAdvert);
    	
    	print("\r\nУдаляем объявление в ЛК");
    	print("ID удаляемого объявления = " + sIdAdvert);
    	DeleteAdvert(sHost, sAuth_token, sIdAdvert);
    	
    	print("\r\nПолучаем листинг объявлений в ЛК");
    	jData = GetListOwnAdvert(sHost, sAuth_token);
    	
    	print("\r\nИщем удаленное из ЛК объявление");
    	FindAdvertFromListAfterDelete(jData, sIdAdvert);
    	
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());	
	}
	//удаление объявления для автотестов
	private void DeleteAdvert(String sHost, String sAuth_token, String sIdAdvert) throws URISyntaxException, ExceptFailTest, IOException, JSONException
	{
		print("\r\nУдаление объявления".toUpperCase());
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("sIdAdvert = " + sIdAdvert);
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
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление было удалено");
    	else
    	{
    		print("Не удалось удалить объявление c ID = "+ sIdAdvert +"\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	//поиск объявления по id в листингах  после удаления объявления short advertisment для автотестов
	private void FindAdvertFromListAfterDelete(JSONObject jObj, String sIdAdvert) throws JSONException, ExceptFailTest
	{
		JSONObject jTemp = jObj, jData;
		
		if(jObj.getString("advertisements").equals("[]"))
		{
			print("Листинг объявлений получен, но в листинге нету ни одного объявления");
			print("Объявление с ID = ".toUpperCase() + sIdAdvert +" удалено/деактивировано".toUpperCase());
	    	return;
		}
		
		
		JSONArray ar = jTemp.getJSONArray("advertisements");
		for(int i=0; i<ar.length(); i++)
		{
		
			jData = (JSONObject) ar.get(i);
			if(jData.getString("id").equals(sIdAdvert))
			{
				print("Объявление с ID = " + sIdAdvert + " найдено в листинге. После удаления/деактивации");	
				print("Тест провален".toUpperCase());
	    		throw new ExceptFailTest("Тест провален");
			}
		}
		
		print("После удаления/деактивации, объявление с ID = " + sIdAdvert + " не отображается(не найдено) в листинге. Корректно");
	}
	//поиск объявления по id в листингах  после добавления объявления short advertisment для автотестов
	private int FindAdvertFromListAfterPost(JSONObject jObj, String sIdAdvert) throws JSONException, ExceptFailTest
	{
		JSONObject jTemp = jObj, jData;
		boolean bFlag = false;
		int k = -1;
		
		if(jObj.getString("advertisements").equals("[]"))
		{
			print("Листинг объявлений получен, но в листинге нету неодного объявления");
			print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
		}
		
		JSONArray ar = jTemp.getJSONArray("advertisements");
		for(int i=0; i<ar.length(); i++)
		{
		
			jData = (JSONObject) ar.get(i);
			if(jData.getString("id").equals(sIdAdvert))
			{
				print("Объявление с ID = " + sIdAdvert + " найдено в листинге. Корректно");	
				bFlag = true;
				k = i;
			}
		}
		
		if(!bFlag)
		{
			print("После подачи/добавления в избранное, объявление с ID = " + sIdAdvert + " не отображается в листинге");
			print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
		}
		return (k+1); //возвращаем порядковый номер объявления в листинге 
	}	
	// получение листинга ЛК ОП для автотестов
	private JSONObject GetListOwnAdvert(String sHost, String sAuth_token) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{	
		String sDataForSearchOwnAdvert = "{offset=0, limit=25}";
		JSONObject jTemp;
		
		print("Получение листинга «своих» объявлений".toUpperCase());
		print("Параметры для запроса");
		print("DataForSearchOwnAdvert = " + sDataForSearchOwnAdvert);
		print("sAuth_token = " + sAuth_token);
		
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
    	jTemp = jsonObject;
    	
    	if(jsonObject.isNull("error"))
    	{
    		if(jTemp.getString("advertisements").equals("[]"))
    		{
    			return jTemp;
    		}
    		else
    		{
    			print("Листинг «своих» объявлений получен");
	    		JSONArray ar = jsonObject.getJSONArray("advertisements");
	    		for(int i=0; i<ar.length(); i++)
	    		{
	    			print("--------------------------------------------------------------------------------------------------------------");
	    			print("Объявление №" + i);
	    			jsonObject = (JSONObject) ar.get(i);
	    			print(jsonObject.toString(10));
	    		
	    		}
	    		return jTemp;
    		}
    	}
    	else
    	{
    		print("Не удалось получить листинг «своих» объявлений \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	
	// Подача объявлени(польз 1)/Добавление в избраное(П2)/Получение листинга избран(П2)/Удаление из избранного(П2)
	//Получение листинга из избранного(П2)/Подача(П1)/Попытка добавить в избранное()
	public void AddFavGetListFavDeleteFavOP(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String sIdAdvert; 
		String sLogin = Proper.GetProperty("login_authOP");
		String sLogin2 =  Proper.GetProperty("login_authOP2");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		JSONObject jData;
		InnerDataHM objRealt;
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Добавление в избранное , получение листинга избранного, удаление из избранного ОП - Тест".toUpperCase()+"\r\n");
		print("Авторизация пользователем - " + sLogin2);
		sAuth_token = Authorization_1_1(sHost, sLogin2, sPassword);
		
		print("\r\nПодача объявления в рубрику Авто с пробегом".toUpperCase());
		objRealt = PostAdvert(sHost, mas_Advertisment, mas_Auto2, sAuth_token, "category_auto", "image");
		sIdAdvert = objRealt.GetID();  // сюда сохраняем значение id
		
		print("\r\nАвторизация пользователем - " + sLogin);
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
		
		print("\r\nДобавляем объявление с ID = " + sIdAdvert + " в вкладку «Избранное» для пользователя " + sLogin);
		AddAdvertToFavourite(sHost, sAuth_token, sIdAdvert);
		
		print("\r\nПолучаем листинг вкладки «Избранное» для пользователя " + sLogin);
		jData = GetListFavourite(sHost, sAuth_token);
		
		print("\r\nИщем объявление с ID = " + sIdAdvert + " в листинге «Избранное» для пользоватея " + sLogin);
		FindAdvertFromListAfterPost(jData, sIdAdvert);
		
		print("\r\nУдаляем объявление c ID = " + sIdAdvert + " из вкладки «Избранное» для пользователя" + sLogin);
		DeleteAdvertFromFavourite(sHost, sAuth_token, sIdAdvert);
		
		print("\r\nПолучаем листинг вкладки «Избранное» для пользователя " + sLogin);
		jData = GetListFavourite(sHost, sAuth_token);
		
		print("\r\nИщем объявление с ID = " + sIdAdvert + " в листинге «Избранное» для пользоватея " + sLogin);
		FindAdvertFromListAfterDelete(jData, sIdAdvert);
		
		print("\r\nПопытка добавить собственное объявление в избранное для пользователя "+ sLogin2);
		
		sAuth_token = Authorization_1_1(sHost, sLogin2, sPassword);
		print("Авторизация пользователем - " + sLogin2);
		print("\r\nДобавляем объявление с ID = " + sIdAdvert + " в вкладку «Избранное» для пользователя " + sLogin2);
		AddOwnAdvertToFavourite(sHost, sAuth_token, sIdAdvert);
		
		print("\r\nУдаляем поданное объявление");
		DeleteAdvert(sHost, sAuth_token, sIdAdvert);
		
		print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
	}	
	//добавление в избранное для автотеста
	private void AddAdvertToFavourite(String sHost, String sAuth_token, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		print("\r\nДобавление объявления в «Избранное»".toUpperCase());
		print("Параметры для запроса");
		print("auth_token = " + sAuth_token);
		print("sIdAdvert = "+ sIdAdvert);
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
    		print("Ответ сервера:" + jsonObject.toString(10) + " Объявление c ID = " + sIdAdvert + " добавлено в избранное");
    	else
    	{
    		print("Не удалось добавить объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	//получение листинга вкладки избранное для автотеста
	private JSONObject GetListFavourite(String sHost, String sAuth_token) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		String sDataForFavourite =  "{offset=0, limit=25}";
		JSONObject jTemp;
		print("\r\nПолучение листинга объявлений, добавленных в «Избранное»".toUpperCase());
		print("Параметры для запроса");
		print("DataForFavourite = "+ sDataForFavourite);
		
		String sQuery = CreateSimpleRequest(sDataForFavourite);
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
    	jTemp = jsonObject;
    	
    	if(jsonObject.isNull("error"))
    	{
    		if(jTemp.getString("advertisements").equals("[]"))
    		{
    			return jTemp;
    		}
    		else
    		{
    			print("Листинг объявлений вкладки «Избранное» получен");
	    		JSONArray ar = jsonObject.getJSONArray("advertisements");
	    		for(int i=0; i<ar.length(); i++)
	    		{
	    			print("--------------------------------------------------------------------------------------------------------------");
	    			print("Объявление №" + i);
	    			jsonObject = (JSONObject) ar.get(i);
	    			print(jsonObject.toString(10));
	    		
	    		}
	    		return jTemp;
    		}
    	}
    	else
    	{
    		print("Не удалось получить листинг объявлений вкладки «Избранное» \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	//удаление из избранного для автотеста
	private void DeleteAdvertFromFavourite(String sHost, String sAuth_token, String sIdAdvert) throws URISyntaxException, ExceptFailTest, IOException, JSONException
	{
		print("\r\nУдаление объявления из «Избранное»".toUpperCase());
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
    		print("Ответ сервера:" + jsonObject.toString(10) + " Объявление c ID = " + sIdAdvert + " удалено из избранного");
    	else
    	{
    		print("Не удалось удалить объявление из избранного \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	//добавление своего объявления в избранное
	private void AddOwnAdvertToFavourite(String sHost, String sAuth_token, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		print("\r\nДобавление объявления в «Избранное»".toUpperCase());
		print("Параметры для запроса");
		print("auth_token = " + sAuth_token);
		print("sIdAdvert = "+ sIdAdvert);
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
    	{
    		print("Ответ сервера:" + jsonObject.toString(10) + " Объявление c ID = " + sIdAdvert + " добавлено в избранное");
    		print("Но это собственное объявление пользователя. Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}
    	else
    	{
    		print("Не удалось добавить объявление \r\n Ответ сервера:\r\n" + jsonObject.toString(10) + "Корректно. Так как это собственное объявление пользователя");
    	}	
	}
	
	
	//Подача в бесплатную/деактивация/активация/Продление/Поднятие/Выделение/Назначение премиум/Получение листинга категории и проверка его
	public void AddDeactivateActivateProlongPushupHighlightPremiumOPFreeAdvert(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, InterruptedException
	{
		String sIdAdvert, sIdAdvert2; 
		String sLogin = Proper.GetProperty("login_authOP");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		JSONObject jData, jData2, jDataPostAsvert;
		InnerDataHM objRealt;
		String sDataForList = "{category=real-estate/apartments-sale/secondary/, region=russia/arhangelskaya-obl/arhangelsk-gorod/, currency=RUR, offset=0, limit=45, sort_by=date_sort:desc, include_privates=true, include_companies=true}";
		int nNumberList, nNumberList2;
		
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Подача , деактивация, активация, продление, поднятие, выделение, премиум  ОП(бесплатное объявление) - Тест".toUpperCase()+"\r\n");
		// авторизация
		print("\r\nАвторизация пользователем - " + sLogin);
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
		
		// подача двух объявлений
		print("\r\nШАГ 1");
		print("Подача двух объявлений в бесплатную рубрику. Недвижимость - Вторичный рынок".toUpperCase());
		print("\r\nПодача объявления в рубрику Недвижимость - Вторичный рынок");
		print("Объявление №1");
    	objRealt = PostAdvert(sHost, mas_Advertisment, mas_Realt2, sAuth_token, "category_realt", "image2");
    	sIdAdvert = objRealt.GetID();
   	
    	print("\r\nПодача объявления в рубрику Недвижимость - Вторичный рынок");
		print("Объявление №2");
    	objRealt = PostAdvert(sHost, mas_Advertisment, mas_Realt2, sAuth_token, "category_realt", "image4");
    	sIdAdvert2 = objRealt.GetID();
    	
    	print("\r\nОжидаем индексации, время ожидания ".toUpperCase() + Integer.parseInt(Proper.GetProperty("timeWait"))/(1000*60) + " минут(ы)".toUpperCase());
    	Sleep(Integer.parseInt(Proper.GetProperty("timeWait")));
    	
    	// проверка их появления в листинге и коректного расположения 
    	print("\r\nШАГ 2");
    	print("Проверяем появление объявлений в листинге и их корректное расположение".toUpperCase());
    	print("\r\nПолучаем листинг категории объявлений рубрики Недвижимость - Вторичный рынок");
    	jData = GetListCategory(sHost, sDataForList);
    	
    	print("\r\nИщем поданные объявления в листинге и запоминаем их порядковые номера");
    	nNumberList = FindAdvertFromListAfterPost(jData, sIdAdvert);
    	print("Объявление номер 1(поданное первым) распологается в листинге на " + nNumberList + " месте");
    	nNumberList2 = FindAdvertFromListAfterPost(jData, sIdAdvert2);
    	print("Объявление номер 2(поданное вторым) распологается в листинге на " + nNumberList2 + " месте");
    	
    	print("Сравниваем расположения первого подданного объявления с ID = " + sIdAdvert + " со вторым поданным объявлением с ID = " + sIdAdvert2);
    	ValidetePlaceAdvert(nNumberList2, sIdAdvert2, nNumberList, sIdAdvert);
    	
    	// проверка продления объявления
    	print("\r\nШАГ 3");
    	print("Проверка продления объявления".toUpperCase());
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Запоминаем время окончания размещения объявления");
    	jDataPostAsvert = GetAdvert(sHost, sIdAdvert,  "Недвижимость - Вторичный рынок" );// запоминаем json объект в нем время окончания размещения сраз после подачи
    	
    	print("\r\nПродлеваем объявление с ID = " + sIdAdvert +  " для пользователя " + sLogin);
    	print("Объявление подано в бесплатую рубрику продлеваем без отправки App_Token");
    	ProlongAdvert(sHost, sAuth_token, sIdAdvert, false, 1);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Проверяем значение времени окончания размещения объявления после продления");
    	jData2 = GetAdvert(sHost, sIdAdvert,  "Недвижимость - Вторичный рынок" );
    	print("Сравниваем время окончания размещения объявления до и после продления");
    	ValidateDateFinishAdvert(jDataPostAsvert, jData2, 1);   	
    		
    	// проверка деактиивации объявления
    	print("\r\nШАГ 4");
    	print("Проверка деактивации объявления".toUpperCase());
    	print("\r\nДеактивируем объявление с ID = " + sIdAdvert +  " для пользователя " + sLogin);
    	DeactivateAdvert(sHost, sAuth_token, sIdAdvert);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Проверяем значение статуса объявления");
    	jData = GetAdvert(sHost, sIdAdvert,  "Недвижимость - Вторичный рынок" );
    	
    	print("\r\nПроверяем статус объявление с ID = " + sIdAdvert + " после деактивации объявления");
    	ValidateStatus("2", jData, sIdAdvert, " после деактивации объявления");
    	
    	print("\r\nОжидаем индексации, время ожидания ".toUpperCase() + Integer.parseInt(Proper.GetProperty("timeWait"))/(1000*60) + " минут(ы)".toUpperCase());
    	Sleep(Integer.parseInt(Proper.GetProperty("timeWait")));
    	
    	print("\r\nИщем деактивированное объявление в листинге категории");
    	print("\r\nПолучаем листинг категории объявлений рубрики Недвижимость - Вторичный рынок");
    	jData = GetListCategory(sHost, sDataForList);
    	FindAdvertFromListAfterDelete(jData, sIdAdvert);
    	
    	// проверка активации объявления
    	print("\r\nШАГ 5");
    	print("Проверка активации объявления".toUpperCase());
    	print("\r\nАктивируем объявление с ID = " + sIdAdvert +  " для пользователя " + sLogin);
    	print("Объявление подано в бесплатую рубрику активируем без отправки App_Token");
    	ActivateAdvert(sHost, sAuth_token, sIdAdvert, false, 1);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Проверяем значение статуса объявления");
    	jData = GetAdvert(sHost, sIdAdvert,  "Недвижимость - Вторичный рынок" );
    	
    	print("\r\nПроверяем статус объявление с ID = " + sIdAdvert + " после активации объявления");
    	ValidateStatus("1", jData, sIdAdvert, " после активации объявления");
    	
    	print("\r\nОжидаем индексации, время ожидания ".toUpperCase() + Integer.parseInt(Proper.GetProperty("timeWait"))/(1000*60) + " минут(ы)".toUpperCase());
    	Sleep(Integer.parseInt(Proper.GetProperty("timeWait")));
    	
    	print("\r\nИщем активированное объявление в листинге категории");
    	print("\r\nПолучаем листинг категории объявлений рубрики Недвижимость - Вторичный рынок");
    	jData = GetListCategory(sHost, sDataForList);
    	FindAdvertFromListAfterPost(jData, sIdAdvert);

    	// проверка попытки поднять объявление без оплаты
    	print("\r\nШАГ 6");
    	print("Проверка попытки поднять объявление без оплаты".toUpperCase());
    	print("\r\nПытаемся поднять  объявление с ID = " + sIdAdvert +  " для пользователя " + sLogin + " без передачи ключа оплаты");
    	PushUpAdvert(sHost, sAuth_token, sIdAdvert, false, 2);
    	
    	print("\r\nОжидаем индексации, время ожидания ".toUpperCase() + Integer.parseInt(Proper.GetProperty("timeWait"))/(1000*60) + " минут(ы)".toUpperCase());
    	Sleep(Integer.parseInt(Proper.GetProperty("timeWait")));
    	
    	print("\r\nПроверяем что объявление с ID = " + sIdAdvert + " не было поднято");
    	print("\r\nПолучаем листинг категории объявлений рубрики Недвижимость - Вторичный рынок");
    	jData = GetListCategory(sHost, sDataForList);
    	
    	print("\r\nИщем поданные объявления в листинге и запоминаем их порядковые номера");
    	nNumberList = FindAdvertFromListAfterPost(jData, sIdAdvert);
    	print("Объявление номер 1(которое мы пытались поднять без оплаты) распологается в листинге на " + nNumberList + " месте");
    	nNumberList2 = FindAdvertFromListAfterPost(jData, sIdAdvert2);
    	print("Объявление номер 2(которое распологалось в листинге выше, чем то которое мы пытались поднять) распологается в листинге на " + nNumberList2 + " месте");
    	
    	print("Сравниваем расположения объявления которое мы пытались поднять без оплаты  ID = " + sIdAdvert + " с объявлением с ID = " + sIdAdvert2 +
    			" которое распологалось в листинге выше, чем то которое мы пытались поднять");
    	ValidetePlaceAdvert(nNumberList2, sIdAdvert2, nNumberList, sIdAdvert);
    	
    	//   проверка поднятия объявления
    	print("\r\nШАГ 7");
    	print("Проверка поднятия объявления".toUpperCase());
    	print("\r\nПодымаем  объявление с ID = " + sIdAdvert +  " для пользователя " + sLogin + " передаем ключ оплаты");
    	PushUpAdvert(sHost, sAuth_token, sIdAdvert, true, 1);
    	
    	print("\r\nОжидаем индексации, время ожидания ".toUpperCase() + Integer.parseInt(Proper.GetProperty("timeWait"))/(1000*60) + " минут(ы)".toUpperCase());
    	Sleep(Integer.parseInt(Proper.GetProperty("timeWait")));
    	
    	print("\r\nПроверяем что объявление с ID = " + sIdAdvert + " поднято");
    	print("\r\nПолучаем листинг категории объявлений рубрики Недвижимость - Вторичный рынок");
    	jData = GetListCategory(sHost, sDataForList);
    	
    	print("\r\nИщем поданные объявления в листинге и запоминаем их порядковые номера");
    	nNumberList = FindAdvertFromListAfterPost(jData, sIdAdvert);
    	print("Объявление номер 1 распологается в листинге на " + nNumberList + " месте");
    	nNumberList2 = FindAdvertFromListAfterPost(jData, sIdAdvert2);
    	print("Объявление номер 2 распологается в листинге на " + nNumberList2 + " месте");
    	print("Сравниваем расположения поднятого объявления с ID = " + sIdAdvert + " с объявлением с ID = " + sIdAdvert2 +
    			" которое распологалось до поднятия выше поднятого ");
    	ValidetePlaceAdvert(nNumberList, sIdAdvert, nNumberList2, sIdAdvert2);
    	
    	// попытка выделения объявления без оплаты
    	print("\r\nШАГ 8");
    	print("Проверка попытки выделить объявление без оплаты".toUpperCase());
    	print("\r\nПытаемся выделить объявление с ID = " + sIdAdvert +  " для пользователя " + sLogin + " без передачи ключа оплаты");
    	HighLightAdvert(sHost, sAuth_token, sIdAdvert, false, 2);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Проверяем значение статуса выделения объявления");
    	jData = GetAdvert(sHost, sIdAdvert,  "Недвижимость - Вторичный рынок" );
    	
    	print("\r\nПроверяем статус выделения объявление с ID = " + sIdAdvert + " после попытки выделить объявления без передачи ключа оплаты");
    	ValidateHighLight("0", jData, sIdAdvert, " после попытки выделить объявления без передачи ключа оплаты");
    	
    	// выделение объявления
    	print("\r\nШАГ 9");
    	print("Проверка выделения объявление".toUpperCase());
    	print("\r\nВыделяем объявление с ID = " + sIdAdvert +  " для пользоватея " + sLogin + " передаем ключ оплаты");
    	HighLightAdvert(sHost, sAuth_token, sIdAdvert, true, 1);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Проверяем значение статуса выделения объявления");
    	jData = GetAdvert(sHost, sIdAdvert,  "Недвижимость - Вторичный рынок" );
    	
    	print("\r\nПроверяем статус выделения для объявления с ID = " + sIdAdvert + " после выделения объявления");
    	ValidateHighLight("1", jData, sIdAdvert, " после выделения объявления");
    	
    	//попытка назначения премиум объявления без оплаты
    	print("\r\nШАГ 10");
    	print("Проверка попытки назначить премиум объявлению без оплаты".toUpperCase());
    	print("\r\nПытаемся назначить премиум объявлению с ID = " + sIdAdvert2 +  " для пользователя " + sLogin + " без передачи ключа оплаты");
    	SetPremiumAdvert(sHost, sAuth_token, sIdAdvert2, false, 2);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert2 + " Проверяем значение статуса премиум объявления");
    	jData = GetAdvert(sHost, sIdAdvert2,  "Недвижимость - Вторичный рынок" );
    	
    	print("\r\nПроверяем статус премиум для объявления с ID = " + sIdAdvert2 + " после попытки назначить премиум объявлению без передачи ключа оплаты");
    	ValidatePremiun("false", jData, sIdAdvert2, " после попытки назначить премиум объявлению без передачи ключа оплаты");
    	
    	// назначение премиума
    	print("\r\nШАГ 11");
    	print("Проверка назначения премиум объявлению".toUpperCase());
    	print("\r\nНазначаем премиум объявлению с ID = " + sIdAdvert2 +  " для пользователя " + sLogin + " передаем ключ оплаты");
    	SetPremiumAdvert(sHost, sAuth_token, sIdAdvert2, true, 1);
    	
    	Sleep(2000);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert2 + " Проверяем значение статуса премиум объявления");
    	jData = GetAdvert(sHost, sIdAdvert2,  "Недвижимость - Вторичный рынок" );
    	
    	print("\r\nПроверяем статус премиум для объявления с ID = " + sIdAdvert2 + " после назначения премиума объявлению");
    	ValidatePremiun("true", jData, sIdAdvert2, " после назначения премиума объявлению");
    	
    	//удаляем поданные обяъвления
    	print("\r\nШАГ 12");
    	print("Удаляем поданные объявления".toUpperCase());
    	DeleteAdvert(sHost, sAuth_token, sIdAdvert);
    	DeleteAdvert(sHost, sAuth_token, sIdAdvert2);
    	
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
	}
	// деактивация объявления для автотеста
	private void DeactivateAdvert(String sHost, String sAuth_token, String sIdAdvert) throws URISyntaxException, ExceptFailTest, IOException, JSONException
	{
		print("\r\nДеактивация объявления".toUpperCase());
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
    		print("Ответ сервера:" + jsonObject.toString(10) + " Объявление деактивировано");
    	else
    	{
    		print("Не удалось деактивировать объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// обаработка статуса объявления автотесты
	private void ValidateStatus(String sWaitStatus, JSONObject jObj, String sIdAdvert, String sText) throws JSONException, ExceptFailTest
	{
		String sStatus = jObj.getJSONObject("advertisement").getString("status");
		if(sStatus.equals(sWaitStatus))
		{
			print("Текущий статус объявления ID = " + sIdAdvert + ", status = " + sStatus + " совпал с ожидаемым статусом  = " + sWaitStatus + sText);
		}
		else
		{
			print("Текущий статус объявления s ID = " + sIdAdvert + ", status = " + sStatus + " не совпал с ожидаемым статусом  = " + sWaitStatus + sText);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
	}
	// сравнение даты окнчания объявления до и после продления
	private void ValidateDateFinishAdvert(JSONObject jObj,  JSONObject jObj2, int nResult) throws JSONException, ExceptFailTest
	{
		String sDateFinish = jObj.getJSONObject("advertisement").getString("date_finish");
		String sDateFinish2 = jObj2.getJSONObject("advertisement").getString("date_finish");
		switch(nResult)
		{
			case 1:
				if(GetTimesMillisec(sDateFinish) < GetTimesMillisec(sDateFinish2))
				{
					print("Объявление продлено, время при подаче объявления " + sDateFinish + " время после продления объявления " + sDateFinish2);
				}
				else
				{
					print("Объявление  не продлено, время при подаче объявления " + sDateFinish + " время после продления объявления " + sDateFinish2);
					print("Тест провален".toUpperCase());
					throw new ExceptFailTest("Тест провален");
				}
				break;
			case 2:
				if(GetTimesMillisec(sDateFinish) == GetTimesMillisec(sDateFinish2))
				{
					print("Объявление не продлено, время при подаче объявления " + sDateFinish + " время после попытки продления объявления " + sDateFinish2 + " Корректно. Так как ключ оплаты не передавался");
				}
				else
				{
					print("Объявление продлено, время при подаче объявления " + sDateFinish + " время после продления объявления " + sDateFinish2);
					print("Тест провален".toUpperCase());
					throw new ExceptFailTest("Тест провален");
				}
		}
	}
	// активация объявления для автотеста
	private void ActivateAdvert(String sHost, String sAuth_token, String sIdAdvert, boolean bFlagApp_Token, int nResult) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		
		String sApp_token = "";
		if(bFlagApp_Token)
			sApp_token = "true";
		
		print("Активация объявлений".toUpperCase());
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
    	
    	switch (nResult)   // взависимости какой результата нам надо проверить
    	{
    		case 1: // положительный резултат проверяем
		    	if(jsonObject.isNull("error"))
		    	{
		    			if(jsonObject.getString("actions").equals("true"))
		    				print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление активировано");
		    			else
		    			{
		    				print("Не удалось активировать объявление \r\n"+
				    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    				print("Тест провален".toUpperCase());
		    				throw new ExceptFailTest("Тест провален");
		    			}
		    	}
		    	else
		    	{
		    		print("Не удалось активировать объявление \r\n"+
		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    		print("Тест провален".toUpperCase());
		    		throw new ExceptFailTest("Тест провален");
		    	}	
		    	break;
    		case 2: // отрицательный результат проверяем
    			if(jsonObject.isNull("error"))
		    	{
    				if(jsonObject.getString("actions").equals("true"))
    				{	
    					print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление активировано");
		    			print("Объявление не должно было активироваться, так как ключ оплаты передан не был");
		    			print("Тест провален");
		    			throw new ExceptFailTest("Тест провален");
    				}
    				else
    				{
    					print("Не удалось активировать объявление \r\n"+
    		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		    		print("Объявление не активировалось, так как ключ оплаты передан не был. Корректно");
    				}
		    	}
		    	else
		    	{
		    		print("Не удалось активировать объявление \r\n"+
		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    		print("Объявление не активировалось, так как ключ оплаты передан не был. Корректно");
		    		
		    	}	
		    	break;	
    	}
	}
	// продление объявления для автотеста
	private void ProlongAdvert(String sHost, String sAuth_token, String sIdAdvert, boolean bFlagApp_Token, int nResult) throws URISyntaxException, ExceptFailTest, JSONException, IOException 
	{
		String sApp_token = "";
		if(bFlagApp_Token)
			sApp_token = "true";
		
		print("Продление объявления".toUpperCase());
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
    	switch (nResult)   // взависимости какой результата нам надо проверить
    	{
    		case 1: // положительный резултат проверяем
		    	if(jsonObject.isNull("error"))
		    	{
		    			if(jsonObject.getString("actions").equals("true"))
		    				print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление продлено");
		    			else
		    			{
		    				print("Не удалось продлить объявление \r\n"+
				    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    				print("Тест провален".toUpperCase());
		    				throw new ExceptFailTest("Тест провален");
		    			}
		    	}
		    	else
		    	{
		    		print("Не удалось продлить объявление \r\n"+
		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    		print("Тест провален".toUpperCase());
		    		throw new ExceptFailTest("Тест провален");
		    	}	
		    	break;
    		case 2: // отрицательный результат проверяем
    			if(jsonObject.isNull("error"))
		    	{
    				if(jsonObject.getString("actions").equals("true"))
    				{	
    					print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление продленно");
		    			print("Объявление не должно было продлиться, так как ключ оплаты передан не был");
		    			print("Тест провален");
		    			throw new ExceptFailTest("Тест провален");
    				}
    				else
    				{
    					print("Не удалось продлить объявление \r\n"+
    		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		    		print("Объявление не продленно, так как ключ оплаты передан не был. Корректно");
    				}
		    	}
		    	else
		    	{
		    		print("Не удалось продлить объявление \r\n"+
		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    		print("Объявление не продленно, так как ключ оплаты передан не был. Корректно");
		    		
		    	}	
		    	break;	
    	}
	}
	// поднятие объявления для автотеста 
	private void PushUpAdvert(String sHost, String sAuth_token, String sIdAdvert, boolean bFlagApp_Token , int nResult) throws URISyntaxException, ExceptFailTest, IOException, JSONException 
	{
		String sApp_token = "";
		if(bFlagApp_Token)
			sApp_token = "true";
		
		print("Поднятие объявления в списке".toUpperCase());
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
    	
    	switch (nResult)   // взависимости какой результата нам надо проверить
    	{
    		case 1: // положительный резултат проверяем
		    	if(jsonObject.isNull("error"))
		    	{
		    			print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление поднято");
		    	}
		    	else
		    	{
		    		print("Не удалось поднять объявление \r\n"+
		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    		throw new ExceptFailTest("Тест провален");
		    	}	
		    	break;
    		case 2: // отрицательный результат проверяем
    			if(jsonObject.isNull("error"))
		    	{
		    			print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление поднято");
		    			print("Объявление не должно было подняться, так как ключ оплаты передан не был");
		    			print("Тест провален");
		    			throw new ExceptFailTest("Тест провален");
		    	}
		    	else
		    	{
		    		print("Не удалось поднять объявление \r\n"+
		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    		print("Объявление не поднялось, так как ключ оплаты передан не был. Корректно");
		    		
		    	}	
		    	break;	
    	}
	}
	// получение листинга категории для автотеста
	private JSONObject GetListCategory(String sHost, String sDataForList) throws ExceptFailTest, URISyntaxException, IOException, JSONException
	{
		JSONObject jTemp;	
		print("Получение листинга объявлений категории".toUpperCase());
		print("Параметры для запроса");
		print("sDataForList = "+ sDataForList);
		
		String sQuery = CreateSimpleRequest(sDataForList);
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
    	jTemp = jsonObject;
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера: Листинг объявлений получен");
    		print("");
    		JSONArray ar = jsonObject.getJSONArray("advertisements");
    		for(int i=0; i<ar.length(); i++)
    		{
    			print("--------------------------------------------------------------------------------------------------------------");
    			print("Объявление №" + i);
    			jsonObject = (JSONObject) ar.get(i);
    			print(jsonObject.toString(10));
    		
    		}
    		return jTemp;
    	}
    	else
    	{
    		print("Не удалось получить листинг категории \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// сравнение расположения объявлений 
	private void ValidetePlaceAdvert(int nAdvert, String sIdAdvert, int nAdvert2, String sIdAdvert2) throws ExceptFailTest
	{
		if(nAdvert < nAdvert2)
		{
			print("Объявление с ID = " + sIdAdvert + " распологается в листинге выше чем объявление с ID = " + sIdAdvert2 + " Корректно.");
		}
		else
		{
			print("Объявление с ID = " + sIdAdvert + "распологается в листинге ниже чем объявление с ID = " + sIdAdvert2);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
	}
	// перевод времени подачи в миллисекунды для автотеста
	private long GetTimesMillisec(String sDateFinish)
	{
		Calendar c = Calendar.getInstance();
		String mas[] = sDateFinish.split(" ");
		String mas2[] = mas[0].split("-");
		String mas3[] = mas[1].split(":"); 
		c.set(Integer.parseInt(mas2[0]), Integer.parseInt(mas2[1]), Integer.parseInt(mas2[2]), Integer.parseInt(mas3[0]), Integer.parseInt(mas3[1]), Integer.parseInt(mas3[2]));
		long l = c.getTimeInMillis();
		return l;
	}
	// выделения объявления для автотеста
	private void HighLightAdvert(String sHost, String sAuth_token, String sIdAdvert, boolean bFlagApp_Token, int nResult) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		String sApp_token = "";
		if(bFlagApp_Token)
			sApp_token = "true";
		
		print("Выделения объявления в списке".toUpperCase());
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
    	
    	switch (nResult)   // взависимости какой результата нам надо проверить
    	{
    		case 1: // положительный резултат проверяем
		    	if(jsonObject.isNull("error"))
		    	{
		    			if(jsonObject.getString("actions").equals("true"))
		    				print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление выделено");
		    			else
		    			{
		    				print("Не удалось выделить объявление \r\n"+
				    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    				print("Тест провален".toUpperCase());
		    				throw new ExceptFailTest("Тест провален");
		    			}
		    	}
		    	else
		    	{
		    		print("Не удалось выделить объявление \r\n"+
		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    		print("Тест провален".toUpperCase());
		    		throw new ExceptFailTest("Тест провален");
		    	}	
		    	break;
    		case 2: // отрицательный результат проверяем
    			if(jsonObject.isNull("error"))
		    	{
    				if(jsonObject.getString("actions").equals("true"))
    				{	
    					print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление выделено");
		    			print("Объявление не должно было выделиться, так как ключ оплаты передан не был");
		    			print("Тест провален");
		    			throw new ExceptFailTest("Тест провален");
    				}
    				else
    				{
    					print("Не удалось выделить объявление \r\n"+
    		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		    		print("Объявление не выделилось, так как ключ оплаты передан не был. Корректно");
    				}
		    	}
		    	else
		    	{
		    		print("Не удалось выделить объявление \r\n"+
		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    		print("Объявление не выделилось, так как ключ оплаты передан не был. Корректно");
		    		
		    	}	
		    	break;	
    	}
    	
	}
	// обработка статуса выделения объявления для автотестов
	private void ValidateHighLight(String sWaitStatus, JSONObject jObj, String sIdAdvert, String sText) throws JSONException, ExceptFailTest 
	{
		String sStatus = jObj.getJSONObject("advertisement").getString("ismarkup");
		if(sStatus.equals(sWaitStatus))
		{
			print("Текущий статус выделения объявления ID = " + sIdAdvert + ", ismarkup = " + sStatus + " совпал с ожидаемым статусом выделения ismarkup  = " + sWaitStatus + sText);
		}
		else
		{
			print("Текущий статус выделения объявления ID = " + sIdAdvert + ", ismarkup = " + sStatus + " не совпал с ожидаемым статусом выделения ismarkup  = " + sWaitStatus + sText);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
	}
	// назначение премиум для автотестов
	private void SetPremiumAdvert(String sHost, String sAuth_token, String sIdAdvert, boolean bFlagApp_Token, int nResult) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		String sApp_token = "";
		if(bFlagApp_Token)
			sApp_token = "true";
		
		print("Назначение «Премиум» объявлению".toUpperCase());
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("ADVERTISEMENT_ID = "+ sIdAdvert);
		print("sApp_token = "+ sApp_token);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/premium")
    		.setParameter("auth_token", sAuth_token)
    		.setParameter("app_token", sApp_token)
    		.setParameter("number", "7");
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
    	
    	switch (nResult)   // взависимости какой результата нам надо проверить
    	{
    		case 1: // положительный резултат проверяем
		    	if(jsonObject.isNull("error"))
		    	{
		    			if(jsonObject.getString("actions").equals("true"))
		    				print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявлению назначен премиум");
		    			else
		    			{
		    				print("Не удалось назначить премиум объявлению \r\n"+
				    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    				print("Тест провален".toUpperCase());
		    				throw new ExceptFailTest("Тест провален");
		    			}
		    	}
		    	else
		    	{
		    		print("Не удалось назначить премиум объявлению \r\n"+
		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    		print("Тест провален".toUpperCase());
		    		throw new ExceptFailTest("Тест провален");
		    	}	
		    	break;
    		case 2: // отрицательный результат проверяем
    			if(jsonObject.isNull("error"))
		    	{
    				if(jsonObject.getString("actions").equals("true"))
    				{	
    					print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление назначен премиум");
		    			print("Объявлению не должен было стать премиумом, так как ключ оплаты передан не был");
		    			print("Тест провален");
		    			throw new ExceptFailTest("Тест провален");
    				}
    				else
    				{
    					print("Не удалось назначить объявлению премиум \r\n"+
    		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		    		print("Объявлению не назначен премиум, так как ключ оплаты передан не был. Корректно");
    				}
		    	}
		    	else
		    	{
		    		print("Не удалось назначить объявлению премиум \r\n"+
		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    		print("Объявлению не назначен премиум, так как ключ оплаты передан не был. Корректно");
		    		
		    	}	
		    	break;	
    	}
	}
	// обработка статуса премиума для автотестов 
	private void ValidatePremiun(String sWaitStatus, JSONObject jObj, String sIdAdvert, String sText) throws JSONException, ExceptFailTest
	{
		String sStatus = jObj.getJSONObject("advertisement").getString("ispremium");
		if(sStatus.equals(sWaitStatus))
		{
			print("Текущий статус премиум, объявления ID = " + sIdAdvert + ", ispremium = " + sStatus + " совпал с ожидаемым статусом премиум ispremium  = " + sWaitStatus + sText);
		}
		else
		{
			print("Текущий статус премиум, объявления ID = " + sIdAdvert + ", ispremium = " + sStatus + " не совпал с ожидаемым статусом премиум ispremium  = " + sWaitStatus + sText);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
	}
	
	
	//Подача в бесплатную/деактивация/активация/Продление/Поднятие/Выделение/Назначение премиум/Получение листинга категории и проверка его (Платное объявление) 
	public void AddDeactivateActivateProlongPushupHighlightPremiumOPPaidAdvert(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, InterruptedException
	{
		String sIdAdvert, sIdAdvert2; 
		String sLogin = Proper.GetProperty("login_authOP");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		JSONObject jData, jData2, jDataPostAsvert;
		InnerDataHM objRealt;
		String sDataForList = "{category=cars/passenger/new/, region=russia/moskva-gorod/, currency=RUR, offset=0, limit=45, sort_by=date_sort:desc, include_privates=true, include_companies=true}";
		int nNumberList, nNumberList2;
		
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Подача , деактивация, активация, продление, поднятие, выделение, премиум, получение листингов  ОП(платное объявление) - Тест".toUpperCase()+"\r\n");
		// авторизация
		print("\r\nАвторизация пользователем - " + sLogin);
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
		
		// подача двух объявлений
		print("\r\nШАГ 1");
		print("Подача двух объявлений в платную рубрику Авто - Новые авто".toUpperCase());
		print("\r\nПодача объявления в рубрику Авто - Новые авто");
		print("Объявление №1");
    	objRealt = PostAdvert(sHost, mas_Advertisment, mas_Auto2, sAuth_token, "category_auto_new", "image");
    	sIdAdvert = objRealt.GetID();
   	
    	print("\r\nПодача объявления в рубрику Авто - Новые авто");
		print("Объявление №2");
    	objRealt = PostAdvert(sHost, mas_Advertisment, mas_Auto2, sAuth_token, "category_auto_new", "image");
    	sIdAdvert2 = objRealt.GetID();
    	
    	// проверка статуса объявлений поданных в платную рубрику
    	print("\r\nШАГ 2");
    	print("Проверяем статус объявлений подданых в платную рубрику и неоплаченных".toUpperCase());
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Проверяем значение статуса объявления");
    	jData = GetAdvert(sHost, sIdAdvert,  "Авто - Новые авто" );
    	print("\r\nПроверяем статус объявление с ID = " + sIdAdvert + " после подачи объявления");
    	ValidateStatus("2", jData, sIdAdvert, " после подачи объявления");
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert2 + " Проверяем значение статуса объявления");
    	jData = GetAdvert(sHost, sIdAdvert,  "Авто - Новые авто" );
    	print("\r\nПроверяем статус объявление с ID = " + sIdAdvert2 + " после подачи объявления");
    	ValidateStatus("2", jData, sIdAdvert2, " после подачи объявления");
    	
    	
    	// проверка попытки  активации объявления без оплаты
    	print("\r\nШАГ 3");
    	print("Проверка попытки активации объявления без оплаты".toUpperCase());
    	
    	print("\r\nПытаемся активировать  объявление с ID = " + sIdAdvert +  " для пользователя " + sLogin + " без передачи ключа оплаты");
    	ActivateAdvert(sHost, sAuth_token, sIdAdvert, false, 2);
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Проверяем значение статуса объявления");
    	jData = GetAdvert(sHost, sIdAdvert,  "Авто - Новые авто" );
    	print("\r\nПроверяем статус объявление с ID = " + sIdAdvert + " после попытки активации объявления, без передачи ключа оплаты");
    	ValidateStatus("2", jData, sIdAdvert, " после попытки активации объявления, без передачи ключа оплаты");
    	
    	print("\r\nПытаемся активировать  объявление с ID = " + sIdAdvert2 +  " для пользователя " + sLogin + " без передачи ключа оплаты");
    	ActivateAdvert(sHost, sAuth_token, sIdAdvert2, false, 2);
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert2 + " Проверяем значение статуса объявления");
    	jData = GetAdvert(sHost, sIdAdvert2,  "Авто - Новые авто" );
    	print("\r\nПроверяем статус объявление с ID = " + sIdAdvert2 + " после попытки активации объявления, без передачи ключа оплаты");
    	ValidateStatus("2", jData, sIdAdvert2, " после попытки активации объявления, без передачи ключа оплаты");
    	
    	
    	// Проверка активации объявлений с оплатой
    	print("\r\nШАГ 4");
    	print("Активации объявления с оплатой".toUpperCase());
    	
    	print("\r\nАктивируем объявление с ID = " + sIdAdvert +  " для пользователя " + sLogin + " передаем ключ оплаты");
    	ActivateAdvert(sHost, sAuth_token, sIdAdvert, true, 1);
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Проверяем значение статуса объявления");
    	jData = GetAdvert(sHost, sIdAdvert,  "Авто - Новые авто" );
    	print("\r\nПроверяем статус объявление с ID = " + sIdAdvert + " после активации с оплатой");
    	ValidateStatus("1", jData, sIdAdvert, " после активации с оплатой");
    	
    	print("\r\nАктивируем объявление с ID = " + sIdAdvert2 +  " для пользователя " + sLogin + " передаем ключ оплаты");
    	ActivateAdvert(sHost, sAuth_token, sIdAdvert2, true, 1);
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert2 + " Проверяем значение статуса объявления");
    	jData = GetAdvert(sHost, sIdAdvert2,  "Авто - Новые авто" );
    	print("\r\nПроверяем статус объявление с ID = " + sIdAdvert2 + " после активации с оплатой");
    	ValidateStatus("1", jData, sIdAdvert2, " после активации с оплатой");
    	
    	print("\r\nОжидаем индексации, время ожидания ".toUpperCase() + Integer.parseInt(Proper.GetProperty("timeWait"))/(1000*60) + " минут(ы)".toUpperCase());
    	Sleep(Integer.parseInt(Proper.GetProperty("timeWait")));
    	
    	// проверка их появления в листинге и коректного расположения 
    	print("\r\nШАГ 5");
    	print("Проверяем появление объявлений в листинге и их корректное расположение".toUpperCase());
    	print("\r\nПолучаем листинг категории объявлений рубрики  Авто - Новые авто");
    	jData = GetListCategory(sHost, sDataForList);
    	
    	print("\r\nИщем поданные объявления в листинге и запоминаем их порядковые номера");
    	nNumberList = FindAdvertFromListAfterPost(jData, sIdAdvert);
    	print("Объявление номер 1(поданное первым) распологается в листинге на " + nNumberList + " месте");
    	nNumberList2 = FindAdvertFromListAfterPost(jData, sIdAdvert2);
    	print("Объявление номер 2(поданное вторым) распологается в листинге на " + nNumberList2 + " месте");
    	
    	print("Сравниваем расположения первого подданного объявления с ID = " + sIdAdvert + " со вторым поданным объявлением с ID = " + sIdAdvert2);
    	ValidetePlaceAdvert(nNumberList2, sIdAdvert2, nNumberList, sIdAdvert);
    	
    	// проверка попытки продления объявления без оплаты
    	print("\r\nШАГ 6");
    	print("Проверка попытки продления объявления без передачи ключа оплаты".toUpperCase());
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Запоминаем время окончания размещения объявления");
    	jDataPostAsvert = GetAdvert(sHost, sIdAdvert,  "Авто - Новые авто" );// запоминаем json объект в нем время окончания размещения сраз после подачи
    	
    	print("\r\nПытаемя продлить объявление с ID = " + sIdAdvert +  " для пользователя " + sLogin + " без передачи ключа оплаты");
    	ProlongAdvert(sHost, sAuth_token, sIdAdvert, false, 2);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Проверяем значение времени окончания размещения объявления после попытки продления");
    	jData2 = GetAdvert(sHost, sIdAdvert,  "Авто - Новые авто" );
    	print("Сравниваем время окончания размещения объявления до и после попытки продления");
    	ValidateDateFinishAdvert(jDataPostAsvert, jData2, 2);   	
    	
    	
    	// проверка продления объявления
    	print("\r\nШАГ 7");
    	print("Проверка продления объявления".toUpperCase());
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Запоминаем время окончания размещения объявления");
    	jDataPostAsvert = GetAdvert(sHost, sIdAdvert,  "Авто - Новые авто" );// запоминаем json объект в нем время окончания размещения сраз после подачи
    	
    	print("\r\nПродлеваем объявление с ID = " + sIdAdvert +  " для пользователя " + sLogin + " передаем ключ оплаты");
    	ProlongAdvert(sHost, sAuth_token, sIdAdvert, true, 1);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Проверяем значение времени окончания размещения объявления после продления");
    	jData2 = GetAdvert(sHost, sIdAdvert,  "Авто - Новые авто" );
    	print("Сравниваем время окончания размещения объявления до и после продления");
    	ValidateDateFinishAdvert(jDataPostAsvert, jData2, 1);   	
    	
    	// проверка попытки поднять объявление без оплаты
    	print("\r\nШАГ 8");
    	print("Проверка попытки поднять объявление без оплаты".toUpperCase());
    	print("\r\nПытаемся поднять  объявление с ID = " + sIdAdvert +  " для пользователя " + sLogin + " без передачи ключа оплаты");
    	PushUpAdvert(sHost, sAuth_token, sIdAdvert, false, 2);
    	
    	print("\r\nОжидаем индексации, время ожидания ".toUpperCase() + Integer.parseInt(Proper.GetProperty("timeWait"))/(1000*60) + " минут(ы)".toUpperCase());
    	Sleep(Integer.parseInt(Proper.GetProperty("timeWait")));
    	
    	print("\r\nПроверяем что объявление с ID = " + sIdAdvert + " не было поднято");
    	print("\r\nПолучаем листинг категории объявлений рубрики Авто - Новые авто");
    	jData = GetListCategory(sHost, sDataForList);
    	
    	print("\r\nИщем поданные объявления в листинге и запоминаем их порядковые номера");
    	nNumberList = FindAdvertFromListAfterPost(jData, sIdAdvert);
    	print("Объявление номер 1(которое мы пытались поднять без оплаты) распологается в листинге на " + nNumberList + " месте");
    	nNumberList2 = FindAdvertFromListAfterPost(jData, sIdAdvert2);
    	print("Объявление номер 2(которое распологалось в листинге выше, чем то которое мы пытались поднять) распологается в листинге на " + nNumberList2 + " месте");
    	
    	print("Сравниваем расположения объявления которое мы пытались поднять без оплаты  ID = " + sIdAdvert + " с объявлением с ID = " + sIdAdvert2 +
    			" которое распологалось в листинге выше, чем то которое мы пытались поднять");
    	ValidetePlaceAdvert(nNumberList2, sIdAdvert2, nNumberList, sIdAdvert);
    	
    	
    	//   проверка поднятия объявления
    	print("\r\nШАГ 9");
    	print("Проверка поднятия объявления".toUpperCase());
    	print("\r\nПодымаем  объявление с ID = " + sIdAdvert +  " для пользователя " + sLogin + " передаем ключ оплаты");
    	PushUpAdvert(sHost, sAuth_token, sIdAdvert, true, 1);
    	
    	print("\r\nОжидаем индексации, время ожидания ".toUpperCase() + Integer.parseInt(Proper.GetProperty("timeWait"))/(1000*60) + " минут(ы)".toUpperCase());
    	Sleep(Integer.parseInt(Proper.GetProperty("timeWait")));
    	
    	print("\r\nПроверяем что объявление с ID = " + sIdAdvert + " поднято");
    	print("\r\nПолучаем листинг категории объявлений рубрики Авто - Новые авто");
    	jData = GetListCategory(sHost, sDataForList);
    	
    	print("\r\nИщем поданные объявления в листинге и запоминаем их порядковые номера");
    	nNumberList = FindAdvertFromListAfterPost(jData, sIdAdvert);
    	print("Объявление номер 1 распологается в листинге на " + nNumberList + " месте");
    	nNumberList2 = FindAdvertFromListAfterPost(jData, sIdAdvert2);
    	print("Объявление номер 2 распологается в листинге на " + nNumberList2 + " месте");
    	print("Сравниваем расположения поднятого объявления с ID = " + sIdAdvert + " с объявлением с ID = " + sIdAdvert2 +
    			" которое распологалось до поднятия выше поднятого ");
    	ValidetePlaceAdvert(nNumberList, sIdAdvert, nNumberList2, sIdAdvert2);
    	
    	
    	// попытка выделения объявления без оплаты
    	print("\r\nШАГ 10");
    	print("Проверка попытки выделить объявление без оплаты".toUpperCase());
    	print("\r\nПытаемся выделить объявление с ID = " + sIdAdvert +  " для пользователя " + sLogin + " без передачи ключа оплаты");
    	HighLightAdvert(sHost, sAuth_token, sIdAdvert, false, 2);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Проверяем значение статуса выделения объявления");
    	jData = GetAdvert(sHost, sIdAdvert,  "Авто - Новые авто" );
    	
    	print("\r\nПроверяем статус выделения объявление с ID = " + sIdAdvert + " после попытки выделить объявления без передачи ключа оплаты");
    	ValidateHighLight("0", jData, sIdAdvert, " после попытки выделить объявления без передачи ключа оплаты");
    	
    	// выделение объявления
    	print("\r\nШАГ 11");
    	print("Проверка выделения объявление".toUpperCase());
    	print("\r\nВыделяем объявление с ID = " + sIdAdvert +  " для пользоватея " + sLogin + " передаем ключ оплаты");
    	HighLightAdvert(sHost, sAuth_token, sIdAdvert, true, 1);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Проверяем значение статуса выделения объявления");
    	jData = GetAdvert(sHost, sIdAdvert,  "Авто - Новые авто" );
    	
    	print("\r\nПроверяем статус выделения для объявления с ID = " + sIdAdvert + " после выделения объявления");
    	ValidateHighLight("1", jData, sIdAdvert, " после выделения объявления");
    	
    	//попытка назначения премиум объявления без оплаты
    	print("\r\nШАГ 12");
    	print("Проверка попытки назначить премиум объявлению без оплаты".toUpperCase());
    	print("\r\nПытаемся назначить премиум объявлению с ID = " + sIdAdvert2 +  " для пользователя " + sLogin + " без передачи ключа оплаты");
    	SetPremiumAdvert(sHost, sAuth_token, sIdAdvert2, false, 2);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert2 + " Проверяем значение статуса премиум объявления");
    	jData = GetAdvert(sHost, sIdAdvert2,  "Авто - Новые авто" );
    	
    	print("\r\nПроверяем статус премиум для объявления с ID = " + sIdAdvert2 + " после попытки назначить премиум объявлению без передачи ключа оплаты");
    	ValidatePremiun("false", jData, sIdAdvert2, " после попытки назначить премиум объявлению без передачи ключа оплаты");
    	
    	// назначение премиума
    	print("\r\nШАГ 13");
    	print("Проверка назначения премиум объявлению".toUpperCase());
    	print("\r\nНазначаем премиум объявлению с ID = " + sIdAdvert2 +  " для пользователя " + sLogin + " передаем ключ оплаты");
    	SetPremiumAdvert(sHost, sAuth_token, sIdAdvert2, true, 1);
    	
    	Sleep(2000);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert2 + " Проверяем значение статуса премиум объявления");
    	jData = GetAdvert(sHost, sIdAdvert2,  "Авто - Новые авто" );
    	
    	print("\r\nПроверяем статус премиум для объявления с ID = " + sIdAdvert2 + " после назначения премиума объявлению");
    	ValidatePremiun("true", jData, sIdAdvert2, " после назначения премиума объявлению");
    	
    	
    	//проверка деактиивации объявления
    	print("\r\nШАГ 4");
    	print("Проверка деактивации объявления".toUpperCase());
    	print("\r\nДеактивируем объявление с ID = " + sIdAdvert +  " для пользователя " + sLogin);
    	DeactivateAdvert(sHost, sAuth_token, sIdAdvert);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Проверяем значение статуса объявления");
    	jData = GetAdvert(sHost, sIdAdvert,  "Авто - Новые авто" );
    	
    	print("\r\nПроверяем статус объявление с ID = " + sIdAdvert + " после деактивации объявления");
    	ValidateStatus("2", jData, sIdAdvert, " после деактивации объявления");
    	
    	print("\r\nОжидаем индексации, время ожидания ".toUpperCase() + Integer.parseInt(Proper.GetProperty("timeWait"))/(1000*60) + " минут(ы)".toUpperCase());
    	Sleep(Integer.parseInt(Proper.GetProperty("timeWait")));
    	
    	print("\r\nИщем деактивированное объявление в листинге категории");
    	print("\r\nПолучаем листинг категории объявлений рубрики Авто - Новые авто");
    	jData = GetListCategory(sHost, sDataForList);
    	FindAdvertFromListAfterDelete(jData, sIdAdvert);
    	
    	
    	//удаляем поданные обяъвления
    	print("\r\nШАГ 14");
    	print("Удаляем поданные объявления".toUpperCase());
    	DeleteAdvert(sHost, sAuth_token, sIdAdvert);
    	DeleteAdvert(sHost, sAuth_token, sIdAdvert2);
    	
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
    	
    	
	}

	
// Параметризированные тесты
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
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
		print("1.1.	Авторизация".toUpperCase());
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
    	
    	if(sTempResponse.equals("{\"error\":{\"description\":\"Не указан логин или пароль\",\"code\":1}}"))
    	{
    		print("Не указан логин или пароль");
    		print("Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}
    	if(sTempResponse.equals("{\"error\":{\"description\":\"Пользователя с такими данными не существует\",\"code\":3}}"))
    	{
    		print("Пользователя с такими данными не существует");
    		print("Ответ сервера:\r\n"+ jsonObject.toString() + "\r\n");
    		throw new ExceptFailTest("Тест провален");
    	}
    	if(sTempResponse.equals("{\"error\":{\"description\":\"Пользователь не активный\",\"code\":6}}"))
    	{
    		print("Пользовател неактивен или забанен");
    		print("Ответ сервера:\r\n"+ jsonObject.toString() + "\r\n");
    		throw new ExceptFailTest("Тест провален");
    	}
    	
    	String sAuth_token = (String) jsonObject.get("auth_token");
    	if(sAuth_token != null)
    	{
    	         print("Auth_token = "+ sAuth_token);
    	         print("Ответ сервера:\r\n"+ jsonObject.toString(10) + "\r\nПользователь авторизован");
    	         return sAuth_token;
    	}
    	else 
    	{
    		print("Ответ сервера:\r\n"+ jsonObject.toString(10) + "\r\n");
    		throw new ExceptFailTest("Тест провален");
    	}
	}
	// Получение профиля
	public String GetProfile_1_2(String sHost,String sUsername, String sPassword, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		String  sAuth_token= "";
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		
		print("1.2.	Получение профиля".toUpperCase());
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
    	
    	
    	print("Ответ сервера:\r\n"+ jsonObject.toString(10));
    	
    	if(jsonObject.isNull("error"))
    	{
    		return sAuth_token;
    	}
    	else
    	{
    		print("Тест провален");
    		throw new ExceptFailTest("Тест провален");
    	}
    	
	}
	// Редактирование профиля
	public void EditProfile_1_3(String sHost,String sUsername, String sPassword, String sUser_info, boolean bAuthFlag) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		sAuth_token = GetProfile_1_2(sHost, sUsername, sPassword, bAuthFlag);
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
    	
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:\r\n"+ jsonObject.toString(10));
    	else
    	{
    		print("Тест провален");
    		print("Ответ сервера:\r\n"+ jsonObject.toString(10));
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
	public void PostAdvert_2_1(String sHost, String sUsername, String sPassword, String sCatRegAdv, String sAdvertisement, String sCustom_fields, String sVideoUrl, String sPathImage, boolean bAuthFlag) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		
		print("");
		print("2.1.	Подача объявления");
		print("Параметры для запроса");
		print("sCatRegAdv = "+ sCatRegAdv);
		print("sAdvertisement = "+ sAdvertisement);
		print("sCustom_fields = "+ sCustom_fields);
		print("sAuth_token = "+ sAuth_token);
		print("sVideoUrl = "+ sVideoUrl);
		
		String sVideo = "&advertisement[video]="+sVideoUrl;
		String sRequest = CreateSimpleRequest(sCatRegAdv);
		String sRequest1 = CreateArrayRequest("advertisement" ,sAdvertisement);
		String sRequest2 = CreateDoubleArrayRequest("advertisement", "custom_fields", sCustom_fields);
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert")
    		.setQuery(sRequest+sRequest1+sRequest2+sVideo)
    		.setParameter("auth_token", sAuth_token);
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%").replace("%3D", "=").replace("%3F", "?");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequestImage(uri, sPathImage);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("\r\nОтвет сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление создано");
    		
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
    		print("Ответ сервера:" + jsonObject.toString(2) + " Объявление получено");
    		
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
	public void EditAdvert_2_3(String sHost, String sUsername, String sPassword, String sIdAdvert, String sAdvertisement, String sCustom_fields, String sPathImageNew, String sVideoUrl, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		
		String sVideo = "&advertisement[video]="+sVideoUrl;
		String  sAuth_token= "";
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующие запросы уйдет пустой ключ auth_token");
		String sUrlImage = GetAdvert_2_2(sHost, sIdAdvert);
		print("");
		print("2.3.	Редактирование объявления");
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("sVideoUrl = " + sVideoUrl);
		print("ADVERTISEMENT_ID = "+ sIdAdvert);
		if(sUrlImage.equals("false"))
		{
			print("В объявлении нет изображений ");
		}
		else
		{
			print("UrlImage = "+ sUrlImage +"\r\n");
		}
		
		String sRequest = CreateDoubleArrayRequest("advertisement", "custom_fields", sCustom_fields);
		String sRequest1 = CreateArrayRequest("advertisement" , sAdvertisement);
		
		builder = new URIBuilder();
		
		if(!sUrlImage.equals("false"))
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/"+ sIdAdvert)
    		.setQuery(sRequest1 + sRequest + sVideo + "&deleted_images[0]=" + sUrlImage).setParameter("auth_token", sAuth_token);

    	else
    		builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/"+ sIdAdvert)
    		.setQuery(sRequest1 + sRequest + sVideo).setParameter("auth_token", sAuth_token);
    	
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
    		print("\r\nОтвет сервера:\r\n" + jsonObject.toString(10) + "\r\n Объявление отредактировано");
    		
    	else
    	{
    		print("Не удалось отредактировать объявление\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}
		
	}
	// Удаление объявления
	public void DeleteAdvert_2_4(String sHost, String sUsername, String sPassword, String sIdAdvert, boolean bAuthFlag) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующие запросы уйдет пустой ключ auth_token");
		
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
	public void AddAdvertToFavourite_2_5(String sHost, String sUsername, String sPassword, String sIdAdvert, boolean bAuthFlag) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		
		String  sAuth_token= "";
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		
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
    		print("Ответ сервера:" + jsonObject.toString(10) + " Объявление добавлено в избранное, проверьте вкладку избранные для данного пользователя");
    	else
    	{
    		print("Не удалось добавить объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Удаления объявления из избранного
	public void DeleteAdvertFromFavourite_2_6(String sHost, String sUsername, String sPassword, String sIdAdvert, boolean bAuthFlag) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
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
	public void GetPaidProductsToStepToAdd_2_7(String sHost, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
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
    		print("Ответ сервера:" + jsonObject.toString(10) + "\r\nСписок получен");
    	else
    	{
    		print("Не удалось получить список продуктов \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Получение списка платных продуктов для объявления доступных в личном кабинете пользователя
	public void GetPaidProductsFromLK_2_8(String sHost, String sIdAdvert) throws ExceptFailTest, URISyntaxException, IOException, JSONException
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
    		print("Ответ сервера:" + jsonObject.toString(10) + "\r\nСписок получен");
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
	public void ActivationAdvert_2_10(String sHost, String sUsername, String sPassword, String sIdAdvert, boolean bApp_token, boolean bAuthFlag) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		String  sApp_token="";
		
		if(bApp_token)
			sApp_token = "true";
		else print("Передан параметр не передавать ключ оплаты App_token. В следующий запрос уйдет пустой ключ app_token");
			
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		print("2.10.	Активация объявлений".toUpperCase());
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
	public void DeactivateAdvert_2_11(String sHost, String sUsername, String sPassword, String sIdAdvert, boolean bAuthFlag) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		
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
	public void Prolongadvert_2_12(String sHost, String sUsername, String sPassword, String sIdAdvert, boolean bApp_token, boolean bAuthFlag) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		String  sApp_token="";
		
		if(bApp_token)
			sApp_token = "true";
		else print("Передан параметр не передавать ключ оплаты App_token. В следующий запрос уйдет пустой ключ app_token");
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		
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
    	{
    		if(jsonObject.toString().equals("{\"error\":null,\"actions\":false}"))
    			print("Ответ сервера:" + jsonObject.toString() + " Объявление не продлено (возможно оно неактивно, неоплачено)");
    		else 
    			print("Ответ сервера:" + jsonObject.toString() + " Объявление продлено");
    	}
    	else
    	{
    		print("Не удалось продлить объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Поднятие объявления
	public void PushUpAdvert_2_13(String sHost, String sUsername, String sPassword, String sIdAdvert, boolean bApp_token,  boolean bAuthFlag) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		String  sApp_token="";
		
		if(bApp_token)
			sApp_token = "true";
		else print("Передан параметр не передавать ключ оплаты App_token. В следующий запрос уйдет пустой ключ app_token");
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		
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
    	{
    		if(jsonObject.toString().equals("{\"error\":null,\"actions\":false}"))
    			print("Ответ сервера:" + jsonObject.toString() + " Объявление не поднято (возможно оно неактивно, неоплачено)");
    		else 
    			print("Ответ сервера:" + jsonObject.toString() + " Объявление поднято");
    	}
    	else
    	{
    		print("Не удалось поднять объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Выделение объявления 
	public void HighLightAdvert_2_14(String sHost, String sUsername, String sPassword, String sIdAdvert, boolean bApp_token, boolean bAuthFlag) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		String  sApp_token="";
		
		if(bApp_token)
			sApp_token = "true";
		else print("Передан параметр не передавать ключ оплаты App_token. В следующий запрос уйдет пустой ключ app_token");
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		
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
    	{
    		if(jsonObject.toString().equals("{\"error\":null,\"actions\":false}"))
    			print("Ответ сервера:" + jsonObject.toString() + " Объявление не выделено (возможно оно неактивно, неоплачено)");
    		else 
    			print("Ответ сервера:" + jsonObject.toString() + " Объявление выделено");
    	}
    	else
    	{
    		print("Не удалось выделить объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Назначение «Премиум» объявлению
	public void SetPremiumForAdvert_2_15(String sHost, String sUsername, String sPassword, String sIdAdvert, boolean bApp_token, String sNumberDays, boolean bAuthFlag) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		String  sApp_token="";
		
		if(bApp_token)
			sApp_token = "true";
		else print("Передан параметр не передавать ключ оплаты App_token. В следующий запрос уйдет пустой ключ app_token");
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		
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
    	{
    		if(jsonObject.toString().equals("{\"error\":null,\"actions\":false}"))
    			print("Ответ сервера:" + jsonObject.toString() + " Объявление не назначен премиум (возможно оно неактивно, неоплачено)");
    		else 
    			print("Ответ сервера:" + jsonObject.toString() + " Объявление назначен премиум");
    	}
    	else
    	{
    		print("Не удалось назначить премиум объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// Проголосовать за объявление (повысить рейтинг объявления)
	public void VoteForAdvertHigh_2_16(String sHost, String sUsername, String sPassword, String sIdAdvert, boolean bAuthFlag) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		
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
	public void VoteForAdvertLower_2_17(String sHost, String sUsername, String sPassword, String sIdAdvert, boolean bAuthFlag) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		
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
    			print("--------------------------------------------------------------------------------------------------------------");
    			print("Объявление №" + i);
    			jsonObject = (JSONObject) ar.get(i);
    			print(jsonObject.toString(10));
    		
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
    		print("Ответ сервера: Листинг объявлений получен");
    		print("");
    		JSONArray ar = jsonObject.getJSONArray("advertisements");
    		for(int i=0; i<ar.length(); i++)
    		{
    			print("--------------------------------------------------------------------------------------------------------------");
    			print("Объявление №" + i);
    			jsonObject = (JSONObject) ar.get(i);
    			print(jsonObject.toString(10));
    		
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
	public void GetListFavourite_2_20(String sHost, String sUsername, String sPassword, String sDataForSearchFavourite, boolean bAuthFlag) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		
		
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
    		print("Ответ сервера: Листинг объявлений, добавленных в «Избранное» получен");
    		JSONArray ar = jsonObject.getJSONArray("advertisements");
    		for(int i=0; i<ar.length(); i++)
    		{
    			print("--------------------------------------------------------------------------------------------------------------");
    			print("Объявление №" + i);
    			jsonObject = (JSONObject) ar.get(i);
    			print(jsonObject.toString(10));
    		
    		}
    	}
    	else
    	{
    		print("Не удалось получить листинг объявлений, добавленных в «Избранное» \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	//2.21.	Получение листинга «своих» объявлений
	public void GetListOwnAdvert_2_21(String sHost, String sUsername, String sPassword, String sDataForSearchOwnAdvert, boolean bAuthFlag) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String  sAuth_token= "";
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		
		
		print("2.21.	Получение листинга «своих» объявлений".toUpperCase());
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
    		print("Ответ сервера: Листинг «своих» объявлений получен");
    		print("");
    		JSONArray ar = jsonObject.getJSONArray("advertisements");
    		for(int i=0; i<ar.length(); i++)
    		{
    			print("--------------------------------------------------------------------------------------------------------------");
    			print("Объявление №" + i);
    			jsonObject = (JSONObject) ar.get(i);
    			print(jsonObject.toString(10));
    		
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
    		print("Ответ сервера: Листинг объявлений пользователя получен");
    		JSONArray ar = jsonObject.getJSONArray("advertisements");
    		for(int i=0; i<ar.length(); i++)
    		{
    			print("--------------------------------------------------------------------------------------------------------------");
    			print("Объявление №" + i);
    			jsonObject = (JSONObject) ar.get(i);
    			print(jsonObject.toString(10));
    		
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
    	
    	JSONObject jsonTemp;
    	String sResponse = HttpGetRequest(uri);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:" + jsonObject.toString() + "список полей рубрики для подачи объявления получен");
    		print("--------------------------------------------------------------------------------------------------------------");
			print("group_custom_fields");
			jsonTemp = jsonObject.getJSONObject("group_custom_fields");
			print(jsonTemp.toString(10));
			
			
			JSONArray ar = jsonObject.getJSONArray("video");
    		for(int i=0; i<ar.length(); i++)
    		{
    			print("--------------------------------------------------------------------------------------------------------------");
    			print("video");
    			jsonTemp = (JSONObject) ar.get(i);
    			print(jsonTemp.toString(10));
    		
    		}
			
    		ar = jsonObject.getJSONArray("contacts");
    		for(int i=0; i<ar.length(); i++)
    		{
    			print("--------------------------------------------------------------------------------------------------------------");
    			print("contacts");
    			jsonTemp = (JSONObject) ar.get(i);
    			print(jsonTemp.toString(10));
    		
    		}
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
    	JSONObject jsonTemp;
    	
    	if(jsonObject.isNull("error"))
    	{
	    	print("Ответ сервера. Cписок полей рубрики для редактирования объявлений получен \r\n");
	    	
	    	print("--------------------------------------------------------------------------------------------------------------");
			print("group_custom_fields");
			jsonTemp = jsonObject.getJSONObject("group_custom_fields");
			print(jsonTemp.toString(10));
			
			
			JSONArray ar = jsonObject.getJSONArray("video");
    		for(int i=0; i<ar.length(); i++)
    		{
    			print("--------------------------------------------------------------------------------------------------------------");
    			print("video");
    			jsonTemp = (JSONObject) ar.get(i);
    			print(jsonTemp.toString(10));
    		
    		}
			
    		ar = jsonObject.getJSONArray("contacts");
    		for(int i=0; i<ar.length(); i++)
    		{
    			print("--------------------------------------------------------------------------------------------------------------");
    			print("contacts");
    			jsonTemp = (JSONObject) ar.get(i);
    			print(jsonTemp.toString(10));
    		
    		}
			
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
    	JSONObject jsonTemp;
    	jsonObject = ParseResponse(sResponse);
    	jsonTemp = jsonObject;
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера. Cписок полей рубрики для фильтрации объявлений получен");
    		print("");
    		JSONArray ar = jsonTemp.getJSONArray("default");
    		for(int i=0; i<ar.length(); i++)
    		{
    			print("--------------------------------------------------------------------------------------------------------------");
    			print("default");
    			jsonObject = (JSONObject) ar.get(i);
    			print(jsonObject.toString(10));
    		
    		}
    		ar = jsonTemp.getJSONArray("extended");
    		for(int i=0; i<ar.length(); i++)
    		{
    			print("--------------------------------------------------------------------------------------------------------------");
    			print("extended");
    			jsonObject = (JSONObject) ar.get(i);
    			print(jsonObject.toString(10));
    		
    		}
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

		print("4.3. Получение списка городов, принадлежащих определенному субъекту РФ".toUpperCase());
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
    		print("Ответ сервера: \r\n" + jsonObject.toString(10) + "\r\nсписок популярных городов получен");
    	}
    	else
    	{
    		print("Не удалось получить популярных городов \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	//4.2.1.	Получение списка городов, для которых заведены поддомены
	public void GetCitiesWithDomen_4_2_1(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest
		{

			print("4.2.	Получение списка городов, для которых заведены поддомены".toUpperCase());
			print("Параметры для запроса");
			builder = new URIBuilder();
	    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/regions/popular_cities");
	    	
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
	    		print("Ответ сервера: \r\n" + jsonObject.toString(10) + "\r\nсписок городов, для которых заведены поддомены");
	    	}
	    	else
	    	{
	    		print("Не удалось получить список городов, для которых заведены поддомены \r\n"+
	    				"Ответ сервера:\r\n"+ jsonObject.toString());
	    		throw new ExceptFailTest("Тест провален");
	    	}	
		}
	// 4.3.	Поиск городов и населенных пунктов по названию (саджест)
	public void GetCitiesSuggest_4_3(String sHost, String sDataCitiesSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("4.4. Поиск городов и населенных пунктов по названию (саджест)".toUpperCase());
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
    		print("Ответ сервера: \r\n" + jsonObject.toString(10) + "\r\n список городов и населенных пунктов по названию (саджест) получен");
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

		print("4.5. Получение списка улиц (саджест)".toUpperCase());
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
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nсписок улиц (саджест) получен");
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

		print("4.6. Получение списка домов улицы (саджест)".toUpperCase());
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
	//4.6.	Получение списка районов (саджест)
	public void GetDistrictSuggest_4_6(String sHost, String sDataDistrictSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("4.7.	Получение списка районов (саджест)".toUpperCase());
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
    		print("Ответ сервера:" + jsonObject.toString() + "\r\nсписок районов (саджест) получен \r\n");
    		JSONArray ar = jsonObject.getJSONArray("districts");
    		for(int i=0; i<ar.length(); i++)
    			print(ar.get(i));
    	}
    	else
    	{
    		print("Не удалось получить список районов (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}	
	//4.8	Получение списка районов (саджест)
	public void GetMicroDistrictSuggest_4_8(String sHost, String sDataMicroDistrictSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("4.8. Получение списка микрорайонов (саджест)".toUpperCase());
		print("Параметры для запроса");
		print("DataDistrictSuggest = "+ sDataMicroDistrictSuggest);
	
		String sQuery = CreateSimpleRequest(sDataMicroDistrictSuggest);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/regions/microdistricts")
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
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nсписок микрорайонов (саджест) получен \r\n");
    	}
    	else
    	{
    		print("Не удалось получить список микрорайонов (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	//4.9	Получение списка АО (саджест)
	public void GetAOSuggest_4_9(String sHost, String sAOSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
		{

			print("4.9. Получение списка административных округов (саджест)".toUpperCase());
			print("Параметры для запроса");
			print("DataDistrictSuggest = "+ sAOSuggest);
		
			String sQuery = CreateSimpleRequest(sAOSuggest);
			builder = new URIBuilder();
	    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/regions/ao")
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
	    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nсписок административных округов (саджест) получен \r\n");
	    	}
	    	else
	    	{
	    		print("Не удалось получить список административных округов (саджест) \r\n"+
	    				"Ответ сервера:\r\n"+ jsonObject.toString());
	    		throw new ExceptFailTest("Тест провален");
	    	}	
		}
	//4.10.	Получение списка направлений (саджест)
	public void GetDirectionSuggest_4_10(String sHost, String sDataDirectionSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("4.10. Получение списка направлений (саджест)".toUpperCase());
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
    		print("Ответ сервера:" + jsonObject.toString() + "\r\nсписок направлений (саджест) получен \r\n");
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
	//4.11.	Получение списка шоссе (саджест)
	public void GetHighwaySuggest_4_11(String sHost, String sDataHighwaySuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("4.11. Получение списка шоссе (саджест)".toUpperCase());
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
    		print("Ответ сервера:" + jsonObject.toString() + "\r\nсписок шоссе (саджест) получен\r\n");
    		JSONArray ar = jsonObject.getJSONArray("highways");
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
	//4.12.	Получение списка станций метро (саджест)
	public void GetMetroSuggest_4_12(String sHost, String sDataMetroSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("4.12.	Получение списка станций метро (саджест)".toUpperCase());
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
    		print("Ответ сервера:" + jsonObject.toString() + "\r\nсписок станций метро (саджест) получен\r\n");
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
    		print("Ответ сервера:" + jsonObject.toString() + "\r\nсписок валют получен");
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
    		print("Ответ сервера:" + jsonObject.toString() + "\r\nзначения словаря получены\r\n");
    		JSONArray ar = jsonObject.getJSONArray("values");
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
	    		print(sResponse+"\r\n");
	    		exc.printStackTrace();
	    		throw new ExceptFailTest("Не удалось распарсить ответ");
	    	}
		   	return tempJsonObject;
	   }
	
	
}

