package pack_page;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import pack_utils.ExceptFailTest;
import pack_utils.Proper;

//конфиг с данными для объявок надо сделать
//логатор

public class Page_Stargate extends Page
{
	@FindBy(xpath="//a[@href='/stargate/?logout=true']")
	private WebElement wLinkLogout;
	// Блок Навигация
	@FindBy(xpath="//a[@href='/stargate/workspace/admanagement/']") // Ссылка Объявления
	private WebElement wAdvert;
	@FindBy(xpath="//a[@href='/stargate/workspace/admanagement/catalog/']/span[contains(text(),'Каталог объявлений')]") // Создать объявление
	private WebElement wCatalogAdvert;
	@FindBy(xpath="//a[@href='/stargate/workspace/admanagement/premium/']/span[contains(text(),'Создать премиум')]") // Создать премиум
	private WebElement wCreatePremium;
	@SuppressWarnings("unused")
	@FindBy(xpath="//a[@class='x-tree-node-anchor']/span[contains(text(),'Категории каталога')]")
	private WebElement wCategoryCatalog;
	
	// ����
	@FindBy(xpath="//a[@class='x-tree-node-anchor']/span[contains(text(),Авто и мото')]")
	private WebElement wAutoMain;
	@FindBy(xpath="//a[@class='x-tree-node-anchor']/span[contains(text(),'Легковые автомобили')]")
	private WebElement wEasyCar;		
	@FindBy(xpath="//a[@class='x-tree-node-anchor']/span[contains(text(),'���������� � ��������')]")
	private WebElement wEasyCarOld;
	@FindBy(xpath="//a[@class='x-tree-node-anchor']/span[contains(text(),'���� � ��������')]")
	private WebElement wEasyCarOld_1;
	
	// ����� �����
	@FindBy(xpath="//a[@class='x-tree-node-anchor']/span[contains(text(),'����� �����')]")
	private WebElement wTakeFree;
	@FindBy(xpath="//ul[@class='x-tree-root-ct x-tree-lines']//ul/li[25]/ul//a[@class='x-tree-node-anchor']/span[contains(text(),'����� �����')]")
	private WebElement wTakeFree_1;
	
	@FindBy(xpath="//button[@class='x-btn-text icon-plus' and contains(text(),'�������')]") // ������ ������� ��� ������� ����������
	private WebElement wButtonCreateAdvert;
	
	//	���� ������ �������
	@FindBy(xpath="//span[@class='x-window-header-text' and contains(text(),'��������������')]")
	private WebElement wTitledWindowRegion;
	@FindBy(xpath="//fieldset[@class=' x-fieldset x-fieldset-noborder x-form-label-left']//input[2]")
	private WebElement wFieldWindowRegion;
	@FindBy(xpath="//div[@class='x-window-footer']//button[contains(text(),'���������')]")
	private WebElement wButtonSaveWindowRegion;
	
	//  ������ ��������� �������
	@FindBy(xpath="//div[@class='x-panel-bbar x-panel-bbar-noborder']//button[contains(text(),'���������')]")
	private WebElement wButtonSavePremium;
	//  ������ ��������� ������� ����������
	@FindBy(xpath="//div[@class='x-tab-panel-footer']//button[contains(text(),'���������')]")
	private WebElement wButtonSaveAdvert;
	// ������ �� ����� ����
	@FindBy(xpath="//div[@class='x-window x-window-plain x-window-dlg']//button[contains(text(),'OK')]")
	private WebElement wButtonWindowPopup;
	
	/*
	 div[@id='propspanel']/div/div/div/div[2]/div/div/div/div[2]/div  / ������ ��� /div[1,2,3,4,5 � � �]/ � ������� ���� ������ 
	 ����� /table/tbody/tr/td[3]/
	 	 
	 td[@class='x-grid3-col x-grid3-cell x-grid3-td-value x-grid3-cell-last  x-grid3-cell-selected'] ���� � ���� �����
	 */
		
