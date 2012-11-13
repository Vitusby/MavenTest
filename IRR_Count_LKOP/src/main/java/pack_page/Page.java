package pack_page;
import pack_utils.ExceptFailTest;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class Page
{
	protected WebDriver driver;
	
	public Page(WebDriver driver){this.driver=driver;}

	// ћетод открыти€ страницы
	public abstract void OpenPage();
	
	// ћетод проверки что у элемента есть атрибут с искомым значением (примен€ть когда у элемента входе какого либо действи€ 
	// измен€тс€ значение атрибута)
	// sAtribute - искомый атрибут ( к примеру class)
	// sFindAtribute - значение атрибута
	// wElement - элемент у которого мы ожидаем найти это значение
	public void CheckAtributeElement(final String sAtribute, final String sFindAtribute, final WebElement wElement) throws ExceptFailTest
	{
		Boolean bFlag = false;
		WebDriverWait wWaitDriver = new WebDriverWait(driver, 10);
		try
		{
			bFlag = wWaitDriver.until(new ExpectedCondition<Boolean>()
					{
						public Boolean apply(WebDriver wd)
						{
							return wElement.getAttribute(sAtribute).equals(sFindAtribute);
						}
					}
								  	  );
		}
		catch(TimeoutException exc){System.out.println("Ёлемент c атрибутом "+sFindAtribute+" не найден");}
		if(!bFlag)
		{
			throw new ExceptFailTest("Ёлемент c атрибутом "+sFindAtribute+" не найден");
		}
		else System.out.println("OKKKKKK");
			
	}
	
	// ћетод проверки существовани€ элемента
	public void CheckElementPresent (final int nKey, final String sLocator) throws ExceptFailTest
	{
		WebElement wElement = null;
		WebDriverWait wWaitDriver = new WebDriverWait(driver, 10);
		try
		{
		wElement = wWaitDriver.until(new ExpectedCondition<WebElement>()
				{
					public WebElement apply(WebDriver wd)
					{
					WebElement wEl = null;
						switch (nKey)
						{
							case 1:
								wEl =  wd.findElement(By.xpath(sLocator));
								break;
							case 2:
								wEl = wd.findElement(By.id(sLocator));
								break;
							case 3:
								wEl = wd.findElement(By.name(sLocator));
								break;
							case 4:
								wEl = wd.findElement(By.className(sLocator));
								break;
							case 5:
								wEl = wd.findElement(By.linkText(sLocator));
								break;
							case 6:
								wEl = wd.findElement(By.cssSelector(sLocator));
								break;
						}
						return wEl;
					}
				}								
											  );
		}
		catch(TimeoutException exc){System.out.println("Ёлемент "+sLocator+" не найден");}
		
		if(wElement == null)
		{
			throw new ExceptFailTest("Ёлемент "+sLocator+" не найден");
		}
	}
	
	// ћетод проверки что элемент кликабелен и доступен // доработать на все элементы и на ексептион
	public void CheckElementEnabled (String sLocator) throws ExceptFailTest 
	{
		WebElement wElement = null;
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wElement = wait.until(ExpectedConditions.elementToBeClickable(By.id(sLocator)));
		if(wElement==null)
			throw new ExceptFailTest("Ёлемент "+sLocator+" найден, но не доступен");
			
	}
	
	// ћетод остановки процесса i количество миллисекунд
	public void Sleep(int i)
	{
		try{Thread.sleep(i);}catch(InterruptedException exc){exc.printStackTrace();}
	}
	
	// ћетод даблклика по элементу
	public void DoubleClickElement(WebElement wElement) 
	{
		Actions builder;
		Action dClick;
		builder = new Actions(driver);
		builder.doubleClick(wElement); // создали последовательность действий
		dClick = builder.build(); // получили само действие
		dClick.perform(); // выполнили действие
	}
	
	// ћетод клика по элементу (если не работает обычный click)
	public void ClickElement(WebElement wElement) 
	{
		System.out.println(wElement.getLocation());
		Actions builder;
		Action cClick;
		builder = new Actions(driver);
		builder.click(wElement); // создали последовательность действий
		cClick = builder.build(); // получили само действие
		cClick.perform(); // выполнили действие
	}
	
/*	private boolean isElementPresent(By by)
	{
		try
		{
			driver.findElement(by);
			return true;
		}
		catch (NoSuchElementException exc) {return false;}
	}
*/

}

