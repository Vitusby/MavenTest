package pack1;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestTest {

  @Test
  @Parameters({ "url" })
  public void Test(String url)
  {
	  	String s = Proper.GetProperty("url");
	  	System.out.println(s);
	  	System.out.println("Параметр из Testng: "+ url);
	  	System.out.println("Start Test");
		WebDriver driver = new FirefoxDriver();
		driver.get(url);
		driver.quit();
		System.out.println("Close Test");
  }
}