	@FindBy(xpath="//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'������')]")
	private WebElement wDivTitleRegion;
	@FindBy(xpath="//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'����� ����������')]")
	private WebElement wDivDescription;
	@FindBy(xpath="//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'����')]")
	private WebElement wDivTitlePrice;
	@FindBy(xpath="//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'������')]")
	private WebElement wDivTitleCurrency;
	@FindBy(xpath="//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'�������� ����������')]")
	private WebElement wDivTitleOwnerAdvert;
	@FindBy(xpath="//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'�����')]")
	private WebElement wDivTitleMake;
	@FindBy(xpath="//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'������')]")
	private WebElement wDivTitleModel;
	@FindBy(xpath="//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'��� �������')]")
	private WebElement wDivTitleYear;
	@FindBy(xpath="//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'��� ������')]")
	private WebElement wDivTitleTypeOfBody;
	@FindBy(xpath="//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'��� �����������')]")
	private WebElement wDivTitleTypeOfTransmission;
	@FindBy(xpath="//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'������ ���������')]")
	private WebElement wDivStatusOfModer;
	@FindBy(xpath="//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'���������� ����������')]")
	private WebElement wDivActionOfAdvet;
	@FindBy(xpath="//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'���������')]")
	private WebElement wDivTitleTitle;
	@FindBy(xpath="//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'�����')]")
	private WebElement wDivText;
	
	public Page_Stargate(WebDriver driver){super(driver);}
	
	
	
	public void OpenFormCreatePremiumFree() throws ExceptFailTest
	{
		
		OpenListPremium();
		CheckElementPresent(1,"//a[@class='x-tree-node-anchor']/span[contains(text(),'��������� ��������')]");
		CheckElementPresent(1,"//a[@class='x-tree-node-anchor']/span[contains(text(),'����� �����')]");
		DoubleClickElement(wTakeFree);
		CheckElementPresent(1,"//ul[@class='x-tree-root-ct x-tree-lines']//ul/li[25]/ul//a[@class='x-tree-node-anchor']/span[contains(text(),'����� �����')]");
		DoubleClickElement(wTakeFree_1);
		Sleep(2000);
	}

	public void OpenFormCreateAdvertFree() throws ExceptFailTest
	{
		
		OpenListAdvert();
		CheckElementPresent(1,"//a[@class='x-tree-node-anchor']/span[contains(text(),'����� �����')]");
		DoubleClickElement(wTakeFree);
		CheckElementPresent(1,"//ul[@class='x-tree-root-ct x-tree-lines']//ul/li[25]/ul//a[@class='x-tree-node-anchor']/span[contains(text(),'����� �����')]");
		DoubleClickElement(wTakeFree_1);
		CheckElementPresent(1, "//button[@class='x-btn-text icon-plus' and contains(text(),'�������')]");
		CheckCssElement("color","rgba(68, 68, 68, 1)",wButtonCreateAdvert);
		wButtonCreateAdvert.click();
		CheckElementPresent(1,"//div[contains(text(),'����� �����')]");
		Sleep(2000);
	}
	
	public void OpenFormCreatePremiumAuto() throws ExceptFailTest
	{
		OpenListPremium();
		CheckElementPresent(1,"//a[@class='x-tree-node-anchor']/span[contains(text(),'��������� ��������')]");
		CheckElementPresent(1,"//a[@class='x-tree-node-anchor']/span[contains(text(),'���� � ����')]");
		DoubleClickElement(wAutoMain);
		CheckElementPresent(1,"//a[@class='x-tree-node-anchor']/span[contains(text(),'�������� ����������')]");
		DoubleClickElement(wEasyCar);
		CheckElementPresent(1,"//a[@class='x-tree-node-anchor']/span[contains(text(),'���������� � ��������')]");
		DoubleClickElement(wEasyCarOld);
		CheckElementPresent(1,"//a[@class='x-tree-node-anchor']/span[contains(text(),'���� � ��������')]");
		DoubleClickElement(wEasyCarOld_1);
		//CheckElementPresent(1,"//div[contains(text(),'���� � ���� -> �������� ���������� -> ���������� � ��������')]");
		Sleep(2000);
	}
	
