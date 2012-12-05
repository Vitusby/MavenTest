package pack.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;


import java.io.IOException;
import java.net.URL;
import java.util.Properties;


class Proper
{
	protected static Properties prop;
	static
	{
		prop = new Properties();
		URL url = ClassLoader.getSystemResource("p.properties"); // получаем  урл проперти файла
		try
		{
			prop.load(url.openStream()); // грузим данные из файла в проперти
		}
		catch(IOException exc){exc.printStackTrace();} System.out.println("Dont Work");
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
	  System.out.println("Start Test");
	  WebDriver driver = new FirefoxDriver();
	  driver.get(Proper.GetProperty("url"));
	  driver.quit();
	  System.out.println("Close Test");
  }
}
