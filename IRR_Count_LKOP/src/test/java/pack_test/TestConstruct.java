package pack_test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import pack_utils.ListenerThatHiglilightsElements;
import pack_utils.Proper;


public class TestConstruct
{
	protected WebDriver driver;
	protected EventFiringWebDriver driver1;
	protected final String firebugPath = "F:\\firebug-1.10.6-fx.xpi";
	protected ListenerThatHiglilightsElements lthe;
	
	
	private FirefoxProfile GetFireFoxProfile()
	{
		FirefoxProfile profile = new FirefoxProfile();
		disableCacheFF(profile);
		/*try 
		{
			profile.addExtension(new File(firebugPath));
		}
		catch (IOException e) {e.printStackTrace();}*/
		return profile;
	}
	
	public void disableCacheFF(FirefoxProfile firefoxProfile) {
	    //firefoxProfile.setPreference("browser.cache.disk.enable", false);
	    //firefoxProfile.setPreference("browser.cache.memory.enable", false);
	    //firefoxProfile.setPreference("browser.cache.offline.enable", false);
		firefoxProfile.setEnableNativeEvents(true);
	    firefoxProfile.setPreference("network.http.use-cache", false);
}
	
	public WebDriver GetWebDriver()
	{
		
		if(driver==null)
		{
			driver = new FirefoxDriver(/*GetFireFoxProfile()*/);
			lthe = new ListenerThatHiglilightsElements("#FFFF00", 1, 250, TimeUnit.MILLISECONDS);
			//driver = new FirefoxDriver();
			if(Proper.GetProperty("lightElement").equals("yes"))
			{
				driver1 = new EventFiringWebDriver(this.driver);
				driver1.register(lthe);
			
				//System.out.println(driver.toString());
				driver1.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				return driver1;
			}
			else
			{
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				return driver;
			}
		}
		else 
		{
			if(Proper.GetProperty("lightElement").equals("yes"))
				return driver1;
			else
				return driver;				
		}
	}
	
}