	public void OpenFormCreateAdvertAuto() throws ExceptFailTest
	{
		OpenListAdvert();
		CheckElementPresent(1,"//a[@class='x-tree-node-anchor']/span[contains(text(),'��������� ��������')]");
		CheckElementPresent(1,"//a[@class='x-tree-node-anchor']/span[contains(text(),'���� � ����')]");
		DoubleClickElement(wAutoMain);
		CheckElementPresent(1,"//a[@class='x-tree-node-anchor']/span[contains(text(),'�������� ����������')]");
		DoubleClickElement(wEasyCar);
		CheckElementPresent(1,"//a[@class='x-tree-node-anchor']/span[contains(text(),'���������� � ��������')]");
		DoubleClickElement(wEasyCarOld);
		CheckElementPresent(1,"//a[@class='x-tree-node-anchor']/span[contains(text(),'���� � ��������')]");
		DoubleClickElement(wEasyCarOld_1);
		CheckElementPresent(1, "//button[@class='x-btn-text icon-plus' and contains(text(),'�������')]");
		CheckCssElement("color","rgba(68, 68, 68, 1)",wButtonCreateAdvert);
		wButtonCreateAdvert.click();
		CheckElementPresent(1,"//div[contains(text(),'���� � ���� -> �������� ���������� -> ���������� � ��������')]");
		Sleep(2000);
	}
		
	public void InputDataFree() throws ExceptFailTest
	{
		wLog.WriteString(1, "������� ���������� � ������� \"����� �����\"");
		System.out.println("������� ���������� � ������� \"����� �����\"");
		wLog.WriteString(1, "������ ������");
		System.out.println("������ ������");
		InputDataToElement(wDivTitleRegion, "region" , "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'������')]");
		Sleep(100);
		wLog.WriteString(1, "������ ���������");
		System.out.println("������ ���������");
		InputDataToElement(wDivTitleTitle, "title", "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'���������')]");
		Sleep(100);
		wLog.WriteString(1, "������ ����� ����������");
		System.out.println("������ ����� ����������");
		InputDataToElement(wDivText, "textAdvert", "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'�����')]");
		Sleep(100);
		wLog.WriteString(1, "������ email ��������� ����������");
		System.out.println("������ email ��������� ����������");
		InputDataToElement(wDivTitleOwnerAdvert, "email", "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'�������� ����������')]");
		Sleep(200);
		wLog.WriteString(1, "������ ������ ���������� ����������");
		System.out.println("������ ������ ���������� ����������");
		InputDataToElement(wDivActionOfAdvet, "actionOfAdvet", "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'���������� ����������')]");
		Sleep(100);
		wLog.WriteString(1, "������ ������ ��������� ����������");
		System.out.println("������ ������ ��������� ����������");
		InputDataToElement(wDivStatusOfModer, "statusOfModer", "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'������ ���������')]");
		Sleep(100);
		wLog.WriteString(1, "��������� ��������� ������");
		System.out.println("��������� ��������� ������");
		if(Proper.GetProperty("typeAdvert").equals("premium"))
		{
			CheckElementPresent(1,"//div[@class='x-panel-bbar x-panel-bbar-noborder']//button[contains(text(),'���������')]");
			ScrollToElement(wButtonSavePremium);
			wButtonSavePremium.click();
			CheckElementPresent(1,"//div[@class='x-window x-window-plain x-window-dlg']//button[contains(text(),'OK')]");
			wButtonWindowPopup.click();
		}
		else
		{
			CheckElementPresent(1,"//div[@class='x-tab-panel-footer']//button[contains(text(),'���������')]");
			wButtonSaveAdvert.click();	
		}
		Sleep(1500);
		wLog.WriteString(1, "��������� ������� �� ����������");
		System.out.println("��������� ������� �� ����������");
		if(driver.findElement(By.xpath("//div[contains(text(),'����� �����')]")).isDisplayed())
		{
			wLog.WriteString(2, "���������� � ������� \"����� �����\" �� ������");
			System.out.println("���������� � ������� \"����� �����\" �� ������");
			throw new ExceptFailTest("���������� � ������� \"����� �����\" �� ������");
		}
		wLog.WriteString(1, "���������� �������");
		System.out.println("���������� �������");
		wLinkLogout.click();
		
	}
	
