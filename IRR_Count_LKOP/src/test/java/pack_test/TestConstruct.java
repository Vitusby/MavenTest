package pack_test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class TestConstruct
{
	protected WebDriver driver;
	
	public WebDriver GetWebDriver()
	{
		if(driver==null)
		{
			driver = new FirefoxDriver();
			System.out.println(driver.toString());
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			return driver;
		}
		else return driver;
	}
	
}
