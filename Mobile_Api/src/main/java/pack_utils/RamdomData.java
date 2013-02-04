package pack_utils;

import java.util.Random;

public class RamdomData
{
	public static String GetRamdomString(int lenth)
	{
		Random r = new Random();
		char mas[] = new char[lenth];
		for(int i=0; i<lenth; i++)
		{
			mas[i] = (char)(r.nextInt(25)+97);
		}
		String s = new String(mas);
		return s;
	}
}