	public void InputDataAuto() throws ExceptFailTest
	{
		wLog.WriteString(1, "������� ���������� � ������� \"���� � ��������\"");
		System.out.println("������� ���������� � ������� \"���� � ��������\"");
		wLog.WriteString(1, "������ ������");
		System.out.println("������ ������");
		InputDataToElement(wDivTitleRegion, "region", "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'������')]");
		Sleep(100);
		wLog.WriteString(1, "������ ����� ����������");
		System.out.println("������ ����� ����������");
		InputDataToElement(wDivDescription, "textAdvert", "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'����� ����������')]");
		Sleep(100);
		wLog.WriteString(1, "������ ���y");
		System.out.println("������ ����");
		InputDataToElement(wDivTitlePrice, "price", "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'����')]");
		Sleep(100);
		wLog.WriteString(1, "������ ������");
		System.out.println("������ ������");
		InputDataToElement(wDivTitleCurrency, "currency", "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'������')]");
		Sleep(200);
		wLog.WriteString(1, "������ email ��������� ����������");
		System.out.println("������ email ��������� ����������");
		InputDataToElement(wDivTitleOwnerAdvert, "email", "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'�������� ����������')]");
		Sleep(200);
		wLog.WriteString(1, "������ �����");
		System.out.println("������ �����");
		InputDataToElement(wDivTitleMake, "make", "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'�����')]");
		Sleep(100);
		wLog.WriteString(1, "������ ������");
		System.out.println("������ ������");
		InputDataToElement(wDivTitleModel, "model", "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'������')]");
		Sleep(100);
		wLog.WriteString(1, "������ ��� �������");
		System.out.println("������ ��� �������");
		InputDataToElement(wDivTitleYear, "year", "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'��� �������')]");
		Sleep(100);
		wLog.WriteString(1, "������ ��� ������");
		System.out.println("������ ��� ������");
		InputDataToElement(wDivTitleTypeOfBody, "typeOfBody", "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'��� ������')]");
		Sleep(100);
		wLog.WriteString(1, "������ ��� �����������");
		System.out.println("������ ��� �����������");
		InputDataToElement(wDivTitleTypeOfTransmission, "typeOfTransmission", "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'��� �����������')]");
		Sleep(100);
		wLog.WriteString(1, "������ ������ ���������� ����������");
		System.out.println("������ ������ ���������� ����������");
		InputDataToElement(wDivActionOfAdvet, "actionOfAdvet", "//div[@class='x-grid3-cell-inner x-grid3-col-title' and contains(text(),'���������� ����������')]");
		wLog.WriteString(1, "��������� ��������� �����");
		System.out.println("��������� ��������� ������");
		if(Proper.GetProperty("typeAdvert").equals("premium"))
		{
			CheckElementPresent(1,"//div[@class='x-panel-bbar x-panel-bbar-noborder']//button[contains(text(),'���������')]");
			ScrollToElement(wButtonSavePremium);
			wButtonSavePremium.click();
			CheckElementPresent(1,"//div[@class='x-window x-window-plain x-window-dlg']//button[contains(text(),'OK')]");
			wButtonWindowPopup.click();
		}
		else
		{
			CheckElementPresent(1,"//div[@class='x-tab-panel-footer']//button[contains(text(),'���������')]");
			wButtonSaveAdvert.click();
		}
		Sleep(2000);
		wLog.WriteString(1, "��������� ������� �� ����������");
		System.out.println("��������� ������� �� ����������");
		if(driver.findElement(By.xpath("//div[contains(text(),'���� � ���� -> �������� ���������� -> ���������� � ��������')]")).isDisplayed())
		{
			wLog.WriteString(2, "���������� � ������� \"���������� � ��������\" �� ������");
			System.out.println("���������� � ������� \"���������� � ��������\" �� ������");
			throw new ExceptFailTest("���������� � ������� \"���������� � ��������\" �� ������");
		}
		wLog.WriteString(1, "���������� �������");
		System.out.println("���������� �������");
		driver.get(driver.getCurrentUrl());
	}
	    
