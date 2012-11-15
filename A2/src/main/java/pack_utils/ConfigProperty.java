package pack_utils;

import java.io.IOException;
import java.util.Properties;

public class ConfigProperty
{
	//gdfgvxczvxcv
	private Properties PROP;
	
	public void loadProp()
	{
		PROP = new Properties();
		try
		{
			PROP.load(ClassLoader.getSystemResourceAsStream("config.properties"));
		}
		catch(IOException exc){exc.printStackTrace();}
	}
	
	public String getproperty(String key)
	{
		return PROP.getProperty(key);
	}
}
