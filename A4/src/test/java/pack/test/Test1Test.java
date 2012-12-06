package pack.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

public class Test1Test {

  @Test
  public void TestStart()
  {
	  WebDriver driver = new FirefoxDriver();
	  driver.get("http://open.by");
	  driver.quit();
  }
}
