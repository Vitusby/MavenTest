package pack.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

public class Test1Test {

  @Test
  public void TestStart()
  {
	  System.out.println("Star Test");
	  WebDriver driver = new FirefoxDriver();
	  driver.get("http://open.by");
	  driver.quit();
	  System.out.println("Close Test");
  }
}
