package pack_test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import pack_utils.ListenerThatHiglilightsElements;
import pack_utils.Proper;
import pack_utils.WriterLog;


public class TestConstruct
{
	protected WebDriver driver;
	protected EventFiringWebDriver driver1;
	protected ListenerThatHiglilightsElements lthe;
	protected WriterLog wLog;
	
	@SuppressWarnings("unused")
	private FirefoxProfile GetFireFoxProfile()
	{
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("network.http.use-cache", false);
		return profile;
	}
	
		
	public WebDriver GetWebDriver()
	{
		
		if(driver == null)
		{
			driver = new FirefoxDriver(/*GetFireFoxProfile()*/);
			lthe = new ListenerThatHiglilightsElements("#FFFF00", 1, 250, TimeUnit.MILLISECONDS);
			if(Proper.GetProperty("lightElement").equals("yes"))
			{
				driver1 = new EventFiringWebDriver(this.driver);
				driver1.register(lthe);
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
			if(Proper.GetProperty("lightElement").equals("yes")){return driver1;}
			else{return driver;}
		}
	}
	
	public void CaptureScreenshot()
	{
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String path = "src\\" + screenshot.getName();
        System.out.println(path);
       
        try
        {
            FileUtils.copyFile(screenshot, new File("screenshot.png"));
        } 
        catch (IOException e) {e.printStackTrace();}
    }
	
	
}
