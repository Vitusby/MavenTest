package packtest;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class TestClass {
	private WebDriver driver;
	//private ConfigProperty prop;
	@BeforeTest
	public void setUp() throws Exception {
		System.out.println("Start @BeforeTest");	
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		System.out.println("End @BeforeTest");
	}

	@Test
	public void testCase() throws Exception {
		System.out.println("Start @Test");
		//driver.get(prop.getproperty("url") + "/");
		driver.get("http://open.by" + "/");
		driver.findElement(By.id("uname")).clear();
		//driver.findElement(By.id("uname")).sendKeys(prop.getproperty("login"));
		driver.findElement(By.id("uname")).sendKeys("timon2181");
		driver.findElement(By.id("Password")).clear();
		//driver.findElement(By.id("Password")).sendKeys(prop.getproperty("password"));
		driver.findElement(By.id("Password")).sendKeys("retry2");
		driver.findElement(By.cssSelector("span.btn2 > b")).click();
		Assert.assertTrue(isElementPresent(By.linkText("выход")));
		System.out.println("End @Test");
	}

	@AfterTest
	public void tearDown() throws Exception {
		System.out.println("Start @AfterTest");
		driver.quit();
		//fsdfsdf
		System.out.println("End @AfterTest");
		
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}