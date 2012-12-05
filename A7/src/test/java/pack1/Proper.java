package pack1;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class Proper
{
	protected static Properties prop;
	static
	{
		prop = new Properties();
		try
		{
			prop.load(ClassLoader.getSystemResourceAsStream("r1.properties"));
		}
		catch(IOException exc){exc.printStackTrace(); System.out.println("Не удалось загрузить файл конфигурации");}
	}
	
	public static String GetProperty(String sKey)
	{
		return prop.getProperty(sKey);
	}

}