	// ���� �������
	private void SetRegion(String sNameRegion) throws ExceptFailTest
	{
		//System.out.println("��� �������");
		String sRegion = Proper.GetProperty(sNameRegion); // �������� ������� �� ����� ������� ������� �������
		String s1 = "//div[contains(text(),'"+sRegion+"  (')]"; // �������� xpath ���������� ������ ������� 
		CheckElementPresent(1,"//fieldset[@class=' x-fieldset x-fieldset-noborder x-form-label-left']//input[2]"); // ��������� ��� ���� ���� ������� ����������
		wFieldWindowRegion.sendKeys(sRegion);
		CheckElementPresent(1,s1); // ��������� ��� �������� ���������� ������ � ���������
		WebElement wListSaveWindowRegion = driver.findElement(By.xpath(s1)); // �������� ��� �������� 
		wListSaveWindowRegion.click(); // �������� ���
		CheckElementPresent(1,"//div[@class='x-window-footer']//button[contains(text(),'���������')]"); //��������� ��� ���� ������ ��������� 
		wButtonSaveWindowRegion.click(); // ���������
	}
	
	// ���� ������ �������� � �������� img
	private void SetOtherImageDiv(String sNameField) throws ExceptFailTest
	{
		//System.out.println("��� ���������");
		String sDataField = Proper.GetProperty(sNameField); // �������� �������� �� ����� ������� ��� �����
		String s1 = "//div[@class='x-combo-list-inner']/div[contains(text(),'"+sDataField+"')]"; // �������� xpath �������� � ���������� ������ �� ����������
		CheckElementPresent(1,s1); // ��������� ��� �������� ���������� ������ �� ��������� ������
		WebElement wListElement = driver.findElement(By.xpath(s1)); // �������� ������� �� ���������
		wListElement.click();
	}
	
	// ���� �������� ���� ������� ����� img
	private void SetDropDownList(WebElement wElement, String sNameField) throws ExceptFailTest
	{
		//System.out.println("�������� SetDropDownList");
		try  // �������� ���� ���������� ������ ��� �� ������ 
		{
			String sDataField = Proper.GetProperty(sNameField); // �������� �������� �� ����� ������� ��� �����
			String s1 = "//div[@class='x-combo-list-inner']/div[contains(text(),'"+sDataField+"')]"; 
			if(!driver.findElement(By.xpath(s1)).isDisplayed())  // � ������� ������� ���� ������� �� ������������
				wElement.click(); // �������� ��� �� ������ �����������
		}
		catch(NoSuchElementException exc){wElement.click();} // ����� ���� ��� ��� ����

		if(wTitledWindowRegion.isDisplayed()) // ���� ����������� ���� ������ �������� (� ���� ��������� "�������������")
			SetRegion(sNameField); // ������ ������
		else
		{
			SetOtherImageDiv(sNameField);
		}
	}
	
	private void SetInput(WebElement wElement, String sNameField)
	{
		//System.out.println("�������� SetInput");
		String sDataField = Proper.GetProperty(sNameField);
		wElement.clear();
		wElement.sendKeys(sDataField);
		
	}
	
	// ���� �������� ���� ������� ����� textarea
	private void SetTextArea(WebElement wElement, String sNameField)
	{
		//System.out.println("�������� SetTextArea");
		String sDataField = Proper.GetProperty(sNameField);
		wElement.clear();
		wElement.sendKeys(sDataField);	
	}
	
	// ���� �������� ���� ������� ����� select
	private void SetSelect(WebElement wElement, String sNameField)
	{
		//System.out.println("�������� SetSelect");
		String sDataField = Proper.GetProperty(sNameField);
		List<WebElement> allOptions = wElement.findElements(By.tagName("option"));
		for(WebElement wE : allOptions)
		{
			if(wE.getText().equals(sDataField))
				wE.click();
		}
	}
		
