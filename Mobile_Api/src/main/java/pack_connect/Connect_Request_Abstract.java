package pack_connect;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;


public abstract class Connect_Request_Abstract
{	  
    public Connect_Request_Abstract(){}
    //Post, Put, Get, Delete
    public String HttpPostRequest(URI uri) throws URISyntaxException, IOException
    {
    	HttpClient hClient = new DefaultHttpClient();
    	HttpPost post = new HttpPost();
    	HttpResponse response;
    	String sTempResponse;
    	
    	post.setURI(uri);
    	response = hClient.execute(post);
    	sTempResponse = GetContentResponse(response);
    	return sTempResponse;
    }
    
    public String HttpPostRequestImage(URI uri , String sPath) throws URISyntaxException, IOException
    {
    	HttpClient hClient = new DefaultHttpClient();
    	HttpPost post = new HttpPost();
    	HttpResponse response;
    	String sTempResponse;  	
    	
    	post.setURI(uri);
    	
    	FileBody bin = new FileBody(new File(sPath));
        StringBody comment = new StringBody("Filename: Image" );
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("image1", bin);
        reqEntity.addPart("comment", comment);
        post.setEntity(reqEntity);
    	
    	
    	response = hClient.execute(post);
    	sTempResponse = GetContentResponse(response);
    	return sTempResponse;
    }
    
    public String HttpPutRequest(URI uri) throws URISyntaxException, IOException 
    {
		HttpClient hClient = new DefaultHttpClient();
    	HttpPut put = new HttpPut();
    	HttpResponse response;
    	String sTempResponse;
   
    	put.setURI(uri);
    	response = hClient.execute(put);
    	sTempResponse = GetContentResponse(response);
    	return sTempResponse;
    } 
    public String HttpGetRequest(URI uri) throws URISyntaxException, IOException 
    {
		HttpClient hClient = new DefaultHttpClient();
    	HttpGet get = new HttpGet();
    	HttpResponse response;
    	String sTempResponse;
   
    	get.setURI(uri);
    	response = hClient.execute(get);
    	sTempResponse = GetContentResponse(response);
    	return sTempResponse;
    }
    public String HttpDeleteRequest(URI uri) throws URISyntaxException, IOException 
    {
		HttpClient hClient = new DefaultHttpClient();
    	HttpDelete delete = new HttpDelete();
    	HttpResponse response;
    	String sTempResponse;
   
    	delete.setURI(uri);
    	response = hClient.execute(delete);
    	sTempResponse = GetContentResponse(response);
    	return sTempResponse;
    }
    
	// ?param=value
	public final String CreateSimpleRequest(String sDataForSimpleRequest) throws UnsupportedEncodingException
	{
		sDataForSimpleRequest = sDataForSimpleRequest.replaceAll(" ", "").replaceAll("}", "").replace("{", "").replaceAll("}", "").replaceAll("\"", "");
		String s0[] = sDataForSimpleRequest.split(",");
		String request = "";
		for(int i=0; i<s0.length; i++)
		{
			String temp = "&"+URLEncoder.encode(s0[i],"UTF-8");
			request  +=temp;
		}
		request = request.replaceAll("%3D", "=");
		return request;	
	}
	
	// ?param[value_param1]=value1
	public final String CreateArrayRequest(String sMainParam, String sDataForArrayRequest) throws UnsupportedEncodingException
	{
		sDataForArrayRequest = sDataForArrayRequest.replaceAll(" ", "").replaceAll("}", "").replaceAll(",", ",[").replaceAll("=", "]=").replace("{", "[")
				.replaceAll("\"", "");
		String s1[] = sDataForArrayRequest.split(",");
		
		for(String sTemp: s1)
			System.out.println(sTemp.replaceAll("\\[", "").replaceAll("\\]", ""));
		
		String request = "";
		for(int i=0; i<s1.length; i++)
		{
			String temp = "&"+URLEncoder.encode(sMainParam, "UTF-8")+URLEncoder.encode(s1[i],"UTF-8");
			request  +=temp;
		}
		request = request.replaceAll("%3D", "=");
		return request;
	}
	// ?param[value_param1][value1_param2][0]=value2
	public final String CreateDoubleArrayRequest(String sMainParam, String sChildMainParam, String sDataForDoubleArrayRequest) throws UnsupportedEncodingException
	{
		sDataForDoubleArrayRequest = sDataForDoubleArrayRequest.replaceAll(" ", "").replaceAll("}", "").replaceAll(",", ",[").replaceAll("=", "][0]=")
				.replace("{", "[").replaceAll("\"", "");
		String s1[] = sDataForDoubleArrayRequest.split(",");
		String request ="";
		for(int i=0; i<s1.length; i++)
		{
			String temp = "&"+URLEncoder.encode((sMainParam)+"["+(sChildMainParam)+"]"+(s1[i]),"UTF-8");
			request  +=temp;
		}
		request = request.replaceAll("%3D", "=");
		return request;
	}

	private String GetContentResponse(HttpResponse response) throws IOException
	{
		BufferedReader br; 
		StringBuffer sBuffer;
		String line, s;
		br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		sBuffer = new StringBuffer();
	    while((line = br.readLine()) != null)
	    {
	    	sBuffer.append(line);
	    	sBuffer.append('\r');
	    }
	    br.close();
	    s = new String(sBuffer);
		return s;
	}

	public <T> void print(T obj)
		{
			PrintWriter pw = new PrintWriter(System.out, true);
			pw.println(obj);
		}
	
	public void Sleep(int nTime) throws InterruptedException
	{
		Thread.sleep(nTime);
	}
}
