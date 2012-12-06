package pack_utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriterLog
{
	private File file;
	private FileWriter fw;
	
	
	public void SetUpWriterLog(String sPath) throws ExceptFailTest
	{
		try
		{
			file = new File(sPath); 
			fw = new FileWriter(file);
		}
		catch(NullPointerException exc)
		{
			System.out.println("ÐÐµ ÑÐ´Ð°Ð»Ð¾ÑÑ ÑÐ¾Ð·Ð´Ð°ÑÑ ÑÐ°Ð¹Ð» Ð»Ð¾Ð³Ð°");
			throw new ExceptFailTest("ÐÐµ ÑÐ´Ð°Ð»Ð¾ÑÑ ÑÐ¾Ð·Ð´Ð°ÑÑ ÑÐ°Ð¹Ð» Ð»Ð¾Ð³Ð°");
		}
		catch(IOException exc)
		{
			System.out.println("ÐÐµ ÑÐ´Ð°Ð»Ð¾ÑÑ ÑÐ¾Ð·Ð´Ð°ÑÑ ÑÐ°Ð¹Ð» Ð»Ð¾Ð³Ð°");
			throw new ExceptFailTest("ÐÐµ ÑÐ´Ð°Ð»Ð¾ÑÑ ÑÐ¾Ð·Ð´Ð°ÑÑ ÑÐ°Ð¹Ð» Ð»Ð¾Ð³Ð°");
		}
		System.out.println("Создаем файл лога");
		WriteString(0, "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;\"><title>Log_Result</title></head><body>");
		WriteString(0, "<style> pre {margin:0;padding:0; font-family:arial; font: arial 18px/16px;} </style>");
	}
	
	public void WriteString(int iTypeOfString, String sMessage) throws ExceptFailTest
	{
		try
		{
			switch(iTypeOfString)
			{
				case 1:
					fw.write("<pre style=\"color:green\">"+sMessage+"</pre>"+"\n");
					break;
				case 2:
					fw.write("<pre style=\"color:red\">"+sMessage+"</pre>"+"\n");
					break;
				default:
					fw.write(sMessage+"\n");
					break;
			}
		}
		catch(IOException exc)
		{
			System.out.println("ÐÐµ ÑÐ´Ð°Ð»Ð¾ÑÑ Ð·Ð°Ð¿Ð¸ÑÐ°ÑÑ Ð² Ð»Ð¾Ð³ ÑÑÑÐ¾ÐºÑ: "+sMessage);
			throw new ExceptFailTest("ÐÐµ ÑÐ´Ð°Ð»Ð¾ÑÑ Ð·Ð°Ð¿Ð¸ÑÐ°ÑÑ Ð² Ð»Ð¾Ð³ ÑÑÑÐ¾ÐºÑ: "+sMessage);
		}
	}
	
	public void CloseFile() throws ExceptFailTest
	{
		WriteString(0, "</body></html>");
		System.out.println("ÐÐ°ÐºÑÑÐ²Ð°ÐµÐ¼ ÑÐ°Ð¹Ð» Ð»Ð¾Ð³Ð°");
		try {fw.close();} 
		catch (IOException exc){System.out.println("ÐÐµ ÑÐ´Ð°Ð»Ð¾ÑÑ Ð·Ð°ÐºÑÑÑÑ ÑÐ°Ð¹Ð»");}
	}
	
	/*
	public WriterLog(String sPath)
	{
		try
		{
			file = new File(sPath); 
			fw = new FileWriter(file);
		}
		catch(NullPointerException exc)
		{
			System.out.println("ÐÐµ ÑÐ´Ð°Ð»Ð¾ÑÑ ÑÐ¾Ð·Ð´Ð°ÑÑ ÑÐ°Ð¹Ð» Ð»Ð¾Ð³Ð°"); 
			try
			{
				throw new ExceptFailTest("ÐÐµ ÑÐ´Ð°Ð»Ð¾ÑÑ ÑÐ¾Ð·Ð´Ð°ÑÑ ÑÐ°Ð¹Ð» Ð»Ð¾Ð³Ð°");
			}
			catch (ExceptFailTest e1) {throw new RuntimeException(e1);}
		}
		catch(IOException exc)
		{
			System.out.println("ÐÐµ ÑÐ´Ð°Ð»Ð¾ÑÑ ÑÐ¾Ð·Ð´Ð°ÑÑ ÑÐ°Ð¹Ð» Ð»Ð¾Ð³Ð°");
			try
			{
				throw new ExceptFailTest("ÐÐµ ÑÐ´Ð°Ð»Ð¾ÑÑ ÑÐ¾Ð·Ð´Ð°ÑÑ ÑÐ°Ð¹Ð» Ð»Ð¾Ð³Ð°");
			}
			catch (ExceptFailTest e2) {throw new RuntimeException(e2);}
		}
		System.out.println("Ð¡Ð¾Ð·Ð´Ð°ÐµÐ¼ ÑÐ°Ð¹Ð» Ð»Ð¾Ð³Ð°");
	}
	*/
}