	private void InputDataToElement(WebElement wElement, String sNameField , String sPath) throws ExceptFailTest
	{  	
		CheckElementPresent(1, sPath);
		ScrollToElement(wElement); //������� � ��������� �������  ����
		wElement.click(); //�������� ���
		KeyPress(wElement, Keys.ARROW_RIGHT, 1); // ��������� �� �������� ������ (� ��� �������� �����) 
		CheckElementPresent(1,"//td[contains(@class,'x-grid3-cell-selected')]"); // ��������� ��� wTdSecondFields ��������
		WebElement wTdSecondFields = driver.findElement(By.xpath("//td[contains(@class,'x-grid3-cell-selected')]")); // ������ ������ �� ��������� (���� ��� ������)
		if(Proper.GetProperty("typeAdvert").equals("premium"))
			wTdSecondFields.click();
		else DoubleClickElement(wTdSecondFields);
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		// ��������� ����� ������� �������� � ������ ��� �����
		try // �������� ������� ��������� ����������� ������ img
		{
			WebElement wImageArrow = driver.findElement(By.xpath("//div[contains(@class,'x-trigger-wrap-focus')]/img")); 
			if(wImageArrow.isDisplayed())										
			{	
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				SetDropDownList(wImageArrow, sNameField);
				return;
			}
		}
		catch(NoSuchElementException exc){/*System.out.println("��� �������� ���������� ��������� ��������");*/}
		
		try // �������� ������� ��������� ������
		{
			WebElement wInput = driver.findElement(By.xpath("//input[contains(@class,'x-form-focus')]"));
			if(wInput.isDisplayed())										
			{
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				SetInput(wInput, sNameField);
				return;
			}
		}
		catch(NoSuchElementException exc){/*System.out.println("��� �������� ���������� ������");*/}
		
		try // �������� ������� ��������� �������
		{
			WebElement wSelect = driver.findElement(By.xpath("//select[contains(@class,'x-form-focus')]"));
			if(wSelect.isDisplayed())
			{
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				SetSelect(wSelect, sNameField);
				return;
			}
		}
		catch(NoSuchElementException exc){/*System.out.println("��� �������� ���������� �������");*/}
		
		try // �������� ������� ��������� ���������
		{
			WebElement wArea = driver.findElement(By.xpath("//textarea[contains(@class, 'x-form-focus')]"));
			if(wArea.isDisplayed())
			{
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				SetTextArea(wArea, sNameField);
				return;
			}
		}
		catch(NoSuchElementException exc){/*System.out.println("��� �������� ���������� ���������");*/}
	}

	@Override
	public void OpenPage(){}
	
	private void OpenListAdvert() throws ExceptFailTest //Открытие листинга выбора рубрики добавления обычного объявления
	{
		Sleep(1000);
		wLog.WriteString(1, "Проверяем вошли ли");
		System.out.println("Проверяем вошли ли");
		CheckElementPresent(1, "//a[@href='/stargate/workspace/admanagement/']");
		wLog.WriteString(1, "Вошли");
		System.out.println("Вошли");
		wAdvert.click();
		Sleep(1000);
		CheckElementPresent(1,"//a[@href='/stargate/workspace/admanagement/catalog/']/span[contains(text(),'Каталог объявлений')]");
		wLog.WriteString(1, "Открываем форму создания объявления");
		System.out.println("Открываем форму создания объявления");
		wCatalogAdvert.click();
		Sleep(1000);
	}
	
	private void OpenListPremium() throws ExceptFailTest//Открытие листинга выбора рубрики добавления премиум объявления
	{
		Sleep(1000);
		wLog.WriteString(1, "Проверяем вошли ли");
		System.out.println("Проверяем вошли ли");
		CheckElementPresent(1, "//a[@href='/stargate/workspace/admanagement/']");
		wLog.WriteString(1, "Вошли");
		System.out.println("Вошли");
		wAdvert.click();
		Sleep(1000);
		CheckElementPresent(1,"//a[@href='/stargate/workspace/admanagement/premium/']//span[contains(text(),'Создать премиум')]");
		wLog.WriteString(1, "Открываем форму создания премиума");
		System.out.println("Открываем форму создания премиума");
		wCreatePremium.click();
		Sleep(1000);
	}
}