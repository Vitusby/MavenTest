package pack.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;


import java.io.IOException;
import java.util.Properties;


class Proper
{
	protected static Properties prop;
	static
	{
		prop = new Properties();
		try
		{
			prop.load(ClassLoader.getSystemResourceAsStream("p.properties"));
		}
		catch(IOException exc){exc.printStackTrace(); System.out.println("Не удалось загрузить файл конфигурации");}
	}
	
	public static String GetProperty(String sKey)
	{
		return prop.getProperty(sKey);
	}
}


public class Test1Test {

  @Test
  public void TestStart()
  {
	  System.out.println("Star Test");
	  WebDriver driver = new FirefoxDriver();
	  driver.get(Proper.GetProperty("url"));
	  driver.quit();
	  System.out.println("Close Test");
  }
}
