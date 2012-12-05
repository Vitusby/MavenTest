package pack1;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

public class TestTest {

  @Test
  public void Test()
  {
	  String s = Proper.GetProperty("url");
	  	System.out.println("Start Test");
		WebDriver driver = new FirefoxDriver();
		driver.get(s);
		driver.quit();
		System.out.println("Close Test");
  }
}
