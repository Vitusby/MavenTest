package pack_test;

import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import pack_utils.ListenerThatHiglilightsElements;
import pack_utils.Proper;



public class Test_Construct
{
	protected WebDriver driver;
	protected EventFiringWebDriver event_driver;
	
	private FirefoxProfile GetFireFoxProfile()
	{
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("network.http.use-cache", false);
		return profile;
	}
	
	public WebDriver GetWebDriver()
	{
		if(driver==null)
		{
			driver = new FirefoxDriver(GetFireFoxProfile());
			if(Proper.GetProperty("lightElement").equals("yes"))  // используем ли драйвер слушатель
			{
				event_driver = new EventFiringWebDriver(this.driver);
				event_driver.register(new ListenerThatHiglilightsElements("#FFFF00", 1, 250, TimeUnit.MILLISECONDS));
				return event_driver;
			}
			else
			{
				return driver;
			}
		}
		else 
		{
			if(Proper.GetProperty("lightElement").equals("yes"))
				return event_driver;
			else
				return driver;				
		}
	}
	
	public <T> void print(T obj)
	{
		PrintWriter pw = new PrintWriter(System.out, true);
		pw.println(obj);
	}
}
