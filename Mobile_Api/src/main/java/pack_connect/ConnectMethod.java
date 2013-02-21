package pack_connect;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apache.http.client.utils.URIBuilder;
import com.google.appengine.repackaged.org.json.*;

import pack_utils.ExceptFailTest;
import pack_utils.HM;
import pack_utils.JString;
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
	String mas_TIY2[] = {"make_vacuum", "used-or-new", "vacuumclean_wash", "offertype", "model"};
	String mas_TIY_Mobile[] = {"make_mobile", "used-or-new", "offertype", "model", "corpus_type", "mobile_two_sim_card"};
	String mas_Darom[] = {"mapStreet", "mapHouseNr", "used-or-new", "otdam_za", "pochti_darom", "goodorpet"};
	String mas_Job[] = {"schedule", "car"};
	
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
		
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account");
    	
    	String sE = "email=" + sEmail + "&password=" + sPassword;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequest2(uri, sE);
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/login");
    	
    	String sE =  "username=" + Proper.GetProperty("login_authOP") + "&password=" + Proper.GetProperty("password");
    		
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequest2(uri, sE);
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/login");
    	
    	sE =  "username=" + Proper.GetProperty("login_authIP") + "&password=" + Proper.GetProperty("password");
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	sResponse = HttpPostRequest2(uri, sE);
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/login");
    	
    	sE =  "username=" + Proper.GetProperty("login_authNotExist") + "&password=" + Proper.GetProperty("password");
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	sResponse = HttpPostRequest2(uri, sE);
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/login");
    	
    	sE =  "username=" + Proper.GetProperty("login_authBan") + "&password=" + Proper.GetProperty("password");
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	sResponse = HttpPostRequest2(uri, sE);
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
		builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/login");
		
		sE =  "username=" + Proper.GetProperty("login_authNotActive") + "&password=" + Proper.GetProperty("password");
		
		uri = builder.build();
		print("Отправляем запрос. Uri Запроса: "+ uri.toString());
		sResponse = HttpPostRequest2(uri, sE);
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
	// Редактирование профиля Автотест
	public void GetAndEditProfile(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		JSONObject jTemp, jData;
		String jLogin="", jEmail="";
		String sResponse;
		String sLogin = Proper.GetProperty("login_authOP");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		print("------------------------------------------------------------------------------------------------------------");
		print("Авторизация, редактирование профиля - Тест".toUpperCase()+"\r\n");
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
		
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
		
		String sQuery = CreateArrayRequestForPostAndPut("user_info", hObj.GetStringFromAllHashMap());
		print("user_info = "+ hObj.GetStringFromAllHashMap());
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account");
    		
    	
    	String sE = "auth_token=" + sAuth_token + sQuery;
    	
    	uri = builder.build();
    	
    	print("Отправляем запрос. Uri Запроса: " + uri.toString());
    	
    	sResponse = HttpPutRequest2(uri, sE);
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/restore");
    	
    	String sE = "email=" + Proper.GetProperty("login_authOP");
    
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: " + uri.toString());
    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nНа указанный email отправлено письмо восстановления с инструкцией по восстановлению пароля");
    	else
    	{
    		print("Не удалось восстановить пароль\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
	}
	// Получение ссылки активации Автотест
	public void GetLinkActivasion(String sHost) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		print("------------------------------------------------------------------------------------------------------------");
		print("Получение ссылки активации аккаунта - Тест".toUpperCase()+"\r\n");
		print("Получение ссылки активации аккаунта".toUpperCase());
		print("Параметры для запроса");
		print("login = "+ Proper.GetProperty("login_authNotActive"));
		print("password = "+ Proper.GetProperty("password"));
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/activationkey");
    	
    	String sE = "login=" + Proper.GetProperty("login_authNotActive") + "&password=" + Proper.GetProperty("password");
    
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: " + uri.toString());
    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nНа email пользователя отправлено письмо со ссылкой на активацию");
    	else
    	{
    		print("Не удалось запросить ссылку\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
	}
	// Смена пароля пользователя автотест
	public void ChangePassword(String sHost) throws URISyntaxException, IOException, ExceptFailTest, JSONException, ClassNotFoundException
	{	
		String sAuth_token = "";
		print("------------------------------------------------------------------------------------------------------------");
		print("Авторизация,смена пароля - Тест".toUpperCase()+"\r\n");
		sAuth_token = Authorization_1_1(sHost, Proper.GetProperty("login_changePassword"), Proper.GetProperty("password"));
		
		
		print("Смена пароля пользователя - Тест".toUpperCase());
		print("Смена пароля пользователя".toUpperCase());
		print("Параметры для запроса");
		print("login = "+ Proper.GetProperty("login_changePassword"));
		print("password = "+ Proper.GetProperty("password"));
		print("new password = "+ "retry1");
		print("auth_token = "+ sAuth_token);
		
		String sE = "auth_token=" + sAuth_token + "&old_password=" + Proper.GetProperty("password") + "&new_password=" + "retry1";
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/changepassword");
    	uri = builder.build();
    	
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nпароль для пользователя " + Proper.GetProperty("login_changePassword") + " изменен на retry1");
    		
    	}
    	else
    	{
    		print("Не удалось изменить пароль\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}
    	
    	print("\r\nАвторизуемся пользователем " + Proper.GetProperty("login_changePassword") + " используя новый пароль " + "retry1");
    	Authorization_1_1(sHost, Proper.GetProperty("login_changePassword"), "retry1");
    	
    	print("Изменяем новый пароль retry1 на старый retry2");
    	print("Смена пароля пользователя".toUpperCase());
		print("Параметры для запроса");
		print("login = "+ Proper.GetProperty("login_changePassword"));
		print("password = retry1");
		print("new password = "+ Proper.GetProperty("password"));
		print("auth_token = "+ sAuth_token);
		
		sE = "auth_token=" + sAuth_token + "&old_password=" + "retry1" + "&new_password=" + Proper.GetProperty("password");
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/changepassword");
    	uri = builder.build();
    	
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nпароль для пользователя " + Proper.GetProperty("login_changePassword") + " изменен на " + Proper.GetProperty("password"));
    		
    	}
    	else
    	{
    		print("Не удалось изменить пароль\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}
    	
    	print("\r\nАвторизуемся пользователем " + Proper.GetProperty("login_changePassword") + " используя новый пароль " + Proper.GetProperty("password"));
    	Authorization_1_1(sHost, Proper.GetProperty("login_changePassword"), Proper.GetProperty("password"));
    	
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
	}
	
	
	
	// Подача/Получение/Редактирование объявление ОП Автотест
	public void AddGetEditAdvertOP(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, InterruptedException
	{
		String sIdAuto="", sIdRealt="", sIdTIU="", sIdTIUMobile="", sIdDarom="", sIdJob="";
		String sImageUrlAuto, sImageUrlRealt, sImageUrlTIY, sImageUrlTIYMobile, sImageUrlDarom, sImageUrlJob; 
		String sLogin = Proper.GetProperty("login_authOP");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		HM<String, String> hObj_Auto;
		HM<String, String> hObj_Auto2;
		HM<String, String> hObj_Realt;
		HM<String, String> hObj_Realt2;
		HM<String, String> hObj_TIY;
		HM<String, String> hObj_TIY2;
		HM<String, String> hObj_TIY_Mobile;
		HM<String, String> hObj_TIY_Mobile2;
		HM<String, String> hObj_Darom;
		HM<String, String> hObj_Darom2;
		HM<String, String> hObj_Job;
		HM<String, String> hObj_Job2;
		JSONObject jData;
		InnerDataHM objAuto, objRealt, objTIY, objTIY_Mobile, objDarom, objJob;
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Подача, получение, редактирование объявления ОП - Тест".toUpperCase()+"\r\n");
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
		try
		{
/////////////////////////////////////////////////////////////////////////////////////////////////	
			print("\r\nШАГ №1");
			print("Подача объявления в рубрику Авто с пробегом".toUpperCase());
			objAuto = PostAdvert(sHost, mas_Advertisment, mas_Auto2, sAuth_token, "category_auto", "image");
			sIdAuto = objAuto.GetID();  // сюда сохраняем значение id
			hObj_Auto = objAuto.GetAdvertismentData(); // сюда сохраняем значение массива адветисемент (контакты, title, web, price и т.д. указанные при подаче )  
			hObj_Auto2 = objAuto.GetCustomfieldData(); // сюда сохраняем значение массива кастомфилдов, указанные при подаче
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////  
			print("\r\nШАГ №1-1");
	    	print("Подача объявления в рубрику Недвижимость - Вторичный рынок".toUpperCase());
	    	objRealt = PostAdvert(sHost, mas_Advertisment, mas_Realt2, sAuth_token, "category_realt", "image2");
	    	sIdRealt = objRealt.GetID();
	    	hObj_Realt = objRealt.GetAdvertismentData();
	    	hObj_Realt2 = objRealt.GetCustomfieldData();
	    	
	    	
///////////////////////////////////////////////////////////////////////////////////////////////// 
	    	print("\r\nШАГ №1-2");
	    	print("Подача объявления в рубрику Электроника и техника - Пылесосы".toUpperCase());
	    	objTIY = PostAdvert(sHost, mas_Advertisment, mas_TIY2, sAuth_token, "category_electron", "image3");
	    	sIdTIU = objTIY.GetID();
	    	hObj_TIY = objTIY.GetAdvertismentData();
	    	hObj_TIY2 = objTIY.GetCustomfieldData();
	    
/////////////////////////////////////////////////////////////////////////////////////////////////	
	    	print("\r\nШАГ №1-3");
	    	print("Подача объявления в рубрику Телефоны и связь - Мобильные телефоны".toUpperCase());
	    	objTIY_Mobile = PostAdvert(sHost, mas_Advertisment, mas_TIY_Mobile, sAuth_token, "category_mobile", "image5");
	    	sIdTIUMobile = objTIY_Mobile.GetID();
	    	hObj_TIY_Mobile = objTIY_Mobile.GetAdvertismentData();
	    	hObj_TIY_Mobile2 = objTIY_Mobile.GetCustomfieldData();
	    	
	    	
/////////////////////////////////////////////////////////////////////////////////////////////////	
			print("\r\nШАГ №1-4");
			print("Подача объявления в рубрику Отдам даром".toUpperCase());
			objDarom = PostAdvert(sHost, mas_Advertisment, mas_Darom, sAuth_token, "category_darom", "image6");
			sIdDarom = objDarom.GetID();
			hObj_Darom = objDarom.GetAdvertismentData();
			hObj_Darom2 = objDarom.GetCustomfieldData();


/////////////////////////////////////////////////////////////////////////////////////////////////	
			print("\r\nШАГ №1-5");
			print("Подача объявления в рубрику Работа и образование - Резюме(Бытовые и коммунальные услуги, муниципалитет)".toUpperCase());
			objJob = PostAdvert(sHost, mas_Advertisment, mas_Job, sAuth_token, "category_jobs", "image7");
			sIdJob = objJob.GetID();
			hObj_Job = objJob.GetAdvertismentData();
			hObj_Job2 = objJob.GetCustomfieldData();


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    	print("\r\nШАГ №2");
	    	jData = GetAdvert(sHost, sIdAuto, "Авто с пробегом");
	    	print("Проверяем корректность указанных данных при подаче объявления");
	    	sImageUrlAuto = ValidateDataFromAdvertAfterPost(mas_Advertisment, mas_Auto2, hObj_Auto, hObj_Auto2, jData);
			print("");
	    	
			print("\r\nШАГ №2-1");
			jData = GetAdvert(sHost, sIdRealt, "Вторичный рынок");
	    	print("Проверяем корректность указанных данных при подаче объявления");
	    	sImageUrlRealt = ValidateDataFromAdvertAfterPost(mas_Advertisment, mas_Realt2, hObj_Realt, hObj_Realt2, jData);
			print("");
			
			print("\r\nШАГ №2-2");
			jData = GetAdvert(sHost, sIdTIU, "Пылесосы");
	    	print("Проверяем корректность указанных данных при подаче объявления");
	    	sImageUrlTIY = ValidateDataFromAdvertAfterPost(mas_Advertisment, mas_TIY2, hObj_TIY, hObj_TIY2, jData);
			print("");
			
			print("\r\nШАГ №2-3");
			jData = GetAdvert(sHost, sIdTIUMobile, "Мобильные телефоны");
	    	print("Проверяем корректность указанных данных при подаче объявления");
	    	sImageUrlTIYMobile = ValidateDataFromAdvertAfterPost(mas_Advertisment, mas_TIY_Mobile, hObj_TIY_Mobile, hObj_TIY_Mobile2, jData);
			print("");
			
			print("\r\nШАГ №2-4");
			jData = GetAdvert(sHost, sIdDarom, "Отдам даром");
	    	print("Проверяем корректность указанных данных при подаче объявления");
	    	sImageUrlDarom = ValidateDataFromAdvertAfterPost(mas_Advertisment, mas_Darom, hObj_Darom, hObj_Darom2, jData);
			print("");
			
			print("\r\nШАГ №2-5");
			jData = GetAdvert(sHost, sIdJob, "Резюме - Бытовые и коммунальные услуги, муниципалитет");
	    	print("Проверяем корректность указанных данных при подаче объявления");
	    	sImageUrlJob = ValidateDataFromAdvertAfterPost(mas_Advertisment, mas_Job, hObj_Job, hObj_Job2, jData);
			print("");
			
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			print("\r\nШАГ №3");
			print("\r\nРедактирование объявления. Авто с пробегом".toUpperCase());
			objAuto = EditAdvert(sHost, mas_Advertisment, mas_Auto2, objAuto, sAuth_token, sImageUrlAuto, "category_auto");
			sIdAuto = objAuto.GetID(); // сюда сохраняем значение id
			hObj_Auto = objAuto.GetAdvertismentData(); // сюда сохраняем значение массива адветисемент (контакты, title, web, price и т.д. указанные при редактировании )  
			hObj_Auto2 = objAuto.GetCustomfieldData(); // сюда сохраняем значение массива кастомфилдов, указанные при редактировании
			ValidateDataFromAdvertAfterEdit(mas_Advertisment, mas_Auto2, hObj_Auto, hObj_Auto2);
			
			print("\r\nШАГ №3-1");
			print("\r\nРедактирование объявления. Вторичный рынок".toUpperCase());
			objRealt = EditAdvert(sHost, mas_Advertisment, mas_Realt2, objRealt, sAuth_token, sImageUrlRealt, "category_realt");
			sIdRealt = objRealt.GetID(); // сюда сохраняем значение id
			hObj_Realt = objRealt.GetAdvertismentData(); // сюда сохраняем значение массива адветисемент (контакты, title, web, price и т.д. указанные при редактировании )  
			hObj_Realt2 = objRealt.GetCustomfieldData(); // сюда сохраняем значение массива кастомфилдов, указанные при редактировании
			ValidateDataFromAdvertAfterEdit(mas_Advertisment, mas_Realt2, hObj_Realt, hObj_Realt2);
			
			print("\r\nШАГ №3-2");
			print("\r\nРедактирование объявления. Пылесосы".toUpperCase());
			objTIY = EditAdvert(sHost, mas_Advertisment, mas_TIY2, objTIY, sAuth_token, sImageUrlTIY, "category_electron");
			sIdTIU = objTIY.GetID(); // сюда сохраняем значение id
			hObj_TIY = objTIY.GetAdvertismentData(); // сюда сохраняем значение массива адветисемент (контакты, title, web, price и т.д. указанные при редактировании )  
			hObj_TIY2 = objTIY.GetCustomfieldData(); // сюда сохраняем значение массива кастомфилдов, указанные при редактировании
			ValidateDataFromAdvertAfterEdit(mas_Advertisment, mas_TIY2, hObj_TIY, hObj_TIY2);
			
			print("\r\nШАГ №3-3");
			print("\r\nРедактирование объявления. Мобильные телефоны".toUpperCase());
			objTIY_Mobile = EditAdvert(sHost, mas_Advertisment, mas_TIY_Mobile, objTIY_Mobile, sAuth_token, sImageUrlTIYMobile, "category_mobile");
			sIdTIUMobile = objTIY_Mobile.GetID(); // сюда сохраняем значение id
			hObj_TIY_Mobile = objTIY_Mobile.GetAdvertismentData(); // сюда сохраняем значение массива адветисемент (контакты, title, web, price и т.д. указанные при редактировании )  
			hObj_TIY_Mobile2 = objTIY_Mobile.GetCustomfieldData(); // сюда сохраняем значение массива кастомфилдов, указанные при редактировании
			ValidateDataFromAdvertAfterEdit(mas_Advertisment, mas_TIY_Mobile, hObj_TIY_Mobile, hObj_TIY_Mobile2);
			
			
			print("\r\nШАГ №3-4");
			print("\r\nРедактирование объявления. Отдам даром".toUpperCase());
			objDarom = EditAdvert(sHost, mas_Advertisment, mas_Darom, objDarom, sAuth_token, sImageUrlDarom, "category_darom");
			sIdDarom = objDarom.GetID(); // сюда сохраняем значение id
			hObj_Darom = objDarom.GetAdvertismentData(); // сюда сохраняем значение массива адветисемент (контакты, title, web, price и т.д. указанные при редактировании )  
			hObj_Darom2 = objDarom.GetCustomfieldData(); // сюда сохраняем значение массива кастомфилдов, указанные при редактировании
			ValidateDataFromAdvertAfterEdit(mas_Advertisment, mas_Darom, hObj_Darom, hObj_Darom2);
			
			print("\r\nШАГ №3-5");
			print("\r\nРедактирование объявления. Резюме - Бытовые и коммунальные услуги, муниципалитет".toUpperCase());
			objJob = EditAdvert(sHost, mas_Advertisment, mas_Job, objJob, sAuth_token, sImageUrlJob, "category_jobs");
			sIdJob = objJob.GetID(); // сюда сохраняем значение id
			hObj_Job = objJob.GetAdvertismentData(); // сюда сохраняем значение массива адветисемент (контакты, title, web, price и т.д. указанные при редактировании )  
			hObj_Job2 = objJob.GetCustomfieldData(); // сюда сохраняем значение массива кастомфилдов, указанные при редактировании
			ValidateDataFromAdvertAfterEdit(mas_Advertisment, mas_Job, hObj_Job, hObj_Job2);
		
///////////////////////////////////////////////////////////////////////////////////////
			Sleep(10000);
		}
		finally
		{
			print("\r\nУдаляем поданные объявления");
			DeleteAdvert(sHost, sAuth_token, sIdAuto);
			DeleteAdvert(sHost, sAuth_token, sIdRealt);
			DeleteAdvert(sHost, sAuth_token, sIdTIU);
			DeleteAdvert(sHost, sAuth_token, sIdTIUMobile);
			DeleteAdvert(sHost, sAuth_token, sIdDarom);
			DeleteAdvert(sHost, sAuth_token, sIdJob);
		}
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
    	
	}
	// редактирование объявления автотест
	private InnerDataHM EditAdvert(String sHost, String sMas_Adv[], String sMas_Cust[], InnerDataHM obj_old,  String sAuth_token, String sUrlImage, String sCategoryData) throws URISyntaxException, IOException, ExceptFailTest, JSONException
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
			if(sMas_Adv[i].equals("currency")) // если редактируем валюту то для нее только RUR
				hObj_Adv_New.SetValue(sMas_Adv[i], RamdomData.GetRandomData(Proper.GetProperty(sMas_Adv[i]), ""));	
			else
				hObj_Adv_New.SetValue(sMas_Adv[i], RamdomData.GetRandomData(Proper.GetProperty(sMas_Adv[i]), obj_old.GetAdvertismentData().GetValue(sMas_Adv[i])));
		}
		String sRequest1 = CreateArrayRequestForPostAndPut("advertisement",  hObj_Adv_New.GetStringFromAllHashMap());

		// генерим advertisement [custom_fields]
		HM<String, String> hObj_Cust_New = new HM<String, String>(); 
		for(int i=0; i<sMas_Cust.length; i++)
		{
			if(sMas_Cust[i].equals("make_mobile") || sMas_Cust[i].equals("make_vacuum"))
				hObj_Cust_New.SetValue("make", RamdomData.GetRandomData(Proper.GetProperty(sMas_Cust[i]), obj_old.GetCustomfieldData().GetValue("make")));
			else
				hObj_Cust_New.SetValue(sMas_Cust[i], RamdomData.GetRandomData(Proper.GetProperty(sMas_Cust[i]), obj_old.GetCustomfieldData().GetValue(sMas_Cust[i])));
		}
		hObj_Cust_New.PrintKeyAndValue();
		String sRequest2 = CreateDoubleArrayRequestForPostAndPut("advertisement", "custom_fields",  hObj_Cust_New.GetStringFromAllHashMap());
		
		builder = new URIBuilder();
		builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/"+ obj_old.GetID());
		
		String sE="";
    	if(sCategoryData.equals("category_jobs")) // для проверки [0], [1] массивов
    		sE = "auth_token=" + sAuth_token + sRequest1 + sRequest2 + sVideo + "&deleted_images[0]=" + sUrlImage
    		+ Proper.GetProperty("job_special_edit")/* + Proper.GetProperty("language_edit") + Proper.GetProperty("language2_edit")*/;
    	else
    		sE = "auth_token=" + sAuth_token + sRequest1 + sRequest2 + sVideo + "&deleted_images[0]=" + sUrlImage;
		
		uri = builder.build();
		print("Отправляем запрос. Uri Запроса: " + uri.toString());
		
    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("\r\nОтвет сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление отредактировано");
    		obj_after_edit = new InnerDataHM(hObj_Adv_New, hObj_Cust_New, sId); // сохраняем значения данных после редактирования
    		return obj_after_edit;
    	}
    	else
    	{
    		print("Не удалось отредактировать объявление\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
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
			sRequest = CreateSimpleRequestForPostAndPut(Proper.GetProperty(sCategoryData)); //category_auto
			
			//генерим advertisement 
			HM<String, String> hObj_Adv = new HM<String, String>(); //здесь будем хранить {param=value} для advertisement
			for(int i=0; i<sMas_Adv.length; i++)
			{
				hObj_Adv.SetValue(sMas_Adv[i], RamdomData.GetRandomData(Proper.GetProperty(sMas_Adv[i]), ""));
			}
			sRequest1 = CreateArrayRequestForPostAndPut("advertisement",  hObj_Adv.GetStringFromAllHashMap());
			
			// генерим advertisement [custom_fields]
			HM<String, String> hObj_Cust = new HM<String, String>();  //здесь будем хранить {param=value} для advertisement [customfields]
			for(int i=0; i<sMas_Cust.length; i++)
			{
				if(sMas_Cust[i].equals("make_mobile") || sMas_Cust[i].equals("make_vacuum"))
					hObj_Cust.SetValue("make", RamdomData.GetRandomData(Proper.GetProperty(sMas_Cust[i]), ""));
				else
					hObj_Cust.SetValue(sMas_Cust[i], RamdomData.GetRandomData(Proper.GetProperty(sMas_Cust[i]), ""));
			}
			hObj_Cust.PrintKeyAndValue();
			sRequest2 = CreateDoubleArrayRequestForPostAndPut("advertisement", "custom_fields",  hObj_Cust.GetStringFromAllHashMap());
			
			builder = new URIBuilder();
	    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert");
	    		
	    	String sE="";
	    	if(sCategoryData.equals("category_jobs")) // для проверки [0], [1] массивов
	    		sE = "auth_token=" + sAuth_token + sRequest + sRequest1 + sRequest2 + sVideo + Proper.GetProperty("job_special")// +
	    				/*Proper.GetProperty("language") + Proper.GetProperty("language2")*/;
	    	else
	    		sE = "auth_token=" + sAuth_token + sRequest + sRequest1 + sRequest2 + sVideo;
	    		
	    	uri = builder.build();
	    	
	    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
	    	String sResponse = HttpPostRequestImage2(uri, Proper.GetProperty(sImage), sE);
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
	    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
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
			if(mas_Cust[i].equals("make_mobile") || mas_Cust[i].equals("make_vacuum"))
			{
				if(obj_Cust.GetValue("make").equals(objHM.GetValue("make")))
				{
					print("Значение " + mas_Cust[i] +" = " + obj_Cust.GetValue("make") + " указанное для при редактировании объявления," +
							" совпало со значение после получения данного объявления " + mas_Cust[i] + " = " + objHM.GetValue("make"));		
				}
				else
				{
					print("Значение " + mas_Cust[i] +" = " + obj_Cust.GetValue("make") + " указанное для при редактировании объявления," +
							" не совпало со значение после получения данного объявления " + mas_Cust[i] + " = " + objHM.GetValue("make"));	
					print("Тест провален".toUpperCase());
		    		throw new ExceptFailTest("Тест провален");
				}
			}
			else
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
		}	
		
		
		// если рубрика работа и образование то мы должны проверить значения специализации и языка (кастыль)
		jTemp = jsonObject.getJSONObject("advertisement");
		if(jTemp.getString("category").equals("Бытовые и коммунальные услуги, муниципалитет"))
		{
			if(objHM.GetValue("job_specialization").equals("строительство;экология"))
				print("Значение job_specialization = \"строительство;экология\", указанное при подаче объявления," +
						" совпало со значением, после получения данного объявления job_specialization = " + objHM.GetValue("job_specialization"));
			else 
			{	
				print("Значение job_specialization = \"строительство;экология\", указанное при подаче объявления," +
						" не совпало со значением, после получения данного объявления job_specialization = " + objHM.GetValue("job_specialization"));
				throw new ExceptFailTest("Тест провален");
			}
			
			if(objHM.GetValue("language_type").equals("немецкий;испанский"))
				print("Значение language_type = \"немецкий;испанский\", указанное при подаче объявления," +
						" совпало со значением, после получения данного объявления language_type = " + objHM.GetValue("language_type"));
			else 
			{	
				print("Значение language_type = \"немецкий;испанский\", указанное при подаче объявления," +
						" не совпало со значением, после получения данного объявления language_type = " + objHM.GetValue("language_type"));
				throw new ExceptFailTest("Тест провален");
			}
			
			if(objHM.GetValue("study_level").equals("свободный;свободный"))
				print("Значение study_level = \"свободный;свободный\", указанное при подаче объявления," +
						" совпало со значением, после получения данного объявления study_level = " + objHM.GetValue("study_level"));
			else 
			{	
				print("Значение study_level = \"свободный;свободный\", указанное при подаче объявления," +
						" не совпало со значением, после получения данного объявления study_level = " + objHM.GetValue("study_level"));
				throw new ExceptFailTest("Тест провален");
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
			
			if(mas_Cust[i].equals("make_mobile") || mas_Cust[i].equals("make_vacuum"))
			{
				if(obj_Cust.GetValue("make").equals(objHM.GetValue("make")))
				{
					print("Значение " + mas_Cust[i] +" = " + obj_Cust.GetValue("make") + " указанное при подаче объявления," +
							" совпало со значение после получения данного объявления " + mas_Cust[i] + " = " + objHM.GetValue("make"));		
				}
				else
				{
					print("Значение " + mas_Cust[i] +" = " + obj_Cust.GetValue("make") + " указанное при подаче объявления," +
							" не совпало со значение после получения данного объявления " + mas_Cust[i] + " = " + objHM.GetValue("make"));	
					print("Тест провален".toUpperCase());
		    		throw new ExceptFailTest("Тест провален");
				}
			}
			else
			{
				if(obj_Cust.GetValue(mas_Cust[i]).equals(objHM.GetValue(mas_Cust[i])))
				{
					print("Значение " + mas_Cust[i] +" = " + obj_Cust.GetValue(mas_Cust[i]) + " указанное при подаче объявления," +
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
						print("Значение " + mas_Cust[i] +" = " + obj_Cust.GetValue(mas_Cust[i]) + " указанное  при подаче объявления," +
								" не совпало со значение после получения данного объявления " + mas_Cust[i] + " = " + objHM.GetValue(mas_Cust[i]));	
						print("Тест провален".toUpperCase());
			    		throw new ExceptFailTest("Тест провален");
					}
				}
			}
		
		}
		// если рубрика работа и образование то мы должны проверить значения специализации и языка (кастыль)
		jTemp = jObj.getJSONObject("advertisement");
		if(jTemp.getString("category").equals("Бытовые и коммунальные услуги, муниципалитет"))
		{
			if(objHM.GetValue("job_specialization").equals("архитектура;дизайн"))
				print("Значение job_specialization = \"архитектура;дизайн\", указанное при подаче объявления," +
						" совпало со значением, после получения данного объявления job_specialization = " + objHM.GetValue("job_specialization"));
			else 
			{	
				print("Значение job_specialization = \"архитектура;дизайн\", указанное при подаче объявления," +
						" не совпало со значением, после получения данного объявления job_specialization = " + objHM.GetValue("job_specialization"));
				throw new ExceptFailTest("Тест провален");
			}
			
			if(objHM.GetValue("language_type").equals("английский;греческий"))
				print("Значение language_type = \"английский;греческий\", указанное при подаче объявления," +
						" совпало со значением, после получения данного объявления language_type = " + objHM.GetValue("language_type"));
			else 
			{	
				print("Значение language_type = \"английский;греческий\", указанное при подаче объявления," +
						" не совпало со значением, после получения данного объявления language_type = " + objHM.GetValue("language_type"));
				throw new ExceptFailTest("Тест провален");
			}
			
			if(objHM.GetValue("study_level").equals("базовый;базовый"))
				print("Значение study_level = \"английский;греческий\", указанное при подаче объявления," +
						" совпало со значением, после получения данного объявления study_level = " + objHM.GetValue("study_level"));
			else 
			{	
				print("Значение study_level = \"базовый;базовый\", указанное при подаче объявления," +
						" не совпало со значением, после получения данного объявления study_level = " + objHM.GetValue("study_level"));
				throw new ExceptFailTest("Тест провален");
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

	
	//Попытка Подачи/редактирования/деактивации/активация/продление/поднятие/выделение/премиум/удаления ИП 
	public void AddActivateDeactivateProlongPushUpHighLightPremiumIP(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, InterruptedException
	{
		String sLogin = Proper.GetProperty("login_authIP");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		JSONObject jData, jDataPostAsvert;
		String sIdAdvert;
		int nCountAdvert, nCountAdvert_after;
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Попытка подачи, редактирования, активации, деактивации, выделения, продления, поднятия, назначения премиум ИП - Тест".toUpperCase()+"\r\n");
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
//////////////////////////////////////////////////////////////////	
		print("\r\nШаг 1".toUpperCase());
		print("Запоминаем количество объявлений в ЛК ИП до подачи объявления".toUpperCase());
		print("Получаем листинг объявлений в ЛК (Данному пользователю для проведения теста поданно одно объявления с фронта сайта)");
		jData = GetListOwnAdvert(sHost, sAuth_token);
		print("Получаем количество объявлений в листинге ЛК до подачи");
		nCountAdvert = GetCountAdvertListLK(jData);
		print("У пользователя " + sLogin +" в ЛК " + nCountAdvert + " объявление(ий)");
		
//////////////////////////////////////////////////////////////////		
		
		print("\r\nШаг 2".toUpperCase());
		print("Попытка подачи объявления в рубрику Авто с пробегом ИП".toUpperCase());
		print("Подаем объявление");
		PostAdvertIP(sHost, mas_Advertisment, mas_Auto2, sAuth_token, "category_auto", "image");
		
		print("\r\nПроверяем количество объявлений в ЛК ИП после попытки подачи");
		print("\r\nПолучаем листинг объявлений в ЛК (Данному пользователю для проведения теста поданно одно объявления с фронта сайта)".toUpperCase());
		jData = GetListOwnAdvert(sHost, sAuth_token);
		print("Получаем количество объявлений в листинге ЛК после подачи");
		nCountAdvert_after = GetCountAdvertListLK(jData);
		if(nCountAdvert == nCountAdvert_after)
			print("У пользователя " + sLogin +" после попытки подачи объявления в ЛК " + nCountAdvert_after + " объявление(ий). Корректно. Осталось столько же как и до попытки подачи");
		else
		{
			print("У пользователя " + sLogin +" после попытки подачи объявления в ЛК " + nCountAdvert_after + " объявление(ий). Изменилось количество объявлений после попытки подачи");
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");	
		}
//////////////////////////////////////////////////////////////////		
		
		print("\r\nШаг 3".toUpperCase());
		print("Получаем ID первого объявления в листинге ЛК ИП".toUpperCase());
		JSONArray ar = jData.getJSONArray("advertisements");
		jData = (JSONObject) ar.get(0);
		sIdAdvert = jData.getString("id");
		print("ID объявления для которого будут производится попытки манипуляций = " + sIdAdvert);
		
//////////////////////////////////////////////////////////////////		
		
		print("\r\nШаг 4".toUpperCase());
		print("Попытка редактирования объявления".toUpperCase());
		print("Редактируем объявление с ID = " + sIdAdvert);
		EditAdvertIP(sHost, sAuth_token, sIdAdvert);
		
//////////////////////////////////////////////////////////////////		
		
		print("\r\nШаг 5".toUpperCase());
		print("Попытка деактивации объявления".toUpperCase());
		print("Деактивируем объявление с ID = " + sIdAdvert);
		DeactivateAdvert(sHost, sAuth_token, sIdAdvert, 2);
		
		print("\r\nПолучаем объявление с ID = " + sIdAdvert);
    	jData = GetAdvert(sHost, sIdAdvert,  "Авто - б/у" );
		print("Проверяяем статус объявления. Которое мы пытались деактивировать");
		ValidateStatus("1", jData, sIdAdvert, " после попытки деактивации");
		
//////////////////////////////////////////////////////////////////	
		
		print("\r\nШаг 6".toUpperCase());
		print("Попытка продления объявления".toUpperCase());
		
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Запоминаем время окончания размещения объявления");
    	jDataPostAsvert = GetAdvert(sHost, sIdAdvert,  "Авто - б/у" );// запоминаем json объект в нем время окончания размещения сраз после подачи
    	
    	print("\r\nПродлеваем объявление с ID = " + sIdAdvert +  " для пользователя " + sLogin);
    	ProlongAdvert(sHost, sAuth_token, sIdAdvert, true, 2);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Проверяем значение времени окончания размещения объявления после продления");
    	jData = GetAdvert(sHost, sIdAdvert,  "Авто - б/у" );
    	print("Сравниваем время окончания размещения объявления до и после продления");
    	ValidateDateFinishAdvert(jDataPostAsvert, jData, 2);   	
    	
//////////////////////////////////////////////////////////////////		
    	
    	print("\r\nШАГ 7");
    	print("Попытка поднятия объявления".toUpperCase());
    	print("\r\nПодымаем  объявление с ID = " + sIdAdvert );
    	PushUpAdvert(sHost, sAuth_token, sIdAdvert, true, 2);
    	
//////////////////////////////////////////////////////////////////
    	
    	// выделение объявления
    	print("\r\nШАГ 8");
    	print("Попытка выделения объявление".toUpperCase());
    	print("\r\nВыделяем объявление с ID = " + sIdAdvert);
    	HighLightAdvert(sHost, sAuth_token, sIdAdvert, true, 2);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Проверяем значение статуса выделения объявления");
    	jData = GetAdvert(sHost, sIdAdvert,  "Авто - б/у" );
    	
    	print("\r\nПроверяем статус выделения для объявления с ID = " + sIdAdvert + " после попытки выделения объявления");
    	ValidateHighLight("0", jData, sIdAdvert, " после попытки выделения объявления");
    	
//////////////////////////////////////////////////////////////////
    	
    	print("\r\nШАГ 9");
    	print("Попытка назначения премиум объявлению".toUpperCase());
    	print("\r\nНазначаем премиум объявлению с ID = " + sIdAdvert);
    	SetPremiumAdvert(sHost, sAuth_token, sIdAdvert, true, 2);
    	
    	Sleep(2000);
    	
    	print("\r\nПолучаем объявление с ID = " + sIdAdvert + " Проверяем значение статуса премиум для объявления");
    	jData = GetAdvert(sHost, sIdAdvert,  "Недвижимость - Вторичный рынок" );
    	
    	print("\r\nПроверяем статус премиум для объявления с ID = " + sIdAdvert + " после попытки назначения премиума объявлению");
    	ValidatePremiun("false", jData, sIdAdvert, " после попытки назначения премиума объявлению");
    	
//////////////////////////////////////////////////////////////////
    	
    	print("\r\nШАГ 10");
    	print("Попытка удаления объявления".toUpperCase());
    	print("\r\nУдаляем объявление с ID = " + sIdAdvert);
    	DeleteAdvertIP(sHost, sAuth_token, sIdAdvert);
    	
    	print("\r\nПроверяем количество объявлений в ЛК ИП после попытки удаления");
		print("\r\nПолучаем листинг объявлений в ЛК".toUpperCase());
		jData = GetListOwnAdvert(sHost, sAuth_token);
		print("Получаем количество объявлений в листинге ЛК после попытки удаления");
		nCountAdvert_after = GetCountAdvertListLK(jData);
		if(nCountAdvert == nCountAdvert_after)
			print("У пользователя " + sLogin +" после попытки удаления объявления в ЛК " + nCountAdvert_after + " объявление(ий). Корректно. Осталось столько же как и до попытки удаления");
		else
		{
			print("У пользователя " + sLogin +" после попытки удаления объявления в ЛК " + nCountAdvert_after + " объявление(ий). Изменилось количество объявлений после попытки удаления");
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");	
		}
		
		print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
	
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
		sRequest = CreateSimpleRequestForPostAndPut(Proper.GetProperty(sCategoryData)); //category_auto
		
		//генерим advertisement 
		HM<String, String> hObj_Adv = new HM<String, String>(); //здесь будем хранить {param=value} для advertisement
		for(int i=0; i<sMas_Adv.length; i++)
		{
			hObj_Adv.SetValue(sMas_Adv[i], RamdomData.GetRandomData(Proper.GetProperty(sMas_Adv[i]), ""));
		}
		sRequest1 = CreateArrayRequestForPostAndPut("advertisement",  hObj_Adv.GetStringFromAllHashMap());
		
		// генерим advertisement [custom_fields]
		HM<String, String> hObj_Cust = new HM<String, String>();  //здесь будем хранить {param=value} для advertisement [customfields]
		for(int i=0; i<sMas_Cust.length; i++)
		{
				hObj_Cust.SetValue(sMas_Cust[i], RamdomData.GetRandomData(Proper.GetProperty(sMas_Cust[i]), ""));
		}
		hObj_Cust.PrintKeyAndValue();
		sRequest2 = CreateDoubleArrayRequestForPostAndPut("advertisement", "custom_fields",  hObj_Cust.GetStringFromAllHashMap());
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert");
    	
    	String sE = "auth_token=" + sAuth_token + sRequest + sRequest1 + sRequest2 + sVideo;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: " + uri.toString());
    	String sResponse = HttpPostRequestImage2(uri, Proper.GetProperty(sImage), sE);
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
    				"Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\n");
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
	// редактирование для ИП для автотестов
	private void EditAdvertIP(String sHost, String sAuth_token, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		
		String sAdvertisement = "{title=Тайтл}";
		String sCustom_fields = "{make=BMW, model=116}";
		
		print("Редактирование объявления ИП".toUpperCase());
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("ADVERTISEMENT_ID = "+ sIdAdvert);
		
		String sRequest = CreateDoubleArrayRequestForPostAndPut("advertisement", "custom_fields", sCustom_fields);
		String sRequest1 = CreateArrayRequestForPostAndPut("advertisement" , sAdvertisement);
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/"+ sIdAdvert);
    	
    	String sE = "auth_token=" + sAuth_token + sRequest1 + sRequest;
    	
    	uri = builder.build();
    	
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPutRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("\r\nОтвет сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление отредактировано. Но ИП партнер не имеет право редактировать объявления.");
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");	
    	}
    		
    	else
    	{
    		print("Не удалось отредактировать объявление\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		print("Корректно. Так как ИП не имеет право редактировать свои объявления");
    	}
	}
	// удаление объявление ИП для автотестов
	private void DeleteAdvertIP(String sHost, String sAuth_token, String sIdAdvert) throws URISyntaxException, ExceptFailTest, IOException, JSONException
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
    	{
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление было удалено. Но ИП партнер не имеет право удалять объявления.");
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}
    	else
    	{
    		print("Не удалось удалить объявление c ID = "+ sIdAdvert +"\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		print("Корректно. Так как ИП не имеет право удалять свои объявления");;
    	}	
	}
	
	
	// Подача/Получение листинга ЛК/Удаление
	public void AddGetListDeleteOP(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		String sIdAdvert=""; 
		String sLogin = Proper.GetProperty("login_authOP");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		JSONObject jData;
		
		InnerDataHM objRealt;
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Подача,получение листинга ЛК, удаление объявления ОП - Тест".toUpperCase()+"\r\n");
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
		try
		{
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
		}
		finally
		{
			print("\r\nУдаляем поданное объявление(если тест провалился)");
			DeleteAdvert(sHost, sAuth_token, sIdAdvert);
		}
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
		String sIdAdvert=""; 
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
		try
		{
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
		}
		finally
		{
			print("\r\nУдаляем поданное объявление");
			DeleteAdvert(sHost, sAuth_token, sIdAdvert);
		}
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert +"/favorite");
    	
    	String sE = "auth_token=" + sAuth_token;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление c ID = " + sIdAdvert + " добавлено в избранное");
    	else
    	{
    		print("Не удалось добавить объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert +"/favorite");
    	
    	String sE = "auth_token=" + sAuth_token;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: " + uri.toString());
    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление c ID = " + sIdAdvert + " добавлено в избранное");
    		print("Но это собственное объявление пользователя. Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}
    	else
    	{
    		print("Не удалось добавить объявление \r\n Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nКорректно. Так как это собственное объявление пользователя");
    	}	
	}
	
	
	//Подача в бесплатную/деактивация/активация/Продление/Поднятие/Выделение/Назначение премиум/Получение листинга категории и проверка его
	public void AddDeactivateActivateProlongPushupHighlightPremiumOPFreeAdvert(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, InterruptedException
	{
		String sIdAdvert="", sIdAdvert2=""; 
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
		try
		{
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
	    	DeactivateAdvert(sHost, sAuth_token, sIdAdvert, 1);
	    	
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
		}
		finally
		{
	    	//удаляем поданные обяъвления
	    	print("\r\nШАГ 12");
	    	print("Удаляем поданные объявления".toUpperCase());
	    	DeleteAdvert(sHost, sAuth_token, sIdAdvert);
	    	DeleteAdvert(sHost, sAuth_token, sIdAdvert2);
		}
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
	}
	// деактивация объявления для автотеста
	private void DeactivateAdvert(String sHost, String sAuth_token, String sIdAdvert, int nResult) throws URISyntaxException, ExceptFailTest, IOException, JSONException
	{
		print("\r\nДеактивация объявления".toUpperCase());
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("ADVERTISEMENT_ID = "+ sIdAdvert);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/deactivate");
    	
    	String sE = "auth_token=" + sAuth_token;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: " + uri.toString());
    	

    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	switch(nResult)
    	{
    		case 1: // Для ОП
		    	if(jsonObject.isNull("error"))
		    		print("Ответ сервера:" + jsonObject.toString(10) + " Объявление деактивировано");
		    	else
		    	{
		    		print("Не удалось деактивировать объявление \r\n"+
		    				"Ответ сервера:\r\n"+ jsonObject.toString());
		    		throw new ExceptFailTest("Тест провален");
		    	}	
		    	break;
    		case 2: // Для ИП
    			if(jsonObject.isNull("error"))
    			{
    				print("Ответ сервера:" + jsonObject.toString(10) + " Объявление деактивировано");
    				print("Нельзя деактивировать объявление ИП");
    				print("Тест провален".toUpperCase());
    				throw new ExceptFailTest("Тест провален");
    			}
		    	else
		    	{
		    		print("Не удалось деактивировать объявление \r\n"+
		    				"Ответ сервера:\r\n"+ jsonObject.toString());
		    		print("Корректно. Нельзя выполнять никаких действий в ЛК ИП над объявления");
		    		
		    	}	
		    	break;
		    	
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
					print("Объявление не продлено, время при подаче объявления " + sDateFinish + " время после попытки продления объявления " + sDateFinish2 + " Корректно. Так как ключ оплаты не передавался/или объявление принадлежит ИП");
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/activate");
    		
    	String sE = "auth_token=" + sAuth_token + "&app_token=" + sApp_token;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: " + uri.toString());
    	
    	String sResponse = HttpPostRequest2(uri, sE);
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/prolong");
    		
    	String sE = "auth_token=" + sAuth_token + "&app_token=" + sApp_token;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpPostRequest2(uri, sE);
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
		    			print("Объявление не должно было продлиться, так как ключ оплаты передан не был/или объявление принадлежит ИП");
		    			print("Тест провален");
		    			throw new ExceptFailTest("Тест провален");
    				}
    				else
    				{
    					print("Не удалось продлить объявление \r\n"+
    		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		    		print("Объявление не продленно, так как ключ оплаты передан не был/или объявление принадлежит ИП. Корректно");
    				}
		    	}
		    	else
		    	{
		    		print("Не удалось продлить объявление \r\n"+
		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    		print("Объявление не продленно, так как ключ оплаты передан не был/или объявление принадлежит ИП. Корректно");
		    		
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/pushup");
    		
    	String sE = "auth_token=" + sAuth_token + "&app_token=" + sApp_token;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: " + uri.toString());

    	String sResponse = HttpPostRequest2(uri, sE);
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
		    			print("Объявление не должно было подняться, так как ключ оплаты передан не был/или объявление принадлежит ИП");
		    			print("Тест провален");
		    			throw new ExceptFailTest("Тест провален");
		    	}
		    	else
		    	{
		    		print("Не удалось поднять объявление \r\n"+
		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    		print("Объявление не поднялось, так как ключ оплаты передан не был/или объявление принадлежит ИП. Корректно");
		    		
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
    			print("Объявление №" + (i+1));
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/highlight");
    		
    	String sE = "auth_token=" + sAuth_token + "&app_token=" + sApp_token;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: " + uri.toString());
    	
    	String sResponse = HttpPostRequest2(uri, sE);
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
		    			print("Объявление не должно было выделиться, так как ключ оплаты передан не был/или объявление принадлежит ИП");
		    			print("Тест провален");
		    			throw new ExceptFailTest("Тест провален");
    				}
    				else
    				{
    					print("Не удалось выделить объявление \r\n"+
    		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		    		print("Объявление не выделилось, так как ключ оплаты передан не был/или объявление принадлежит ИП. Корректно");
    				}
		    	}
		    	else
		    	{
		    		print("Не удалось выделить объявление \r\n"+
		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    		print("Объявление не выделилось, так как ключ оплаты передан не был/или объявление принадлежит ИП. Корректно");
		    		
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/premium");
    	
    	String sE = "auth_token=" + sAuth_token + "&app_token=" + sApp_token + "&number=7" ;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: " + uri.toString());
    	
    	String sResponse = HttpPostRequest2(uri, sE);
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
		    			print("Объявлению не должен было стать премиумом, так как ключ оплаты передан не был/или объявление принадлежит ИП");
		    			print("Тест провален");
		    			throw new ExceptFailTest("Тест провален");
    				}
    				else
    				{
    					print("Не удалось назначить объявлению премиум \r\n"+
    		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		    		print("Объявлению не назначен премиум, так как ключ оплаты передан не был/или объявление принадлежит ИП. Корректно");
    				}
		    	}
		    	else
		    	{
		    		print("Не удалось назначить объявлению премиум \r\n"+
		    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
		    		print("Объявлению не назначен премиум, так как ключ оплаты передан не был/или объявление принадлежит ИП. Корректно");
		    		
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
	
	
	//Подача/деактивация/активация/Продление/Поднятие/Выделение/Назначение премиум/Получение листинга категории и проверка его (Платное объявление) 
	public void AddDeactivateActivateProlongPushupHighlightPremiumOPPaidAdvert(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, InterruptedException
	{
		String sIdAdvert="", sIdAdvert2=""; 
		String sLogin = Proper.GetProperty("login_authOP");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		JSONObject jData, jData2, jDataPostAsvert;
		InnerDataHM objCar;
		String sDataForList = "{category=cars/passenger/new/, region=russia/moskva-gorod/, currency=RUR, offset=0, limit=45, sort_by=date_sort:desc, include_privates=true, include_companies=true}";
		int nNumberList, nNumberList2;
		
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Подача , деактивация, активация, продление, поднятие, выделение, премиум, получение листингов  ОП(платное объявление) - Тест".toUpperCase()+"\r\n");
		// авторизация
		print("\r\nАвторизация пользователем - " + sLogin);
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
		try
		{
			// подача двух объявлений
			print("\r\nШАГ 1");
			print("Подача двух объявлений в платную рубрику Авто - Новые авто".toUpperCase());
			print("\r\nПодача объявления в рубрику Авто - Новые авто");
			print("Объявление №1");
			objCar = PostAdvert(sHost, mas_Advertisment, mas_Auto2, sAuth_token, "category_auto_new", "image");
	    	sIdAdvert = objCar.GetID();
	   	
	    	print("\r\nПодача объявления в рубрику Авто - Новые авто");
			print("Объявление №2");
			objCar = PostAdvert(sHost, mas_Advertisment, mas_Auto2, sAuth_token, "category_auto_new", "image");
	    	sIdAdvert2 = objCar.GetID();
	    	
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
	    	print("\r\nШАГ 14");
	    	print("Проверка деактивации объявления".toUpperCase());
	    	print("\r\nДеактивируем объявление с ID = " + sIdAdvert +  " для пользователя " + sLogin);
	    	DeactivateAdvert(sHost, sAuth_token, sIdAdvert, 1);
	    	
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
		}
		finally
		{
	    	
	    	//удаляем поданные обяъвления
	    	print("\r\nШАГ 15");
	    	print("Удаляем поданные объявления".toUpperCase());
	    	DeleteAdvert(sHost, sAuth_token, sIdAdvert);
	    	DeleteAdvert(sHost, sAuth_token, sIdAdvert2);
		}
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
    	
    	
	}

	
	// Подача/Получение листинга пользователя
	public void AddAvdertGetListUserOP(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, NumberFormatException, InterruptedException
	{
		String sIdAdvert="", sIdAdvert2="", sIdAdvert3=""; 
		String sLogin = Proper.GetProperty("login_authOP2");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		JSONObject jData;
		InnerDataHM objRealt, objAuto;
		String sDataForSearchUserAdvert = "{user_id=10930240, offset=0, limit=20}"; // для пользователя api12@yopmail.com
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Подача объявлений, получение листинга активных объявлений пользователя ОП - Тест".toUpperCase()+"\r\n");
		
		// авторизация
		print("\r\nАвторизация пользователем - " + sLogin);
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
		try
		{
			// подача трех объявлений
			print("\r\nШАГ 1");
			print("Подача трех объявлений одно в платную рубрику Авто - Новые авто, два в бесплатную рубрику Недвижимость - Вторичный рынок".toUpperCase());
			print("\r\nПодача объявления в рубрику Авто - Новые авто");
			print("Объявление №1");
			objAuto = PostAdvert(sHost, mas_Advertisment, mas_Auto2, sAuth_token, "category_auto_new", "image");
			sIdAdvert = objAuto.GetID();
			
	    	print("\r\nПодача объявления в рубрику Недвижимость - Вторичный рынок");
			print("Объявление №2");
			objRealt = PostAdvert(sHost, mas_Advertisment, mas_Realt2, sAuth_token, "category_realt", "image2");
	    	sIdAdvert2 = objRealt.GetID();
	    	
	    	print("\r\nПодача объявления в рубрику Недвижимость - Вторичный рынок");
			print("Объявление №3");
			objRealt = PostAdvert(sHost, mas_Advertisment, mas_Realt2, sAuth_token, "category_realt", "image4");
	    	sIdAdvert3 = objRealt.GetID();
	    	
	    	print("\r\nОжидаем индексации, время ожидания ".toUpperCase() + Integer.parseInt(Proper.GetProperty("timeWait"))/(1000*60) + " минут(ы)".toUpperCase());
	    	Sleep(Integer.parseInt(Proper.GetProperty("timeWait")));
	    	
	    	// получаем листинг объявлений пользователя и проверяем статус объявлений в нем
	    	print("\r\nШАГ 2");
	    	print("Получаем листинг объявлений пользователя".toUpperCase());
	    	print("\r\nПолучаем листинг объявлени для для пользователя " + sLogin);
	    	jData = GetListUserAdvert(sHost, sDataForSearchUserAdvert);
	    	print("\r\nПроверяем статус объявлений в листинге");
	    	ValidateListUser(sHost, jData, sIdAdvert2, sIdAdvert3, 1);
	    	
	    	// деактивируем первое объявление и удаляем второе
	    	print("\r\nШАГ 3");
	    	print("Деактивируем первое из поданных активных объявлений и удаляем второе из поданных активных объявлений".toUpperCase());
	    	print("Деактивируем объявлени с ID = " + sIdAdvert2);
	    	DeactivateAdvert(sHost, sAuth_token, sIdAdvert2, 1);
	
	    	print("Удаляем объявлени с ID = " + sIdAdvert3);
	    	DeleteAdvert(sHost, sAuth_token, sIdAdvert3);
	    	
	    	print("\r\nОжидаем индексации, время ожидания ".toUpperCase() + Integer.parseInt(Proper.GetProperty("timeWait"))/(1000*60) + " минут(ы)".toUpperCase());
	    	Sleep(Integer.parseInt(Proper.GetProperty("timeWait")));
	    	
	    	// получаем листинг объявлений пользователя и проверяем статус объявлений в нем
	    	print("\r\nШАГ 4");
	    	print("Получаем листинг объявлений пользователя".toUpperCase());
	    	print("\r\nПолучаем листинг объявлений для пользователя " + sLogin + " и проверяем статус активности объявлений в листинге");
	    	print("\r\nПолучаем листинг объявлени для для пользователя " + sLogin);
	    	jData = GetListUserAdvert(sHost, sDataForSearchUserAdvert);
	    	print("\r\nПроверяем статус объявлений в листинге");
	    	ValidateListUser(sHost, jData, sIdAdvert2, sIdAdvert3, 2);
		}
		finally
		{
	    	// удаляем еще не удаленное объявление
	    	print("\r\nШАГ 5");
	    	print("Удаление поданных объявлений пользователя".toUpperCase());
	    	print("Удаляем объявление с ID = " + sIdAdvert2);
	    	DeleteAdvert(sHost, sAuth_token, sIdAdvert2);
	    	
	    	print("Удаляем объявление с ID = " + sIdAdvert);
	    	DeleteAdvert(sHost, sAuth_token, sIdAdvert);
		}
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
	}
	// получение листинга объявлений пользователя для автотестов
	private JSONObject GetListUserAdvert(String sHost, String sDataForSearchUserAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		JSONObject jTemp;
		print("Получение листинга объявлений пользователя".toUpperCase());
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
    	jTemp = jsonObject;
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера: Листинг объявлений пользователя получен");
    		JSONArray ar = jsonObject.getJSONArray("advertisements");
    		for(int i=0; i<ar.length(); i++)
    		{
    			print("--------------------------------------------------------------------------------------------------------------");
    			print("Объявление №" + (i+1));
    			jsonObject = (JSONObject) ar.get(i);
    			print(jsonObject.toString(10));
    		
    		}
    		return jTemp;
    	}
    	else
    	{
    		print("Не удалось получить листинг объявлений пользователя \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// проверка листинга объявлений пользователя
	private void ValidateListUser(String sHost, JSONObject jObj, String sIdAdvert, String sIdAdvert2, int nResult) throws JSONException, ExceptFailTest, URISyntaxException, IOException
	{
		JSONObject jTemp, jData;
		jTemp = jObj;
		String sId;
		boolean bFlagAdvert = false, bFlagAdvert2 = false;
		
		switch (nResult)
		{
			case 1:
				if(jTemp.getString("advertisements").equals("[]"))
				{
					print("В листинге активных объявлений пользователя, нету ни одного объявления. Но мы только что подали два объявления");
					print("Тест провален".toUpperCase());
					throw new ExceptFailTest("Тест провален");
				}
				else
				{
					print("Проверяем status объявлений в листинге. Все объявления должны иметь статус равный 1," +
							" и так же ищем в листинге только что поданные для данного пользователя объявления");
					JSONArray ar = jTemp.getJSONArray("advertisements");
		    		for(int i=0; i<ar.length(); i++)
		    		{
		    			print("\r\nПроверяем объявление №" + (i+1));
		    			jTemp = (JSONObject) ar.get(i);
		    			sId = jTemp.getString("id");
		    			print("ID "+ (i+1) +" объявления в листинге равно " + sId);
		    			if(sId.equals(sIdAdvert))
		    				bFlagAdvert = true;
		    			if(sId.equals(sIdAdvert2))
		    				bFlagAdvert2 = true;
		    			print("Получаем данные по объявлению с ID = " + sId);
		    			jData = GetAdvert(sHost, sId, " листинг активных объявлений пользователя");
		    			print("Проверяем статус активность для объявления ID = " + sId);
		    			ValidateStatus("1", jData, sId, "");	
		    		}
		    		if((bFlagAdvert == true) && (bFlagAdvert2 == true))
		    			print("Все объявления в листинге пользователя активны (status = 1). В листинге так же найдены, только что поданные объявления. Корректно");
		    		else
		    		{
		    			print("В листинге активных объявлений пользователя отсутствуют, только что поданные объявления");
		    			print("Тест провален");
		    			throw new ExceptFailTest("Тест провален");
		    		}
				}
				break;
				
			case 2:
				if(jTemp.getString("advertisements").equals("[]"))
				{
					print("В листинге активных объявлений пользователя, нету ни одного объявления. Корректно. Так как мы деактивировали и удалили объявления в ЛК");
				}
				else
				{
					print("Проверяем status объявлений в листинге. Все объявления должны иметь статус равный 1," +
							" и так же ищем в листинге только что в листинге отсутствуют поданные, а потом деактивированные и удаленные объявления, для данного пользователя объявления");
					JSONArray ar = jTemp.getJSONArray("advertisements");
		    		for(int i=0; i<ar.length(); i++)
		    		{
		    			print("\r\nПроверяем объявление №" + (i+1));
		    			jTemp = (JSONObject) ar.get(i);
		    			sId = jTemp.getString("id");
		    			print("ID "+ (i+1) +" объявления в листинге равно " + sId);
		    			if(sId.equals(sIdAdvert))
		    				bFlagAdvert = true;
		    			if(sId.equals(sIdAdvert2))
		    				bFlagAdvert2 = true;
		    			print("Получаем данные по объявлению с ID = " + sId);
		    			jData = GetAdvert(sHost, sId, " листинг активных объявлений пользователя");
		    			print("Проверяем статус активность для объявления ID = " + sId);
		    			ValidateStatus("1", jData, sId, "");	
		    		}
		    		if((bFlagAdvert == true) && (bFlagAdvert2 == true))
		    		{
		    			print("В листинге пользователя остались объявления которые были поданы, а потом деактивированы или удалены");
		    			print("Тест провален");
		    			throw new ExceptFailTest("Тест провален");
		    		}
		    		else
		    		{
		    			print("В листинге только активные объявления. Так же в листинге активных объявлений пользователя отсутствуют объявления которые были поданы, " +
		    					"а потом деактивированы или удалены. Корректно");
	
		    		}
				}
				break;
			
		}
		
	}
	
	
	// Подача, получение листинга с фильтрацией 
	public void AddGetFilterList(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, NumberFormatException, InterruptedException
	{
		String sIdAuto="", sIdRealt="", sIdTIU="", sIdMobile="", sIdJob=""; 
		String sLogin = Proper.GetProperty("login_authOP2");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		HM<String, String> hObj_Auto;
		HM<String, String> hObj_Auto2;
		HM<String, String> hObj_Realt;
		HM<String, String> hObj_Realt2;
		HM<String, String> hObj_TIY;
		HM<String, String> hObj_TIY2;
		HM<String, String> hObj_Mobile;
		HM<String, String> hObj_Mobile2;
		HM<String, String> hObj_Job;
		HM<String, String> hObj_Job2;
		JSONObject jData;
		InnerDataHM objAuto, objRealt, objTIY, objMobile, objJob;
		String sDataForListing = "{category=cars/passenger/used/, region=russia/moskva-gorod/, offset=0, limit=30, sort_by=date_sort:desc}";
		String sDataForListing2 = "{category=real-estate/apartments-sale/secondary/, region=russia/arhangelskaya-obl/arhangelsk-gorod/, offset=0, limit=30, sort_by=date_sort:desc}";
		String sDataForListing3 = "{category=electronics-technics/vacuum/, region=russia/tatarstan-resp/kazan-gorod/, offset=0, limit=30, sort_by=date_sort:desc}";
		String sDataForListing4 = "{category=communication/mobile/mobiles/, region=russia/permskiy-kray/perm-gorod/, currency=RUR, offset=0, limit=20, sort_by=date_sort:desc}";
		String sDataForListing5 = "{category=jobs-education/resumes/communal/, region=russia/moskva-gorod/, offset=0, limit=30, sort_by=date_sort:desc}";
		print("------------------------------------------------------------------------------------------------------------");
		print("Подача, получение фильтрованного листинга - Тест".toUpperCase()+"\r\n");
		// авторизация
		print("\r\nАвторизация пользователем - " + sLogin);
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
		
		
		// подача трех объявлений
		print("\r\nШАГ 1");
		print("Подача пяти объявлений".toUpperCase());
		try
		{
			print("\r\nПодача объявления в рубрику Авто с пробегом. Регион Москва ".toUpperCase());
			objAuto = PostAdvert(sHost, mas_Advertisment, mas_Auto2, sAuth_token, "category_auto", "image");
			sIdAuto = objAuto.GetID();  // сюда сохраняем значение id
			hObj_Auto = objAuto.GetAdvertismentData(); // сюда сохраняем значение массива адветисемент (контакты, title, web, price и т.д. указанные при подаче )  
			hObj_Auto2 = objAuto.GetCustomfieldData(); // сюда сохраняем значение массива кастомфилдов, указанные при подаче
	
	/////////////////////////////////////////////////////////////////////////////////////////////////    	
	    	print("\r\nПодача объявления в рубрику Недвижимость - Вторичный рынок. Регион Архангельск".toUpperCase());
	    	objRealt = PostAdvert(sHost, mas_Advertisment, mas_Realt2, sAuth_token, "category_realt", "image2");
	    	sIdRealt = objRealt.GetID();
	    	hObj_Realt = objRealt.GetAdvertismentData();
	    	hObj_Realt2 = objRealt.GetCustomfieldData();
	    	
	///////////////////////////////////////////////////////////////////////////////////////////////// 
	    	print("\r\nПодача объявления в рубрику Электроника и техника - Пылесосы. Регион Казань".toUpperCase());
	    	objTIY = PostAdvert(sHost, mas_Advertisment, mas_TIY2, sAuth_token, "category_electron", "image3");
	    	sIdTIU = objTIY.GetID();
	    	hObj_TIY = objTIY.GetAdvertismentData();
	    	hObj_TIY2 = objTIY.GetCustomfieldData();
	    	
///////////////////////////////////////////////////////////////////////////////////////////////// 
			print("\r\nПодача объявления в рубрику Телефоны и связь - Мобильные телефоны. Регион Пермь".toUpperCase());
			objMobile = PostAdvert(sHost, mas_Advertisment, mas_TIY_Mobile, sAuth_token, "category_mobile", "image5");
			sIdMobile = objMobile.GetID();
			hObj_Mobile = objMobile.GetAdvertismentData();
			hObj_Mobile2 = objMobile.GetCustomfieldData();
			
			
///////////////////////////////////////////////////////////////////////////////////////////////// 
			print("\r\nПодача объявления в рубрику Работа и образование - Резюме(Бытовые и коммунальные услуги, муниципалитет). Регион Москва".toUpperCase());
			objJob = PostAdvert(sHost, mas_Advertisment, mas_Job, sAuth_token, "category_jobs", "image7");
			sIdJob = objJob.GetID();
			hObj_Job = objJob.GetAdvertismentData();
			hObj_Job2 = objJob.GetCustomfieldData();
	    	
			
	    	print("\r\nОжидаем индексации, время ожидания ".toUpperCase() + Integer.parseInt(Proper.GetProperty("timeWait"))/(1000*60) + " минут(ы)".toUpperCase());
	    	Sleep(Integer.parseInt(Proper.GetProperty("timeWait")));
	    	
	    	// получаем фильтрованный листинг для  рубрик и региона
	    	print("\r\nШАГ 2");
	    	print("Получаем фильтрованный листинг для категорий".toUpperCase());
	    	
	    	print("\r\nФормируем запрос для категории Авто с пробегом. Регион Москва. Ищем поданное объявление с ID = " + sIdAuto);
	    	print("Строка для поиска в рубрике Авто с пробегом для только что поданного объявления = "+ GetStringFilterAuto(hObj_Auto, hObj_Auto2));
	    	print("Производим поиск");
	    	jData = GetListSearchCategory(sHost, sDataForListing, GetStringFilterAuto(hObj_Auto, hObj_Auto2));
	    	print("Ищем в полученном листинге-фильтрации поданное объявление с ID = " + sIdAuto);
	    	FindAdvertFromListAfterPost(jData, sIdAuto);
	    	
	    	print("\r\nФормируем запрос для категории Недвижимость - Вторичный рынок. Регион Архангельск. Ищем поданное объявление с ID = " + sIdRealt);
	    	print("Строка для поиска в рубрике Недвижимость - Вторичный рынок для только что поданного объявления = "+ GetStringFilterRealt(hObj_Realt, hObj_Realt2));
	    	print("Производим поиск");
	    	jData = GetListSearchCategory(sHost, sDataForListing2, GetStringFilterRealt(hObj_Realt, hObj_Realt2));
	    	print("Ищем в полученном листинге-фильтрации поданное объявление с ID = " + sIdRealt);
	    	FindAdvertFromListAfterPost(jData, sIdRealt);
	    	
	    	print("\r\nФормируем запрос для категории Электроника и техника - Пылесосы. Регион Казань. Ищем поданное объявление с ID = " + sIdTIU);
	    	print("Строка для поиска в рубрике Электроника и техника - Пылесосы для только что поданного объявления = "+ GetStringFilterTIY(hObj_TIY, hObj_TIY2));
	    	print("Производим поиск");
	    	Sleep(10000);
	    	jData = GetListSearchCategory(sHost, sDataForListing3, GetStringFilterTIY(hObj_TIY, hObj_TIY2));
	    	print("Ищем в полученном листинге-фильтрации поданное объявление с ID = " + sIdTIU);
	    	FindAdvertFromListAfterPost(jData, sIdTIU);
	    	
	    	
	    	print("\r\nФормируем запрос для категории Телефоны и связь - Мобильные телефоны. Регион Пермь. Ищем поданное объявление с ID = " + sIdMobile);
	    	print("Строка для поиска в рубрике Телефоны и связь - Мобильные телефоны для только что поданного объявления = "+ GetStringFilterMobile(hObj_Mobile, hObj_Mobile2));
	    	print("Производим поиск");
	    	Sleep(5000);
	    	jData = GetListSearchCategory(sHost, sDataForListing4, GetStringFilterMobile(hObj_Mobile, hObj_Mobile2));
	    	print("Ищем в полученном листинге-фильтрации поданное объявление с ID = " + sIdMobile);
	    	FindAdvertFromListAfterPost(jData, sIdMobile);
	    	
		}
		finally
		{
	    	// удаляем объявления 
	    	print("\r\nШАГ 3");
	    	print("Удаление поданных объявлений пользователя".toUpperCase());
	    	print("Удаляем объявление с ID = " + sIdAuto);
	    	DeleteAdvert(sHost, sAuth_token, sIdAuto);
	    	
	    	print("Удаляем объявление с ID = " + sIdRealt);
	    	DeleteAdvert(sHost, sAuth_token, sIdRealt);
	    	
	    	print("Удаляем объявление с ID = " + sIdTIU);
	    	DeleteAdvert(sHost, sAuth_token, sIdTIU);
	    	
	    	print("Удаляем объявление с ID = " + sIdTIU);
	    	DeleteAdvert(sHost, sAuth_token, sIdMobile);
	    	
	    	print("Удаляем объявление с ID = " + sIdJob);
	    	DeleteAdvert(sHost, sAuth_token, sIdJob);
		}
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
    	
	}
	// получение строки фильтра для поиска для Авто для автотеста
	private String GetStringFilterAuto(HM<String, String> hObj, HM<String, String> hObj2) throws UnsupportedEncodingException
	{

		String sMileage = hObj2.GetValue("mileage");
		int m = Integer.parseInt(sMileage);
		m = m/1000;
		sMileage = Integer.toString(m);
	
		String sDataForSearch = "currency="+hObj.GetValue("currency")+"/price="+ hObj.GetValue("price") +
				"/car-year="+ hObj2.GetValue("car-year") +"/hasimages=1/bodytype=" + GetCrc32(hObj2.GetValue("bodytype")).toString() + 
				"/transmittion=" + GetCrc32(hObj2.GetValue("transmittion")).toString() + "/mileage=" + sMileage +"/" ;
		
		return sDataForSearch;
	}
	// получение строки фильтра для поиска для Недвижимости для автотеста
	private String GetStringFilterRealt(HM<String, String> hObj, HM<String, String> hObj2) throws UnsupportedEncodingException
	{
		String sDataForSearch = "";	
		String sPrivate = "1";

		if(hObj2.GetValue("private").equals("0"))
			sDataForSearch = "currency="+hObj.GetValue("currency")+"/price="+ hObj.GetValue("price") +
			"/rooms=" + hObj2.GetValue("rooms") + "/meters-total=" + hObj2.GetValue("meters-total") + 
			"/currency=RUR/hasimages=1" +
			"/state=" + GetCrc32(hObj2.GetValue("state")).toString() + "/etage-all=" + hObj2.GetValue("etage-all") + "/" +
			"/keywords=" + hObj.GetValue("text") + "/";
		else
			sDataForSearch = "currency="+hObj.GetValue("currency")+"/price="+ hObj.GetValue("price") +
			"/rooms=" + hObj2.GetValue("rooms") + "/meters-total=" + hObj2.GetValue("meters-total") + 
			"/currency=RUR/hasimages=1" +
			"/state=" + GetCrc32(hObj2.GetValue("state")).toString() + "/etage-all=" +hObj2.GetValue("etage-all")+ "/private=" + sPrivate +
			"/keywords=" + hObj.GetValue("text") + "/";
		
	
		return sDataForSearch;
	}
	// получение строки фильтра для поиска для Электроники - пылесосы для автотеста
	private String GetStringFilterTIY(HM<String, String> hObj, HM<String, String> hObj2) throws UnsupportedEncodingException
	{
		String sDataForSearch = "";
		String sVacuumclean="1";
		
		if(hObj2.GetValue("vacuumclean_wash").equals("0"))
			sDataForSearch = "currency="+hObj.GetValue("currency")+"/price="+ hObj.GetValue("price") +
			"/offertype=" + GetCrc32(hObj2.GetValue("offertype")).toString() + "/used-or-new=" + GetCrc32(hObj2.GetValue("used-or-new")).toString() +
			"/hasimages=1/" +
			"/keywords=" + hObj.GetValue("text") + "/";
		else
			sDataForSearch = "currency="+hObj.GetValue("currency")+"/price="+ hObj.GetValue("price") +
			"/offertype=" + GetCrc32(hObj2.GetValue("offertype")).toString() + "/used-or-new=" + GetCrc32(hObj2.GetValue("used-or-new")).toString() + 
			"/hasimages=1/vacuumclean_wash=" + sVacuumclean + 
			"/keywords=" + hObj.GetValue("text") + "/";
		
		return sDataForSearch;
	}
	// получение строки фильтра для поиска для Телефоны - сотовые для автотеста
	private String GetStringFilterMobile(HM<String, String> hObj, HM<String, String> hObj2) throws UnsupportedEncodingException
	{

		//{"make_mobile"};
		String sDataForSearch;
		String sMobile_two_sim_card = "1";

		if(hObj2.GetValue("mobile_two_sim_card").equals("0"))
			sDataForSearch = "currency=" + hObj.GetValue("currency") + "/price="+ hObj.GetValue("price") +
				"/used-or-new=" + GetCrc32(hObj2.GetValue("used-or-new")).toString() + "/hasimages=1/offertype=" + GetCrc32(hObj2.GetValue("offertype")).toString() + 
				"/corpus_type=" + GetCrc32(hObj2.GetValue("corpus_type")).toString() + "/make=" + hObj2.GetValue("make")
				+ "/keywords=" + hObj.GetValue("text") + "/";
		else
			sDataForSearch = "currency=" + hObj.GetValue("currency") + "/price="+ hObj.GetValue("price") +
			"/used-or-new=" + hObj2.GetValue("used-or-new") + "/hasimages=1/offertype=" + GetCrc32(hObj2.GetValue("offertype")).toString() + 
			"/corpus_type=" + GetCrc32(hObj2.GetValue("corpus_type")).toString() + "/make=" + hObj2.GetValue("make")
			+ "/keywords=" + hObj.GetValue("text") + "/mobile_two_sim_card=" + sMobile_two_sim_card +"/";
				
		
		return sDataForSearch;
	}
	// фильтрация получение листинга
	private  JSONObject GetListSearchCategory(String sHost, String sDataForListing, String sDataForSearch) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		JSONObject jTemp;
		print("Фильтрация/поиск объявлений по критериям".toUpperCase());
		print("Параметры для запроса");
		print("DataForListing = "+ sDataForListing);
		print("sDataForSearch = "+ sDataForSearch);
		
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
    	jTemp = jsonObject;
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера: Листинг объявлений получен");
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
    		print("Не удалось получить фильтр-листинг по критериям \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// перевод строи в crc32
	private Long GetCrc32(String sData) throws UnsupportedEncodingException
	{
         byte bytes[] = sData.getBytes("UTF-8");
         Checksum cs = new CRC32();
         cs.update(bytes,0,bytes.length);        
         Long l = cs.getValue();
         return l;
	}
	
	
	//Подача, голосование + , голосование -
	public void AddVoteHighLower(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, InterruptedException
	{
		String sIdAdvert=""; 
		String sLogin = Proper.GetProperty("login_authOP2");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		JSONObject jData;
		InnerDataHM objRealt;
		
		
		print("Подача , голосование '+', голосование '-' - Тест".toUpperCase()+"\r\n");
		// авторизация
		print("\r\nАвторизация пользователем - " + sLogin);
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
		try
		{
			// подача двух объявлений
			print("\r\nШАГ 1");
			print("Подача объявления в бесплатную рубрику. Недвижимость - Вторичный рынок".toUpperCase());
			print("\r\nПодача объявления в рубрику Недвижимость - Вторичный рынок");
			print("Объявление №1");
	    	objRealt = PostAdvert(sHost, mas_Advertisment, mas_Realt2, sAuth_token, "category_realt", "image2");
	    	sIdAdvert = objRealt.GetID();
	    	
	    	print("\r\nШАГ 2");
			print("Голосование за объявление (повысить рейтинг)".toUpperCase());
			print("\r\nГолосуем за объявление с ID = " + sIdAdvert);
			jData = VoteForAdvertHigh(sHost, sAuth_token, sIdAdvert);
			
			if(jData.getString("votes").equals("1"))
				print("Объявлению был добавлен голос. Общее количество голосов = " + jData.getString("votes"));
			else
			{
				print("Объявлению либо не был добавлен голос, либо добавлен более чем один голос");
				print("Тесть провален".toUpperCase());
				throw new ExceptFailTest("Тест провален");
			}
			
			
			print("\r\nШАГ 3");
			print("Голосование за объявление (понизить рейтинг)".toUpperCase());
			print("\r\nГолосуем за объявление с ID = " + sIdAdvert);
			jData = VoteForAdvertLowerLower(sHost, sAuth_token, sIdAdvert);
			
			if(jData.getString("votes").equals("0"))
				print("Объявлению был убран голос. Общее количество голосов = " + jData.getString("votes"));
			else
			{
				print("Объявлению либо не был убран голос, либо убран более чем один голос");
				print("Тесть провален".toUpperCase());
				throw new ExceptFailTest("Тест провален");
			}
		}
		finally
		{
			// удаляем объявление 
	    	print("\r\nШАГ 3");
	    	print("Удаление поданных объявлений пользователя".toUpperCase());
	    	print("Удаляем объявление с ID = " + sIdAdvert);
	    	DeleteAdvert(sHost, sAuth_token, sIdAdvert);
		}
    	
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
   	
	}
	// голосование за объявление '+' для автотеста
	private  JSONObject VoteForAdvertHigh(String sHost, String sAuth_token, String sIdAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		
		print("Проголосовать за объявление (повысить рейтинг объявления)".toUpperCase());
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("ADVERTISEMENT_ID = "+ sIdAdvert);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/vote");
    	
    	String sE = "auth_token=" + sAuth_token;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявлению +1 голос");
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось добавить голос объявлению \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// голосование за объявление '-' для автотеста
	private JSONObject VoteForAdvertLowerLower(String sHost, String sAuth_token, String sIdAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{	
		print("Проголосовать за объявление (снизить рейтинг объявления)");
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
    	{
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявлению -1 голос");
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось отнять голос у объявления \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	
	//Подача, получения листинга категории и проверка объявлений на статус, категорию, регион
	public void AddAdvertGetCitiesGetListCategory(String sHost) throws JSONException, URISyntaxException, IOException, ExceptFailTest, NumberFormatException, InterruptedException
	{
		String sIdAuto="", sIdRealt="", sIdTIU="", sIdMobile="", sIdJob="";
		String sLogin = Proper.GetProperty("login_authOP");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		JSONObject jData;
		InnerDataHM objAuto, objRealt, objTIY, objMobile, objJob;
		String sDataForListAuto = "{category=cars/passenger/used/, region=russia/moskva-gorod/, currency=RUR, offset=0, limit=20, sort_by=date_sort:desc, include_privates=true, include_companies=true}";
		String sDataForListRealt = "{category=real-estate/apartments-sale/secondary/, region=russia/arhangelskaya-obl/arhangelsk-gorod/, currency=RUR, offset=0, limit=20, sort_by=date_sort:desc, include_privates=true, include_companies=true}";
		String sDataForListTIY = "{category=electronics-technics/vacuum/, region=russia/tatarstan-resp/kazan-gorod/, currency=RUR, offset=0, limit=20, sort_by=date_sort:desc, include_privates=true, include_companies=true}";
		String sDataForListTIYMobile = "{category=communication/mobile/mobiles/, region=russia/permskiy-kray/perm-gorod/, currency=RUR, offset=0, limit=20, sort_by=date_sort:desc, include_privates=true, include_companies=true}";
		String sDataForListJob = "{category=jobs-education/resumes/communal/, region=russia/moskva-gorod/, currency=RUR, offset=0, limit=20, sort_by=date_sort:desc, include_privates=true, include_companies=true}";
		String sRegionNameAuto = "Москва";
		String sCategoryNameAuto = "Автомобили с пробегом";
		String sRegionNameRealt = "Архангельск";
		String sCategoryNameRealt = "Вторичный рынок";
		String sRegionNameTIY = "Казань";
		String sCategoryNameTIY = "Пылесосы";
		String sRegionNameMobile = "Пермь";
		String sCategoryNameMobile = "Мобильные телефоны";
		//String sRegionNameJob = "Москва";
		//String sCategoryNameJob = "Бытовые и коммунальные услуги, муниципалитет";
		
		
	
		print("------------------------------------------------------------------------------------------------------------");
		print("Подача, получение листинга категории - Тест".toUpperCase()+"\r\n");
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
		
/////////////////////////////////////////////////////////////////////////////////////////////////	
		print("\r\nШАГ 1");
		print("Подача пяти объявлений".toUpperCase());
		try
		{
			print("\r\nПодача объявления в рубрику Авто с пробегом. Регион Москва".toUpperCase());
			print("Объявление №1");	
			objAuto = PostAdvert(sHost, mas_Advertisment, mas_Auto2, sAuth_token, "category_auto", "image");
			sIdAuto = objAuto.GetID();  // сюда сохраняем значение id
			
	
	/////////////////////////////////////////////////////////////////////////////////////////////////    	
	    	print("\r\nПодача объявления в рубрику Недвижимость - Вторичный рынок. Регион Архангельск".toUpperCase());
	    	print("Объявление №2");	
	    	objRealt = PostAdvert(sHost, mas_Advertisment, mas_Realt2, sAuth_token, "category_realt", "image2");
	    	sIdRealt = objRealt.GetID();
	    	
	///////////////////////////////////////////////////////////////////////////////////////////////// 
	    	print("\r\nПодача объявления в рубрику Электроника и техника - пылесосы. Регион Казань".toUpperCase());
	    	print("Объявление №3");	
	    	objTIY = PostAdvert(sHost, mas_Advertisment, mas_TIY2, sAuth_token, "category_electron", "image3");
	    	sIdTIU = objTIY.GetID();
	    	
	    	
	///////////////////////////////////////////////////////////////////////////////////////////////// 
			print("\r\nПодача объявления в рубрику Телефоны и связь - сотовые телефоны. Регион Пермь".toUpperCase());
			print("Объявление №4");	
			objMobile = PostAdvert(sHost, mas_Advertisment, mas_TIY_Mobile, sAuth_token, "category_mobile", "image5");
			sIdMobile = objMobile.GetID();    
			
	///////////////////////////////////////////////////////////////////////////////////////////////// 
			print("\r\nПодача объявления в рубрику Работа и образование - Резюме(Бытовые и коммунальные услуги, муниципалитет). Регион Москва".toUpperCase());
			print("Объявление №5");	
			objJob = PostAdvert(sHost, mas_Advertisment, mas_Job, sAuth_token, "category_jobs", "image7");
			sIdJob = objJob.GetID();    
			
			

	    	print("\r\nОжидаем индексации, время ожидания ".toUpperCase() + Integer.parseInt(Proper.GetProperty("timeWait"))/(1000*60) + " минут(ы)".toUpperCase());
	    	Sleep(Integer.parseInt(Proper.GetProperty("timeWait")));
	    	
	    	
	    	// получение листинга, проверка что объявления появились, проверка что все другие активны и принадлежат этому листингу	
	    	print("\r\nШАГ 2");
	    	print("Получаем листинг категории Авто с пробегом. Регион Москва".toUpperCase());
	    	print("\r\nПолучаем листинг категории объявлений рубрики Авто с пробегом");
	    	jData = GetListCategory(sHost, sDataForListAuto);
	    	print("\r\nПроверяем status объявлений в листинге, region объявлений, category объявлений.");
	    	ValidateListCategory(sHost, jData, sIdAuto, sRegionNameAuto, sCategoryNameAuto, "russia/moskva-gorod/");
	    	
	    
	    	// получение листинга, проверка что объявления появились, проверка что все другие активны и принадлежат этому листингу	
	    	print("\r\nШАГ 3");
	    	print("Получаем листинг категории Недвижимость - Вторичный рынок. Регион Архангельск".toUpperCase());
	    	print("\r\nПолучаем листинг категории объявлений рубрики Недвижимость - Вторичный рынок");
	    	jData = GetListCategory(sHost, sDataForListRealt);
	    	print("\r\nПроверяем status объявлений в листинге, region объявлений, category объявлений.");
	    	ValidateListCategory(sHost, jData, sIdRealt, sRegionNameRealt, sCategoryNameRealt, "russia/arhangelskaya-obl/arhangelsk-gorod/");
	    		
	    
	    	// получение листинга, проверка что объявления появились, проверка что все другие активны и принадлежат этому листингу	
	    	print("\r\nШАГ 4");
	    	print("Получаем листинг категории Электроника и техника - пылесосы. Регион Казань".toUpperCase());
	    	print("\r\nПолучаем листинг категории объявлений рубрики Электроника и техника - пылесосы");
	    	jData = GetListCategory(sHost, sDataForListTIY);
	    	print("\r\nПроверяем status объявлений в листинге, region объявлений, category объявлений.");
	    	ValidateListCategory(sHost, jData, sIdTIU, sRegionNameTIY, sCategoryNameTIY, "russia/tatarstan-resp/kazan-gorod/");
	    	
	    	
	    	// получение листинга, проверка что объявления появились, проверка что все другие активны и принадлежат этому листингу	
	    	print("\r\nШАГ 5");
	    	print("Получаем листинг категории Телефоны и связь - сотовые телефоны. Регион Пермь".toUpperCase());
	    	print("\r\nПолучаем листинг категории объявлений рубрики Телефоны и связь - сотовые телефоны.");
	    	jData = GetListCategory(sHost, sDataForListTIYMobile);
	    	print("\r\nПроверяем status объявлений в листинге, region объявлений, category объявлений.");
	    	ValidateListCategory(sHost, jData, sIdMobile, sRegionNameMobile, sCategoryNameMobile, "russia/permskiy-kray/perm-gorod/");
	    	
	    	
	    	// получение листинга, проверка что объявления появились, проверка что все другие активны и принадлежат этому листингу	
	    	print("\r\nШАГ 6");
	    	print("Получаем листинг категории Работа и образование - Резюме(Бытовые и коммунальные услуги, муниципалитет). Регион Москва".toUpperCase());
	    	print("\r\nПолучаем листинг категории объявлений рубрики Работа и образование - Резюме(Бытовые и коммунальные услуги, муниципалитет).");
	    	jData = GetListCategory(sHost, sDataForListJob);
	    	//print("\r\nПроверяем status объявлений в листинге, region объявлений, category объявлений.");
	    	//ValidateListCategory(sHost, jData, sIdJob, sRegionNameJob, sCategoryNameJob, "russia/moskva-gorod/"); // После исправления бага 44474 раскомментить
	    		
		}
		
		finally
		{
	    	// удаляем объявления 
	    	print("\r\nШАГ 5");
	    	print("Удаление поданных объявлений пользователя".toUpperCase());
	    	print("Удаляем объявление с ID = " + sIdAuto);
	    	DeleteAdvert(sHost, sAuth_token, sIdAuto);
	    	
	    	print("Удаляем объявление с ID = " + sIdRealt);
	    	DeleteAdvert(sHost, sAuth_token, sIdRealt);
	    	
	    	print("Удаляем объявление с ID = " + sIdTIU);
	    	DeleteAdvert(sHost, sAuth_token, sIdTIU);
	    	
	    	print("Удаляем объявление с ID = " + sIdMobile);
	    	DeleteAdvert(sHost, sAuth_token, sIdMobile);
	    	
	    	print("Удаляем объявление с ID = " + sIdJob);
	    	DeleteAdvert(sHost, sAuth_token, sIdJob);
		}
    	print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
   
	}
	// проверка листинга на содержимое
	private void ValidateListCategory(String sHost, JSONObject jObj, String sIdAdvert, String sRegionNameAuto, String sCategoryNameAuto, String sWaitUrl) throws JSONException, URISyntaxException, IOException, ExceptFailTest
	{
		JSONObject jTemp, jData;
		String sId;
		boolean bFlagAdvert = false;
		jTemp = jObj;
		
		
		if(jTemp.getString("advertisements").equals("[]"))
		{
			print("В листинге объявлений категории, нету ни одного объявления. Но мы только что подали объявление");
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		else
		{
			print("Проверяем status объявлений в листинге, region объявлений, category объявлений." +
					"\r\nВсе объявления должны иметь статус равный 1," +
					"\r\nРегион должен быть равен \"" + sRegionNameAuto + "\" или принадлежать региону \"" + sRegionNameAuto + "\"" +
					"\r\nКатегория должна быть равна \"" + sCategoryNameAuto + "\"" +
					"\r\nВ листинге должно быть только что поданное в данную рубрику и регион объявление, для данного пользователя");
			JSONArray ar = jTemp.getJSONArray("advertisements");
    		for(int i=0; i<ar.length(); i++)
    		{
    			print("\r\nПроверяем объявление №" + (i+1));
    			jTemp = (JSONObject) ar.get(i);
    			sId = jTemp.getString("id");
    			print("ID "+ (i+1) +" объявления в листинге равно " + sId);
    			if(sId.equals(sIdAdvert))
    				bFlagAdvert = true;
    			print("Получаем данные по объявлению с ID = " + sId);
    			jData = GetAdvert(sHost, sId, " листинг активных объявлений пользователя");
    			print("Проверяем статус активность для объявления ID = " + sId);
    			ValidateStatus("1", jData, sId, "");
    			print("Проверяем категорию объявления для объявления ID = " + sId);
    			ValidateCategory(sCategoryNameAuto, jData, sId, "");
    			print("Проверяем регион объявления для объявления ID = " + sId);
    			ValidateRegion(sRegionNameAuto, jData, sId, "", sWaitUrl);
    		}
    		if(bFlagAdvert == true)
    			print("Все объявления в листинге категории активны (status = 1), принадлежат категории \"" + sCategoryNameAuto + "\" и региону \"" + sRegionNameAuto + "\". В листинге так же найдено, только что поданное объявления. Корректно");
    		else
    		{
    			print("В листинге категории отсутствует, только что поданное объявление");
    			print("Тест провален");
    			throw new ExceptFailTest("Тест провален");
    		}
			
		}
	}
	// получаем список городов принадлежащих региону листинга
	@SuppressWarnings("unused")
	private  JSONObject GetChildrenCities(String sHost, String sRegion) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Получение списка городов, принадлежащих определенному субъекту РФ".toUpperCase());
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
    		print("Ответ сервера: \r\n" + jsonObject.toString(10) + "\r\nсписок городов принадледащих " + sRegion+ " получен");
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить популярных городов \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// получение HM городов входящих в регион
	@SuppressWarnings("unused")
	private HM<String, String> GetAllChildrenCities(JSONObject jObj) throws JSONException
	{
		HM<String, String> hCities = new HM<String, String>();
		JSONObject jTemp;
		JSONArray ar;
		ar =  jObj.getJSONArray("regions");
		for(int i=0; i<ar.length(); i++)
		{
			jTemp = ar.getJSONObject(i);
			hCities.SetValue(jTemp.getString("short_name"), jTemp.getString("short_name"));
		}
		hCities.PrintKeyAndValue();
		return hCities;
	}
	// проверка категории для автотеста
	private void ValidateCategory(String sWaitStatus, JSONObject jObj, String sIdAdvert, String sText) throws JSONException, ExceptFailTest
	{
		String sCategory = jObj.getJSONObject("advertisement").getString("category");
		if(sCategory.equals(sWaitStatus))
		{
			print("Текущая категория объявления ID = " + sIdAdvert + ",  = " + sCategory + " совпала с ожидаемой категорией  = " + sWaitStatus + sText);
		}
		else
		{
			print("Текущая категория объявления ID = " + sIdAdvert + ",  = " + sCategory + " не совпала с ожидаемой категорией  = " + sWaitStatus + sText);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
	}
	// проверка региона для автотеста
	private void ValidateRegion(String sWaitStatus, JSONObject jObj, String sIdAdvert, String sText, String sWaitUrl) throws JSONException, ExceptFailTest
	{
		String sRegion = jObj.getJSONObject("advertisement").getString("region");
		String sUrlRegion = jObj.getJSONObject("advertisement").getString("region_url");
		print(sUrlRegion);
		if(sRegion.equals(sWaitStatus))
		{
			print("Текущий регион объявления ID = " + sIdAdvert + ",  = " + sRegion + " совпал с ожидаемым регионом  = " + sWaitStatus + sText);
		}
		else
		{
			if(sUrlRegion.lastIndexOf(sWaitUrl) == -1)
			{
				print("Текущий регион объявления ID = " + sIdAdvert + ",  = " + sRegion + " не совпал с ожидаемым регионом  = " + sWaitStatus + sText);
				print("Тест провален".toUpperCase());
				throw new ExceptFailTest("Тест провален");
			}
			else
			{
				print("Текущий регион объявления ID = " + sIdAdvert + ",  = " + sRegion + " не совпал с ожидаемым регионом  = " + sWaitStatus + sText);
				print("Однако данный регион входит в регион " + sWaitStatus + " и его нахождение в листинге корректно");
			}
		}
	}
	
	//Получение и проверка рубрикатора
	public void GetAndCheckRubricator(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, ClassNotFoundException
	{
		String sCategory = "/", sCategoryCars = "cars/", sCategoryRealt = "real-estate/";
		JSONObject jData;
		@SuppressWarnings("unused")
		JString Js;
		String smas[] = new String [3];
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Получение и проверка рубрикатора - Тест".toUpperCase());

		print("\r\nШАГ 1");
		print("Получаем рубрикатор основных рубрик сайта".toUpperCase());
		jData = GetRubricator(sHost, sCategory);
		String sCurrentRubricator = jData.toString(10); 
		
		print("Получаем рубрикатор рубрики Авто сайта".toUpperCase());
		jData = GetRubricator(sHost, sCategoryCars);
		String sCurrentRubricatorCars = jData.toString(10); 
		
		print("Получаем рубрикатор рубрики Недвижимость сайта".toUpperCase());
		jData = GetRubricator(sHost, sCategoryRealt);
		String sCurrentRubricatorRealt = jData.toString(10); 
		
		smas[0] = sCurrentRubricator;
		smas[1] = sCurrentRubricatorCars;
		smas[2] = sCurrentRubricatorRealt;
		
		//Раскоментить если надо будет обновить значения и закомментить после обновления
		//Js = new JString(smas); // запись рубрикаторов в файл
		//SaveJson(Js, "Rubricators.txt");
		
		String sIdealRubricator[] = LoadJson("Rubricators.txt");
		
		print("\r\nШАГ 2");
		print("Сравниваем основной рубрикатор сайта полученный запросом с основным рубрикатором сайта из сохранения".toUpperCase());
		if(sIdealRubricator[0].equals(sCurrentRubricator))
		{
			print("Рубрикаторы идентичны. Корректно");
			print("Полученный из сохранения рубрикатор");
			print(sIdealRubricator[0]);
		}
		else 
		{
			print("Рубрикаторы не совпадают");
			print("Полученный из сохранения рубрикатор:");
			print(sIdealRubricator[0]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("Сравниваем рубрикатор рубрики Авто сайта полученный запросом с рубрикатором Авто из сохранения".toUpperCase());
		if(sIdealRubricator[1].equals(sCurrentRubricatorCars))
		{
			print("Рубрикаторы идентичны. Корректно");
			print("Полученный из сохранения рубрикатор:");
			print(sIdealRubricator[1]);
		}
		else 
		{
			print("Рубрикаторы не совпадают");
			print("Полученный из сохранения рубрикатор:");
			print(sIdealRubricator[1]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("Сравниваем рубрикатор рубрики Недвижимость сайта полученный запросом с рубрикатором Недвижимости из сохранения".toUpperCase());
		if(sIdealRubricator[2].equals(sCurrentRubricatorRealt))
		{
			print("Рубрикаторы идентичны. Корректно");
			print("Полученный из сохранения рубрикатор:");
			print(sIdealRubricator[2]);
		}
		else 
		{
			print("Рубрикаторы не совпадают");
			print("Полученный из сохранения рубрикатор:");
			print(sIdealRubricator[2]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
		
	}
	//получение рубрикатора для автотеста
	private JSONObject GetRubricator(String sHost, String sRubricator) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		print("Получение рубрикатора сайта".toUpperCase());
		print("Параметры для запроса");
		print("category = "+ sRubricator);
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/categories")
    		.setParameter("category", sRubricator);
    	
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
    		print("Ответ сервера:" + jsonObject.toString(10) + "рубрикатора сайта получен");
    		return jsonObject;
    		
    	}
    	else
    	{
    		print("Не удалось получить рубрикатора сайта \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// сохранение данных в виде объекта класса JSON (в нем храним json ответ списка категорий корневых рубрик)
	@SuppressWarnings("unused")
	private void SaveJson(JString obj , String sNameFile) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(sNameFile);
		ObjectOutputStream ous = new ObjectOutputStream(fos);
		ous.writeObject(obj);
		ous.flush();
		ous.close();
		fos.close();
	}
	// восстановление данных 
	private String[] LoadJson(String sNameFile) throws IOException, ClassNotFoundException
	{
		JString Js;
		String sR[];
		FileInputStream fis = null;
		ObjectInputStream in = null;

		fis = new FileInputStream(sNameFile);
		in = new ObjectInputStream(fis);
		Js = (JString) in.readObject(); 
		in.close();
		fis.close();
		sR = Js.GetJsonString();
		return sR;
	}
	
	//Получение и проверка списка полей рубрики для подачи
	public void GetFieldsForAddAdvert(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, ClassNotFoundException
	{
		String sSearchCarsNewMoskva = "{category=cars/passenger/new/, region=russia/moskva-gorod/, advert_type=auto_new}";
		String sSearchRealtNewMasslenikovo = "{category=real-estate/apartments-sale/new/,  region=russia/moskovskaya-obl/volokolamskiy-r_n/maslennikovo-derevnya/, advert_type=realty_new}";
		String sSearchDataRealtNewKapovo = "{category=real-estate/apartments-sale/new/, region=russia/arhangelskaya-obl/kargopolskiy-r_n/kapovo-derevnya/, advert_type=realty_new}";
		String sSearchDataRealtNewArhangelsk = "{category=real-estate/apartments-sale/new/, region=russia/arhangelskaya-obl/arhangelsk-gorod/, advert_type=realty_new}";
		String sSearchDataTIYBookreaderKazan = "{category=electronics-technics/vacuum/, region=russia/tatarstan-resp/kazan-gorod/, advert_type=vacuum_cleaner}";
		
		JSONObject jData;
		@SuppressWarnings("unused")
		JString Js;
		String smas[] = new String [5];
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Получение и проверка списка полей рубрики для подачи - Тест".toUpperCase());

		print("\r\nШАГ 1");
		print("Получаем список полей рубрики для подачи Авто - Новые автомобили. Регион Москва.".toUpperCase());
		jData = GetCustomfieldsForAddAdvert(sHost, sSearchCarsNewMoskva);
		String sCusAutoMoskva = jData.toString(10);
		
		print("\r\nПолучаем список полей рубрики для подачи Недвижимость - Новостройки. Регион Масленниково(Московская обл)".toUpperCase());
		jData = GetCustomfieldsForAddAdvert(sHost, sSearchRealtNewMasslenikovo);
		String sCusRealtMasslenikovo = jData.toString(10);
		
		print("\r\nПолучаем список полей рубрики для подачи Недвижимость - Новостройки. Регион Капово(Архангельская обл)".toUpperCase());
		jData = GetCustomfieldsForAddAdvert(sHost, sSearchDataRealtNewKapovo);
		String sCusRealtKapovo = jData.toString(10);;
		
		print("\r\nПолучаем список полей рубрики для подачи Недвижимость - Новостройки. Регион Архангельск".toUpperCase());
		jData = GetCustomfieldsForAddAdvert(sHost, sSearchDataRealtNewArhangelsk);
		String sCusRealtArxangelsk = jData.toString(10);
	
		print("\r\nПолучаем список полей рубрики для подачи Электроника и техника - Пылесосы. Регион Казань".toUpperCase());
		jData = GetCustomfieldsForAddAdvert(sHost, sSearchDataTIYBookreaderKazan);
		String sCusTIYKazan = jData.toString(10);
		
		smas[0] = sCusAutoMoskva;
		smas[1] = sCusRealtMasslenikovo;
		smas[2] = sCusRealtKapovo;
		smas[3] = sCusRealtArxangelsk;
		smas[4] = sCusTIYKazan;
		
		//Js = new JString(smas); // запись полей для рубрик в файл
		//SaveJson(Js, "CustomfieldsForAdd.txt");
		
		String sIdealFields[] = LoadJson("CustomfieldsForAdd.txt");
		
		print("\r\nШАГ 2");
		print("Сравниваем список полей рубрики для подачи Авто - Новые автомобили. Регион Москва, полученных запросом, со списком полей из сохранения".toUpperCase());
		if(sIdealFields[0].equals(sCusAutoMoskva))
		{
			print("Списки полей для подачи идентичны. Корректно");
			print("Полученный из сохранения список полей для подачи:");
			print(sIdealFields[0]);
		}
		else 
		{
			print("Списки полей не совпадают");
			print("Полученный из сохранения список полей для подачи:");
			print(sIdealFields[0]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\nСравниваем список полей рубрики для подачи Недвижимость - Новостройки. Регион Масленниково(Московская обл), полученных запросом, со списком полей из сохранения".toUpperCase());
		if(sIdealFields[1].equals(sCusRealtMasslenikovo))
		{
			print("Списки полей для подачи идентичны. Корректно");
			print("Полученный из сохранения список полей для подачи:");
			print(sIdealFields[1]);
		}
		else 
		{
			print("Списки полей не совпадают");
			print("Полученный из сохранения список полей для подачи:");
			print(sIdealFields[1]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\nСравниваем список полей рубрики для подачи Недвижимость - Новостройки. Регион Капово(Архангельская обл), полученных запросом, со списком полей из сохранения".toUpperCase());
		if(sIdealFields[2].equals(sCusRealtKapovo))
		{
			print("Списки полей для подачи идентичны. Корректно");
			print("Полученный из сохранения список полей для подачи:");
			print(sIdealFields[2]);
		}
		else 
		{
			print("Списки полей не совпадают");
			print("Полученный из сохранения список полей для подачи:");
			print(sIdealFields[2]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\nСравниваем список полей рубрики для подачи Недвижимость - Новостройки. Регион Архангельск, полученных запросом, со списком полей из сохранения".toUpperCase());
		if(sIdealFields[3].equals(sCusRealtArxangelsk))
		{
			print("Списки полей для подачи идентичны. Корректно");
			print("Полученный из сохранения список полей для подачи:");
			print(sIdealFields[3]);
		}
		else 
		{
			print("Списки полей не совпадают");
			print("Полученный из сохранения список полей для подачи:");
			print(sIdealFields[3]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		print("\r\nСравниваем список полей рубрики для подачи Электроника и техника - Пылесосы. Регион Казань, полученных запросом, со списком полей из сохранения".toUpperCase());
		if(sIdealFields[4].equals(sCusTIYKazan))
		{
			print("Списки полей для подачи идентичны. Корректно");
			print("Полученный из сохранения список полей для подачи:");
			print(sIdealFields[4]);
		}
		else 
		{
			print("Списки полей не совпадают");
			print("Полученный из сохранения список полей для подачи:");
			print(sIdealFields[4]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
		
	}
	//получение списка полей рубрики для подачи для автотеста
	private JSONObject GetCustomfieldsForAddAdvert(String sHost, String sDataCustomfieldsAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Получение списка полей рубрики для подачи объявления".toUpperCase());
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
    		print("Ответ сервера:" + jsonObject.toString(10) + "список полей рубрики для подачи объявления получен");
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить список полей рубрики для подачи объявления \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	
	//Получение и проверка списка полей рубрики для редактирования
	public void GetFieldsForEditAdvert(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, ClassNotFoundException
	{
		String sSearchCarsNewMoskva = "{category=cars/passenger/new/, region=russia/moskva-gorod/, advert_type=auto_new}";
		String sSearchRealtNewMasslenikovo = "{category=real-estate/apartments-sale/new/,  region=russia/moskovskaya-obl/volokolamskiy-r_n/maslennikovo-derevnya/, advert_type=realty_new}";
		String sSearchDataRealtNewKapovo = "{category=real-estate/apartments-sale/new/, region=russia/arhangelskaya-obl/kargopolskiy-r_n/kapovo-derevnya/, advert_type=realty_new}";
		String sSearchDataRealtNewArhangelsk = "{category=real-estate/apartments-sale/new/, region=russia/arhangelskaya-obl/arhangelsk-gorod/, advert_type=realty_new}";
		String sSearchDataTIYBookreaderKazan = "{category=electronics-technics/vacuum/, region=russia/tatarstan-resp/kazan-gorod/, advert_type=vacuum_cleaner}";
		
		JSONObject jData;
		@SuppressWarnings("unused")
		JString Js;
		String smas[] = new String [5];
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Получение и проверка списка полей рубрики для редактирования - Тест".toUpperCase());

		print("\r\nШАГ 1");
		print("Получаем список полей рубрики для редактирования Авто - Новые автомобили. Регион Москва.".toUpperCase());
		jData = GetCastomfieldsForEditAdvert(sHost, sSearchCarsNewMoskva);
		String sCusAutoMoskva = jData.toString(10);
		
		print("\r\nПолучаем список полей рубрики для редактирования Недвижимость - Новостройки. Регион Масленниково(Московская обл)".toUpperCase());
		jData = GetCastomfieldsForEditAdvert(sHost, sSearchRealtNewMasslenikovo);
		String sCusRealtMasslenikovo = jData.toString(10);
		
		print("\r\nПолучаем список полей рубрики для редактирования Недвижимость - Новостройки. Регион Капово(Архангельская обл)".toUpperCase());
		jData = GetCastomfieldsForEditAdvert(sHost, sSearchDataRealtNewKapovo);
		String sCusRealtKapovo = jData.toString(10);;
		
		print("\r\nПолучаем список полей рубрики для редактирования Недвижимость - Новостройки. Регион Архангельск".toUpperCase());
		jData = GetCastomfieldsForEditAdvert(sHost, sSearchDataRealtNewArhangelsk);
		String sCusRealtArxangelsk = jData.toString(10);
		
		print("\r\nПолучаем список полей рубрики для редактирования Электроника и техника - Пылесосы. Регион Казань".toUpperCase());
		jData = GetCastomfieldsForEditAdvert(sHost, sSearchDataTIYBookreaderKazan);
		String sCusTIYKazan = jData.toString(10);
		
		smas[0] = sCusAutoMoskva;
		smas[1] = sCusRealtMasslenikovo;
		smas[2] = sCusRealtKapovo;
		smas[3] = sCusRealtArxangelsk;
		smas[4] = sCusTIYKazan;
		
		//Раскоментить если надо будет обновить значения и закомментить после обновления
		//Js = new JString(smas); // запись полей для рубрик в файл
		//SaveJson(Js, "CustomfieldsForEdit.txt");
		
		String sIdealFields[] = LoadJson("CustomfieldsForEdit.txt");
		
		print("\r\nШАГ 2");
		print("Сравниваем список полей рубрики для редактирования Авто - Новые автомобили. Регион Москва, полученных запросом, со списком полей из сохранения".toUpperCase());
		if(sIdealFields[0].equals(sCusAutoMoskva))
		{
			print("Списки полей для редактирования идентичны. Корректно");
			print("Полученный из сохранения список полей для подачи:");
			print(sIdealFields[0]);
		}
		else 
		{
			print("Списки полей не совпадают");
			print("Полученный из сохранения список полей для редактирования:");
			print(sIdealFields[0]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\nСравниваем список полей рубрики для редактирования Недвижимость - Новостройки. Регион Масленниково(Московская обл), полученных запросом, со списком полей из сохранения".toUpperCase());
		if(sIdealFields[1].equals(sCusRealtMasslenikovo))
		{
			print("Списки полей для редактирования идентичны. Корректно");
			print("Полученный из сохранения список полей для редактирования:");
			print(sIdealFields[1]);
		}
		else 
		{
			print("Списки полей не совпадают");
			print("Полученный из сохранения список полей для редактирования:");
			print(sIdealFields[1]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\nСравниваем список полей рубрики для редактирования Недвижимость - Новостройки. Регион Капово(Архангельская обл), полученных запросом, со списком полей из сохранения".toUpperCase());
		if(sIdealFields[2].equals(sCusRealtKapovo))
		{
			print("Списки полей для редактирования идентичны. Корректно");
			print("Полученный из сохранения список полей для редактирования:");
			print(sIdealFields[2]);
		}
		else 
		{
			print("Списки полей не совпадают");
			print("Полученный из сохранения список полей для редактирования:");
			print(sIdealFields[2]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\nСравниваем список полей рубрики для редактирования Недвижимость - Новостройки. Регион Архангельск, полученных запросом, со списком полей из сохранения".toUpperCase());
		if(sIdealFields[3].equals(sCusRealtArxangelsk))
		{
			print("Списки полей для редактирования идентичны. Корректно");
			print("Полученный из сохранения список полей для редактирования:");
			print(sIdealFields[3]);
		}
		else 
		{
			print("Списки полей не совпадают");
			print("Полученный из сохранения список полей для редактирования:");
			print(sIdealFields[3]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\nСравниваем список полей рубрики для редактирования Электроника и техника - Пылесосы. Регион Казань, полученных запросом, со списком полей из сохранения".toUpperCase());
		if(sIdealFields[4].equals(sCusTIYKazan))
		{
			print("Списки полей для редактирования идентичны. Корректно");
			print("Полученный из сохранения список полей для редактирования:");
			print(sIdealFields[4]);
		}
		else 
		{
			print("Списки полей не совпадают");
			print("Полученный из сохранения список полей для редактирования:");
			print(sIdealFields[4]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
		
	}
	// полусение полей для редактирования для автотеста
	private JSONObject GetCastomfieldsForEditAdvert(String sHost, String sDataCustomfieldsEditAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Получение списка полей рубрики для редактирования объявления".toUpperCase());
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
	    	print("Ответ сервера.\r\n" +jsonObject.toString(10)+ "\r\nCписок полей рубрики для редактирования объявлений получен.");
	    	return jsonObject;
			
    	}
    	else
    	{
    		print("Не удалось получить список полей рубрики для редактирования объявления\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	
	//Получение и проверка списка полей рубрики для фильтрации
	public void GetFieldsForSearchAdvert(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, ClassNotFoundException
	{
		String sSearchCarsMainMoskva = "{category=cars/, region=russia/moskva-goro/}";
		String sSearchCarsNewMoskva = "{category=cars/passenger/new/, region=russia/moskva-gorod/}";
		String sSearchRealtNewMoskva = "{category=real-estate/apartments-sale/new/, region=russia/moskva-gorod/}";
		String sSearchDataRealtMainArhangelsk = "{category=real-estate/, region=russia/arhangelskaya-obl/arhangelsk-gorod/}";
		String sSearchDataTIYMainKazan = "{category=electronics-technics/, region=russia/tatarstan-resp/kazan-gorod/}";
		String sSearchDataTIYBookreaderKazan = "{category=electronics-technics/bookreader/, region=russia/tatarstan-resp/kazan-gorod/}";
		
		JSONObject jData;
		@SuppressWarnings("unused")
		JString Js;
		String smas[] = new String [6];
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Получение и проверка списка полей рубрики для фильтрации - Тест".toUpperCase());

		print("\r\nШАГ 1");
		print("Получаем список полей фильтрации для рубрики Авто - Главная. Регион Москва".toUpperCase());
		jData = GetCustomfieldsForSearchAdvert(sHost, sSearchCarsMainMoskva);
		String sCusAutoMainMoskva = jData.toString(10);

		print("\r\nПолучаем список полей фильтрации для рубрики Авто - Новые автомобили. Регион Москва".toUpperCase());
		jData = GetCustomfieldsForSearchAdvert(sHost, sSearchCarsNewMoskva);
		String sCusAutoNewMoskva = jData.toString(10);
		
		print("\r\nПолучаем список полей фильтрации для рубрики Недвижимость - Главная. Регион Архангельск".toUpperCase());
		jData = GetCustomfieldsForSearchAdvert(sHost, sSearchDataRealtMainArhangelsk);
		String sCusRealtMainArxangelsk = jData.toString(10);
		
		print("\r\nПолучаем список полей фильтрации для рубрики Недвижимость - Новостройки. Регион Москва".toUpperCase());
		jData = GetCustomfieldsForSearchAdvert(sHost, sSearchRealtNewMoskva);
		String sCusRealtNewMoskva = jData.toString(10);;
		
		print("\r\nПолучаем список полей фильтрации для рубрики Электроника и техника - Главная. Регион Казань".toUpperCase());
		jData = GetCustomfieldsForSearchAdvert(sHost, sSearchDataTIYMainKazan);
		String sCusTIYMainKazan = jData.toString(10);
		
		print("\r\nПолучаем список полей фильтрации для рубрики Электроника и техника - Пылесосы. Регион Казань".toUpperCase());
		jData = GetCustomfieldsForSearchAdvert(sHost, sSearchDataTIYBookreaderKazan);
		String sCusTIYBookReaderKazan = jData.toString(10);
		
		smas[0] = sCusAutoMainMoskva;
		smas[1] = sCusAutoNewMoskva;
		smas[2] = sCusRealtMainArxangelsk;
		smas[3] = sCusRealtNewMoskva;
		smas[4] = sCusTIYMainKazan;
		smas[5] = sCusTIYBookReaderKazan;

		//Раскоментить если надо будет обновить значения и закомментить после обновления
		//Js = new JString(smas); // запись полей для рубрик в файл
		//SaveJson(Js, "CustomfieldsForSearch2.txt");
		
		
		String sIdealSearchFields[] = LoadJson("CustomfieldsForSearch2.txt");
		
		print("\r\nШАГ 2");
		print("Сравниваем список полей рубрики для фильтрации Авто - Главная. Регион Москва, полученных запросом, со списком полей из сохранения".toUpperCase());
		if(sIdealSearchFields[0].equals(sCusAutoMainMoskva))
		{
			print("Списки полей для фильтрации идентичны. Корректно");
			print("Полученный из сохранения список полей для фильтрации:");
			print(sIdealSearchFields[0]);
		}
		else 
		{
			print("Списки полей не совпадают");
			print("Полученный из сохранения список полей для фильтрации:");
			print(sIdealSearchFields[0]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		
		print("Сравниваем список полей рубрики для фильтрации Авто - Новые автомобили. Регион Москва, полученных запросом, со списком полей из сохранения".toUpperCase());
		if(sIdealSearchFields[1].equals(sCusAutoNewMoskva))
		{
			print("Списки полей для фильтрации идентичны. Корректно");
			print("Полученный из сохранения список полей для фильтрации:");
			print(sIdealSearchFields[1]);
		}
		else 
		{
			print("Списки полей не совпадают");
			print("Полученный из сохранения список полей для фильтрации:");
			print(sIdealSearchFields[1]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\"Сравниваем список полей рубрики для фильтрации Недвижимость - Главная. Регион Архангельск, полученных запросом, со списком полей из сохранения".toUpperCase());
		if(sIdealSearchFields[2].equals(sCusRealtMainArxangelsk))
		{
			print("Списки полей для фильтрации идентичны. Корректно");
			print("Полученный из сохранения список полей для фильтрации:");
			print(sIdealSearchFields[2]);
		}
		else 
		{
			print("Списки полей не совпадают");
			print("Полученный из сохранения список полей для фильтрации:");
			print(sIdealSearchFields[2]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\nСравниваем список полей рубрики для фильтрации Недвижимость - Новостройки. Регион Москва, полученных запросом, со списком полей из сохранения".toUpperCase());
		if(sIdealSearchFields[3].equals(sCusRealtNewMoskva))
		{
			print("Списки полей для фильтрации идентичны. Корректно");
			print("Полученный из сохранения список полей для фильтрации:");
			print(sIdealSearchFields[3]);
		}
		else 
		{
			print("Списки полей не совпадают");
			print("Полученный из сохранения список полей для фильтрации:");
			print(sIdealSearchFields[3]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		
		print("\r\nСравниваем список полей рубрики для фильтрации Электроника и техника - Главная. Регион Казань, полученных запросом, со списком полей из сохранения".toUpperCase());
		if(sIdealSearchFields[4].equals(sCusTIYMainKazan))
		{
			print("Списки полей для фильтрации идентичны. Корректно");
			print("Полученный из сохранения список полей для фильтрации:");
			print(sIdealSearchFields[4]);
		}
		else 
		{
			print("Списки полей не совпадают");
			print("Полученный из сохранения список полей для фильтрации:");
			print(sIdealSearchFields[4]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\nСравниваем список полей рубрики для фильтрации Электроника и техника - Электронные книги. Регион Казань, полученных запросом, со списком полей из сохранения".toUpperCase());
		if(sIdealSearchFields[5].equals(sCusTIYBookReaderKazan))
		{
			print("Списки полей для фильтрации идентичны. Корректно");
			print("Полученный из сохранения список полей для фильтрации:");
			print(sIdealSearchFields[5]);
		}
		else 
		{
			print("Списки полей не совпадают");
			print("Полученный из сохранения список полей для фильтрации:");
			print(sIdealSearchFields[5]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
		
	}
	// получение полей для фильтрации для автотестов
	private JSONObject GetCustomfieldsForSearchAdvert(String sHost, String sDataCustomfieldsEditAdvert) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Получение списка полей рубрики для фильтрации объявлений".toUpperCase());
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
    		print("Ответ сервера.\r\n" + jsonObject.toString(10) + "\r\nCписок полей рубрики для фильтрации объявлений получен");
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить список полей рубрики для фильтрации объявлений \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	//Получение и проверка списка субъектов
	public void GetRegionRussionFederation(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, ClassNotFoundException
	{
		//String sRegion = "/", sRegionMoskva = "russia/moskva-gorod/", sRegionArxangObl = "russia/arhangelskaya-obl/";
		JSONObject jData;
		@SuppressWarnings("unused")
		JString Js;
		String smas[] = new String [1];
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Получение и проверка списка субъектов РФ - Тест".toUpperCase());

		print("\r\nШАГ 1");
		print("Получаем cисок субъектов РФ".toUpperCase());
		jData = GetRegions(sHost);
		String sCurrentRegion = jData.toString(10); 
		
		smas[0] = sCurrentRegion;
		
		//Раскоментить если надо будет обновить значения и закомментить после обновления
		//Js = new JString(smas); // запись рубрикаторов в файл
		//SaveJson(Js, "Region.txt");
		
		String sIdealRegion[] = LoadJson("Region.txt");
		
		print("\r\nШАГ 2");
		print("Сравниваем список субъектов РФ полученный запросом, со списком субъектов РФ из сохранения".toUpperCase());
		if(sIdealRegion[0].equals(sCurrentRegion))
		{
			print("Списки идентичны. Корректно");
			print("Полученный из сохранения список субъектов РФ");
			print(sIdealRegion[0]);
		}
		else 
		{
			print("Списки не совпадают");
			print("Полученный из сохранения список субъектов РФ");
			print(sIdealRegion[0]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
		
	}	
	// получение списка субъектов РФ для атвотеста
	private JSONObject GetRegions(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{
		print("Получение списка субъектов РФ".toUpperCase());
		
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
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nсписок субъектов РФ получен");
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить список субъектов РФ \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	
	//Получени и проверка списка регионов с поддоменами
	public void GetRegionsWithDomen(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, ClassNotFoundException
	{
		JSONObject jData;
		@SuppressWarnings("unused")
		JString Js;
		String smas[] = new String [1];
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Получение и проверка списка регионов для которых заведены поддомены - Тест".toUpperCase());

		print("\r\nШАГ 1");
		print("Получаем cписок регионов с поддоменами РФ".toUpperCase());
		jData = GetRegionDomen(sHost);
		String sCurrentRegionDomen = jData.toString(10); 
		
		smas[0] = sCurrentRegionDomen;
		
		//Раскоментить если надо будет обновить значения и закомментить после обновления
		//Js = new JString(smas); // запись рубрикаторов в файл
		//SaveJson(Js, "RegionDomen.txt");
		
		String sIdealRegionDomen[] = LoadJson("RegionDomen.txt");
		
		print("\r\nШАГ 2");
		print("Сравниваем список регионов с поддоменами полученный запросом, со списком регионов с поддоменами из сохранения".toUpperCase());
		if(sIdealRegionDomen[0].equals(sCurrentRegionDomen))
		{
			print("Списки идентичны. Корректно");
			print("Полученный из сохранения список регионов с поддоменами");
			print(sIdealRegionDomen[0]);
		}
		else 
		{
			print("Списки не совпадают");
			print("Полученный из сохранения регионов с поддоменами");
			print(sIdealRegionDomen[0]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
	}
	// получение списка регинов с поддоменами для автотестов
	private JSONObject GetRegionDomen(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Получение списка городов, для которых заведены поддомены".toUpperCase());
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
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить список городов, для которых заведены поддомены \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	//Получение и проверка списка городов принадлежащего субъекту РФ
	public void GetCitiesInsideRegion(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, ClassNotFoundException
	{
		String sRegionMoskva = "russia/moskva-gorod/";
		String sRegionMoskObl = "russia/moskovskaya-obl";
		String sRegionArxangelskObl = "russia/arhangelskaya-obl/";
		JSONObject jData;
		@SuppressWarnings("unused")
		JString Js;
		String smas[] = new String [3];
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Получение и проверка списка городов принадлежащих региону - Тест".toUpperCase());

		print("\r\nШАГ 1");
		print("Получаем cписок городов принадлежащих региону Москва".toUpperCase());		
		jData = GetCitiesInReg(sHost, sRegionMoskva);
		String sCurrentCitiesInRegMoskva = jData.toString(10); 
		
		print("\r\nПолучаем cписок городов принадлежащих региону Московская область".toUpperCase());
		jData = GetCitiesInReg(sHost, sRegionMoskObl);
		String sCurrentCitiesInRegMoskovObl = jData.toString(10); 
		
		print("\r\nПолучаем cписок городов принадлежащих региону Архангельская область".toUpperCase());
		jData = GetCitiesInReg(sHost, sRegionArxangelskObl);
		String sCurrentCitiesInRegArxangelObl = jData.toString(10); 
		
		smas[0] = sCurrentCitiesInRegMoskva;
		smas[1] = sCurrentCitiesInRegMoskovObl;
		smas[2] = sCurrentCitiesInRegArxangelObl;
		
		//Раскоментить если надо будет обновить значения и закомментить после обновления
		//Js = new JString(smas); // запись рубрикаторов в файл
		//SaveJson(Js, "CitiesInRegion.txt");
		
		String sIdealCitiesInReg[] = LoadJson("CitiesInRegion.txt");
		
		print("\r\nШАГ 2");
		print("Сравниваем список городов принадлежащих региону Москва полученных запросом, со списком городов принадлежащих региону Москва из сохранения".toUpperCase());
		if(sIdealCitiesInReg[0].equals(sCurrentCitiesInRegMoskva))
		{
			print("Списки идентичны. Корректно");
			print("Полученный из сохранения список городов принадлежащих региону Москва");
			print(sIdealCitiesInReg[0]);
		}
		else 
		{
			print("Списки не совпадают");
			print("Полученный из сохранения список городов принадлежащих региону Москва");
			print(sIdealCitiesInReg[0]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\nСравниваем список городов принадлежащих региону Московская область полученных запросом, со списком городов принадлежащих региону Московская область из сохранения".toUpperCase());
		if(sIdealCitiesInReg[1].equals(sCurrentCitiesInRegMoskovObl))
		{
			print("Списки идентичны. Корректно");
			print("Полученный из сохранения список городов принадлежащих региону Московская область");
			print(sIdealCitiesInReg[1]);
		}
		else 
		{
			print("Списки не совпадают");
			print("Полученный из сохранения список городов принадлежащих региону Московская область");
			print(sIdealCitiesInReg[1]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\nСравниваем список городов принадлежащих региону Архангельская область полученных запросом, со списком городов принадлежащих региону Архангельская область из сохранения".toUpperCase());
		if(sIdealCitiesInReg[2].equals(sCurrentCitiesInRegArxangelObl))
		{
			print("Списки идентичны. Корректно");
			print("Полученный из сохранения список городов принадлежащих региону Архангельская область");
			print(sIdealCitiesInReg[2]);
		}
		else 
		{
			print("Списки не совпадают");
			print("Полученный из сохранения список городов принадлежащих региону Архангельская область");
			print(sIdealCitiesInReg[2]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
	}
	// получение списка городов принадлежащего определенному субъекту РФ
	private JSONObject GetCitiesInReg(String sHost, String sRegion) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Получение списка городов, принадлежащих определенному субъекту РФ".toUpperCase());
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
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить популярных городов \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}

	
	// Получение и проверка всех саджестов
	public void GetAllSuggest(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, ClassNotFoundException
	{
		String sCitiesSuggest = "{region=russia/moskovskaya-obl/, search_string=кам}";
		String sStreetsSuggest = "{region=russia/moskva-gorod/, search_string=кам}";
		String sHouseSugeest = "{street_id=9230, search_string=2}";
		String sDistrictSuggets = "{region=russia/sankt-peterburg-gorod/, search_string=кра}";
		String sMicroDistrictSuggest = "{region=russia/sankt-peterburg-gorod/, search_string=N+4}";
		String sAOSuggest = "{region=russia/moskva-gorod/, search_string=сев}";
		String sDirectionSuggest = "{region=russia/moskovskaya-obl/, search_string=кур}";
		String sHighWaySuggest = "{region=russia/moskovskaya-obl/, search_string=мин}";
		String sMetroSuggest = "{region=russia/nizhegorodskaya-obl/nizhniy-novgorod-gorod/, search_string=бур}";
		
		JSONObject jData;
		@SuppressWarnings("unused")
		JString Js;
		String smas[] = new String [9];
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Получение и проверка Населенных пунктов, улиц, домов, районов, микрорайонов, АО, направлений, шоссе, ст. метро Suggest - Тест".toUpperCase());
		
		print("\r\nШАГ 1");
		print("Получаем suggest для города и нас. пункта при поиске по слову \"кам\" для региона Московская область.".toUpperCase());		
		jData = GetCitiesSuggest(sHost, sCitiesSuggest);
		String sCurrentCitiesSuggest = jData.toString(10); 
		
		smas[0] = sCurrentCitiesSuggest;
		
		print("\r\nПолучаем suggest для улицы при поиске по слову \"кам\" для региона Москва.".toUpperCase());		
		jData = GetStreetsSuggest(sHost, sStreetsSuggest);
		String sCurrentStreetSuggest = jData.toString(10); 
		
		smas[1] = sCurrentStreetSuggest;
		
		print("\r\nПолучаем suggest для дома при поиске по цифре \"2\" для региона Барнаул и улице \"Ленина пр-кт\"(id_street=9230).".toUpperCase());		
		jData = GetHousesSuggest(sHost, sHouseSugeest);
		String sCurrentHouseSuggest = jData.toString(10); 
		
		smas[2] = sCurrentHouseSuggest;
		
		print("\r\nПолучаем suggest для района при поиске по слову \"кра\" для региона Санкт-Петербург.".toUpperCase());	
		jData = GetDistrictSuggest(sHost, sDistrictSuggets);
		String sCurrentDistrictSuggest = jData.toString(10); 
		
		smas[3] = sCurrentDistrictSuggest;
		
		print("\r\nПолучаем suggest для микрорайона при поиске по слову \"N 4\" для региона Санкт-Петербург.".toUpperCase());	
		jData = GetMicroDistrictSuggest(sHost, sMicroDistrictSuggest);
		String sCurrentMicroDistrictSuggest = jData.toString(10); 
		
		smas[4] = sCurrentMicroDistrictSuggest;
		
		print("\r\nПолучаем suggest для АО при поиске по слову \"сев\" для региона Москва.".toUpperCase());	
		jData = GetAOSuggest(sHost, sAOSuggest);
		String sCurrentAOSuggest = jData.toString(10); 
		
		smas[5] = sCurrentAOSuggest;
		
		print("\r\nПолучаем suggest для направлений при поиске по слову \"кур\" для региона Московская область.".toUpperCase());	
		jData = GetDirectionSuggest(sHost, sDirectionSuggest);
		String sCurrentDirectionSuggest = jData.toString(10); 
		
		smas[6] = sCurrentDirectionSuggest;
		
		print("\r\nПолучаем suggest для шоссе при поиске по слову \"мин\" для региона Московская область.".toUpperCase());	
		jData = GetHighwaySuggest(sHost, sHighWaySuggest);
		String sCurrentHighWaySuggest = jData.toString(10); 
		
		smas[7] = sCurrentHighWaySuggest;
		
		print("\r\nПолучаем suggest для метро при поиске по слову \"бур\" для региона Нижний Новгород.".toUpperCase());	
		jData = GetMetroSuggest(sHost, sMetroSuggest);
		String sCurrentMetroSuggest = jData.toString(10); 
		
		smas[8] = sCurrentMetroSuggest;

		
		//Раскоментить если надо будет обновить значения и закомментить после обновления
		//Js = new JString(smas); // запись рубрикаторов в файл
		//SaveJson(Js, "Suggest.txt");
		
		String sIdealSuggest[] = LoadJson("Suggest.txt");
		
		print("\r\nШАГ 2");
		print("Сравниваем suggest для города и нас. пункта при поиске по слову \"кам\" для региона Московская область полученных запросом, с suggest для города и нас. пункта при поиске по слову \"кам\" для региона Московская область из сохранения".toUpperCase());
		if(sIdealSuggest[0].equals(sCurrentCitiesSuggest))
		{
			print("Suggest идентичны. Корректно");
			print("Полученный из сохранения suggest для города и нас. пункта при поиске по слову \"кам\" для региона Московская область:");
			print(sIdealSuggest[0]);
		}
		else 
		{
			print("Списки не совпадают");
			print("Полученный из сохранения suggest для города и нас. пункта при поиске по слову \"кам\" для региона Московская область:");
			print(sIdealSuggest[0]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}

		
		print("\r\nСравниваем suggest для улицы при поиске по слову \"кам\" для региона Москва полученных запросом, с suggest для улицы при поиске по слову \"кам\" для региона Москва из сохранения.".toUpperCase());
		if(sIdealSuggest[1].equals(sCurrentStreetSuggest))
		{
			print("Suggest идентичны. Корректно");
			print("Полученный из сохранения suggest для улицы при поиске по слову \"кам\" для региона Москва:");
			print(sIdealSuggest[1]);
		}
		else 
		{
			print("Списки не совпадают");
			print("Полученный из сохранения suggest для улицы при поиске по слову \"кам\" для региона Москва:");
			print(sIdealSuggest[1]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\nСравниваем suggest для дома при поиске по цифре \"2\" для региона Барнаул и улице \"Ленина пр-кт\"(id_street=9230) полученных запросом, с suggest для дома при поиске по цифре \"2\" для региона Барнаул и улице \"Ленина пр-кт\"(id_street=9230) из сохранения.".toUpperCase());
		if(sIdealSuggest[2].equals(sCurrentHouseSuggest))
		{
			print("Suggest идентичны. Корректно");
			print("Полученный из сохранения suggest для дома при поиске по цифре \"2\" для региона Барнаул и улице \"Ленина пр-кт\"(id_street=9230):");
			print(sIdealSuggest[2]);
		}
		else 
		{
			print("Списки не совпадают");
			print("Полученный из сохранения suggest для дома при поиске по цифре \"2\" для региона Барнаул и улице \"Ленина пр-кт\"(id_street=9230):");
			print(sIdealSuggest[2]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\nСравниваем suggest для района при поиске по слову \"кра\" для региона Санкт-Петербург полученных запросом, с suggest для района при поиске по слову \"кра\" для региона Санкт-Петербург из сохранения.".toUpperCase());
		if(sIdealSuggest[3].equals(sCurrentDistrictSuggest))
		{
			print("Suggest идентичны. Корректно");
			print("Полученный из сохранения suggest для района при поиске по слову \"кра\" для региона Санкт-Петербург:");
			print(sIdealSuggest[3]);
		}
		else 
		{
			print("Списки не совпадают");
			print("Полученный из сохранения suggest для района при поиске по слову \"кра\" для региона Санкт-Петербург:");
			print(sIdealSuggest[3]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\nСравниваем suggest для микрорайона при поиске по слову \"N 4\" для региона Санкт-Петербург полученных запросом, с suggest для микрорайона при поиске по слову \"N 4\" для региона Санкт-Петербург из сохранения.".toUpperCase());
		if(sIdealSuggest[4].equals(sCurrentMicroDistrictSuggest))
		{
			print("Suggest идентичны. Корректно");
			print("Полученный из сохранения suggest для микрорайона при поиске по слову \"N 4\" для региона Санкт-Петербург:");
			print(sIdealSuggest[4]);
		}
		else 
		{
			print("Списки не совпадают");
			print("Полученный из сохранения suggest для микрорайона при поиске по слову \"N 4\" для региона Санкт-Петербург:");
			print(sIdealSuggest[4]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\nСравниваем suggest для АО при поиске по слову \"сев\" для региона Москва полученных запросом, с suggest для АО при поиске по слову \"сев\" для региона Москва из сохранения.".toUpperCase());
		if(sIdealSuggest[5].equals(sCurrentAOSuggest))
		{
			print("Suggest идентичны. Корректно");
			print("Полученный из сохранения suggest для АО при поиске по слову \"сев\" для региона Москва:");
			print(sIdealSuggest[5]);
		}
		else 
		{
			print("Списки не совпадают");
			print("Полученный из сохранения suggest для АО при поиске по слову \"сев\" для региона Москва:");
			print(sIdealSuggest[5]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		
		print("\r\nСравниваем suggest для направлений при поиске по слову \"кур\" для региона Московская область полученных запросом, с suggest для направлений при поиске по слову \"кур\" для региона Московская область из сохранения.".toUpperCase());
		if(sIdealSuggest[6].equals(sCurrentDirectionSuggest))
		{
			print("Suggest идентичны. Корректно");
			print("Полученный из сохранения suggest для направлений при поиске по слову \"кур\" для региона Московская область:");
			print(sIdealSuggest[6]);
		}
		else 
		{
			print("Списки не совпадают");
			print("Полученный из сохранения suggest для направлений при поиске по слову \"кур\" для региона Московская область:");
			print(sIdealSuggest[6]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("\r\nСравниваем suggest для шоссе при поиске по слову \"мин\" для региона Московская область полученных запросом, с suggest для шоссе при поиске по слову \"мин\" для региона Московская область из сохранения.".toUpperCase());
		if(sIdealSuggest[7].equals(sCurrentHighWaySuggest))
		{
			print("Suggest идентичны. Корректно");
			print("Полученный из сохранения suggest для шоссе при поиске по слову \"мин\" для региона Московская область:");
			print(sIdealSuggest[7]);
		}
		else 
		{
			print("Списки не совпадают");
			print("Полученный из сохранения suggest для шоссе при поиске по слову \"мин\" для региона Московская область:");
			print(sIdealSuggest[7]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		
		print("\r\nСравниваем suggest для метро при поиске по слову \"бур\" для региона Нижний Новгород полученных запросом, с suggest для метро при поиске по слову \"бур\" для региона Нижний Новгород из сохранения.".toUpperCase());
		if(sIdealSuggest[8].equals(sCurrentMetroSuggest))
		{
			print("Suggest идентичны. Корректно");
			print("Полученный из сохранения suggest для метро при поиске по слову \"бур\" для региона Нижний Новгород:");
			print(sIdealSuggest[8]);
		}
		else 
		{
			print("Списки не совпадают");
			print("Полученный из сохранения suggest для метро при поиске по слову \"бур\" для региона Нижний Новгород:");
			print(sIdealSuggest[8]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());
		
	}
	// получение саджеста городов и нас. пунктов для автотеста
	private JSONObject GetCitiesSuggest(String sHost, String sDataCitiesSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Поиск городов и населенных пунктов по названию (саджест)".toUpperCase());
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
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить городов и населенных пунктов по названию (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// получение саджеста улицы для автотеста
	private JSONObject GetStreetsSuggest(String sHost, String sDataStreetsSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Получение списка улиц (саджест)".toUpperCase());
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
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить список улиц (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}	
	// получение саджеста улицы для автотеста
	private JSONObject GetHousesSuggest(String sHost, String sDataHousesSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Получение списка домов улицы (саджест)".toUpperCase());
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
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\n список домов улицы (саджест) получен");
    		return jsonObject;  		
    	}
    	else
    	{
    		print("Не удалось получить список домов улицы (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}	
	}	
	// получение саджестов районов для автотестов
	private JSONObject GetDistrictSuggest(String sHost, String sDataDistrictSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Получение списка районов (саджест)".toUpperCase());
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
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nсписок районов (саджест) получен \r\n");
    		return jsonObject;
    		
    	}
    	else
    	{
    		print("Не удалось получить список районов (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}	
	}	
	// получение саджестов микрорайона для автотестов
	private JSONObject GetMicroDistrictSuggest(String sHost, String sDataMicroDistrictSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Получение списка микрорайонов (саджест)".toUpperCase());
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
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить список микрорайонов (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// получение саджестов АО для автотестов
	private JSONObject GetAOSuggest(String sHost, String sAOSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Получение списка административных округов (саджест)".toUpperCase());
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
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить список административных округов (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// получение саджестов направлений для автотестов
	private JSONObject GetDirectionSuggest(String sHost, String sDataDirectionSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Получение списка направлений (саджест)".toUpperCase());
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
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nсписок направлений (саджест) получен \r\n");
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить список направлений (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}	
	}	
	// получение саджестов шоссе для автотеста
	private JSONObject GetHighwaySuggest(String sHost, String sDataHighwaySuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Получение списка шоссе (саджест)".toUpperCase());
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
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nсписок шоссе (саджест) получен\r\n");
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить список шоссе (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// получение саджестов станций метро для автотеста
	private JSONObject GetMetroSuggest(String sHost, String sDataMetroSuggest) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Получение списка станций метро (саджест)".toUpperCase());
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
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nсписок станций метро (саджест) получен\r\n");
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить список станций метро (саджест) \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	
	//Получение и проверка списка валют
	public void GetCurrencies(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, ClassNotFoundException
	{
		JSONObject jData;
		@SuppressWarnings("unused")
		JString Js;
		String smas[] = new String [1];
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Получение списка валют - Тест".toUpperCase());
		
		print("\r\nШАГ 1");
		print("Получаем список валют.".toUpperCase());		
		jData = GetCur(sHost);
		String sCurrency = jData.toString(10); 
		
		smas[0] = sCurrency;
		
		//Раскоментить если надо будет обновить значения и закомментить после обновления
		//Js = new JString(smas); // запись рубрикаторов в файл
		//SaveJson(Js, "Currency.txt");
		
		String sIdealCurr[] = LoadJson("Currency.txt");
		
		print("\r\nШАГ 2");
		print("Сравниваем список валют полученных запросом, с списком валют из сохранения".toUpperCase());
		if(sIdealCurr[0].equals(sCurrency))
		{
			print("Списки валют идентичны. Корректно");
			print("Полученный из сохранения список валют :");
			print(sIdealCurr[0]);
		}
		else 
		{
			print("Списки валют не совпадают");
			print("Полученный из сохранения список валют :");
			print(sIdealCurr[0]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());

		
	}
	// получение списка валют для автотеста
	private JSONObject GetCur(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Получение списка валют".toUpperCase());
	
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
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nсписок валют получен");
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить список валют \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(1));
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	
	//Получение и проверка словарей
	public void GetDictionary(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, ClassNotFoundException
	{
		String sNameDictionaryCarCom = "Car makers commercial";
		String sNameDictionaryCarBody = "cfd_bodytype";
		String sNameDictionaryColor = "cf_color";
		JSONObject jData;
		@SuppressWarnings("unused")
		JString Js;
		String smas[] = new String [3];
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Получение словарей - Тест".toUpperCase());
		
		print("\r\nШАГ 1");
		print("Получаем словарь Car makers commercial.".toUpperCase());		
		jData = GetDic(sHost, sNameDictionaryCarCom);
		String sCurrentDicCarCom = jData.toString(10); 
		
		smas[0] = sCurrentDicCarCom;
		
		print("Получаем словарь cfd_bodytype.".toUpperCase());		
		jData = GetDic(sHost, sNameDictionaryCarBody);
		String sCurrentDicCarBody = jData.toString(10); 
		
		smas[1] = sCurrentDicCarBody;
		
		
		print("Получаем словарь cf_color.".toUpperCase());		
		jData = GetDic(sHost, sNameDictionaryColor);
		String sCurrentDicColor = jData.toString(10); 
		
		smas[2] = sCurrentDicColor;
		
		//Раскоментить если надо будет обновить значения и закомментить после обновления
		//Js = new JString(smas); // запись в файл
		//SaveJson(Js, "Dictionary.txt");
		
		String sIdealDic[] = LoadJson("Dictionary.txt");
		
		print("\r\nШАГ 2");
		print("Сравниваем словарь Car makers commercial полученный запросом, со словарем Car makers commercial из сохранения".toUpperCase());
		if(sIdealDic[0].equals(sCurrentDicCarCom))
		{
			print("Словари идентичны. Корректно");
			print("Полученный из сохранения словарь:");
			print(sIdealDic[0]);
		}
		else 
		{
			print("Словари не совпадают");
			print("Полученный из сохранения словарь:");
			print(sIdealDic[0]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		
		print("\r\nСравниваем словарь cfd_bodytype полученный запросом, со словарем cfd_bodytype из сохранения".toUpperCase());
		if(sIdealDic[1].equals(sCurrentDicCarBody))
		{
			print("Словари идентичны. Корректно");
			print("Полученный из сохранения словарь:");
			print(sIdealDic[1]);
		}
		else 
		{
			print("Словари не совпадают");
			print("Полученный из сохранения словарь:");
			print(sIdealDic[1]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		
		print("\r\nСравниваем словарь cf_color полученный запросом, со словарем cf_color из сохранения".toUpperCase());
		if(sIdealDic[2].equals(sCurrentDicColor))
		{
			print("Словари идентичны. Корректно");
			print("Полученный из сохранения словарь:");
			print(sIdealDic[2]);
		}
		else 
		{
			print("Словари не совпадают");
			print("Полученный из сохранения словарь:");
			print(sIdealDic[2]);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
		
		
		print("------------------------------------------------------------------------------------------------------------");
    	print("Тест завершен успешно".toUpperCase());

		
	}
	// получение словаря
	private JSONObject GetDic(String sHost, String sNameDict) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("Получение значений словаря".toUpperCase());
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
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nзначения словаря получены\r\n");
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить значения словаря \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	
	
	//Проверка платных продуктов на этапе подачи и в ЛК, проверка бесплатных продуктов в ЛК
	public void AddAdvertCheckPaidAndFreeProducts(String sHost) throws URISyntaxException, IOException, JSONException, ExceptFailTest, NumberFormatException, InterruptedException, ClassNotFoundException
	{
		String sIdAutoPaid, sIdTIU_Free, sIdTIU_Free2, sIdTIU_Free3, sIdTIU_Paid; 
		String sLogin = Proper.GetProperty("login_authOP3");
		String sPassword = Proper.GetProperty("password");
		String sAuth_token = "";
		JSONObject jData;
		InnerDataHM objAuto, objTIY;
		@SuppressWarnings("unused")
		JString Js, Js2, Js3, Js4; // объекты для хранения ответов на запросы
		String smasInStepAdd[] = new String [3];
		String smasAutoInLK[] = new String [6];
		String smasElectronInLK[] = new String [6];
		String smasElectronPaidInLK[] = new String [6];
		String smasFree[] = new String [2];
		
		print("------------------------------------------------------------------------------------------------------------");
		print("Подача платного объявления, подача трех бесплатных объявлений, подача платного объявления сверх лимита бесплатных - Тест".toUpperCase()+"\r\n");
		
		// авторизация
		print("\r\nАвторизация пользователем - " + sLogin);
		sAuth_token = Authorization_1_1(sHost, sLogin, sPassword);
		
		
		// подача платного объявлений
		print("\r\nШАГ 1");
		print("Подача 1 платного объявления".toUpperCase());  	
		print("\r\nПодача объявления в рубрику Авто - Новые авто. Регион Москва".toUpperCase());
		objAuto = PostAdvert(sHost, mas_Advertisment, mas_Auto2, sAuth_token, "category_auto_new", "image");
		sIdAutoPaid = objAuto.GetID();
    	
		print("\r\nШАГ 1-1");
		print("\r\nПодача 1 бесплатного объявления");
    	print("Подача объявления в рубрику Электроника и техника - Пылесосы. Регион Казань".toUpperCase());
    	objTIY = PostAdvert(sHost, mas_Advertisment, mas_TIY2, sAuth_token, "category_electron", "image3");
    	sIdTIU_Free = objTIY.GetID();
    	
    	print("\r\nШАГ 1-2");
    	print("\r\nПодача 2 бесплатного объявления");
    	print("Подача объявления в рубрику Электроника и техника - Пылесосы. Регион Казань".toUpperCase());
    	objTIY = PostAdvert(sHost, mas_Advertisment, mas_TIY2, sAuth_token, "category_electron", "image3");
    	sIdTIU_Free2 = objTIY.GetID();
    	
    	print("\r\nШАГ 1-3");
    	print("\r\nПодача 3 бесплатного объявления");
    	print("Подача объявления в рубрику Электроника и техника - Пылесосы. Регион Казань".toUpperCase());
    	objTIY = PostAdvert(sHost, mas_Advertisment, mas_TIY2, sAuth_token, "category_electron", "image3");
    	sIdTIU_Free3 = objTIY.GetID();
    	
    	print("\r\nШАГ 1-4");
    	print("\r\nПодача 4 объявления, объявление платное, сверх лимита бесплатных");
    	print("Подача объявления в рубрику Электроника и техника - Пылесосы. Регион Казань".toUpperCase());
    	objTIY = PostAdvert(sHost, mas_Advertisment, mas_TIY2, sAuth_token, "category_electron", "image3");
    	sIdTIU_Paid = objTIY.GetID();
  
    	try
		{
	    	// проверка платных продуктов на этапе подачи
	    	print("\r\nШАГ 2");
			print("Получение списка платных продуктов для объявлений на этапе подачи.".toUpperCase());	
			print("\r\nПолучаем список платных продуктов на этапе подачи, для платного объявления в рубрику Авто - Новые автомобили. Регион Москва.".toUpperCase());		
			jData = GetPaidProductsToStepToAdd(sHost, sIdAutoPaid);
			String sCurrentAutoPaid = jData.toString(10); 
			CheckCountId(sCurrentAutoPaid, sIdAutoPaid, 6,  "Авто - Новые автомобили. Регион Москва ");
			sCurrentAutoPaid = sCurrentAutoPaid.replace(sIdAutoPaid, "*****");
			smasInStepAdd[0] = sCurrentAutoPaid;
		
			print("\r\nШАГ 2-1");
			print("\r\nПолучаем список платных продуктов на этапе подачи, для бесплатного объявления в рубрику Электроника и техника - Пылесосы. Регион Казань.".toUpperCase());		
			jData = GetPaidProductsToStepToAdd(sHost, sIdTIU_Free);
			String sCurrentElectronFree = jData.toString(10); 
			CheckCountId(sCurrentElectronFree, sIdTIU_Free, 2,  "Электроника и техника - Пылесосы. Регион Казань ");
			sCurrentElectronFree = sCurrentElectronFree.replace(sIdTIU_Free, "*****");
			smasInStepAdd[1] = sCurrentElectronFree;
	    	
			print("\r\nШАГ 2-2");
			print("\r\nПолучаем список платных продуктов на этапе подачи, для платного объявления в рубрику Электроника и техника - Пылесосы. Регион Казань.".toUpperCase());		
			jData = GetPaidProductsToStepToAdd(sHost, sIdTIU_Paid);
			String sCurrentElectronPaid = jData.toString(10); 
			CheckCountId(sCurrentElectronPaid, sIdTIU_Paid, 6,  "Электроника и техника - Пылесосы(платное объявление-сверх лимита бесплатных). Регион Казань ");
			sCurrentElectronPaid = sCurrentElectronPaid.replace(sIdTIU_Paid, "*****");
			smasInStepAdd[2] = sCurrentElectronPaid;
	    	
	    	//Раскоментить если надо будет обновить значения и закомментить после обновления
	    	//Js = new JString(smasInStepAdd); // запись в файл
	    	//SaveJson(Js, "PaidProductInAddStep.txt");
	    			
	    	String sIdealPaid[] = LoadJson("PaidProductInAddStep.txt");
	    	
	    	print("\r\nШАГ 3");
			print("Сравниваем список платных продуктов для платного объявления в рубрику Авто - Новые автомобили. Регион Москва на этапе подачи полученных запросом, с списком платных продуктов для платного объявления в рубрику Авто - Новые автомобили. Регион Москва из сохранения".toUpperCase());
			CheckAnswerForPaidProduct(sIdealPaid[0],sCurrentAutoPaid);
			
			print("\r\nШАГ 3-1");
			print("Сравниваем список платных продуктов для бесплатного объявления в рубрику Электроника и техника - Пылесосы. Регион Казань на этапе подачи полученных запросом, с списком платных продуктов для бесплатного объявления в рубрику Электроника и техника - Пылесосы. Регион Казань из сохранения".toUpperCase());
			CheckAnswerForPaidProduct(sIdealPaid[1],sCurrentElectronFree);
			
			print("\r\nШАГ 3-2");
			print("Сравниваем список платных продуктов для платного объявления в рубрику Электроника и техника - Пылесосы. Регион Казань на этапе подачи полученных запросом, с списком платных продуктов для платного объявления в рубрику Электроника и техника - Пылесосы. Регион Казань из сохранения".toUpperCase());
			CheckAnswerForPaidProduct(sIdealPaid[2],sCurrentElectronPaid);
			
			
			
			//проверка платных продуктов в ЛК Авто (платное объявление)
			print("\r\nШАГ 4");
			print("Получение списка платных продуктов для объявлений в ЛК. Рубрика Авто - Новые автомобили. Регион Москва.".toUpperCase());	
			print("\r\nПолучаем список платных продуктов в ЛК, для платного объявления в рубрику Авто - Новые автомобили. Регион Москва. Объявление неоплачено,  неактивно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdAutoPaid);
			sCurrentAutoPaid = jData.toString(10); 
			CheckCountId(sCurrentAutoPaid, sIdAutoPaid, 2,  "Авто - Новые автомобили. Регион Москва ");
			sCurrentAutoPaid = sCurrentAutoPaid.replace(sIdAutoPaid, "*****");
			smasAutoInLK[0] = sCurrentAutoPaid;
			
			print("\r\nШАГ 4-1");
			print("\r\nОплачиваем(активируем объявление)".toUpperCase());
			ActivateAdvert(sHost, sAuth_token, sIdAutoPaid, true, 1);
			
			print("\r\nШАГ 4-2");
			print("\r\nПолучаем список платных продуктов в ЛК, для платного объявления в рубрику Авто - Новые автомобили. Регион Москва. Объявление оплачено, активно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdAutoPaid);
			String sCurrentAutoPaid2 = jData.toString(10); 
			CheckCountId(sCurrentAutoPaid2, sIdAutoPaid, 6,  "Авто - Новые автомобили. Регион Москва ");
			sCurrentAutoPaid2 = sCurrentAutoPaid2.replace(sIdAutoPaid, "*****");
			smasAutoInLK[1] = sCurrentAutoPaid2;
			
			print("\r\nШАГ 4-3");
			print("\r\nВыделяем объявление".toUpperCase());
			HighLightAdvert(sHost, sAuth_token, sIdAutoPaid, true, 1);
			
			print("\r\nШАГ 4-4");
			print("\r\nПолучаем список платных продуктов в ЛК, для платного объявления в рубрику Авто - Новые автомобили. Регион Москва. Объявление выделено, активно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdAutoPaid);
			String sCurrentAutoPaid3 = jData.toString(10); 
			CheckCountId(sCurrentAutoPaid3, sIdAutoPaid, 4,  "Авто - Новые автомобили. Регион Москва ");
			sCurrentAutoPaid3 = sCurrentAutoPaid3.replace(sIdAutoPaid, "*****");
			smasAutoInLK[2] = sCurrentAutoPaid3;
			
			print("\r\nШАГ 4-5");
			print("\r\nДеактивируем объявление".toUpperCase());
			DeactivateAdvert(sHost, sAuth_token, sIdAutoPaid, 1);
			
			print("\r\nШАГ 4-6");
			print("\r\nПолучаем список платных продуктов в ЛК, для платного объявления в рубрику Авто - Новые автомобили. Регион Москва. Объявление выделено, неактивно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdAutoPaid);
			String sCurrentAutoPaid4 = jData.toString(10); 
			CheckCountId(sCurrentAutoPaid4, sIdAutoPaid, 2,  "Авто - Новые автомобили. Регион Москва ");
			sCurrentAutoPaid4 = sCurrentAutoPaid4.replace(sIdAutoPaid, "*****");
			smasAutoInLK[3] = sCurrentAutoPaid4;
			
			print("\r\nШАГ 4-7");
			print("\r\nНазначаем премиум объявлению".toUpperCase());
			SetPremiumAdvert(sHost, sAuth_token, sIdAutoPaid, true, 1);
			
			print("\r\nШАГ 4-8");
			print("\r\nПолучаем список платных продуктов в ЛК, для платного объявления в рубрику Авто - Новые автомобили. Регион Москва. Объявлению назначен премиум, активно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdAutoPaid);
			String sCurrentAutoPaid5 = jData.toString(10); 
			CheckCountId(sCurrentAutoPaid5, sIdAutoPaid, 4,  "Авто - Новые автомобили. Регион Москва ");
			sCurrentAutoPaid5 = sCurrentAutoPaid5.replace(sIdAutoPaid, "*****");
			smasAutoInLK[4] = sCurrentAutoPaid5;
			
			print("\r\nШАГ 4-9");
			print("\r\nДеактивируем объявление".toUpperCase());
			DeactivateAdvert(sHost, sAuth_token, sIdAutoPaid, 1);
			
			print("\r\nШАГ 4-10");
			print("\r\nПолучаем список платных продуктов в ЛК, для платного объявления в рубрику Авто - Новые автомобили. Регион Москва. Объявлению назначен премиум, неактивно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdAutoPaid);
			String sCurrentAutoPaid6 = jData.toString(10); 
			CheckCountId(sCurrentAutoPaid6, sIdAutoPaid, 2,  "Авто - Новые автомобили. Регион Москва ");
			sCurrentAutoPaid6 = sCurrentAutoPaid6.replace(sIdAutoPaid, "*****");
			smasAutoInLK[5] = sCurrentAutoPaid6;
			
			
			//Раскоментить если надо будет обновить значения и закомментить после обновления
	    	//Js2 = new JString(smasAutoInLK); // запись в файл
	    	//SaveJson(Js2, "PaidProductAutoLK.txt");
	    			
	    	String sIdealPaidAutoLK[] = LoadJson("PaidProductAutoLK.txt");
			
	    	print("\r\nШАГ 5");
			print("Сравниваем список платных продуктов для объявления в ЛК рубрика Авто - Новые Авто(Москва) полученного запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление неоплачено, неактивно");
			CheckAnswerForPaidProduct(sIdealPaidAutoLK[0],sCurrentAutoPaid);
			
			print("\r\nШАГ 5-1");
			print("Сравниваем список платных продуктов для объявления в ЛК рубрика Авто - Новые Авто(Москва) полученного запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление оплачено, активно");
			CheckAnswerForPaidProduct(sIdealPaidAutoLK[1],sCurrentAutoPaid2);
			
			print("\r\nШАГ 5-2");
			print("Сравниваем список платных продуктов для объявления в ЛК рубрика Авто - Новые Авто(Москва) полученного запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление выделено, активно");
			CheckAnswerForPaidProduct(sIdealPaidAutoLK[2],sCurrentAutoPaid3);
			
			print("\r\nШАГ 5-3");
			print("Сравниваем список платных продуктов для объявления в ЛК рубрика Авто - Новые Авто(Москва) полученного запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление выделено, неактивно");
			CheckAnswerForPaidProduct(sIdealPaidAutoLK[3],sCurrentAutoPaid4);
			
			print("\r\nШАГ 5-4");
			print("Сравниваем список платных продуктов для объявления в ЛК рубрика Авто - Новые Авто(Москва) полученного запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление премиум, активно");
			CheckAnswerForPaidProduct(sIdealPaidAutoLK[4],sCurrentAutoPaid5);
			
			print("\r\nШАГ 5-5");
			print("Сравниваем список платных продуктов для объявления в ЛК рубрика Авто - Новые Авто(Москва) полученного запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление премиум, неактивно");
			CheckAnswerForPaidProduct(sIdealPaidAutoLK[5],sCurrentAutoPaid6);
			
			
			//проверка платных продуктов в ЛК Пылесосы (бесплатное объявление)
			print("\r\nШАГ 6");
			print("Получение списка платных продуктов для объявлений в ЛК. Электроника и техника - Пылесосы. Регион Казань.".toUpperCase());	
			print("\r\nПолучаем список платных продуктов в ЛК, для платного объявления в рубрику Электроника и техника - Пылесосы. Регион Казань. Объявление оплачено, активно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdTIU_Free);
			sCurrentElectronFree = jData.toString(10); 
			CheckCountId(sCurrentElectronFree, sIdTIU_Free, 4,  " Электроника и техника - Пылесосы. Регион Казань ");
			sCurrentElectronFree = sCurrentElectronFree.replace(sIdTIU_Free, "*****");
			smasElectronInLK[0] = sCurrentElectronFree;
			
			print("\r\nШАГ 6-1");
			print("\r\nДеактивируем объявление".toUpperCase());
			DeactivateAdvert(sHost, sAuth_token, sIdTIU_Free, 1);
			
			print("\r\nШАГ 6-2");
			print("\r\nПолучаем список платных продуктов в ЛК, для платного объявления в рубрику Электроника и техника - Пылесосы. Регион Казань. Объявление оплачено, неактивно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdTIU_Free);
			String sCurrentElectronFree2 = jData.toString(10); 
			CheckCountId(sCurrentElectronFree2, sIdTIU_Free, 0,  " Электроника и техника - Пылесосы. Регион Казань ");
			sCurrentElectronFree2 = sCurrentElectronFree2.replace(sIdTIU_Free, "*****");
			smasElectronInLK[1] = sCurrentElectronFree2;
			
			print("\r\nШАГ 6-3");
			print("\r\nАктивируем объявление".toUpperCase());
			ActivateAdvert(sHost, sAuth_token, sIdTIU_Free, false, 1);
			
			print("\r\nШАГ 6-4");
			print("\r\nВыделяем объявление".toUpperCase());
			HighLightAdvert(sHost, sAuth_token, sIdTIU_Free, true, 1);
			
			print("\r\nШАГ 6-5");
			print("\r\nПолучаем список платных продуктов в ЛК, для платного объявления в рубрику Электроника и техника - Пылесосы. Регион Казань. Объявление выделено, активно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdTIU_Free);
			String sCurrentElectronFree3 = jData.toString(10); 
			CheckCountId(sCurrentElectronFree3, sIdTIU_Free, 2,  " Электроника и техника - Пылесосы. Регион Казань ");
			sCurrentElectronFree3 = sCurrentElectronFree3.replace(sIdTIU_Free, "*****");
			smasElectronInLK[2] = sCurrentElectronFree3;
			
			print("\r\nШАГ 6-6");
			print("\r\nДеактивируем объявление".toUpperCase());
			DeactivateAdvert(sHost, sAuth_token, sIdTIU_Free, 1);
			
			print("\r\nШАГ 6-7");
			print("\r\nПолучаем список платных продуктов в ЛК, для платного объявления в рубрику Электроника и техника - Пылесосы. Регион Казань. Объявление выделено, неактивно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdTIU_Free);
			String sCurrentElectronFree4 = jData.toString(10); 
			CheckCountId(sCurrentElectronFree4, sIdTIU_Free, 0,  " Электроника и техника - Пылесосы. Регион Казань ");
			sCurrentElectronFree4 = sCurrentElectronFree4.replace(sIdTIU_Free, "*****");
			smasElectronInLK[3] = sCurrentElectronFree4;
			
			print("\r\nШАГ 6-8");
			print("\r\nНазначаем премиум объявлению".toUpperCase());
			SetPremiumAdvert(sHost, sAuth_token, sIdTIU_Free, true, 1);
			
			print("\r\nШАГ 6-9");
			print("\r\nПолучаем список платных продуктов в ЛК, для платного объявления в рубрику Электроника и техника - Пылесосы. Регион Казань. Объявление премиум, активно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdTIU_Free);
			String sCurrentElectronFree5 = jData.toString(10); 
			CheckCountId(sCurrentElectronFree5, sIdTIU_Free, 2,  " Электроника и техника - Пылесосы. Регион Казань ");
			sCurrentElectronFree5 = sCurrentElectronFree5.replace(sIdTIU_Free, "*****");
			smasElectronInLK[4] = sCurrentElectronFree5;
			
			print("\r\nШАГ 6-10");
			print("\r\nДеактивируем объявление".toUpperCase());
			DeactivateAdvert(sHost, sAuth_token, sIdTIU_Free, 1);
			
			print("\r\nШАГ 6-11");
			print("\r\nПолучаем список платных продуктов в ЛК, для платного объявления в рубрику Электроника и техника - Пылесосы. Регион Казань. Объявление премиум, неактивно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdTIU_Free);
			String sCurrentElectronFree6 = jData.toString(10); 
			CheckCountId(sCurrentElectronFree6, sIdTIU_Free, 0,  " Электроника и техника - Пылесосы. Регион Казань ");
			sCurrentElectronFree6 = sCurrentElectronFree6.replace(sIdTIU_Free, "*****");
			smasElectronInLK[5] = sCurrentElectronFree6;
			
			//Раскоментить если надо будет обновить значения и закомментить после обновления
	    	//Js3 = new JString(smasElectronInLK); // запись в файл
	    	//SaveJson(Js3, "PaidProductElectronFreeLK.txt");
			
	    	String sIdealPaidElectronFreeLK[] = LoadJson("PaidProductElectronFreeLK.txt");
			
	    	print("\r\nШАГ 7");
			print("Сравниваем список платных продуктов для объявления(бесплатное) в ЛК рубрика Электроника и техника - Пылесосы(Казань) полученный запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление оплачено, активно");
			CheckAnswerForPaidProduct(sIdealPaidElectronFreeLK[0],sCurrentElectronFree);
			
			print("\r\nШАГ 7-1");
			print("Сравниваем список платных продуктов для объявления(бесплатное) в ЛК рубрика Электроника и техника - Пылесосы(Казань) полученный запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление оплачено, неактивно");
			CheckAnswerForPaidProduct(sIdealPaidElectronFreeLK[1],sCurrentElectronFree2);
	    	
			print("\r\nШАГ 7-2");
			print("Сравниваем список платных продуктов для объявления(бесплатное) в ЛК рубрика Электроника и техника - Пылесосы(Казань) полученный запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление выделено, активно");
			CheckAnswerForPaidProduct(sIdealPaidElectronFreeLK[2],sCurrentElectronFree3);
			
			print("\r\nШАГ 7-3");
			print("Сравниваем список платных продуктов для объявления(бесплатное) в ЛК рубрика Электроника и техника - Пылесосы(Казань) полученный запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление выделено, неактивно");
			CheckAnswerForPaidProduct(sIdealPaidElectronFreeLK[3],sCurrentElectronFree4);
			
			print("\r\nШАГ 7-4");
			print("Сравниваем список платных продуктов для объявления(бесплатное) в ЛК рубрика Электроника и техника - Пылесосы(Казань) полученный запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление премиум, активно");
			CheckAnswerForPaidProduct(sIdealPaidElectronFreeLK[4],sCurrentElectronFree5);
			
			print("\r\nШАГ 7-5");
			print("Сравниваем список платных продуктов для объявления(бесплатное) в ЛК рубрика Электроника и техника - Пылесосы(Казань) полученный запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление премиум, неактивно");
			CheckAnswerForPaidProduct(sIdealPaidElectronFreeLK[5],sCurrentElectronFree6);
			
			print("\r\nШАГ 7-6");
			print("\r\nАктивируем объявление.(Необходимо для проверки корректной работы платного объявления сверх лимита)".toUpperCase());
			ActivateAdvert(sHost, sAuth_token, sIdTIU_Free, false, 1);
			
			
	 /////////////////////////////////проверка платных продуктов в ЛК Пылесосы (платное объявление, сверх лимита)////////////////////////////////////////////////////
			print("\r\nШАГ 8");
			print("Получение списка платных продуктов для объявлений(платное сверх лимита) в ЛК. Электроника и техника - Пылесосы. Регион Казань.".toUpperCase());	
			print("\r\nПолучаем список платных продуктов в ЛК, для платного(сверх лимита) объявления в рубрику Электроника и техника - Пылесосы. Регион Казань. Объявление неоплачено, неактивно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdTIU_Paid);
			sCurrentElectronPaid = jData.toString(10); 
			CheckCountId(sCurrentElectronPaid, sIdTIU_Paid, 2,  " Электроника и техника - Пылесосы. Регион Казань "); 
			sCurrentElectronPaid = sCurrentElectronPaid.replace(sIdTIU_Paid, "*****");
			smasElectronPaidInLK[0] = sCurrentElectronPaid;
	    	
			print("\r\nШАГ 8-1");
			print("\r\nОплачиваем(активируем объявление)".toUpperCase());
			ActivateAdvert(sHost, sAuth_token, sIdTIU_Paid, true, 1);
			
			print("\r\nШАГ 8-2");
			print("\r\nПолучаем список платных продуктов в ЛК, для платного(сверх лимита) объявления в рубрику Электроника и техника - Пылесосы. Регион Казань. Объявление оплачено, активно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdTIU_Paid);
			String sCurrentElectronPaid2 = jData.toString(10); 
			CheckCountId(sCurrentElectronPaid2, sIdTIU_Paid, 6,  " Электроника и техника - Пылесосы. Регион Казань "); 
			sCurrentElectronPaid2 = sCurrentElectronPaid2.replace(sIdTIU_Paid, "*****");
			smasElectronPaidInLK[1] = sCurrentElectronPaid2;
	    	
			print("\r\nШАГ 8-3");
			print("\r\nВыделяем объявление".toUpperCase());
			HighLightAdvert(sHost, sAuth_token, sIdTIU_Paid, true, 1);
			
			print("\r\nШАГ 8-4");
			print("\r\nПолучаем список платных продуктов в ЛК, для платного(сверх лимита) объявления в рубрику Электроника и техника - Пылесосы. Регион Казань. Объявление выделено, активно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdTIU_Paid);
			String sCurrentElectronPaid3 = jData.toString(10); 
			CheckCountId(sCurrentElectronPaid3, sIdTIU_Paid, 4,  " Электроника и техника - Пылесосы. Регион Казань "); 
			sCurrentElectronPaid3 = sCurrentElectronPaid3.replace(sIdTIU_Paid, "*****");
			smasElectronPaidInLK[2] = sCurrentElectronPaid3;
			
			print("\r\nШАГ 8-5");
			print("\r\nДеактивируем объявление".toUpperCase());
			DeactivateAdvert(sHost, sAuth_token, sIdTIU_Paid, 1);
			
			print("\r\nШАГ 8-6");
			print("\r\nПолучаем список платных продуктов в ЛК, для платного(сверх лимита) объявления в рубрику Электроника и техника - Пылесосы. Регион Казань. Объявление выделено, неактивно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdTIU_Paid);
			String sCurrentElectronPaid4 = jData.toString(10); 
			CheckCountId(sCurrentElectronPaid4, sIdTIU_Paid, 2,  " Электроника и техника - Пылесосы. Регион Казань "); 
			sCurrentElectronPaid4 = sCurrentElectronPaid4.replace(sIdTIU_Paid, "*****");
			smasElectronPaidInLK[3] = sCurrentElectronPaid4;
			
			print("\r\nШАГ 8-7");
			print("\r\nНазначаем премиум объявлению".toUpperCase());
			SetPremiumAdvert(sHost, sAuth_token, sIdTIU_Paid, true, 1);
			
			print("\r\nШАГ 8-8");
			print("\r\nПолучаем список платных продуктов в ЛК, для платного(сверх лимита) объявления в рубрику Электроника и техника - Пылесосы. Регион Казань. Объявление премиум, активно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdTIU_Paid);
			String sCurrentElectronPaid5 = jData.toString(10); 
			CheckCountId(sCurrentElectronPaid5, sIdTIU_Paid, 4,  " Электроника и техника - Пылесосы. Регион Казань "); // здесь баг
			sCurrentElectronPaid5 = sCurrentElectronPaid5.replace(sIdTIU_Paid, "*****");
			smasElectronPaidInLK[4] = sCurrentElectronPaid5;
			
			print("\r\nШАГ 8-9");
			print("\r\nДеактивируем объявление".toUpperCase());
			DeactivateAdvert(sHost, sAuth_token, sIdTIU_Paid, 1);
			
			print("\r\nШАГ 8-10");
			print("\r\nПолучаем список платных продуктов в ЛК, для платного(сверх лимита) объявления в рубрику Электроника и техника - Пылесосы. Регион Казань. Объявление премиум, неактивно".toUpperCase());		
			jData = GetPaidProductsFromLK(sHost, sIdTIU_Paid);
			String sCurrentElectronPaid6 = jData.toString(10); 
			CheckCountId(sCurrentElectronPaid6, sIdTIU_Paid, 2,  " Электроника и техника - Пылесосы. Регион Казань "); // здесь баг
			sCurrentElectronPaid6 = sCurrentElectronPaid6.replace(sIdTIU_Paid, "*****");
			smasElectronPaidInLK[5] = sCurrentElectronPaid6;
			
			//Раскоментить если надо будет обновить значения и закомментить после обновления
	    	//Js3 = new JString(smasElectronPaidInLK); // запись в файл
	    	//SaveJson(Js3, "PaidProductElectronPaidLK.txt");
			
	    	String sIdealPaidElectronPaidLK[] = LoadJson("PaidProductElectronPaidLK.txt");
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    	print("\r\nШАГ 9");
			print("Сравниваем список платных продуктов для объявления(платное сверх лимита) в ЛК рубрика Электроника и техника - Пылесосы(Казань) полученного запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление неоплачено, неактивно");
			CheckAnswerForPaidProduct(sIdealPaidElectronPaidLK[0],sCurrentElectronPaid);
			
	    	print("\r\nШАГ 9-1");
			print("Сравниваем список платных продуктов для объявления(платное сверх лимита) в ЛК рубрика Электроника и техника - Пылесосы(Казань) полученного запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление оплачено, активно");
			CheckAnswerForPaidProduct(sIdealPaidElectronPaidLK[1],sCurrentElectronPaid2);
	    	
			print("\r\nШАГ 9-2");
			print("Сравниваем список платных продуктов для объявления(платное сверх лимита) в ЛК рубрика Электроника и техника - Пылесосы(Казань) полученного запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление выделено, активно");
			CheckAnswerForPaidProduct(sIdealPaidElectronPaidLK[2],sCurrentElectronPaid3);
			
			print("\r\nШАГ 9-3");
			print("Сравниваем список платных продуктов для объявления(платное сверх лимита) в ЛК рубрика Электроника и техника - Пылесосы(Казань) полученного запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление выделено, неактивно");
			CheckAnswerForPaidProduct(sIdealPaidElectronPaidLK[3],sCurrentElectronPaid4);
			
			print("\r\nШАГ 9-4");
			print("Сравниваем список платных продуктов для объявления(платное сверх лимита) в ЛК рубрика Электроника и техника - Пылесосы(Казань) полученного запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление премиум, активно");
			CheckAnswerForPaidProduct(sIdealPaidElectronPaidLK[4],sCurrentElectronPaid5);
	    	
			print("\r\nШАГ 9-5");
			print("Сравниваем список платных продуктов для объявления(платное сверх лимита) в ЛК рубрика Электроника и техника - Пылесосы(Казань) полученного запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление премиум, активно");
			CheckAnswerForPaidProduct(sIdealPaidElectronPaidLK[5],sCurrentElectronPaid6);
			
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			print("\r\nШАГ 10");
			print("Получение списка бесплатных продуктов для объявлений(бесплатное) в ЛК. Электроника и техника - Пылесосы. Регион Казань. Объявление активно".toUpperCase());
			jData = GetFreeProductsForAdvert(sHost, sIdTIU_Free2);
			String sCurrentFree = jData.toString(10); 
			smasFree[0] = sCurrentFree;
			
			print("\r\nШАГ 10-1");
			print("\r\nДеактивируем объявление".toUpperCase());
			DeactivateAdvert(sHost, sAuth_token, sIdTIU_Free2, 1);
			
			print("\r\nШАГ 10-1");
			print("Получение списка бесплатных продуктов для объявлений(бесплатное) в ЛК. Электроника и техника - Пылесосы. Регион Казань. Объявление неактивно.".toUpperCase());
			jData = GetFreeProductsForAdvert(sHost, sIdTIU_Free2);
			String sCurrentFree2 = jData.toString(10); 
			smasFree[1] = sCurrentFree2;
			
			//Раскоментить если надо будет обновить значения и закомментить после обновления
	    	//Js4 = new JString(smasFree); // запись в файл
	    	//SaveJson(Js4, "FreeProductElectron.txt");
			
	    	String sIdealFreeElectron[] = LoadJson("FreeProductElectron.txt");
			
	    	print("\r\nШАГ 11");
			print("Сравниваем список бесплатных продуктов для объявлений(бесплатное) в ЛК. Электроника и техника - Пылесосы(Казань) полученного запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление активно");
			CheckAnswerForPaidProduct(sIdealFreeElectron[0],sCurrentFree);
			
			print("\r\nШАГ 11-1");
			print("Сравниваем список бесплатных продуктов для объявлений(бесплатное) в ЛК. Электроника и техника - Пылесосы(Казань) полученного запросом, ".toUpperCase() +
					"с таким же списком из сохранения".toUpperCase());
			print("Объявление неактивно");
			CheckAnswerForPaidProduct(sIdealFreeElectron[1],sCurrentFree2);
			
		}
		finally
		{
			// удаляем объявления 
	    	print("\r\nШАГ 12");
	    	print("Удаление поданных объявлений пользователя".toUpperCase());
	    	print("Удаляем объявление с ID = " + sIdAutoPaid);
	    	DeleteAdvert(sHost, sAuth_token, sIdAutoPaid);
	    	
	    	print("Удаляем объявление с ID = " + sIdTIU_Free);
	    	DeleteAdvert(sHost, sAuth_token, sIdTIU_Free);
	    	
	    	print("Удаляем объявление с ID = " + sIdTIU_Free2);
	    	DeleteAdvert(sHost, sAuth_token, sIdTIU_Free2);
	    	
	    	print("Удаляем объявление с ID = " + sIdTIU_Free3);
	    	DeleteAdvert(sHost, sAuth_token, sIdTIU_Free3);
	    	
	    	print("Удаляем объявление с ID = " + sIdTIU_Paid);
	    	DeleteAdvert(sHost, sAuth_token, sIdTIU_Paid);
		}
		print("------------------------------------------------------------------------------------------------------------");
		print("Тест завершен успешно".toUpperCase());
    	
	}
	// получение платных продуктов на этапе подачи для автотеста
	private JSONObject GetPaidProductsToStepToAdd(String sHost, String sIdAdvert) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		print("Получение списка платных продуктов для объявления доступных на этапе подачи объявления".toUpperCase());
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
    	{
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nСписок получен");
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить список продуктов \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// получение платных продуктов в ЛК
	private JSONObject GetPaidProductsFromLK(String sHost, String sIdAdvert) throws ExceptFailTest, URISyntaxException, IOException, JSONException
	{
		print("Получение списка платных продуктов для объявления доступных в личном кабинете пользователя".toUpperCase());
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
    	{
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nСписок получен");
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить список продуктов \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// получение бесплатных продуктов в ЛК
	private JSONObject GetFreeProductsForAdvert(String sHost, String sIdAdvert) throws ExceptFailTest, URISyntaxException, IOException, JSONException
	{
		print("Получение списка бесплатных действий над объявлением".toUpperCase());
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
    	{
    		print("Ответ сервера:" + jsonObject.toString(10) + " Список получен");
    		return jsonObject;
    	}
    	else
    	{
    		print("Не удалось получить список продуктов \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	// проверка наличия в ответах правильной id и правильного количества id 
	private void CheckCountId(String sJson, String sId, int nCount, String sText) throws ExceptFailTest
	{
		print("Проверяем совпадения в ответе для конфига платных продуктов, наличия правильного ID объявления и правильного количества повторений ID");
		print("Проверяем рубрику " + sText + "Ищем в ответе совпадение поданного объявления с ID = " + sId + " и повторения данного ID " + nCount +" раз");
		int l = sJson.length();
		int k=0;
		while(sJson.lastIndexOf(sId, l) != -1)
		{
			k++;
			l = sJson.lastIndexOf(sId, l) - 1;

		}
		if(k == nCount)
		{
			print("В ответе присутствует ID(если ожидаемое количество не равно 0) = " + sId + " количество записей ID равно " + k + " что совпало с ожидаемым количеством равным " + nCount + " Корректно.");
		}
		else
		{
			print("В ответе либо не присутствует ID = " + sId + " либо количество записей ID равное " + k + " не совпало с ожидаемым количеством равным " + nCount);
			print("Тест провален".toUpperCase());
    		throw new ExceptFailTest("Тест провален");
		}
	}
	// валидация  ответа на запрос с сохраненным ответом на запрос
	private void CheckAnswerForPaidProduct(String sIdeal , String sCurrent) throws ExceptFailTest
	{
		if(sIdeal.equals(sCurrent))
		{
			print("Списки продуктов идентичны. Корректно");
			print("Полученный из сохранения список продуктов :");
			print(sIdeal);
		}
		else 
		{
			print("Списки продуктов не совпадают");
			print("Полученный из сохранения список продуктов :");
			print(sIdeal);
			print("Тест провален".toUpperCase());
			throw new ExceptFailTest("Тест провален");
		}
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
		
		String sE = "email=" + sEmail + "&password=" + sPassword;
		builder = new URIBuilder();
		
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account");
    		
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequest2(uri, sE);
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
		String sE = "username=" + sUsername + "&password=" + sPassword;
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/login");
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequest2(uri,sE);
    	print("Парсим ответ....");
    	
    	//print(sResponse);
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
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		print("1.3.	Редактирование профиля");
		print("Параметры для запроса");
		print("auth_token = "+ sAuth_token);
		print("user_info = "+ sUser_info);
		
		String sQuery = CreateArrayRequestForPostAndPut("user_info", sUser_info);
		sQuery = sQuery + "&auth_token=" + sAuth_token;
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account"); 
    	uri = builder.build();
   
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpPutRequest2(uri, sQuery);
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
		
		String sE = "email="+sEmail;
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/restore");
    	uri = builder.build();
    	
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequest2(uri, sE);
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
	// Смена пароля
	public void ChangePassword1_5(String sHost, String sUsername, String sPassword, String sNewPassword, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		String  sAuth_token= "";
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		
		print("1.5. Смена пароля пользователя");
		print("Параметры для запроса");
		print("password = "+ sPassword);
		print("auth_token = "+ sAuth_token);
		print("new password = "+ sNewPassword);
		
		String sE = "auth_token=" + sAuth_token + "&old_password=" + sPassword + "&new_password=" + sNewPassword;
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/changepassword");
    	uri = builder.build();
    	
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nпароль для пользователя " + sUsername + " изменен на " + sNewPassword);
    	else
    	{
    		print("Не удалось изменить пароль\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}
    	
	}
	// Получение ссылки на активацию аккаунта
	public void GetUrlActivasion_1_6(String sHost, String sUsername, String sPassword) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		print("1.6. Получение ссылки для активации аккаунта".toUpperCase());
		print("Параметры для запроса");
		print("email = "+ sUsername);
		print("password = "+ sPassword);
		String sE = "username=" + sUsername + "&password=" + sPassword;
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/activationkey");
    	uri = builder.build();
    	if(uri.toString().indexOf("%25") != -1)
    	{
    		String sTempUri = uri.toString().replace("%25", "%");
    		uri = new URI(sTempUri);			
    	}
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequest2(uri,sE);
    	print("Парсим ответ....");
    	
    	print(sResponse);
    	jsonObject = ParseResponse(sResponse);
    	
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\ncсылка на активацию отправлена на почтовый ящик пользователя");
    	else
    	{
    		print("Не удалось повторно запросить ссылку на активацию аккаунта\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}
	}
	// Смена пароля
	public void LogOut1_7(String sHost, String sUsername, String sPassword, boolean bAuthFlag) throws URISyntaxException, IOException, ExceptFailTest, JSONException
	{
		String  sAuth_token= "";
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		
		print("1.7. Выход из приложения");
		print("Параметры для запроса");
		print("password = "+ sPassword);
		print("auth_token = "+ sAuth_token);
	
		
		String sE = "auth_token=" + sAuth_token;
		
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/account/logout");
    	uri = builder.build();
    	
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nпользователь успешно вылогинился");
    	else
    	{
    		print("Не удалось вылогиниться\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
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
		String sRequest = CreateSimpleRequestForPostAndPut(sCatRegAdv);
		String sRequest1 = CreateArrayRequestForPostAndPut("advertisement" ,sAdvertisement);
		String sRequest2 = CreateDoubleArrayRequestForPostAndPut("advertisement", "custom_fields", sCustom_fields);
		String sE = "auth_token=" + sAuth_token + sRequest + sRequest1 + sRequest2 + sVideo;
	
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert");
    		
    	uri = builder.build();
    	
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequestImage2(uri, sPathImage, sE);
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
		String sQuery ="";
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
		
		String sRequest = CreateDoubleArrayRequestForPostAndPut("advertisement", "custom_fields", sCustom_fields);
		String sRequest1 = CreateArrayRequestForPostAndPut("advertisement" , sAdvertisement);
		
		builder = new URIBuilder();
		
		if(!sUrlImage.equals("false"))
    	{
			builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/"+ sIdAdvert);
			sQuery = "auth_token=" + sAuth_token + sRequest1 + sRequest + sVideo + "&deleted_images[0]=" + sUrlImage;
    	}
    	else
    	{
    		builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/"+ sIdAdvert);
    		sQuery = "auth_token=" + sAuth_token + sRequest1 + sRequest + sVideo;
    	}
    	uri = builder.build();
    	
    	print("Отправляем запрос. Uri Запроса: " + uri.toString());
    	print(sQuery);
    	//String sResponse = HttpPutRequest2(uri, sQuery);
    	//String sResponse = HttpPutRequestImage2(uri, "2.jpg", sQuery);
    	String sResponse = HttpPostRequestImage2(uri, "2.jpg", sQuery);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("\r\nОтвет сервера:\r\n" + jsonObject.toString(10) + "\r\n Объявление отредактировано");
    		
    	else
    	{
    		print("Не удалось отредактировать объявление\r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert +"/favorite");

    	String sE = "auth_token=" + sAuth_token;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	String sResponse = HttpPostRequest2(uri, sE);
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/activate");
    	
    	String sE = "auth_token=" + sAuth_token + "&app_token=" + sApp_token;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление активировано");
    	else
    	{
    		print("Не удалось активировать объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/deactivate");
    	
    	String sE = "auth_token=" + sAuth_token;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    
    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление деактивировано");
    	else
    	{
    		print("Не удалось деактивировать объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/prolong");

    	String sE = "auth_token=" + sAuth_token + "&app_token=" + sApp_token;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		if(jsonObject.toString().equals("{\"error\":null,\"actions\":false}"))
    			print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление не продлено (возможно оно неактивно, неоплачено)");
    		else 
    			print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление продлено");
    	}
    	else
    	{
    		print("Не удалось продлить объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/pushup");
    		
    	String sE = "auth_token=" + sAuth_token + "&app_token=" + sApp_token;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	

    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		if(jsonObject.toString().equals("{\"error\":null,\"actions\":false}"))
    			print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление не поднято (возможно оно неактивно, неоплачено)");
    		else 
    			print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nбъявление поднято");
    	}
    	else
    	{
    		print("Не удалось поднять объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/highlight");
    		
    	String sE = "auth_token=" + sAuth_token + "&app_token=" + sApp_token;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		if(jsonObject.toString().equals("{\"error\":null,\"actions\":false}"))
    			print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление не выделено (возможно оно неактивно, неоплачено)");
    		else 
    			print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление выделено");
    	}
    	else
    	{
    		print("Не удалось выделить объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/premium");
    	
    	String sE = "auth_token=" + sAuth_token + "&app_token=" + sApp_token + "&number=" + sNumberDays;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    		if(jsonObject.toString().equals("{\"error\":null,\"actions\":false}"))
    			print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление не назначен премиум (возможно оно неактивно, неоплачено)");
    		else 
    			print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявление назначен премиум");
    	}
    	else
    	{
    		print("Не удалось назначить премиум объявление \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
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
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/advertisements/advert/" + sIdAdvert + "/vote");

    	String sE = "auth_token=" + sAuth_token;
    	
    	uri = builder.build();
    	print("Отправляем запрос. Uri Запроса: "+uri.toString());
    	
    	String sResponse = HttpPostRequest2(uri, sE);
    	print("Парсим ответ....");
    	
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nОбъявлению +1 голос");
    	else
    	{
    		print("Не удалось добавить голос объявлению \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
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
    		
    		
    		print("Ответ сервера: Фильтр-листинг  объявлений получен");
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
    		print("Ответ сервера:" + jsonObject.toString(10) + "рубрикатора сайта получен");
    	}
    	else
    	{
    		print("Не удалось получить рубрикатора сайта \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
    		throw new ExceptFailTest("Тест провален");
    	}	
	}	
	//3.2.	Получение списка полей рубрики для подачи объявления
	public void GetCastomfieldsForAddAdvert_3_2(String sHost, String sDataCustomfieldsAdvert, String sUsername, String sPassword, boolean bAuthFlag) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		String  sAuth_token= "";	
		if(bAuthFlag)
		{
			sAuth_token = Authorization_1_1(sHost, sUsername, sPassword);
		}
		else print("Передан параметр не авторизовывать пользователя. В следующий запрос уйдет пустой ключ auth_token");
		
		
		print("3.2.	Получение списка полей рубрики для подачи объявления");
		print("Параметры для запроса");
		print("DataCustomfieldsAdvert = "+ sDataCustomfieldsAdvert);
		String sQuery = CreateSimpleRequest(sDataCustomfieldsAdvert);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/categories/fields/post")
    		.setQuery(sQuery + "&auth_token=" + sAuth_token);
    	
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
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nсписок полей рубрики для подачи объявления получен");
    		
    	}
    	else
    	{
    		print("Не удалось получить список полей рубрики для подачи объявления \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
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
    	jsonObject = ParseResponse(sResponse);
    	if(jsonObject.isNull("error"))
    	{
    	
    		print("Ответ сервера. Cписок полей рубрики для фильтрации объявлений получен");
    		print(jsonObject.toString(10));
    		
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
    		print("Ответ сервера:" + jsonObject.toString(10) + "список субъектов РФ получен");
    		
    	}
    	else
    	{
    		print("Не удалось получить список субъектов РФ \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString());
    		throw new ExceptFailTest("Тест провален");
    	}	
	}
	//4.2.	Получение списка городов принадлежащих какому либо региону
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
	//4.2.1. Получение списка городов, для которых заведены поддомены
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
    		print("Ответ сервера:" + jsonObject.toString(10) + "\r\n список домов улицы (саджест) получен");
    		
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
    		print("Ответ сервера:" + jsonObject.toString(10) + "\r\nсписок районов (саджест) получен \r\n");
    		
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
	
	//7.1	Получение промо-блока
	public void GetPromo7_1(String sHost, String sParam) throws URISyntaxException, IOException, JSONException, ExceptFailTest
	{

		print("7.1.	Получение промо-блока");
		print("Параметры для запроса");
		print("sParam = "+ sParam);
		
		String sQuery = CreateSimpleRequest(sParam);
		builder = new URIBuilder();
    	builder.setScheme("http").setHost(sHost).setPath("/mobile_api/1.0/promo").setQuery(sQuery);
    	
    	
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
    		print("Ответ сервера:\r\n" + jsonObject.toString(10) + "\r\nданные о промо-блоке получены\r\n");
    		
    	}
    	else
    	{
    		print("Не удалось получить данные о промо-блоке \r\n"+
    				"Ответ сервера:\r\n"+ jsonObject.toString(10));
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

